package com.dreamsinhd.android.youtubefilter.networking;


import android.os.AsyncTask;

import com.dreamsinhd.android.youtubefilter.model.Filter;
import com.google.api.services.youtube.model.Video;

import java.util.List;

// AsyncTask responsible for fetching search results on background thread and publishing results on UI thread
public class VideoFetchTask extends AsyncTask<Filter, Integer, List<Video>> implements YouTubeFetcher.VideoFetchedListener {
    private static final String TAG = "VideoFetchTask";
    private YouTubeFetcher youTubeFetcher = new YouTubeFetcher();
    private ProgressUpdateListener progressUpdateListener;

    public VideoFetchTask(ProgressUpdateListener progressUpdateListener) {
        this.progressUpdateListener = progressUpdateListener;
    }

    public interface ProgressUpdateListener {
        void progressUpdate(Integer amount);
        void finished(List<Video> result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        youTubeFetcher.setVideoFetchedListener(this);
    }

    @Override
    protected List<Video> doInBackground(Filter... filters) {
        publishProgress(null);
        youTubeFetcher.setFilter(filters[0]);

        return youTubeFetcher.getVideosWithFilter();
    }

    @Override
    public void newVideos(int amount) {
        publishProgress(amount);
    }

    @Override
    protected void onProgressUpdate(Integer... integers) {
        if(integers == null) {
            progressUpdateListener.progressUpdate(null);
        } else {
            progressUpdateListener.progressUpdate(integers[0]);
        }
    }

    @Override
    protected void onPostExecute(List<Video> results) {
        progressUpdateListener.finished(results);
    }
}