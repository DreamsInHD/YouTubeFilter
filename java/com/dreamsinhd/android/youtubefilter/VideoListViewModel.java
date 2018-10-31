package com.dreamsinhd.android.youtubefilter;


import android.arch.lifecycle.ViewModel;

import com.google.api.services.youtube.model.Video;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VideoListViewModel extends ViewModel implements Serializable {
    private List<Video> videos = new ArrayList<>();

    public void addVideos(List<Video> videos) {
        this.videos.addAll(videos);
    }

    public void addVideo(Video video) {
        videos.add(video);
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }
}
