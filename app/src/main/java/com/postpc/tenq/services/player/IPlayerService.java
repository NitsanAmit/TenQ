package com.postpc.tenq.services.player;

import android.graphics.Bitmap;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerContext;
import com.spotify.protocol.types.PlayerState;

public interface IPlayerService extends IAudioPlayer {

    boolean isConnected();

    void connect(SpotifyAppRemote spotifyAppRemote,
                 Subscription.EventCallback<PlayerState> stateCallback,
                 Subscription.EventCallback<PlayerContext> contextCallback,
                 CallResult.ResultCallback<Bitmap> trackImageCallback,
                 IOnPlayerError onPlayerError);

    void disconnect();

}
