package com.kabouzeid.gramophone.glide.artistimage.provider.spotify;

import android.content.Context;

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.kabouzeid.gramophone.glide.artistimage.ArtistImage;
import com.kabouzeid.gramophone.spotify.rest.SpotifyRestClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */

public class SpotifyArtistImageLoader implements StreamModelLoader<ArtistImage> {
    // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
    private static final int TIMEOUT = 1000;

    private Context context;
    private SpotifyRestClient spotifyClient;
    private ModelLoader<GlideUrl, InputStream> urlLoader;

    public SpotifyArtistImageLoader(Context context, SpotifyRestClient spotifyClient, ModelLoader<GlideUrl, InputStream> urlLoader) {
        this.context = context;
        this.spotifyClient = spotifyClient;
        this.urlLoader = urlLoader;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(ArtistImage model, int width, int height) {
        return new SpotifyArtistImageFetcher(context, spotifyClient, model, urlLoader, width, height);
    }

    public static class Factory implements ModelLoaderFactory<ArtistImage, InputStream> {
        private SpotifyRestClient spotifyClient;
        private OkHttpUrlLoader.Factory okHttpFactory;

        public Factory(Context context) {
            okHttpFactory = new OkHttpUrlLoader.Factory(new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build());
            spotifyClient = new SpotifyRestClient(SpotifyRestClient.createDefaultOkHttpClientBuilder(context)
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build());
        }

        @Override
        public ModelLoader<ArtistImage, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new SpotifyArtistImageLoader(context, spotifyClient, okHttpFactory.build(context, factories));
        }

        @Override
        public void teardown() {
            okHttpFactory.teardown();
        }
    }
}

