package com.postpc.tenq.ui.listeners;

import android.util.Log;

import com.postpc.tenq.models.PlaylistTrack;
import com.postpc.tenq.models.Track;
import com.postpc.tenq.network.SpotifyClient;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackActionListener implements ITrackActionListener {

    private final BiConsumer<Integer, Boolean> setTrackLikedCallback;
    private final Consumer<Integer> playTrackCallback;

    public TrackActionListener(BiConsumer<Integer, Boolean> setTrackLiked, Consumer<Integer> playTrackCallback) {
        this.setTrackLikedCallback = setTrackLiked;
        this.playTrackCallback = playTrackCallback;
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

    @Override
    public void onTrackPlay(int trackPosition) {
        playTrackCallback.accept(trackPosition);
    }
}
