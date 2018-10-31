package com.dreamsinhd.android.youtubefilter.recyclerview;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dreamsinhd.android.youtubefilter.R;
import com.dreamsinhd.android.youtubefilter.VideoListFragment;
import com.dreamsinhd.android.youtubefilter.interfaces.OnBottomReachedListener;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    private List<SearchResult> searchResults;
    private List<Video> videos;
    private Lifecycle lifecycle;
    private Context context;

    OnBottomReachedListener onBottomReachedListener;

    public VideoAdapter(List<Video> videos, Lifecycle lifecycle, Context context) {
        this.videos = videos;
        this.lifecycle = lifecycle;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.video_list_item, parent, false);

        YouTubePlayerView youTubePlayerView = v.findViewById(R.id.youtube_player_view);
        youTubePlayerView.getPlayerUIController().showFullscreenButton(false);
        lifecycle.addObserver(youTubePlayerView);

        return new VideoHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
        holder.bind(videos.get(position));

        if(position == videos.size() - 1) {
            onBottomReachedListener.onBottomReached(position);
        }
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener) {
        this.onBottomReachedListener = onBottomReachedListener;
    }
}