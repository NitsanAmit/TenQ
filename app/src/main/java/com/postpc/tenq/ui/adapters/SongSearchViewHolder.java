package com.postpc.tenq.ui.adapters;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;

import org.jetbrains.annotations.NotNull;

public class SongSearchViewHolder extends RecyclerView.ViewHolder{

    TextView songName;
    TextView artistName;
    ImageButton addButton;

    public SongSearchViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        songName = itemView.findViewById(R.id.txt_song_name);
        artistName = itemView.findViewById(R.id.txt_song_artist);
        addButton = itemView.findViewById(R.id.button_add);
    }
}
