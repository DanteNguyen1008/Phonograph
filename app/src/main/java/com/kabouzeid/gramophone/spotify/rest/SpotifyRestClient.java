package com.kabouzeid.gramophone.spotify.rest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kabouzeid.gramophone.spotify.rest.service.SpotifyService;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyRestClient {
    public static final String BASE_URL = "https://api.spotify.com/";

    private final SpotifyService spotifyService;

    public SpotifyRestClient(@NonNull Context context) {
        this(createDefaultOkHttpClientBuilder(context).build());
    }

    public SpotifyRestClient(@NonNull Call.Factory client) {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spotifyService = restAdapter.create(SpotifyService.class);
    }

    @Nullable
    public static Cache createDefaultCache(Context context) {
        File cacheDir = new File(context.getCacheDir().getAbsolutePath(), "/okhttp-spotify/");
        if (cacheDir.mkdirs() || cacheDir.isDirectory()) {
            return new Cache(cacheDir, 1024 * 1024 * 10);
        }
        return null;
    }

    public static Interceptor createCacheControlInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request modifiedRequest = chain.request().newBuilder()
                        .addHeader("Cache-Control", String.format("max-age=%d, max-stale=%d", 31536000, 31536000))
                        .build();
                return chain.proceed(modifiedRequest);
            }
        };
    }

    public static OkHttpClient.Builder createDefaultOkHttpClientBuilder(Context context) {
        return new OkHttpClient.Builder()
                .cache(createDefaultCache(context))
                .addInterceptor(createCacheControlInterceptor());
    }

    public SpotifyService getService() {
        return spotifyService;
    }
}
