package com.dreamsinhd.android.youtubefilter.model;

import android.util.Log;

import com.dreamsinhd.android.youtubefilter.model.Filter;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

// Helps with filtering videos
public class FilterUtils {
    private static final String TAG = "FilterUtils";

    public static double genRandomInRange(int from, int to) {
        Random r = new Random();
        return from + (to - from) * r.nextFloat();
    }

    public static String genRandomQuery(int targetLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetLength);
        for (int i = 0; i < targetLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static void setRandomSearchProperties(YouTube.Search.List search) {
        Random random = new Random();
        Object[] entries = TopicId.topics.values().toArray();
        String randomValue = (String) entries[random.nextInt(entries.length)];

        search.setTopicId(randomValue);
    }

    public static String getORQuery(int amountOfSearchWords) {
        String query = "";
        for (int i = 0; i < amountOfSearchWords; i++) {
            query += genRandomQuery((int) genRandomInRange(2, 6)) + "|";
        }
        return query.substring(1, query.length() - 1);
    }

    public static void filterVideoList(List<Video> videos, Filter filter) {
        if (videos.size() != 0) {
            Iterator<Video> iterator = videos.iterator();

            while (iterator.hasNext()) {
                Video currentVideo = iterator.next();
                if (filter.getMaxViews() != null && (currentVideo.getStatistics().getViewCount() == null || filter.getMaxViews().compareTo(currentVideo.getStatistics().getViewCount()) < 0)) {
                    iterator.remove();
                    continue;
                }
                if (filter.getMinViews() != null && (currentVideo.getStatistics().getViewCount() == null || filter.getMinViews().compareTo(currentVideo.getStatistics().getViewCount()) > 0)) {
                    iterator.remove();
                    continue;
                }

                if (filter.getMaxLikes() != null && (currentVideo.getStatistics().getLikeCount() == null || filter.getMaxLikes().compareTo(currentVideo.getStatistics().getLikeCount()) < 0)) {
                    iterator.remove();
                    continue;
                }
                if(filter.getMinLikes() != null && (currentVideo.getStatistics().getLikeCount() == null || filter.getMinLikes().compareTo(currentVideo.getStatistics().getLikeCount()) > 0)) {
                    Log.d(TAG, "Amount of likes: " + currentVideo.getStatistics().getLikeCount() + "| Is Max Likes: " + (filter.getMaxLikes() == null ? "no" : "yes") + " | Allowed: " + filter.getMaxLikes());
                    iterator.remove();
                    continue;
                }

                if (filter.getMaxDislikes() != null && (currentVideo.getStatistics().getDislikeCount() == null || filter.getMaxDislikes().compareTo(currentVideo.getStatistics().getDislikeCount()) < 0)) {
                    iterator.remove();
                    continue;
                }
                if(filter.getMinDislikes() != null && (currentVideo.getStatistics().getDislikeCount() == null || filter.getMinDislikes().compareTo(currentVideo.getStatistics().getDislikeCount()) > 0)) {
                    Log.d(TAG, "Amount of dislikes: " + currentVideo.getStatistics().getDislikeCount() + "| Is Max Dislikes: " + (filter.getMaxDislikes() == null ? "no" : "yes") + " | Allowed: " + filter.getMaxDislikes());
                    iterator.remove();
                    continue;
                }
            }
        }
    }

    public static void removeDuplicateVideos(List<Video> videos) {
        Set<Video> videosSet = new HashSet<>(videos);
        videos.clear();
        videos.addAll(videosSet);
    }

    public static void orderVideosBy(List<Video> videos, String orderBy) {
        Log.d(TAG, "Order by: " + orderBy);
        orderBy = orderBy.toLowerCase();
        switch (orderBy) {
            case "views":
                Collections.sort(videos, (video, video2) -> {
                    if (video.getStatistics().getViewCount() == null) {
                        return 1;
                    } else if (video2.getStatistics().getViewCount() == null) {
                        return -1;
                    }
                    return video2.getStatistics().getViewCount().compareTo(video.getStatistics().getViewCount());
                });
                break;
            case "likes":
                Collections.sort(videos, (video, video2) -> {
                    if (video.getStatistics().getLikeCount() == null) {
                        return 1;
                    } else if (video2.getStatistics().getLikeCount() == null) {
                        return -1;
                    }
                    return video2.getStatistics().getLikeCount().compareTo(video.getStatistics().getLikeCount());
                });
                break;
            case "dislikes":
                Collections.sort(videos, (video, video2) -> {
                    if (video.getStatistics().getDislikeCount() == null) {
                        return 1;
                    } else if (video2.getStatistics().getDislikeCount() == null) {
                        return -1;
                    }
                    return video2.getStatistics().getDislikeCount().compareTo(video.getStatistics().getDislikeCount());
                });
                break;
            default:
                break;
        }
    }
}
