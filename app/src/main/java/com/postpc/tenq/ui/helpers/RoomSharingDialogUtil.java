package com.postpc.tenq.ui.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.postpc.tenq.R;

import net.glxn.qrgen.android.QRCode;

public class RoomSharingDialogUtil {

    public static AlertDialog getShareDialogForRoom(Activity context, String roomId) {
        if (context.isFinishing() || context.isDestroyed()) return null;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.activity_share, null);

        generateQrCode(view, roomId);

        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setView(view).create();

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
        return alertDialog;
    }

    private static void generateQrCode(View view, String curRoomId) {
        ImageView imageView = view.findViewById(R.id.QrCode);
        Bitmap myBitmap = QRCode.from(curRoomId).bitmap();
        imageView.setImageBitmap(myBitmap);
    }

}

