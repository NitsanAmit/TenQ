package com.postpc.tenq.network;

import com.postpc.tenq.models.User;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SpotifyApiService {

    @GET("me") //Get current user's profile
    Call<User> getUserProfile();

    @GET("me/top/artists") //Get current user's top artists
    void getTopArtists(); //TODO create TopArtists model class

}
