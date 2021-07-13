package com.postpc.tenq.ui.activities;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.databinding.ActivityExistingRoomsBinding;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.adapters.ExistingRoomsAdapter;
import com.postpc.tenq.ui.listeners.IRoomActionListener;

import java.util.ArrayList;
import java.util.List;

public class ExistingRoomsActivity extends TenQActivity {

    private ActivityExistingRoomsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExistingRoomsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getUserExistingRooms();

        binding.fabCreateRoom.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.dialog_create_room, null);
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();
            alertDialog.show();
            view.findViewById(R.id.txt_cancel).setOnClickListener(cancel -> alertDialog.dismiss());
            view.findViewById(R.id.txt_create_room).setOnClickListener(createRoom -> {
                TextInputEditText editText = (TextInputEditText) view.findViewById(R.id.input_room_name);
                if (TextUtils.isEmpty(editText.getText())) {
                    editText.setError("Can't be empty");
                } else {
                    Intent intent = new Intent(this, RoomActivity.class);
                    intent.putExtra("room_name", editText.getText().toString());
                    startActivity(intent);
                    alertDialog.dismiss();
                }
            });
        });
        binding.fabJoinWithId.setOnClickListener(v -> startActivity(new Intent(this, JoinLinkActivity.class)));
        binding.fabScanQr.setOnClickListener(v -> startActivity(new Intent(this, JoinQrActivity.class)));
    }

    private void getUserExistingRooms() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(getAuthService().getCurrentUserId())
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        Log.d("ExistingRoomsActivity", "no rooms found.", error);
                        return;
                    }
                    List<String> roomIds = (List<String>) value.get("roomIds");
                    if (roomIds == null || roomIds.size() == 0) {
                        initRecyclerViewWithExistingRooms(new ArrayList<>());
                    }else {
                        getRoomsDetails(roomIds);
                    }
                });
    }

    private void getRoomsDetails(List<String> roomIds) {
        FirebaseFirestore.getInstance()
                .collection("rooms")
                .whereIn("id", roomIds)
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        Log.d("ExistingRoomsActivity", "no rooms found.", error);
                        return;
                    }
                    initRecyclerViewWithExistingRooms(value.toObjects(Room.class));
                });
    }

    private void initRecyclerViewWithExistingRooms(List<Room> rooms) {
        binding.recyclerExistingRooms.setLayoutManager(new LinearLayoutManager(this));
        IRoomActionListener actionListener = new IRoomActionListener() { //TODO implement
            @Override
            public void onRoomExport(Room room) {
                Toast.makeText(ExistingRoomsActivity.this, "export clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRoomDelete(Room room) {
                Toast.makeText(ExistingRoomsActivity.this, "delete clicked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRoomClose(Room room) {
                Toast.makeText(ExistingRoomsActivity.this, "close clicked!", Toast.LENGTH_SHORT).show();
            }
        };
        View.OnClickListener itemClickListener = v -> {
            int itemPosition = binding.recyclerExistingRooms.getChildLayoutPosition(v);
            Intent intent = new Intent(this, RoomActivity.class);
            intent.putExtra("room", rooms.get(itemPosition));
            startActivity(intent);
        };
        ExistingRoomsAdapter adapter = new ExistingRoomsAdapter(
                rooms != null ? rooms : new ArrayList<>(),
                actionListener,
                itemClickListener);
        binding.recyclerExistingRooms.setAdapter(adapter);
    }
}