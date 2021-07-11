package com.postpc.tenq.ui.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.models.searchModels.SearchItem;
import com.postpc.tenq.network.SpotifyClient;
import com.postpc.tenq.ui.adapters.SongSearchAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongSearchActivity extends TenQActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    SongSearchAdapter adapter;
    String offset = "0";
    String limit = "20";
    String searchingSong;

    SearchItem songArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_search);
        searchView = findViewById(R.id.search_song);
        recyclerView = findViewById(R.id.recycler_songs);
        adapter = new SongSearchAdapter(new ArrayList<>());


        recyclerView.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchingSong = query;
                performSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }


            private void performSearch() {
                searchingSong = searchingSong.trim();
                searchingSong = searchingSong.replaceAll(" ", "%20");

                SpotifyClient
                        .getClient()
                        .getSong(searchingSong, "track", limit, offset)
                        .enqueue(new Callback<SearchItem>() {
                            @Override
                            public void onResponse(@NotNull Call<SearchItem> call, @NotNull Response<SearchItem> response) {
                                if (response.code() == 200) {
                                    songArrayList = response.body();
                                    setSearchSongRecycler();
                                    if (songArrayList == null) return; //TODO error
                                    Log.d("SongSearchActivity", "Got Songs!\n");
                                }
                            }

                            private void setSearchSongRecycler() {
                                RecyclerView recyclerView = findViewById(R.id.recycler_songs);
                                SongSearchAdapter adapter = new SongSearchAdapter(songArrayList.getTracks().getItems());
                                recyclerView.setLayoutManager(new LinearLayoutManager(SongSearchActivity.this, RecyclerView.VERTICAL, false));
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onFailure(retrofit2.Call<SearchItem> call, Throwable error) {
                                Log.e("SongSearchActivity", "Get songs failure: ", error);
                            }
                        });

            }

        });


    }
}
