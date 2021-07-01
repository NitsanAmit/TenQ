package com.postpc.tenq.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserProfile implements Serializable {

    @SerializedName("id")
    String id;

    @SerializedName("display_name")
    String username;

    @SerializedName("email")
    String email;

    @SerializedName("href")
    String profileLink;

    @SerializedName("images")
    String[] images;

}
