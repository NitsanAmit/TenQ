package com.postpc.tenq.ui.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;

import org.jetbrains.annotations.NotNull;

public class SongSearchViewHolder extends RecyclerView.ViewHolder{

    public TextView songName;
    public TextView artistName;
    public ImageView albumCover;
    public ImageView addSongButton;

    public SongSearchViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        songName = itemView.findViewById(R.id.txt_song_name);
        albumCover = itemView.findViewById(R.id.img_album_cover);
        artistName = itemView.findViewById(R.id.txt_song_artist);
        addSongButton = itemView.findViewById(R.id.icon_add_song);
    }
}
