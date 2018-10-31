package com.dreamsinhd.android.youtubefilter.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dreamsinhd.android.youtubefilter.R;
import com.google.api.services.youtube.model.Video;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;

import org.threeten.bp.Duration;

import java.math.BigInteger;
import java.text.DecimalFormat;

public class VideoHolder extends RecyclerView.ViewHolder {
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer youTubePlayer;
    private TextView videoTitleTextView;
    private TextView videoViewsTextView;
    private TextView videoLikesTextView;
    private TextView videoDislikesTextView;
    private TextView videoDurationTextView;
    private String videoId;

    private DecimalFormat formatter = new DecimalFormat("###,###,###,###");

    public VideoHolder(View itemView) {
        super(itemView);
        youTubePlayerView = itemView.findViewById(R.id.youtube_player_view);
        videoTitleTextView = itemView.findViewById(R.id.youtube_title);
        videoViewsTextView = itemView.findViewById(R.id.video_views);
        videoLikesTextView = itemView.findViewById(R.id.video_likes);
        videoDislikesTextView = itemView.findViewById(R.id.video_dislikes);
        videoDurationTextView = itemView.findViewById(R.id.video_time);

        youTubePlayerView.initialize(initializedYouTubePlayer -> initializedYouTubePlayer.addListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady() {
                youTubePlayer = initializedYouTubePlayer;
                youTubePlayer.cueVideo(videoId, 0);
            }
        }), true);
    }

    public void bind(Video video) {
        videoId = video.getId();
        videoTitleTextView.setText(video.getSnippet().getTitle());
        videoViewsTextView.setText(formatter.format(video.getStatistics().getViewCount()));

        BigInteger likeCount = video.getStatistics().getLikeCount();
        BigInteger dislikeCount = video.getStatistics().getDislikeCount();

        videoLikesTextView.setText(likeCount != null ? formatter.format(likeCount) : "");
        videoDislikesTextView.setText(dislikeCount != null ? formatter.format(dislikeCount) : "");

        Duration duration = Duration.parse(video.getContentDetails().getDuration());

        videoDurationTextView.setText(getFormattedDuration(duration));

        if (youTubePlayer == null) {
            return;
        }

        youTubePlayer.cueVideo(videoId, 0);
    }

    private String getFormattedDuration(Duration duration) {
        String durationString = "";

        if(duration.toMinutes() >= 3600) {
            long hours = duration.toMinutes() / 60;

            durationString += hours + ":";
            duration = duration.minusMinutes(hours);
        }
        else if(duration.getSeconds() >= 60) {
            long minutes = duration.toMinutes();

            durationString += minutes + ":";
            duration = duration.minusMinutes(minutes);
        } else {
            durationString += "0:";
        }

        long seconds = duration.getSeconds();
        durationString += (seconds < 10 ? "0" + seconds : seconds);

        return durationString;
    }
}