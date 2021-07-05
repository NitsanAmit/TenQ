package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Playlist extends SpotifyEntity implements Serializable {

    @SerializedName("description")
    String description;

    @SerializedName("href")
    String href;

    @SerializedName("owner")
    SpotifyEntity owner;

    @SerializedName("public")
    boolean isPublic;

    @SerializedName("collaborative")
    boolean isCollaborative;

    @SerializedName("tracks")
    TracksPage tracks;

    public TracksPage getTracks() {
        return tracks;
    }
}

