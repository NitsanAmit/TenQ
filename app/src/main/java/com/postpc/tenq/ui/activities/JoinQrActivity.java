package com.postpc.tenq.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.User;
import com.postpc.tenq.models.UserRooms;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JoinQrActivity extends TenQActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setPrompt("");
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();

    }

    /**
     * on result of QR scanner
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // string of QR code result
                String qrResult = intentResult.getContents();

                // update fire-store
                FirebaseFirestore.getInstance().collection("rooms").document(qrResult)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Room room = document.toObject(Room.class);
                                // check if room is active
                                if (room.isActive()) {
                                    activeRoomFlow(room);
                                } else {
                                    Log.d("JoinQrActivity", "No such document");
                                }
                            } else {
                                Log.d("JoinQrActivity", "get failed with ", task.getException());
                            }
                        }
                    }

                    private void activeRoomFlow(Room room) {
                        Intent intent = new Intent(JoinQrActivity.this, RoomActivity.class);
                        intent.putExtra("room_id", qrResult);

                        // 1. add user id to guests list of room in fire-store (if not there already)
                        addUserToRoomGuests(room);
                        // 2. add room id to user rooms in fire-store (if not there already)
                        addRoomToUserRooms(room);

                        startActivity(intent);
                        finish();
                    }


                    private void addRoomToUserRooms(Room room) {
                        FirebaseFirestore.getInstance().collection("users").document(getAuthService().getCurrentUserId())
                                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        UserRooms userRooms = document.toObject(UserRooms.class);

                                        // get list
                                        List<String> allRooms = userRooms.getRooms();
                                        if (allRooms.size() == 0 || !allRooms.contains(room.getId())) {
                                            // update list with room id
                                            allRooms.add(room.getId());
                                            FirebaseFirestore.getInstance().collection("users").document
                                                    (getAuthService().getCurrentUserId()).update("roomIds", allRooms);
                                        }
                                    } else {
                                        Log.d("JoinQrActivity", "No such document");
                                    }
                                } else {
                                    Log.d("JoinQrActivity", "get failed with ", task.getException());
                                }
                            }
                        });
                    }


                    private void addUserToRoomGuests(Room room) {
                        // get list
                        List<User> roomGuests = room.getGuests();
                        // check if list contains user-id
                        boolean found = false;
                        for(User user : roomGuests){
                            if(user.getId().equals(getAuthService().getCurrentUserId())){
                                found = true;
                                break;
                            }
                        }
                        // add user to room if not inside already
                        if (!found) {
                            // update list with user-id
                            roomGuests.add(getAuthService().getCurrentUser());
                            FirebaseFirestore.getInstance().collection("rooms").document
                                    (room.getId()).update("guests", roomGuests);
                        }
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}