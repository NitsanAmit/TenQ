package com.postpc.tenq.ui.adapters.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;

import org.jetbrains.annotations.NotNull;

public class TrackViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    private static final float DRAG_ELEVATION = 60f;
    public TextView trackName;
    public TextView trackDescription;
    public ImageView albumCover;
    public ImageView fullLikeIcon;
    public ImageView emptyLikeIcon;
    public ImageView reorderIcon;
    public ProgressBar loader;

    public TrackViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        trackName = itemView.findViewById(R.id.txt_song_name);
        trackDescription = itemView.findViewById(R.id.txt_song_description);
        albumCover = itemView.findViewById(R.id.img_album_cover);
        fullLikeIcon = itemView.findViewById(R.id.icon_like_full);
        emptyLikeIcon = itemView.findViewById(R.id.icon_like_empty);
        reorderIcon = itemView.findViewById(R.id.icon_reorder);
        loader = itemView.findViewById(R.id.progress_loading_more);
    }


    @Override
    public void onItemSelected() {
        itemView.setElevation(DRAG_ELEVATION);
        itemView.setBackgroundColor(0xFFECECEC);
    }

    @Override
    public void onItemClear() {
        itemView.setElevation(0);
        itemView.setBackgroundColor(0);
    }
}
