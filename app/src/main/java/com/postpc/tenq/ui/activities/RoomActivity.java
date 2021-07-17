package com.postpc.tenq.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.databinding.ActivityRoomBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.adapters.TracksAdapter;
import com.postpc.tenq.ui.listeners.ITrackActionListener;
import com.postpc.tenq.ui.helpers.RoomSharingDialogUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RoomActivity extends TenQActivity {

    private ActivityRoomBinding binding;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // sets new action bar theme
        super.onCreate(savedInstanceState);


        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            room = (Room) extras.getSerializable("room");
            if (room == null) {
                String roomId = extras.getString("roomId");
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
        binding.fabAddSong.setOnClickListener(v -> startActivity(new Intent(RoomActivity.this, SongSearchActivity.class)));
    }

    /** inflate the menu */
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.inside_room, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**  control operations fpr clicking on the action buttons */
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {

        int itemId = item.getItemId();
        if (itemId == R.id.share) { // for debugging use this room id: "yYQK9C19BGy8nt5C4zxY"
            AlertDialog dialog = RoomSharingDialogUtil.getShareDialogForRoom(this, room.getId());
            if (dialog != null && !isFinishing() && !isDestroyed()) {
                dialog.show();
            }
        } else if (itemId == R.id.settings) {
            Intent intent = new Intent(RoomActivity.this, RoomSettingsActivity.class);
            intent.putExtra("roomId", room.getId()); // for debugging use this room id: "yYQK9C19BGy8nt5C4zxY"
            startActivity(intent);
        } else if (itemId == R.id.edit) {
            Toast.makeText(this, "edit Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}