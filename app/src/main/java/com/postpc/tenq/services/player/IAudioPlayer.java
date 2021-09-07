package com.postpc.tenq.services.player;

public interface IAudioPlayer {

    void play(String uri);

    void resume();

    void pause();

    void next();

    void previous();

    void seek(int progress);
}
