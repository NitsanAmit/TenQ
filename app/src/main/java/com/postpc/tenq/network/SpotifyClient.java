
package com.postpc.tenq.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.postpc.tenq.models.Playlist;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyClient {

    public static final String CLIENT_ID = "0257114a4a0141bc9587d0538dda962f";
    public static final String REDIRECT_URI = "compostpctenq://spotifycallback";
    public static final String[] AUTHORIZATION_SCOPES = {
            "user-read-email", "user-top-read", "user-modify-playback-state",
            "playlist-modify-public", "playlist-modify-private", "user-library-read",
            "user-library-modify"
    };
    private static final String SPOTIFY_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    private static SpotifyApiService spotifyApiService;

    public static void init(String token) {
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request requestWithAuth = chain.request()
                            .newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();
                    return chain.proceed(requestWithAuth);
                })
                .addInterceptor(logger)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat(SPOTIFY_DATE_FORMAT)
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        spotifyApiService = retrofit.create(SpotifyApiService.class);
    }

    public static SpotifyApiService getClient() throws SpotifyClientNotInitializedException {
        if (spotifyApiService == null) throw new SpotifyClientNotInitializedException();
        return spotifyApiService;
    }
}
