package com.postpc.tenq.network;

import com.postpc.tenq.models.Page;
import com.postpc.tenq.models.PlaylistTrack;
import com.postpc.tenq.models.SearchResult;
import com.postpc.tenq.models.Playlist;
import com.postpc.tenq.models.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SpotifyApiService {

    @GET("me") //Get current user's profile
    Call<User> getUserProfile();

    @GET("me/tracks/contains")
    Call<List<Boolean>> checkSavedTracks(@Query("ids") String trackIds);

    @PUT("me/tracks")
    Call<Void> saveTrackForUser(@Query("ids") String trackId);

    @DELETE("me/tracks")
    Call<Void> removeTrackForUser(@Query("ids") String trackId);

    @GET("search")
    Call<SearchResult> searchTracks(@Query("q") String query, @Query("type") String type, @Query("limit") int limit, @Query("offset") int offset);

    @GET("playlists/{playlist_id}")
    Call<Playlist> getPlaylist(@Path("playlist_id") String playlistId, @Query("fields") String[] fields);

    @POST("users/{user_id}/playlists")
    Call<Playlist> createPlaylist(@Path("user_id") String userId, @Body() Map<String, Object> details);

    @GET("playlists/{playlist_id}/tracks")
    Call<Page<PlaylistTrack>> getPlaylistTracks(@Path("playlist_id") String playlistId, @Query("limit") int limit, @Query("offset") int offset);

    @PUT("playlists/{playlist_id}/tracks")
    Call<Void> reorderPlaylist(@Path("playlist_id") String playlistId, @Body() Map<String, Object> details);

}
