package com.dreamsinhd.android.youtubefilter.networking;

import android.util.Log;

import com.dreamsinhd.android.youtubefilter.BuildConfig;
import com.dreamsinhd.android.youtubefilter.model.Filter;
import com.dreamsinhd.android.youtubefilter.model.FilterUtils;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class YouTubeFetcher {
    private static final String TAG = "YouTubeFetcher";

    // Global instance of YouTube object, which is used to make YouTube Data API requests
    private YouTube youTube;
    private List<SearchResult> searchResults;
    private String nextPageToken;
    private Filter filter;
    private VideoFetchedListener videoFetchedListener;

    public YouTubeFetcher() {
        // Initialize YouTube object. Last argument is required but nothing needs to be initialized when HttpRequest is initialized
        youTube = new YouTube.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), request -> {
        }).setApplicationName("YoutubeFilter").build();
    }

    public interface VideoFetchedListener {
        void newVideos(int amount);
    }

    public void setVideoFetchedListener(VideoFetchedListener videoFetchedListener) {
        this.videoFetchedListener = videoFetchedListener;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public List<Video> getVideosWithFilter() {
        return getFullVideoList(20);
    }

    // Make YouTube Data API request and return results
    private void getYouTubeResults() {
        try {
            YouTube.Search.List search = youTube.search().list("id,snippet");
            search.setKey(BuildConfig.ApiKey);
            search.setType("video");
            search.setVideoDuration("any");
            search.setMaxResults(50L);
            if (filter.getSearch() != null) {
                search.setQ(filter.getSearch());
            }

            FilterUtils.setRandomSearchProperties(search);

            if (filter.getMaxViews() != null || filter.getMaxLikes() != null) {
                // Random longitude and latitude for increased randomness
                String location = FilterUtils.genRandomInRange(-90, 90) + "," + FilterUtils.genRandomInRange(-180, 180);

                search.setLocation(location);
                search.setLocationRadius(FilterUtils.genRandomInRange(50, 600) + "km");
            } else {
                // Random query for increased randomness
                String query = FilterUtils.getORQuery(15);
                Log.d(TAG, "Query: " + query);
                if (filter.getSearch() == null) {
                    search.setQ(query);
                }
                search.setOrder("viewCount");
            }
            SearchListResponse searchResponse = search.execute();

            nextPageToken = searchResponse.getNextPageToken();

            searchResults = searchResponse.getItems();

        } catch (IOException e) {
            Log.e(TAG, "Couldn't fetch videos because of IOException", e);
        }
    }

    private List<Video> getVideos() {
        List<Video> videos = new ArrayList<>();
        String idString = "";

        // build String that contains all Video ID's. The API requires that all ID's are comma separated
        for (int i = 0; i < searchResults.size(); i++) {
            idString += (searchResults.get(i).getId().getVideoId() + ",");
        }
        if (idString.length() != 0) {
            idString = idString.substring(0, idString.length() - 1);

            try {
            /* Specify which properties the API should return.
               ContentDetails include the duration of the video,
               Statistics include the amount of views, likes,... */
                YouTube.Videos.List videoListByIdRequest = youTube.videos().list("contentDetails,statistics,snippet");

                videoListByIdRequest.setId(idString);
                videoListByIdRequest.setKey(BuildConfig.ApiKey);

                VideoListResponse response = videoListByIdRequest.execute();

                videos = response.getItems();

                FilterUtils.filterVideoList(videos, filter);
            } catch (IOException e) {
                Log.e(TAG, "There was an error while requesting the videos", e);
            }
        }
        return videos;
    }


    // This method makes sure there are enough videos that match the filter in the videos list
    private List<Video> getFullVideoList(int amountOfVideos) {
        List<Video> videos = new ArrayList<>();
        int prevAmount = videos.size();

        while (videos.size() < amountOfVideos) {
            getYouTubeResults();
            FilterUtils.removeDuplicateVideos(videos);
            videos.addAll(getVideos());
            Log.d(TAG, "Amount of videos in list: " + videos.size());
            if (videos.size() > prevAmount) {
                videoFetchedListener.newVideos(videos.size());
                prevAmount = videos.size();
            }
        }

        if (filter.getOrderBy() != null) {
            FilterUtils.orderVideosBy(videos, filter.getOrderBy());
        }

        return videos;
    }
}

