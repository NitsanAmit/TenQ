package com.postpc.tenq.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;

public class User extends SpotifyEntity implements Serializable {

    @SerializedName("email")
    String email;

    @SerializedName("href")
    String profileLink;

    @SerializedName("images")
    SpotifyImage[] images;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "User Details:" +
                "\nid: " + id +
                "\nusername: " + name +
                "\nemail: " + email +
                "\nprofileLink: " + profileLink +
                "\nimages: " + String.join(",", Arrays.stream(images).map(image -> image.url).toArray(String[]::new));
    }
}
