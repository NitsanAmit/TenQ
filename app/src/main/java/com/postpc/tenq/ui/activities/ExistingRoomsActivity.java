package com.postpc.tenq.ui.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.ui.adapters.ExistingRoomsAdapter;
import com.postpc.tenq.ui.listeners.IRoomActionListener;

import java.util.ArrayList;
import java.util.List;

public class ExistingRoomsActivity extends TenQActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_rooms);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(getAuthService().getCurrentUserId())
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        Log.d("ExistingRoomsActivity", "no rooms found.", error);
                        return;
                    }
                    getRooms((List<String>) value.get("roomIds"));
                    Log.d("ExistingRoomsActivity", "User rooms found: " + value.toString());
                });

    }

    private void getRooms(List<String> roomIds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms")
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
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_existing_rooms);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        IRoomActionListener listener = new IRoomActionListener(){ //TODO implement
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
        ExistingRoomsAdapter adapter = new ExistingRoomsAdapter(rooms != null ? rooms : new ArrayList<>(), listener);
        recyclerView.setAdapter(adapter);
    }
}