package com.postpc.tenq.ui.helpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.postpc.tenq.models.Page;
import com.postpc.tenq.models.Playlist;
import com.postpc.tenq.models.PlaylistTrack;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.User;
import com.postpc.tenq.network.SpotifyClient;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExportPlaylistUtil {

    private static final AtomicBoolean isExporting = new AtomicBoolean(false);

    public static void exportPlaylist(Room room, User user, Consumer<String> toastCallback) {
        if (isExporting.get()) return;
        isExporting.set(true);
        createNewPlaylist(room, user, toastCallback);
    }

    private static void createNewPlaylist(Room room, User user, Consumer<String> toastCallback) {
        Map<String, Object> playlistDetails = new HashMap<>(4);
        playlistDetails.put("name", "TenQ exported - " + room.getName());
        playlistDetails.put("public", false);
        playlistDetails.put("description", String.format("Your personal copy of TenQ's '%s' room playlist", room.getName()));
        SpotifyClient.getClient()
                .createPlaylist(user.getId(), playlistDetails)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<Playlist> call, @NonNull Response<Playlist> response) {
                        if (response.code() == 201) {
                            getExistingPlaylistTracks(response.body(), room, toastCallback);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Playlist> call, @NonNull Throwable t) {
                        Log.e("ExportPlaylistUtil", t.getMessage(), t);
                        toastCallback.accept("Error exporting playlist: " + t.getMessage());
                        isExporting.set(false);
                    }
                });
    }

    private static void getExistingPlaylistTracks(Playlist createdPlaylist, Room room, Consumer<String> toastCallback) {
        SpotifyClient.getClient()
                .getPlaylistTracks(room.getPlaylist().getId(), 100, 0) // 100 is the page limit
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NotNull Call<Page<PlaylistTrack>> call, @NotNull Response<Page<PlaylistTrack>> response) {
                        if (response.code() == 200) {
                            addTracksToPlaylist(response.body(), createdPlaylist, toastCallback);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Page<PlaylistTrack>> call, @NotNull Throwable t) {
                        Log.e("ExportPlaylistUtil", t.getMessage(), t);
                        toastCallback.accept("Error exporting playlist: " + t.getMessage());
                        isExporting.set(false);
                    }
                });
    }

    private static void addTracksToPlaylist(@NonNull Page<PlaylistTrack> tracksPage, Playlist createdPlaylist, Consumer<String> toastCallback) {
        SpotifyClient
                .getClient()
                .addTrackToPlaylist(createdPlaylist.getId(), tracksPage.getItems()
                        .stream()
                        .map(track -> track.getTrack().getUri())
                        .collect(Collectors.joining(","))
                )
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        toastCallback.accept("Exported successfully");
                        isExporting.set(false);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("ExportPlaylistUtil", t.getMessage(), t);
                        toastCallback.accept("Error exporting playlist: " + t.getMessage());
                        isExporting.set(false);
                    }
                });
    }
}
