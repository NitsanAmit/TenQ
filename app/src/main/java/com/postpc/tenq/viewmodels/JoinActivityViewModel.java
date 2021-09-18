package com.postpc.tenq.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.User;
import com.postpc.tenq.network.SpotifyClient;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivityViewModel extends ViewModel {

    private final MutableLiveData<ViewModelError> error = new MutableLiveData<>(null);
    private final MutableLiveData<Room> joinedRoom = new MutableLiveData<>(null);

    public LiveData<ViewModelError> getViewModelError() {
        return error;
    }

    public LiveData<Room> getJoinedRoom() {
        return joinedRoom;
    }

    public void joinRoom(String roomId, User user) {
        FirebaseFirestore.getInstance()
                .collection("rooms")
                .document(roomId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            Room room = document.toObject(Room.class);
                            // check if room is active
                            if (room != null && room.isActive()) {
                                addUserToRoom(room, user);
                            } else {
                                error.postValue(new ViewModelError("Can't join, room inactive"));
                            }
                        } else {
                            error.postValue(new ViewModelError("Error finding room, check the id is correct"));
                        }
                    }
                });
    }

    private void addUserToRoom(Room roomToJoin, User user) {
        HashMap<String, Object> bodyDetails = new HashMap<>(1);
        bodyDetails.put("public", true);
        SpotifyClient
                .getClient()
                .followPlaylist(roomToJoin.getPlaylist().getId(), bodyDetails)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        FirebaseFirestore instance = FirebaseFirestore.getInstance();
                        DocumentReference userRef = instance.collection("users").document(user.getId());
                        DocumentReference roomRef = instance.collection("rooms").document(roomToJoin.getId());
                        instance
                                .batch()
                                .update(userRef, "roomIds", FieldValue.arrayUnion(roomToJoin.getId()))
                                .update(roomRef, "guests", FieldValue.arrayUnion(user))
                                .commit()
                                .addOnSuccessListener(unused -> joinedRoom.postValue(roomToJoin))
                                .addOnFailureListener(e -> {
                                    error.postValue(new ViewModelError("Error adding user to room, try again later"));
                                    Log.e("JoinLinkActivity", e.getMessage(), e);
                                });
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Log.e("JoinActivityViewModel", t.getMessage(), t);
                    }
                });
    }
}

