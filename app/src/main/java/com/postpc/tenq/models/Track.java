package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Track extends SpotifyEntity implements Serializable {

    @SerializedName("preview_url")
    String previewUrl;

    @SerializedName("duration_ms")
    int durationMs;

    @SerializedName("artists")
    int artists;

    @SerializedName("album")
    int album;

}
