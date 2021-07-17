package com.postpc.tenq.network;

import com.postpc.tenq.models.SearchResult;
import com.postpc.tenq.models.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpotifyApiService {

    @GET("me") //Get current user's profile
    Call<User> getUserProfile();

    @GET("me/top/artists") //Get current user's top artists
    void getTopArtists(); //TODO create TopArtists model class

    @GET("users/{user-id}") //Get user by id
    Call<User> getUserProfileName(
            @Path("user-id") String user_id
    );

    @GET("search")
    Call<SearchResult> searchTracks(@Query("q") String query, @Query("type") String type, @Query("limit") int limit, @Query("offset") int offset);
}
