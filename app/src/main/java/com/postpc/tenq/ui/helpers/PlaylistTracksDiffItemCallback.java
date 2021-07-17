package com.postpc.tenq.ui.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.postpc.tenq.models.PlaylistTrack;

import org.jetbrains.annotations.NotNull;

public class PlaylistTracksDiffItemCallback extends DiffUtil.ItemCallback<PlaylistTrack> {

    private final TracksDiffItemCallback tracksDiffItemCallback = new TracksDiffItemCallback();

    @Override
    public boolean areItemsTheSame(@NonNull @NotNull PlaylistTrack oldItem, @NonNull @NotNull PlaylistTrack newItem) {
        return tracksDiffItemCallback.areItemsTheSame(oldItem.getTrack(), newItem.getTrack());
    }

    @Override
    public boolean areContentsTheSame(@NonNull @NotNull PlaylistTrack oldItem, @NonNull @NotNull PlaylistTrack newItem) {
        return tracksDiffItemCallback.areContentsTheSame(oldItem.getTrack(), newItem.getTrack());
    }
}

