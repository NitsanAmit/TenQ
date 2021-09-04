package com.postpc.tenq.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.core.TenQApplication;
import com.postpc.tenq.databinding.ActivityRoomBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.services.RecorderService;
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
    private RecorderService recorder; //TODO Noam should not be static
    //1 - make it not static here
    // 2 - move it to the application class, and then it will outlive the activity, so you don't need to instantiate it every time the activity starts

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

        this.recorder = ((TenQApplication) getApplication()).getRecorderService();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) { //TODO NOAM only if sound awareness is on
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recorder.startRecorder();
            }else{
                Log.e("[RECORD_AUDIO]", "Permission denied");
            }
        }
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
        TracksAdapter adapter = new TracksAdapter(listener, onDragStartListener, binding.progressLoadingMore, room, getAuthService().getCurrentUser());

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

    @Override
    protected void onStart() {
        super.onStart();
        //this.recorder.startRecorder(); TODO NOAM only call this is if the user enabled sound awareness...
    }

    @Override
    protected void onStop() {
        //this.recorder.stopRecorder(); TODO NOAM only call this is if the user enabled sound awareness...
        super.onStop();
    }
}