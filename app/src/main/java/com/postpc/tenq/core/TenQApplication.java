package com.postpc.tenq.core;

import android.app.Application;

import com.postpc.tenq.services.AuthService;
import com.postpc.tenq.services.IAuthService;

public class TenQApplication extends Application {

    private IAuthService authService;

    @Override
    public void onCreate() {
        super.onCreate();
        authService = new AuthService(this);
    }

    public IAuthService getAuthService() {
        return this.authService;
    }

}
