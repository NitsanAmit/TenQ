package com.postpc.tenq.services.player;

import android.graphics.Bitmap;
import android.util.Log;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerContext;
import com.spotify.protocol.types.PlayerState;

public class PlayerService implements IPlayerService {

    private SpotifyAppRemote spotifyAppRemote;
    private Subscription<PlayerState> playerStateSubscription;
    private Subscription<PlayerContext> playerContextSubscription;
    private IOnPlayerError onPlayerError;
    private String currentSongUri;

    @Override
    public void connect(SpotifyAppRemote spotifyAppRemote,
                        Subscription.EventCallback<PlayerState> stateCallback,
                        Subscription.EventCallback<PlayerContext> contextCallback,
                        CallResult.ResultCallback<Bitmap> trackImageCallback,
                        IOnPlayerError onPlayerError) {
        SpotifyAppRemote.disconnect(this.spotifyAppRemote);
        this.spotifyAppRemote = spotifyAppRemote;
        this.onPlayerError = onPlayerError;

        // Get current state
        spotifyAppRemote.getPlayerApi().getPlayerState().setResultCallback(stateCallback::onEvent);

        // Subscribe to player state
        if (playerStateSubscription != null && !playerStateSubscription.isCanceled()) {
            playerStateSubscription.cancel();
            playerStateSubscription = null;
        }
        playerStateSubscription = (Subscription<PlayerState>) spotifyAppRemote
                .getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(data -> {
                    if (data.track != null && !data.track.uri.equals(currentSongUri)) {
                        currentSongUri = data.track.uri;
                        spotifyAppRemote.getImagesApi().getImage(data.track.imageUri).setResultCallback(trackImageCallback);
                    }
                    stateCallback.onEvent(data);
                })
                .setErrorCallback(throwable -> {
                    Log.e("PlayerService", "Error subscribing to player state: ", throwable);
                    onPlayerError.onError(new PlayerError(PlayerErrorType.SPOTIFY_CONNECTION_ERROR, throwable));
                });

        // Subscribe to player context
        if (playerContextSubscription != null && !playerContextSubscription.isCanceled()) {
            playerContextSubscription.cancel();
            playerContextSubscription = null;
        }

        playerContextSubscription = (Subscription<PlayerContext>) spotifyAppRemote
                .getPlayerApi()
                .subscribeToPlayerContext()
                .setEventCallback(contextCallback)
                .setErrorCallback(throwable -> {
                    Log.e("PlayerService", "Error subscribing to player context: ", throwable);
                    onPlayerError.onError(new PlayerError(PlayerErrorType.SPOTIFY_CONNECTION_ERROR, throwable));
                });

    }

    //TODO getUserAPI remove songs from here!
    @Override
    public void disconnect() {
        SpotifyAppRemote.disconnect(spotifyAppRemote);
    }

    @Override
    public boolean isConnected() {
        return this.spotifyAppRemote != null &&this.spotifyAppRemote.isConnected();
    }

    @Override
    public void play(String uri) {
        this.spotifyAppRemote
                .getPlayerApi()
                .play(uri)
                .setErrorCallback(t -> onPlayerError.onError(new PlayerError(PlayerErrorType.PLAYBACK_ERROR, t)));
    }

    @Override
    public void resume() {
        this.spotifyAppRemote
                .getPlayerApi()
                .resume()
                .setErrorCallback(t -> onPlayerError.onError(new PlayerError(PlayerErrorType.PLAYBACK_ERROR, t)));
    }

    @Override
    public void pause() {
        this.spotifyAppRemote
                .getPlayerApi()
                .pause()
                .setErrorCallback(t -> onPlayerError.onError(new PlayerError(PlayerErrorType.PLAYBACK_ERROR, t)));
    }

    @Override
    public void next() {
        this.spotifyAppRemote
                .getPlayerApi()
                .skipNext()
                .setErrorCallback(t -> onPlayerError.onError(new PlayerError(PlayerErrorType.PLAYBACK_ERROR, t)));
    }

    @Override
    public void previous() {
        this.spotifyAppRemote
                .getPlayerApi()
                .skipPrevious()
                .setErrorCallback(t -> onPlayerError.onError(new PlayerError(PlayerErrorType.PLAYBACK_ERROR, t)));
    }

    @Override
    public void seek(int progress) {
        this.spotifyAppRemote
                .getPlayerApi()
                .seekTo(progress)
                .setErrorCallback(t -> onPlayerError.onError(new PlayerError(PlayerErrorType.PLAYBACK_ERROR, t)));
    }

}
