package com.postpc.tenq.network;

public class SpotifyClientNotInitializedException extends RuntimeException {

    @Override
    public String getMessage() {
        return "The Spotify client must be initialized before an attempt to use it.";
    }
}
