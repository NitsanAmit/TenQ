package com.postpc.tenq.core;

import android.app.Application;

import com.postpc.tenq.services.AuthService;
import com.postpc.tenq.services.IAuthService;
import com.postpc.tenq.services.RecorderService;
import com.postpc.tenq.services.SoundAwarenessService;

public class TenQApplication extends Application {

    private IAuthService authService;
    private RecorderService recorderService;
    private SoundAwarenessService soundAwarenessService;

    @Override
    public void onCreate() {
        super.onCreate();
        authService = new AuthService(this);
        recorderService = new RecorderService(this);
        soundAwarenessService = new SoundAwarenessService(this, recorderService);
    }

    public IAuthService getAuthService() {
        return this.authService;
    }

    public SoundAwarenessService getSoundAwarenessService() {
        return soundAwarenessService;
    }
}
