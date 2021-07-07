package com.postpc.tenq.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;

import net.glxn.qrgen.android.QRCode;

public class ShareAlertDialog {


    public static void share(Context context, String curRoomId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.activity_create_qr, null);

        String roomId = curRoomId;

        generateQrCode(view, roomId);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
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
                ClipData clipData = ClipData.newPlainText("text", roomId);
                ClipboardManager clipboardManager = (ClipboardManager)  context.getSystemService(Context.CLIPBOARD_SERVICE);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(context.getApplicationContext(), "Data Copied to Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private static void generateQrCode(View view, String curRoomId) {
        ImageView imageView = view.findViewById(R.id.QrCode);
        Bitmap myBitmap = QRCode.from(curRoomId).bitmap();
        imageView.setImageBitmap(myBitmap);
    }

}

