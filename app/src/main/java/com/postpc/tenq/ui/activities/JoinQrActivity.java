package com.postpc.tenq.ui.activities;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.postpc.tenq.R;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class JoinQrActivity extends AppCompatActivity implements View.OnClickListener{

    Button scanBtn;
    TextView messageText;
    TextView messageFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_qr);

    scanBtn = findViewById(R.id.scanBtn);
    messageText = findViewById(R.id.textContent);
    messageFormat = findViewById(R.id.textTitle);

    // scan button listener
        scanBtn.setOnClickListener(this);

}

    @Override
    public void onClick(View v) {
        // create IntentIntegrator class object of
        // the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
//      intentIntegrator.setPrompt("Scan QR Code"); // maybe insert some text or use default
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // set the content and format of scan message
                messageFormat.setText("Room ID scanned:");
                messageText.setText(intentResult.getContents());

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}