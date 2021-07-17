package com.postpc.tenq.ui.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.postpc.tenq.models.Track;

import org.jetbrains.annotations.NotNull;

public class TracksDiffItemCallback extends DiffUtil.ItemCallback<Track> {
    @Override
    public boolean areItemsTheSame(@NonNull @NotNull Track oldItem, @NonNull @NotNull Track newItem) {
        return tracksEqual(oldItem, newItem) && usersEqual(oldItem, newItem);
    }

    private boolean tracksEqual(Track trackA, Track trackB) {
        return trackA.getId().equals(trackB.getId());
    }

    private boolean usersEqual(Track trackA, Track trackB) {
        if (trackA.getAddedBy() == null) {
            return trackB.getAddedBy() == null;
        }
        return trackB.getAddedBy() != null && trackA.getAddedBy().equals(trackB.getAddedBy());
    }

    @Override
    public boolean areContentsTheSame(@NonNull @NotNull Track oldItem, @NonNull @NotNull Track newItem) {
        return tracksEqual(oldItem, newItem);
    }
}

