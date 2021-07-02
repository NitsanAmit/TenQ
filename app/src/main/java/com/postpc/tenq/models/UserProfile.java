package com.postpc.tenq.models;

import android.util.ArraySet;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
    UserImage[] images;

    public String getId() {
        return id;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "User Details:" +
                "\nid: " + id +
                "\nusername: " + username +
                "\nemail: " + email +
                "\nprofileLink: " + profileLink +
                "\nimages: " + String.join(",", Arrays.stream(images).map(image -> image.url).toArray(String[]::new));
    }
}
