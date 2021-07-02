package com.postpc.tenq.core;

import androidx.appcompat.app.AppCompatActivity;

import com.postpc.tenq.services.IAuthService;

public class TenQActivity extends AppCompatActivity {

    public IAuthService getAuthService(){
        return ((TenQApplication) getApplication()).getAuthService();
    }
}
