package com.postpc.tenq.core;

import android.app.Application;

import com.postpc.tenq.services.AuthService;
import com.postpc.tenq.services.IAuthService;
import com.postpc.tenq.services.RecorderService;

public class TenQApplication extends Application {

    private IAuthService authService;
    private RecorderService recorderService; //TODO noam make sure this actually works

    @Override
    public void onCreate() {
        super.onCreate();
        authService = new AuthService(this);
        recorderService = new RecorderService(this);
    }

    public IAuthService getAuthService() {
        return this.authService;
    }

    public RecorderService getRecorderService() {
        return this.recorderService;
    }

}
