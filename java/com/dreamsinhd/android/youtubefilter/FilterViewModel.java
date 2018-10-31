package com.dreamsinhd.android.youtubefilter;

import android.arch.lifecycle.ViewModel;

import com.dreamsinhd.android.youtubefilter.model.Filter;

import java.math.BigInteger;

public class FilterViewModel extends ViewModel {
    private Filter filter = new Filter();

    public FilterViewModel() {

    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getSearch() {
        return filter.getSearch();
    }

    public void setSearch(String search) {
        filter.setSearch(search);
    }

    public void setMaxViews(BigInteger maxViews) {
        filter.setMaxViews(maxViews);
    }

    public BigInteger getMaxViews() {
        return filter.getMaxViews();
    }

    public void setMinViews(BigInteger minViews) {
        filter.setMinViews(minViews);
    }

    public BigInteger getMinViews() {
        return filter.getMinViews();
    }

    public void setMinLikes(BigInteger minLikes) {
        filter.setMinLikes(minLikes);
    }

    public BigInteger getMinLikes() { return filter.getMinLikes(); }

    public BigInteger getMaxLikes() {
        return filter.getMaxLikes();
    }

    public void setMaxLikes(BigInteger maxLikes) {
        filter.setMaxLikes(maxLikes);
    }

    public BigInteger getMinDislikes() {
        return filter.getMinDislikes();
    }

    public void setMinDislikes(BigInteger minDislikes) {
        filter.setMinDislikes(minDislikes);
    }

    public BigInteger getMaxDislikes() {
        return filter.getMaxDislikes();
    }

    public void setMaxDislikes(BigInteger maxDislikes) {
        filter.setMaxDislikes(maxDislikes);
    }

    public String getOrderBy() {
        return filter.getOrderBy();
    }

    public void setOrderBy(String orderBy) {
        filter.setOrderBy(orderBy);
    }
}
