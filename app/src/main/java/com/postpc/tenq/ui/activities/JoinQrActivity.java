package com.postpc.tenq.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.viewmodels.JoinActivityViewModel;

public class JoinQrActivity extends TenQActivity {

    private JoinActivityViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_qr);
        model = new ViewModelProvider(this).get(JoinActivityViewModel.class);

        model.getJoinedRoom().observe(this, joinedRoom -> {
            if (joinedRoom != null) {
                startRoomActivity(joinedRoom);
            }
        });

        model.getViewModelError().observe(this, error -> {
            if (error == null) return;
            Toast.makeText(this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
        });

        new IntentIntegrator(this)
                .setPrompt("")
                .setOrientationLocked(true)
                .initiateScan();
    }

    /**
     * on result of QR scanner
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null && intentResult.getContents() != null) {
            // string of QR code result
            String roomId = intentResult.getContents();
            model.joinRoom(roomId, getAuthService().getCurrentUser());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            finish();
        }
    }

    private void startRoomActivity(Room room) {
        Intent intent = new Intent(JoinQrActivity.this, RoomActivity.class);
        intent.putExtra("room", room);
        startActivity(intent);
        finish();
    }

}