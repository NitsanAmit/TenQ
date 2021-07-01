package com.postpc.tenq.activities;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;

public class RoomActivity extends TenQActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("rooms")
                .document(getAuthService().getCurrentUserId())
                .addSnapshotListener((value, error) -> {
                    if (value == null) {
                        Log.d("MainActivity", "no rooms found.", error);
                        return;
                    }
                    Log.d("MainActivity", "User rooms found: " + value.toString());
                });
    }
}