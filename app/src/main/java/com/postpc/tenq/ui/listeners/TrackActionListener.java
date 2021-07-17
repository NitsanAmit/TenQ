package com.postpc.tenq.ui.listeners;

import android.util.Log;

import com.postpc.tenq.network.SpotifyClient;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActionListener implements ITrackActionListener {

    private final BiConsumer<Integer, Boolean> setTrackLikedCallback;

    public TrackActionListener(BiConsumer<Integer, Boolean> setTrackLiked) {
        this.setTrackLikedCallback = setTrackLiked;
    }

    @Override
    public void onTrackLiked(int trackPosition, String trackId) {
        SpotifyClient
                .getClient()
                .saveTrackForUser(trackId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        setTrackLikedCallback.accept(trackPosition, true);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("TrackActionListener", "Can't add track to user library: " + trackId, t);
                    }
                });
    }

    @Override
    public void onTrackUnliked(int trackPosition, String trackId) {
        SpotifyClient
                .getClient()
                .removeTrackForUser(trackId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        setTrackLikedCallback.accept(trackPosition, false);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("TrackActionListener", "Can't remove track from user library: " + trackId, t);
                    }
                });
    }
}
