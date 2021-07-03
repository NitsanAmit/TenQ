package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Room implements Serializable {

    @SerializedName("id")
    String id; // Allow invites by room id

    @SerializedName("host")
    User host;

    @SerializedName("guests")
    User[] guests;

    @SerializedName("playlist_id")
    Playlist playlist;

}
