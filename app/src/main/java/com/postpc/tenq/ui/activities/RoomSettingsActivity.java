package com.postpc.tenq.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.adapters.UserNamesAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RoomSettingsActivity extends TenQActivity {

    Room room;
    List<String> userNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_settings);
        // todo use button with services (sound awareness)


        // todo - need to decide if each user has different permissions for adding and deleting song, or all have the same permissions
        createUsersListOnScreen();

    }

    private void createUsersListOnScreen() {
        // get all users name in room
        String roomId = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            roomId = (String) extras.getSerializable("room_id");
            if (roomId == null) {
                // todo - error
            }
                }

        FirebaseFirestore.getInstance().collection("rooms").document(roomId)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                 @Override
                                                 public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                     if (task.isSuccessful()) {
                                                         DocumentSnapshot document = task.getResult();
                                                         if (document.exists()) {
                                                             room = document.toObject(Room.class);
                                                             // show list of all user names for disabling
                                                             setRecyclerViewForUsers();
                                                         } else {
                                                             Log.d("RoomSettingsActivity", "get failed with ", task.getException());
                                                         }
                                                     }
                                                 }

            private void setRecyclerViewForUsers() {
                RecyclerView recyclerView = findViewById(R.id.recycler_user_names);
                UserNamesAdapter adapter = new UserNamesAdapter(room.getGuests());
                recyclerView.setLayoutManager(new LinearLayoutManager(RoomSettingsActivity.this, RecyclerView.VERTICAL, false));
                recyclerView.setAdapter(adapter);
            }
        });
    }


}
