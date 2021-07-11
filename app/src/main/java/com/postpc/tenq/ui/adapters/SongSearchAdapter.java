package com.postpc.tenq.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.models.searchModels.Item;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SongSearchAdapter extends RecyclerView.Adapter<SongSearchViewHolder>{

    private List<Item> songList;
    private boolean isBind;

    public SongSearchAdapter(List<Item> items){
        this.songList = items;
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
        this.isBind = true;
        Item currentItem = songList.get(position);
        holder.songName.setText(currentItem.getName());
        holder.artistName.setText(currentItem.getArtists().get(0).getName());

        holder.addButton.setOnClickListener(view -> {
            addSongToPlayList(view);
            notifyDataSetChanged();
        });
    }

    private void addSongToPlayList(View view) {
        // todo - add song to play list and return to room
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
}
