package com.postpc.tenq.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.Track;
import com.postpc.tenq.ui.listeners.IRoomActionListener;
import com.postpc.tenq.ui.listeners.ITrackActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TracksAdapter extends RecyclerView.Adapter<TrackViewHolder> {

    private final List<Track> items;
    private final ITrackActionListener actionListener;
    private boolean binding;


    public TracksAdapter(List<Track> items, ITrackActionListener actionListener) {
        this.items = items;
        this.actionListener = actionListener;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @NonNull
    @NotNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TrackViewHolder holder, int position) {
        binding = true;
        Track track = this.items.get(position);
        holder.trackName.setText(track.getName());
        holder.trackDescription.setText(String.format("Added by %s at %s",
                track.getAddedBy().getName(), track.getAddedAt().toString()));
        if (track.isInUserLibrary()) {
            holder.emptyLikeIcon.setVisibility(View.GONE);
            holder.fullLikeIcon.setVisibility(View.VISIBLE);
        } else {
            holder.emptyLikeIcon.setVisibility(View.VISIBLE);
            holder.fullLikeIcon.setVisibility(View.GONE);
        }
        binding = false;
    }
}
