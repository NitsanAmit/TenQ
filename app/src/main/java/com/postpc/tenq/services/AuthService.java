package com.postpc.tenq.services;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.postpc.tenq.models.Room;
import com.postpc.tenq.models.User;

public class AuthService implements IAuthService {

    private static final String AUTH_SERVICE_PREFS = "auth_service_prefs";
    private static final String USER_KEY = "user";
    private final Context applicationContext;
    private final Gson gson;
    private User currentUser;

    public AuthService(Context applicationContext) {
        this.applicationContext = applicationContext;
        this.gson = new Gson();
    }

    @Override
    @Nullable
    public User getCurrentUser() {
        if (currentUser != null) return currentUser;
        return getUserFromSharedPreferences();
    }

    @Override
    @Nullable
    public String getCurrentUserId() {
        if (currentUser != null) return currentUser.getId();
        User user = getUserFromSharedPreferences();
        return user != null ? user.getId() : null;
    }

    @Override
    public void saveCurrentUser(User user) {
        this.currentUser = user;
        SharedPreferences prefs = getAuthServicePreferences();
        prefs.edit().putString(USER_KEY, gson.toJson(user)).apply();
    }

    @Override
    public void removeCurrentUser() {
        SharedPreferences prefs = getAuthServicePreferences();
        prefs.edit().remove(USER_KEY).apply();
        this.currentUser = null;
    }

    @Nullable
    private User getUserFromSharedPreferences() {
        SharedPreferences prefs = getAuthServicePreferences();
        User prefsUser = gson.fromJson(prefs.getString(USER_KEY, null), User.class);
        if (prefsUser != null) {
            this.currentUser = prefsUser;
        }
        return prefsUser;
    }

    @Override
    public boolean isCurrentUserHost(Room room) {
        if (currentUser == null) return false;
        return room.getHost().getId().equals(currentUser.getId());
    }

    private SharedPreferences getAuthServicePreferences() {
        return applicationContext.getSharedPreferences(AUTH_SERVICE_PREFS, Context.MODE_PRIVATE);
    }

}
