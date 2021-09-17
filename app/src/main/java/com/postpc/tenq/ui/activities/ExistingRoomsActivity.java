package com.postpc.tenq.ui.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.databinding.ActivityExistingRoomsBinding;
import com.postpc.tenq.models.Playlist;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.User;
import com.postpc.tenq.network.SpotifyClient;
import com.postpc.tenq.ui.adapters.ExistingRoomsAdapter;
import com.postpc.tenq.ui.listeners.IRoomActionListener;
import com.postpc.tenq.viewmodels.ViewModelError;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExistingRoomsActivity extends TenQActivity {
    private ActivityExistingRoomsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExistingRoomsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //TODO move all the firebase logic to a viewmodel
        getUserExistingRooms();

        binding.fabCreateRoom.setOnClickListener(v -> {
            View view = getLayoutInflater().inflate(R.layout.dialog_create_room, null);
            AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();
            alertDialog.show();
            view.findViewById(R.id.txt_cancel).setOnClickListener(cancel -> alertDialog.dismiss());
            view.findViewById(R.id.txt_create_room).setOnClickListener(createRoom -> {
                TextInputEditText editText = view.findViewById(R.id.input_room_name);
                if (TextUtils.isEmpty(editText.getText())) {
                    editText.setError("Can't be empty");
                } else {
                    createNewRoom(editText.getText().toString());
                    alertDialog.dismiss();
                }
            });
        });
        binding.fabJoinWithId.setOnClickListener(v -> startActivity(new Intent(this, JoinLinkActivity.class)));
        binding.fabScanQr.setOnClickListener(v -> startActivity(new Intent(this, JoinQrActivity.class)));
    }

    private void createNewRoom(String roomName) {
        User currentUser = getAuthService().getCurrentUser();
        Map<String, Object> playlistDetails = new HashMap<>(4);
        playlistDetails.put("name", "TenQ - " + roomName);
        playlistDetails.put("public", false);
        playlistDetails.put("collaborative", true);
        playlistDetails.put("description", String.format("Your TenQ Collaborative playlist, for room '%s'", roomName));
        SpotifyClient.getClient().createPlaylist(currentUser.getId(), playlistDetails).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    Room newRoom = new Room(roomName, currentUser);
                    newRoom.setPlaylist(response.body());
                    saveRoomInFirestore(newRoom);
                } else {
                    Toast.makeText(ExistingRoomsActivity.this, "Error creating room - try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.e("RoomActivity", "Can't create room", t);
                Toast.makeText(ExistingRoomsActivity.this, "Error creating room - try again later", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveRoomInFirestore(Room newRoom) {
        FirebaseFirestore.getInstance()
                .collection("rooms")
                .add(newRoom)
                .addOnSuccessListener(documentReference -> {
                    String roomId = documentReference.getId();
                    newRoom.setId(roomId);
                    documentReference.update("id", roomId);
                    pushRoomIdToUserRooms(roomId);
                    Intent intent = new Intent(ExistingRoomsActivity.this, RoomActivity.class);
                    intent.putExtra("room", newRoom);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Log.e("RoomActivity", "Can't create room", e);
                    Toast.makeText(ExistingRoomsActivity.this, "Error creating room - try again later", Toast.LENGTH_SHORT).show();
                });
    }

    private void pushRoomIdToUserRooms(String roomId) {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(getAuthService().getCurrentUserId())
                .update("roomIds", FieldValue.arrayUnion(roomId));
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
                    } else {
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
                HashMap<String, Object> bodyDetails = new HashMap<>(1);
                bodyDetails.put("public", false);
                SpotifyClient
                        .getClient()
                        .followPlaylist(room.getPlaylist().getId(), bodyDetails)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                                Toast.makeText(ExistingRoomsActivity.this, "You can now find the playlist on spotify library", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                                Log.e("ExistingRoomsActivity", t.getMessage(), t);
                            }
                        });
            }


            @Override
            public void onRoomDelete(Room room) {
                deleteRoom(room, getAuthService().getCurrentUser());
            }

            @Override
            public void onRoomClose(Room room) {
                if (!room.getHost().getId().equals(getAuthService().getCurrentUser().getId())) {
                    Toast.makeText(ExistingRoomsActivity.this, "Only room host can close room", Toast.LENGTH_SHORT).show();
                } else {
                    // show alert dialog for closing room
                    new AlertDialog.Builder(ExistingRoomsActivity.this)
                            .setTitle(R.string.txt_close_room)
                            .setMessage(R.string.txt_body_close_room)
                            .setPositiveButton(R.string.txt_close_room_red, (dialog, which) -> {
                                FirebaseFirestore.getInstance()
                                        .collection("rooms").document(room.getId())
                                        .update("active", false)
                                        .addOnSuccessListener(documentReference -> {
                                            Toast.makeText(ExistingRoomsActivity.this, "Success room closed", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("ExistingRoomsActivity", "Can't close room", e);
                                            Toast.makeText(ExistingRoomsActivity.this, "Error closing room - try again later", Toast.LENGTH_SHORT).show();
                                        });
                                dialog.dismiss();
                            })
                            .setNeutralButton(R.string.cancel, (dialog, where) -> dialog.dismiss())
                            .create().show();
                    room.setActive(false);
                }
            }
        };

        View.OnClickListener itemClickListener = v -> {
            int itemPosition = binding.recyclerExistingRooms.getChildLayoutPosition(v);
            if (rooms.get(itemPosition).isActive()) {
                Intent intent = new Intent(this, RoomActivity.class);
                intent.putExtra("room", rooms.get(itemPosition));
                startActivity(intent);
            }
        };

        ExistingRoomsAdapter adapter = new ExistingRoomsAdapter(
                rooms != null ? rooms : new ArrayList<>(),
                actionListener,
                itemClickListener,
                getAuthService().getCurrentUserId());
        binding.recyclerExistingRooms.setAdapter(adapter);
    }

    private void deleteRoom(Room room, User user) {
        SpotifyClient
                .getClient()
                .unfollowPlaylist(room.getPlaylist().getId())
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (room.getHost().getId().equals(user.getId())) {
                            FirebaseFirestore.getInstance()
                                    .collection("rooms")
                                    .document(room.getId())
                                    .delete()
                                    .addOnFailureListener(e -> Toast.makeText(ExistingRoomsActivity.this, "Error deleting room", Toast.LENGTH_SHORT).show());
                        } else {
                            removeUserFromRoom(room, user);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Toast.makeText(ExistingRoomsActivity.this, "Error deleting room", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void removeUserFromRoom(Room roomToDelete, User user) {
        FirebaseFirestore instance = FirebaseFirestore.getInstance();
        DocumentReference userRef = instance.collection("users").document(user.getId());
        DocumentReference roomRef = instance.collection("rooms").document(roomToDelete.getId());
        instance
                .batch()
                .update(userRef, "roomIds", FieldValue.arrayRemove(roomToDelete.getId()))
                .update(roomRef, "guests", FieldValue.arrayRemove(user))
                .commit()
                .addOnFailureListener(e -> {
                    Toast.makeText(ExistingRoomsActivity.this, "Error deleting user from room, try again later", Toast.LENGTH_SHORT).show();
                    Log.e("ExistingRoomsActivity", e.getMessage(), e);
                });
    }
}