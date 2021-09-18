package com.postpc.tenq.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.core.TenQApplication;
import com.postpc.tenq.databinding.ActivityRoomSettingsBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.services.SoundAwarenessService;
import com.postpc.tenq.ui.adapters.UserNamesAdapter;

public class RoomSettingsActivity extends TenQActivity {

    private Room room;
    private ActivityRoomSettingsBinding binding;
    private SoundAwarenessService soundAwareness;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.soundAwareness = ((TenQApplication) getApplication()).getSoundAwarenessService();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (room == null) {
                String roomId = extras.getString("roomId");
                fetchRoomFromFirestore(roomId);
                return;
            }
        }
        createUsersListOnScreen();
        setSettings();
    }

    private void createUsersListOnScreen() {
        // get all users name in room
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String roomId = (String) extras.getSerializable("roomId");
            FirebaseFirestore.getInstance()
                    .collection("rooms")
                    .document(roomId)
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        room = document.toObject(Room.class);
                        // show list of all user names for disabling
                        setRecyclerViewForUsers();
                    } else {
                        Log.d("RoomSettingsActivity", "get failed with ", task.getException());
                    }
                }
            });
        } else {
            //TODO else what?
        }
    }

    private void setRecyclerViewForUsers() {
        UserNamesAdapter adapter = new UserNamesAdapter(room.getGuests());
        binding.recyclerUserNames.setLayoutManager(new LinearLayoutManager(RoomSettingsActivity.this, RecyclerView.VERTICAL, false));
        binding.recyclerUserNames.setAdapter(adapter);
    }

    private void setSettings() {
        if (!getAuthService().isCurrentUserHost(room)) { // if the users are not the host then they can't change the settings
            binding.switchSoundsAwareness.setClickable(false);
            binding.switchForeignActions.setClickable(false);
        }

        binding.switchSoundsAwareness.setEnabled(soundAwareness.getRecorderService().isDeviceConnected());
        binding.switchSoundsAwareness.setChecked(soundAwareness.getRecorderService().isUserSetRecorderOn());
        binding.switchForeignActions.setEnabled(getAuthService().isCurrentUserHost(room));
        binding.switchForeignActions.setChecked(room.isUserActionsAllowed());

        binding.switchSoundsAwareness.setOnCheckedChangeListener((compoundButton, isChecked) -> {

            soundAwareness.getRecorderService().setUserSetRecorderOn(isChecked);
            if (isChecked) {
                soundAwareness.getRecorderService().startRecorder();
            } else {
                soundAwareness.getRecorderService().stopRecorder();
            }
            soundAwareness.saveCurrentRecorderState(isChecked);
        });

        binding.switchForeignActions.setOnCheckedChangeListener((compoundButton, isChecked) ->
        {
            setResult(Activity.RESULT_OK, getIntent().putExtra("actionsAllowed", isChecked));
            FirebaseFirestore.getInstance()
                    .collection("rooms")
                    .document(room.getId())
                    .update("userActionsAllowed", isChecked);
        });
    }

    private void fetchRoomFromFirestore(String roomId) {
        FirebaseFirestore.getInstance()
                .collection("rooms")
                .document(roomId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        room = task.getResult().toObject(Room.class);
                        createUsersListOnScreen();
                        setSettings();
                    } else {
                        Log.e("RoomActivity", "Can't fetch room", task.getException());
                    }
                });
    }

}
