package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

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
    List<T> items;

    public List<T> getItems() {
        return items;
    }

    public String getNext() {
        return next;
    }
}
