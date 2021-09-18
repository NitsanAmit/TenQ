package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Album extends SpotifyEntity implements Serializable {

    @SerializedName("artists")
    List<Artist> artists;

    @SerializedName("release_date")
    String releaseDate;

    @SerializedName("images")
    List<SpotifyImage> images;

    @SerializedName("href")
    String href;

    public List<SpotifyImage> getImages() {
        return images;
    }
}
