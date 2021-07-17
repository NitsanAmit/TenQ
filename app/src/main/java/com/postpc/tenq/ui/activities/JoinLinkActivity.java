package com.postpc.tenq.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.databinding.ActivityJoinLinkBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.viewmodels.JoinActivityViewModel;

public class JoinLinkActivity extends TenQActivity {

    private static final int ID_LENGTH = 20;
    private ActivityJoinLinkBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJoinLinkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        JoinActivityViewModel model = new ViewModelProvider(this).get(JoinActivityViewModel.class);

        model.getJoinedRoom().observe(this, joinedRoom -> {
            if (joinedRoom != null) {
                startRoomActivity(joinedRoom);
            }
        });

        model.getViewModelError().observe(this, error -> {
            if (error == null) return;
            Toast.makeText(this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            clearUi();
        });

        binding.btnJoinRoom.setOnClickListener(v -> {
            Editable text = binding.editTxtRoomId.getText();
            if (TextUtils.isEmpty(text) || text.toString().length() < ID_LENGTH) {
                binding.editTxtRoomId.setError("Insert the ID of the room you'd like to join");
            } else {
                binding.btnJoinRoom.setEnabled(false);
                model.joinRoom(text.toString(), getAuthService().getCurrentUser());
            }
        });
    }

    private void startRoomActivity(Room room) {
        Intent intent = new Intent(JoinLinkActivity.this, RoomActivity.class);
        intent.putExtra("room", room);
        startActivity(intent);
        finish();
    }

    private void clearUi() {
        binding.editTxtRoomId.setText("");
        binding.btnJoinRoom.setEnabled(true);
    }
}