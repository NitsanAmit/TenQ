package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpotifyEntity implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName(value ="name", alternate={"display_name"})
    String name;

    @SerializedName("uri")
    String uri;

}
