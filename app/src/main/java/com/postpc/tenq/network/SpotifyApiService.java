package com.postpc.tenq.network;

import com.postpc.tenq.models.UserProfile;
import retrofit2.http.GET;

public interface SpotifyApiService {

    @GET("/me") //Get current user's profile
    UserProfile getUserProfile();

    @GET("/me/top/artists") //Get current user's top artists
    void getTopArtists(); //TODO create TopArtists model class

}
