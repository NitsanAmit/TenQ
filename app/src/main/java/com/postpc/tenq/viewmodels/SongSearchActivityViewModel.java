package com.postpc.tenq.viewmodels;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.SearchResult;
import com.postpc.tenq.models.Track;
import com.postpc.tenq.models.User;
import com.postpc.tenq.network.SpotifyClient;

import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongSearchActivityViewModel extends ViewModel {


    public static final int PAGE_SIZE = 30;
    private final AtomicInteger offset = new AtomicInteger(0);
    private MutableLiveData<List<Track>> results;
    private MutableLiveData<Boolean> addedTracks;
    private String query;

    public LiveData<List<Track>> getResults() {
        if (results == null) {
            results = new MutableLiveData<>(null);
        }
        return results;
    }

    public LiveData<Boolean> getDidAddTracks() {
        if (addedTracks == null) {
            addedTracks = new MutableLiveData<>(false);
        }
        return addedTracks;
    }

    public void searchSongs(String query) {
        this.query = query;
        results.postValue(new ArrayList<>());
        loadNextPage();
    }

    public void loadNextPage() {
        if (query == null || TextUtils.isEmpty(query)) {
            results.postValue(new ArrayList<>());
            return;
        }
        String encodedQuery;
        try{
            encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            Log.e("RoomActivityViewModel", e.getMessage(), e);
            return;
        }
        SpotifyClient
                .getClient()
                .searchTracks(encodedQuery,"track", PAGE_SIZE, offset.get())
                .enqueue(new Callback<SearchResult>() {
                    @Override
                    public void onResponse(@NotNull Call<SearchResult> call, @NotNull Response<SearchResult> response) {
                        if (response.code() == 200) {
                            SearchResult searchResult = response.body();
                            if (searchResult != null && searchResult.getTracks() != null) {
                                offset.set(offset.addAndGet(PAGE_SIZE)); // increase the page offset
                                List<Track> currentResults = results.getValue();
                                if (currentResults == null) {
                                    currentResults = searchResult.getTracks().getItems();
                                }else {
                                    currentResults.addAll(searchResult.getTracks().getItems()); // append tracks from page to existing list
                                }
                                // The list reference must be changed for the list adapter to notify the changes
                                ArrayList<Track> newList = new ArrayList<>(currentResults);
                                results.postValue(newList);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<SearchResult> call, @NotNull Throwable t) {
                        Log.e("RoomActivityViewModel", t.getMessage(), t);
                    }
                });

    }

    public void addTrackToRoomPlaylist(User user, Room room, Track track) {
        SpotifyClient.getClient()
                .addTrackToPlaylist(room.getPlaylist().getId(), track.getUri())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        addedTracks.postValue(true);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("SongSearchActivityViewModel", "Can't add track to playlist " + track.getUri(), t);
                    }
                });
    }
}

