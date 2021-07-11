package com.postpc.tenq.network;

import com.postpc.tenq.models.User;
import com.postpc.tenq.models.searchModels.SearchItem;
import com.postpc.tenq.models.searchModels.Tracks;

import java.util.List;

import okhttp3.ResponseBody;
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
    Call<SearchItem> getSong(@Query("q") String q,
                               @Query("type") String type, @Query("limit") String limit, @Query("offset") String offset);
}
