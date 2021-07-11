package com.postpc.tenq.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.User;
import com.postpc.tenq.models.UserRooms;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JoinLinkActivity extends TenQActivity {

    EditText insertRoomCode;
    Button joinButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_link);

        insertRoomCode = findViewById(R.id.insert_room_code);
        joinButton = findViewById(R.id.join_button);

        joinButton.setOnClickListener(v -> joinRoom());

    }

    private void joinRoom() {
        if (TextUtils.isEmpty(insertRoomCode.getText())) {
            insertRoomCode.setError("name error");
            return;
        }
        String roomCode = insertRoomCode.getText().toString();


        // update fire-store
        FirebaseFirestore.getInstance().collection("rooms").document(roomCode)
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
                            Log.d("JoinLinkActivity", "No such document");
                        }
                    } else {
                        Log.d("JoinLinkActivity", "get failed with ", task.getException());
                    }
                }
            }

            private void activeRoomFlow(Room room) {
                Intent intent = new Intent(JoinLinkActivity.this, ExistingRoomsActivity.class);
                intent.putExtra("room_id", roomCode);

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
                                Log.d("JoinLinkActivity", "No such document");
                            }
                        } else {
                            Log.d("JoinLinkActivity", "get failed with ", task.getException());
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


}