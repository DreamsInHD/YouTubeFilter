package com.dreamsinhd.android.youtubefilter;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dreamsinhd.android.youtubefilter.interfaces.BottomNavActivity;
import com.dreamsinhd.android.youtubefilter.interfaces.NavigationActivity;
import com.dreamsinhd.android.youtubefilter.model.Filter;
import com.dreamsinhd.android.youtubefilter.networking.VideoFetchTask;
import com.dreamsinhd.android.youtubefilter.recyclerview.VideoAdapter;
import com.google.api.services.youtube.model.Video;

import java.util.List;

public class VideoListFragment extends Fragment {
    private static final String VIDEO_LIST_EXTRA = "com.dreamsinhd.android.youtubefilter.VideoListFragment.videos";
    private static final String FILTER_EXTRA = "com.dreamsinhd.android.youtubefilter.VideoListFragment.filter";
    public static final String TAG = "VideoListFragment";

    private Filter filter;
    private boolean newPage = false;
    private int prevPosition;

    private RecyclerView recyclerView;
    private ProgressBar videoLoadProgressBar;
    private LinearLayout noNetworkLayout;
    private MaterialButton tryAgainButton;
    private View videoListView;
    private View progressBarOverlay;

    private BackdropListener backdropListener;
    private VideoFetchTask.ProgressUpdateListener progressUpdateListener;

    private VideoFetchTask videoFetchTask;

    private VideoListViewModel videoListViewModel;

    public static VideoListFragment newInstance(Filter filter) {
        VideoListFragment fragment = new VideoListFragment();

        Bundle args = new Bundle();
        args.putSerializable(FILTER_EXTRA, filter);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoListViewModel = ViewModelProviders.of(this).get(VideoListViewModel.class);

        Bundle args = getArguments();
        if (args != null) {
            filter = (Filter) args.getSerializable(FILTER_EXTRA);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (videoFetchTask != null) {
            videoFetchTask.cancel(true);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_video_list, container, false);

        setupToolbar(v);

        recyclerView = v.findViewById(R.id.video_recycler_view);
        videoLoadProgressBar = v.findViewById(R.id.video_load_progress_bar);
        videoListView = v.findViewById(R.id.video_list);
        noNetworkLayout = v.findViewById(R.id.no_internet_connection);
        tryAgainButton = v.findViewById(R.id.try_again_button);
        progressBarOverlay = v.findViewById(R.id.progress_bar_overlay);

        videoLoadProgressBar.setProgress(0);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false));

        progressUpdateListener = new VideoFetchTask.ProgressUpdateListener() {
            @Override
            public void progressUpdate(Integer amount) {
                if (amount == null) {
                    if(!newPage) {
                        recyclerView.setVisibility(View.GONE);
                    }
                    videoLoadProgressBar.setProgress(0);
                    progressBarOverlay.setVisibility(View.VISIBLE);
                } else {
                    videoLoadProgressBar.setProgress(amount);
                }
            }

            @Override
            public void finished(List<Video> result) {
                if (newPage) {
                    videoListViewModel.addVideos(result);
                    recyclerView.scrollToPosition(prevPosition - 2);
                    newPage = false;
                } else {
                    videoListViewModel.setVideos(result);
                }

                progressBarOverlay.setVisibility(View.GONE);
                videoLoadProgressBar.setProgress(0);
                recyclerView.setVisibility(View.VISIBLE);

                setupAdapter();
            }
        };
        backdropListener = new BackdropListener(getActivity(), videoListView);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy < 0 && backdropListener.isBackdropShown()) {
                    backdropListener.executeAnimation();
                }
            }
        });


        tryAgainButton.setOnClickListener(view -> {
            ((BottomNavActivity) getActivity()).getBottomNavigationView().setSelectedItemId(R.id.filter_page_menu_item);
        });

        videoFetchTask = new VideoFetchTask(progressUpdateListener);

        executeRequestIfOnline();

        setupAdapter();

        return v;
    }

    private void executeRequestIfOnline() {
        videoFetchTask = new VideoFetchTask(progressUpdateListener);
        if (isOnline()) {
            videoFetchTask.execute(filter);
            noNetworkLayout.setVisibility(View.GONE);
            progressBarOverlay.setVisibility(View.VISIBLE);

        } else {
            noNetworkLayout.setVisibility(View.VISIBLE);
            progressBarOverlay.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setupToolbar(View v) {
        Toolbar toolbar = v.findViewById(R.id.app_bar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_video_list, menu);

        MenuItem filter = menu.findItem(R.id.filter_videos);

        filter.setOnMenuItemClickListener(backdropListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                ((NavigationActivity) getActivity()).navigateTo(AboutFragment.newInstance(), true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void setupAdapter() {
        VideoAdapter videoAdapter = new VideoAdapter(videoListViewModel.getVideos(), this.getLifecycle(), getActivity());
        videoAdapter.setOnBottomReachedListener(position -> {
            if(!newPage) {
                newPage = true;
                prevPosition = position;
                executeRequestIfOnline();
            }
        });

        recyclerView.setAdapter(videoAdapter);
    }
}
