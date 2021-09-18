package com.postpc.tenq.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;

import com.bumptech.glide.Glide;
import com.postpc.tenq.R;
import com.postpc.tenq.models.SpotifyEntity;
import com.postpc.tenq.models.Track;
import com.postpc.tenq.ui.activities.ExistingRoomsActivity;
import com.postpc.tenq.ui.activities.SongSearchActivity;
import com.postpc.tenq.ui.adapters.viewholders.SongSearchViewHolder;
import com.postpc.tenq.ui.helpers.TracksDiffItemCallback;
import com.postpc.tenq.ui.listeners.IOnSongAddClickedListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class SongSearchAdapter extends ListAdapter<Track, SongSearchViewHolder> {

    private boolean binding;
    private final ProgressBar progressBar;
    private final IOnSongAddClickedListener listener;

    public SongSearchAdapter(IOnSongAddClickedListener listener, ProgressBar progressLoading) {
        super(new TracksDiffItemCallback());
        this.listener = listener;
        this.progressBar = progressLoading;
    }

    @NonNull
    @NotNull
    @Override
    public SongSearchViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_search, parent, false);
        return new SongSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SongSearchViewHolder holder, int position) {
        binding = true;
        Track track = getItem(position);
        holder.songName.setText(track.getName());
        holder.artistName.setText(track.getArtists().stream().map(SpotifyEntity::getName).collect(Collectors.joining(",")));
        holder.addSongButton.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Added song to playlist", Toast.LENGTH_SHORT).show();
            if (binding) return;
            listener.songAddClicked(track);
        });
        if (hasCoverImage(track)) {
            Glide.with(holder.albumCover).load(track.getAlbum().getImages().get(0).getUrl()).placeholder(R.drawable.ic_music_note).into(holder.albumCover);
        } else {
            Glide.with(holder.albumCover).load(R.drawable.ic_music_note).into(holder.albumCover);
        }
        binding = false;
    }

    @Override
    public void onCurrentListChanged(@NonNull @NotNull List<Track> previousList, @NonNull @NotNull List<Track> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        progressBar.setVisibility(View.GONE);
        notifyDataSetChanged();
    }

    private boolean hasCoverImage(Track trackData) {
        return trackData.getAlbum() != null && trackData.getAlbum().getImages() != null && trackData.getAlbum().getImages().size() > 0;
    }

}
