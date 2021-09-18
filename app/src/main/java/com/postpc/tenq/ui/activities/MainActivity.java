package com.postpc.tenq.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.postpc.tenq.R;
import com.postpc.tenq.core.TenQActivity;
import com.postpc.tenq.models.User;
import com.postpc.tenq.network.SpotifyClient;
import com.postpc.tenq.ui.animations.ProgressBarAnimation;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.postpc.tenq.network.SpotifyClient.AUTHORIZATION_SCOPES;
import static com.postpc.tenq.network.SpotifyClient.CLIENT_ID;

import androidx.annotation.NonNull;

public class MainActivity extends TenQActivity {

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ProgressBarAnimation anim = new ProgressBarAnimation(findViewById(R.id.progress_main_activity));
        anim.setDuration(3500);
        runOnUiThread(() -> findViewById(R.id.progress_main_activity).startAnimation(anim));

        startSpotifyAuthFlow();

    }
    private void startRoomsActivity() {
        startActivity(new Intent(this, ExistingRoomsActivity.class));
        finish();
    }

    private void startSpotifyAuthFlow() {
        final AuthorizationRequest request = getAuthenticationRequest();
        AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    private Uri getRedirectUri() {
        return new Uri.Builder()
                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority(getString(R.string.com_spotify_sdk_redirect_host))
                .build();
    }

    private AuthorizationRequest getAuthenticationRequest() {
        return new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(AUTHORIZATION_SCOPES)
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);
        if (response.getError() != null && !response.getError().isEmpty()) {
            Log.e("LoginActivity", response.getError());
            return;
        }
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String token = response.getAccessToken();
            SpotifyClient.init(token);
            if (getAuthService().getCurrentUser() == null) {
                getUserProfile();
            } else {
                startRoomsActivity();
            }
        }
    }

    private void registerUser() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(getAuthService().getCurrentUserId())
                .set(new HashMap<>(), SetOptions.merge());
    }

    private void getUserProfile() {
        SpotifyClient
                .getClient()
                .getUserProfile()
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                        if (response.code() == 200) {
                            User profile = response.body();
                            if (profile == null) return;
                            MainActivity.this.getAuthService().saveCurrentUser(profile);
                            registerUser();
                            startRoomsActivity();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable error) {
                        Log.e("LoginActivity", "Profile failure: ", error);
                    }
                });
    }

}