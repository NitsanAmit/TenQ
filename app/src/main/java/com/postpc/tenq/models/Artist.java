package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Artist extends SpotifyEntity implements Serializable {

    @SerializedName("images")
    List<SpotifyImage> images;

}
