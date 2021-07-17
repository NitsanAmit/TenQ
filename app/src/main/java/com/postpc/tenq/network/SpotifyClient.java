package com.postpc.tenq.network;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyClient {

    public static final String CLIENT_ID = "0257114a4a0141bc9587d0538dda962f";
    public static final String REDIRECT_URI = "compostpctenq://spotifycallback";
    public static final String[] AUTHORIZATION_SCOPES = {
            "user-read-email", "user-top-read", "user-modify-playback-state",
            "playlist-modify-public", "playlist-modify-private", "user-library-read", "user-library-modify"
    };

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        spotifyApiService = retrofit.create(SpotifyApiService.class);
    }

    public static SpotifyApiService getClient() throws SpotifyClientNotInitializedException {
        if (spotifyApiService == null) throw new SpotifyClientNotInitializedException();
        return spotifyApiService;
    }
}
