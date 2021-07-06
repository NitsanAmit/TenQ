package com.postpc.tenq.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.databinding.ActivityRoomBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.adapters.TracksAdapter;
import com.postpc.tenq.ui.listeners.ITrackActionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RoomActivity extends TenQActivity {

    private ActivityRoomBinding binding;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            room = (Room) extras.getSerializable("room");
            if (room == null) {
                String roomId = extras.getString("room_id");
                //TODO fetch room details from Firestore
            }
        }

        //TODO get room playlist from Spotify
        // SpotifyClient.getClient().getPlaylist(room.getPlaylist().getUri())...


        binding.recyclerTracks.setLayoutManager(new GridLayoutManager(this, 1));
        ITrackActionListener listener = track -> {
            Toast.makeText(RoomActivity.this, "Toggle like", Toast.LENGTH_SHORT).show(); //TODO
        };
        TracksAdapter adapter = new TracksAdapter(new ArrayList<>(), listener);
//        TracksAdapter adapter = new TracksAdapter(room.getPlaylist().getTracks().getItems(), listener);
        binding.recyclerTracks.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //TODO
                // Remove item from list
                // adapter.notifyDataSetChanged();
                Toast.makeText(RoomActivity.this, "Delete!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }
        });

        itemTouchHelper.attachToRecyclerView(binding.recyclerTracks);
    }
}