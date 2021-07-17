package com.postpc.tenq.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.databinding.ActivityRoomBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.listeners.IOnDragStartListener;
import com.postpc.tenq.ui.listeners.PagingScrollListener;
import com.postpc.tenq.ui.adapters.TracksAdapter;
import com.postpc.tenq.ui.helpers.TrackTouchCallback;
import com.postpc.tenq.ui.helpers.RoomSharingDialogUtil;
import com.postpc.tenq.ui.listeners.ITrackActionListener;
import com.postpc.tenq.ui.listeners.TrackActionListener;
import com.postpc.tenq.viewmodels.RoomActivityViewModel;


public class RoomActivity extends TenQActivity {

    private ActivityRoomBinding binding;
    private ItemTouchHelper itemTouchHelper;
    private Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUiLoadingState(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            room = (Room) extras.getSerializable("room");
            if (room == null) {
                String roomId = extras.getString("roomId");
                fetchRoomFromFirestore(roomId);
                return;
            }
        }
        initRecyclerViewAndLoadPlaylist();
    }

    private void fetchRoomFromFirestore(String roomId) {
        FirebaseFirestore.getInstance()
                .collection("rooms")
                .document(roomId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        room = task.getResult().toObject(Room.class);
                        initRecyclerViewAndLoadPlaylist();
                    } else {
                        Log.e("RoomActivity", "Can't fetch room", task.getException());
                        binding.txtErrorState.setVisibility(View.VISIBLE);
                        binding.player.setVisibility(View.GONE);
                        binding.fabAddSong.setVisibility(View.GONE);
                        binding.fabExportPlaylist.setVisibility(View.GONE);
                    }
                });
    }

    private void initRecyclerViewAndLoadPlaylist() {
        getSupportActionBar().setTitle(room.getName());
        binding.fabAddSong.setOnClickListener(v -> startActivity(new Intent(RoomActivity.this, SongSearchActivity.class)));
        // Set the layout manager
        binding.recyclerTracks.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        // Set "Add song to library" action listener (when user clicks the heart icon)
        ITrackActionListener listener = new TrackActionListener(this::setTrackLiked);
        IOnDragStartListener onDragStartListener = viewHolder -> itemTouchHelper.startDrag(viewHolder);
        // Create the recycler view adapter
        TracksAdapter adapter = new TracksAdapter(listener, onDragStartListener, binding.progressLoadingMore);

        // Get the activity's view model
        RoomActivityViewModel model = new ViewModelProvider(this).get(RoomActivityViewModel.class);

        // Set a "load next page" listener when user scroll to the bottom of the list
        binding.recyclerTracks.addOnScrollListener(new PagingScrollListener(model::loadNextPage, binding.progressLoadingMore));

        // Set an ItemTouchHelper for removing tracks from the list with a swipe motion
        itemTouchHelper = new ItemTouchHelper(new TrackTouchCallback(model, adapter));
        itemTouchHelper.attachToRecyclerView(binding.recyclerTracks);

        binding.recyclerTracks.setAdapter(adapter);

        // Fetch the first page of tracks
        model.getTracks(room.getPlaylist().getId()).observe(this, list -> {
            adapter.submitList(list);
            if (list != null && binding.progressListLoading.getVisibility() == View.VISIBLE) {
                setUiLoadingState(false);
            }
        });

    }

    private void setTrackLiked(int trackPosition, boolean isLiked) {
        TracksAdapter tracksAdapter = (TracksAdapter) binding.recyclerTracks.getAdapter();
        tracksAdapter.getCurrentList().get(trackPosition).getTrack().setInUserLibrary(isLiked);
        tracksAdapter.notifyItemChanged(trackPosition);
    }

    private void setUiLoadingState(boolean isLoading) {
        int progressBarVisibility = isLoading ? View.VISIBLE : View.GONE;
        int otherViewsVisibility = isLoading ? View.GONE : View.VISIBLE;
        binding.progressListLoading.setVisibility(progressBarVisibility);
        binding.recyclerTracks.setVisibility(otherViewsVisibility);
        binding.player.setVisibility(otherViewsVisibility);
        binding.fabAddSong.setVisibility(otherViewsVisibility);
        binding.fabExportPlaylist.setVisibility(otherViewsVisibility);
    }

    /** inflate the menu */
    public boolean onCreateOptionsMenu( Menu menu ) {
        getMenuInflater().inflate(R.menu.menu_room_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**  control operations fpr clicking on the action buttons */
    public boolean onOptionsItemSelected(@NonNull MenuItem item ) {

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