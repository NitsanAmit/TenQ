package com.postpc.tenq.viewmodels;

import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.postpc.tenq.models.Page;
import com.postpc.tenq.models.PlaylistTrack;
import com.postpc.tenq.network.SpotifyClient;
import com.postpc.tenq.services.player.IAudioPlayer;
import com.postpc.tenq.services.player.IPlayerService;
import com.postpc.tenq.services.player.PlayerError;
import com.postpc.tenq.services.player.PlayerErrorType;
import com.postpc.tenq.services.player.PlayerService;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.error.AuthenticationFailedException;
import com.spotify.android.appremote.api.error.CouldNotFindSpotifyApp;
import com.spotify.android.appremote.api.error.NotLoggedInException;
import com.spotify.protocol.types.PlayerContext;
import com.spotify.protocol.types.PlayerState;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomActivityViewModel extends ViewModel implements IAudioPlayer {

    public static final int PAGE_SIZE = 30;
    private final AtomicInteger offset = new AtomicInteger(0);
    private MutableLiveData<List<PlaylistTrack>> tracks;
    private String playlistId;
    private final MutableLiveData<PlayerState> playerState;
    private final MutableLiveData<PlayerContext> playerContext;
    private final MutableLiveData<PlayerError> playerError;
    private final MutableLiveData<Bitmap> playerBitmap;
    private final IPlayerService playerService;
    private boolean hasSpotifyInstalled;
    private boolean loggedInSpotify;

    public RoomActivityViewModel() {
        super();
        playerService = new PlayerService();
        playerState = new MutableLiveData<>(null);
        playerContext = new MutableLiveData<>(null);
        playerError = new MutableLiveData<>(null);
        playerBitmap = new MutableLiveData<>(null);
    }

    public LiveData<List<PlaylistTrack>> getTracks(String playlistId) {
        this.playlistId = playlistId;
        if (tracks == null) {
            tracks = new MutableLiveData<>(null);
            loadNextPage();
        }
        return tracks;
    }

    public void removeTrack(int position) {
        List<PlaylistTrack> currentTracksList = tracks.getValue();
        if (currentTracksList == null) return;
        currentTracksList.remove(position);

        // The list reference must be changed for the list adapter to notify the changes
        ArrayList<PlaylistTrack> newList = new ArrayList<>(currentTracksList);
        tracks.postValue(newList);
    }

    public void loadNextPage() {
        if (playlistId == null) return; // TODO not the best solution...
        SpotifyClient
                .getClient()
                .getPlaylistTracks(playlistId, PAGE_SIZE, offset.get())
                .enqueue(new Callback<Page<PlaylistTrack>>() {
                    @Override
                    public void onResponse(@NotNull Call<Page<PlaylistTrack>> call, @NotNull Response<Page<PlaylistTrack>> response) {
                        if (response.code() == 200) {
                            Page<PlaylistTrack> nextPage = response.body();
                            if (nextPage != null) {
                                offset.set(offset.addAndGet(PAGE_SIZE)); // increase the page offset
                                checkSavedTracksInPage(nextPage);
                                return;
                            }
                        }
                        tracks.postValue(new ArrayList<>());
                    }

                    @Override
                    public void onFailure(@NotNull Call<Page<PlaylistTrack>> call, @NotNull Throwable t) {
                        Log.e("RoomActivityViewModel", t.getMessage(), t);
                        tracks.postValue(new ArrayList<>());
                    }
                });

    }

    private void checkSavedTracksInPage(Page<PlaylistTrack> nextPage) {
        String trackIds = nextPage.getItems()
                .stream()
                .map(track -> track.getTrack().getId())
                .collect(Collectors.joining(","));

        SpotifyClient
                .getClient()
                .checkSavedTracks(trackIds)
                .enqueue(new Callback<List<Boolean>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<Boolean>> call, @NotNull Response<List<Boolean>> response) {
                        List<PlaylistTrack> newTracks = nextPage.getItems();
                        if (response.code() == 200 && response.body() != null) {
                            List<Boolean> trackInUserLibrary = response.body();
                            for (int i = 0; i < newTracks.size(); i++) {
                                newTracks.get(i).getTrack().setInUserLibrary(trackInUserLibrary.get(i));
                            }
                        }
                        appendPageToTracksList(newTracks);
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<Boolean>> call, @NotNull Throwable t) {
                        appendPageToTracksList(nextPage.getItems());
                    }
                });
    }

    private void appendPageToTracksList(List<PlaylistTrack> newTracks) {
        List<PlaylistTrack> currentTracksList = tracks.getValue();
        if (currentTracksList == null) {
            currentTracksList = newTracks;
        }else {
            currentTracksList.addAll(newTracks); // append tracks from page to existing list
        }
        // The list reference must be changed for the list adapter to notify the changes
        ArrayList<PlaylistTrack> newList = new ArrayList<>(currentTracksList);
        tracks.postValue(newList);
    }

    public void reorderPlaylistItem(int fromPosition, int toPosition, List<PlaylistTrack> currentList) {
        HashMap<String, Object> reorderDetails = new HashMap<>(4);
        reorderDetails.put("range_start", fromPosition);
        reorderDetails.put("insert_before", toPosition + 1);

        SpotifyClient
                .getClient()
                .reorderPlaylist(playlistId, reorderDetails)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        List<PlaylistTrack> items = new ArrayList<>(currentList);
                        PlaylistTrack removed = items.remove(fromPosition);
                        items.add(toPosition > fromPosition ? toPosition : toPosition+1, removed);
                        tracks.postValue(items);
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("RoomActivityViewModel", t.getMessage(), t);
                    }
                });
    }

    public void connectPlayerService(SpotifyAppRemote spotifyAppRemote){
        // Connection went smoothly on Spotify's end - so the user has the app and is authenticated
        loggedInSpotify = true;
        hasSpotifyInstalled = true;

        // Attach the play service
        playerService.connect(
                spotifyAppRemote,
                playerState::postValue,
                playerContext::postValue,
                playerBitmap::postValue,
                playerError::postValue
        );
    }

    public void disconnectPlayerService(){
        playerService.disconnect();
    }

    public void spotifyAppRemoteConnectionFailure(Throwable throwable) {
        hasSpotifyInstalled = true;
        loggedInSpotify = true;
        if (throwable instanceof CouldNotFindSpotifyApp) {
            hasSpotifyInstalled = false;
        } else if (throwable instanceof NotLoggedInException || throwable instanceof AuthenticationFailedException) {
            loggedInSpotify = false;
        }
        Log.e("RoomActivity", throwable.getMessage(), throwable);
    }

    public LiveData<PlayerState> getPlayerState() {
        return playerState;
    }

    public LiveData<PlayerContext> getPlayerContext() {
        return playerContext;
    }

    public LiveData<Bitmap> getPlayerBitmap() {
        return playerBitmap;
    }

    public LiveData<PlayerError> getPlayerError() {
        return playerError;
    }

    /**
     * MEDIA PLAYING DELEGATION FUNCTIONS
     */
    private boolean isPlayerConnected() {
        if (!hasSpotifyInstalled) {
            this.playerError.postValue(new PlayerError(PlayerErrorType.SPOTIFY_NOT_INSTALLED, null));
        } else if (!loggedInSpotify) {
            this.playerError.postValue(new PlayerError(PlayerErrorType.SPOTIFY_NOT_AUTHENTICATED, null));
        }
        return playerService.isConnected();
    }

    @Override
    public void play(String uri) {
        if (!isPlayerConnected()) return;
        PlayerContext playerContextValue = this.playerContext.getValue();
        if (playerContextValue == null || playerContextValue.uri == null || !playerContextValue.uri.equals(uri)){
            playerService.play(uri);
        } else {
            playerService.resume();
        }
    }

    @Override
    public void resume() {
        playerService.resume();
    }

    @Override
    public void pause() {
        if (!isPlayerConnected()) return;
        playerService.pause();
    }

    @Override
    public void next() {
        if (!isPlayerConnected()) return;
        playerService.next();
    }

    @Override
    public void previous() {
        if (!isPlayerConnected()) return;
        playerService.previous();
    }

    @Override
    public void seek(int progress) {
        if (!isPlayerConnected()) return;
        playerService.seek(progress);
    }



}
