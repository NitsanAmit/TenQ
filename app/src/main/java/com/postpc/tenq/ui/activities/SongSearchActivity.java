package com.postpc.tenq.ui.activities;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.databinding.ActivitySongSearchBinding;
import com.postpc.tenq.ui.adapters.PagingScrollListener;
import com.postpc.tenq.ui.adapters.SongSearchAdapter;
import com.postpc.tenq.viewmodels.SongSearchActivityViewModel;

public class SongSearchActivity extends TenQActivity {

    private ActivitySongSearchBinding binding;
    private SongSearchActivityViewModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySongSearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        model = new ViewModelProvider(this).get(SongSearchActivityViewModel.class);
        setSearchSongRecycler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search_songs, menu);

        // Get the SearchView and set the searchable configuration
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Search song");
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                model.searchSongs(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    private void setSearchSongRecycler() {
        SongSearchAdapter adapter = new SongSearchAdapter(track -> model.addTrackToRoomPlaylist(null, track), binding.progressLoadingMore); //TODO pass room
        model.getResults().observe(this, adapter::submitList);

        binding.recyclerSongs.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recyclerSongs.addOnScrollListener(new PagingScrollListener(model::loadNextPage, binding.progressLoadingMore));
        binding.recyclerSongs.setAdapter(adapter);
    }

}
