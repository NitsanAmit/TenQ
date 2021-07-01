package com.postpc.tenq.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.postpc.tenq.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import static com.postpc.tenq.network.SpotifyClient.CLIENT_ID;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                .setScopes(new String[]{"user-read-email"})
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
        if (requestCode == REQUEST_CODE) {
            String token = response.getAccessToken();
            Log.d("LoginActivity", "Got token!\n"+token);
        }
    }

}