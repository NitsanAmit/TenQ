package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Artist extends SpotifyEntity implements Serializable {

    @SerializedName("images")
    SpotifyImage[] images;

}
