package com.dreamsinhd.android.youtubefilter.model;

import java.io.Serializable;
import java.math.BigInteger;

public class Filter implements Serializable {
    private String search;
    private BigInteger minViews;
    private BigInteger maxViews;
    private BigInteger minLikes;
    private BigInteger maxLikes;
    private BigInteger minDislikes;
    private BigInteger maxDislikes;
    private String orderBy;

    public Filter() {

    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public BigInteger getMinViews() {
        return minViews;
    }

    public void setMinViews(BigInteger minViews) {
        this.minViews = minViews;
    }

    public BigInteger getMaxViews() {
        return maxViews;
    }

    public void setMaxViews(BigInteger maxViews) {
        this.maxViews = maxViews;
    }

    public BigInteger getMinLikes() {
        return minLikes;
    }

    public void setMinLikes(BigInteger minLikes) {
        this.minLikes = minLikes;
    }

    public BigInteger getMaxLikes() {
        return maxLikes;
    }

    public void setMaxLikes(BigInteger maxLikes) {
        this.maxLikes = maxLikes;
    }

    public BigInteger getMinDislikes() {
        return minDislikes;
    }

    public void setMinDislikes(BigInteger minDislikes) {
        this.minDislikes = minDislikes;
    }

    public BigInteger getMaxDislikes() {
        return maxDislikes;
    }

    public void setMaxDislikes(BigInteger maxDislikes) {
        this.maxDislikes = maxDislikes;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
