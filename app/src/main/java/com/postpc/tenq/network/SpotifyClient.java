package com.postpc.tenq.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyClient {

    public static final String CLIENT_ID = "0257114a4a0141bc9587d0538dda962f";
    public static final String REDIRECT_URI = "compostpctenq://spotifycallback";

    private static SpotifyApiService spotifyApiService;

    static SpotifyApiService getClient() {
        if (spotifyApiService != null) return spotifyApiService;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        spotifyApiService = retrofit.create(SpotifyApiService.class);
        return spotifyApiService;
    }

}
