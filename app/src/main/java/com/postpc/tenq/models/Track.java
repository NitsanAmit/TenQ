package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Track extends SpotifyEntity implements Serializable {

    @SerializedName("preview_url")
    String previewUrl;

    @SerializedName("duration_ms")
    int durationMs;

    @SerializedName("artists")
    Artist[] artists;

    @SerializedName("album")
    Album album;

    @SerializedName("added_by")
    User addedBy;

    @SerializedName("added_at")
    Date addedAt;

}
