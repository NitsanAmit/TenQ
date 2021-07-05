package com.postpc.tenq.ui.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;

import org.jetbrains.annotations.NotNull;

public class TrackViewHolder extends RecyclerView.ViewHolder {

    TextView trackName;
    TextView trackDescription;
    ImageView albumCover;
    ImageView fullLikeIcon;
    ImageView emptyLikeIcon;

    public TrackViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        trackName = itemView.findViewById(R.id.txt_song_name);
        trackDescription = itemView.findViewById(R.id.txt_song_description);
        albumCover = itemView.findViewById(R.id.img_album_cover);
        fullLikeIcon = itemView.findViewById(R.id.icon_like_full);
        emptyLikeIcon = itemView.findViewById(R.id.icon_like_empty);
    }

}
