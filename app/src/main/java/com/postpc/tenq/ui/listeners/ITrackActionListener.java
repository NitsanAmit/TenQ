package com.postpc.tenq.ui.listeners;

public interface ITrackActionListener {

    void onTrackLiked(int trackPosition, String trackId);

    void onTrackUnliked(int trackPosition, String trackId);

    void onTrackPlay(int trackPosition);

}
