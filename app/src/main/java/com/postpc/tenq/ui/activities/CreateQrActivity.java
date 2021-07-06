package com.postpc.tenq.ui.activities;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.postpc.tenq.R;
import android.app.AlertDialog;
import android.widget.Toast;


import net.glxn.qrgen.android.QRCode;

public class CreateQrActivity extends AppCompatActivity {
    private ImageView imageView;
    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        imageView = findViewById(R.id.imageView);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    Bitmap myBitmap = QRCode.from("www.example.org").bitmap();
        imageView.setImageBitmap(myBitmap);

        builder = new AlertDialog.Builder(this);


        builder.setTitle(R.string.dialog_title);
        //Setting message manually and performing action on button click
        builder.setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        builder.setView(imageView);
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.show();
    }
}

// todo - create the view of barcode and inflate it to dialog
// todo - work with the figma dialog

//        Bitmap myBitmap = QRCode.from("www.example.org").bitmap();
//        imageView.setImageBitmap(myBitmap);
//
//
//        builder = new AlertDialog.Builder(this);



