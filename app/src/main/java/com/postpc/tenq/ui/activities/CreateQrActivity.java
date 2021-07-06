package com.postpc.tenq.ui.activities;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;

import net.glxn.qrgen.android.QRCode;

public class CreateQrActivity extends TenQActivity {

    // todo change "www.example.org" to real room-id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_qr);
        View view = getLayoutInflater().inflate(R.layout.activity_create_qr, null);

        generateQrCode(view);

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view).create();
        alertDialog.show();

        // cancel button
        view.findViewById(R.id.txt_cancel).setOnClickListener(cancel -> alertDialog.dismiss());

        // copy link button
        view.findViewById(R.id.copy_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyDataToClipBoard();
            }

            private void copyDataToClipBoard() {
                ClipData clipData = ClipData.newPlainText("text", "www.example.org");
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(), "Data Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void generateQrCode(View view) {
        ImageView imageView = view.findViewById(R.id.QrCode);
        Bitmap myBitmap = QRCode.from("www.example.org").bitmap();
        imageView.setImageBitmap(myBitmap);
    }

}

