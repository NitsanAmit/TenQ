package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ExternalUrls  implements Serializable {

    @SerializedName("spotify")
    String spotify;

    public String getSpotify() {
        return spotify;
    }
}
