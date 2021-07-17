package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

public class SearchResult {

    @SerializedName("tracks")
    Page<Track> tracks;

    public Page<Track> getTracks() {
        return tracks;
    }
}
