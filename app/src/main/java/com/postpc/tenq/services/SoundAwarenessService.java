package com.postpc.tenq.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class SoundAwarenessService {

    private static final String RECORD_SERVICE_PREFS = "record_service_prefs";
    private static final String RECORDER_KEY = "recorder";
    private final Context applicationContext;
    private final RecorderService recorderService;

    public SoundAwarenessService(Context applicationContext, RecorderService recorderService) {
        this.applicationContext = applicationContext;
        this.recorderService = recorderService;
        this.recorderService.setUserSetRecorderOn(getRecorderStateFromSharedPreferences());
    }

    public void saveCurrentRecorderState(boolean recorderState) {
        SharedPreferences prefs = applicationContext.getSharedPreferences(RECORD_SERVICE_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(RECORDER_KEY, recorderState).apply();
    }

    private boolean getRecorderStateFromSharedPreferences() {
        SharedPreferences prefs = applicationContext.getSharedPreferences(RECORD_SERVICE_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(RECORDER_KEY, true);
    }

    public RecorderService getRecorderService() {
        return recorderService;
    }
}
