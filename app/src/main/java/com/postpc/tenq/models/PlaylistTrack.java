package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class PlaylistTrack implements Serializable {


    @SerializedName("added_by")
    User addedBy;

    @SerializedName("added_at")
    Date addedAt;

    @SerializedName("track")
    Track track;

    public User getAddedBy() {
        return addedBy;
    }

    public Date getAddedAt() {
        return addedAt;
    }

    public Track getTrack() {
        return track;
    }
}
