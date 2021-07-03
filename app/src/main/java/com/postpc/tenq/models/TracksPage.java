package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TracksPage implements Serializable {

    @SerializedName("limit")
    int limit;

    @SerializedName("offset")
    int offset;

    @SerializedName("total")
    long total;

    @SerializedName("href")
    String href;

    @SerializedName("next")
    String next;

    @SerializedName("previous")
    String previous;

    @SerializedName("items")
    Track[] items;

}
