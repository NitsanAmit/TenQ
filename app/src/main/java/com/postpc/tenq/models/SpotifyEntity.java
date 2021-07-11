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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
