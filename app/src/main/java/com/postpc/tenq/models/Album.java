package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Album extends SpotifyEntity implements Serializable {

    @SerializedName("artists")
    Artist[] artists;

    @SerializedName("release_date")
    String releaseDate;

    @SerializedName("images")
    SpotifyImage[] images;

    @SerializedName("href")
    String href;

    public SpotifyImage[] getImages() {
        return images;
    }
}
