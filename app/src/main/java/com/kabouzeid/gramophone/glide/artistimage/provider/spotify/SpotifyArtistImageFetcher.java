package com.kabouzeid.gramophone.glide.artistimage.provider.spotify;

import android.content.Context;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.kabouzeid.gramophone.glide.artistimage.ArtistImage;
import com.kabouzeid.gramophone.spotify.rest.SpotifyRestClient;
import com.kabouzeid.gramophone.spotify.rest.model.ArtistsPager;
import com.kabouzeid.gramophone.spotify.rest.model.Image;
import com.kabouzeid.gramophone.util.MusicUtil;
import com.kabouzeid.gramophone.util.SpotifyUtil;
import com.kabouzeid.gramophone.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit2.Response;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class SpotifyArtistImageFetcher implements DataFetcher<InputStream> {
    public static final String TAG = SpotifyArtistImageFetcher.class.getSimpleName();
    private Context context;
    private final SpotifyRestClient spotifyClient;
    private final ArtistImage model;
    private ModelLoader<GlideUrl, InputStream> urlLoader;
    private final int width;
    private final int height;
    private volatile boolean isCancelled;
    private DataFetcher<InputStream> urlFetcher;

    public SpotifyArtistImageFetcher(Context context, SpotifyRestClient spotifyClient, ArtistImage model, ModelLoader<GlideUrl, InputStream> urlLoader, int width, int height) {
        this.context = context;
        this.spotifyClient = spotifyClient;
        this.model = model;
        this.urlLoader = urlLoader;
        this.width = width;
        this.height = height;
    }

    @Override
    public String getId() {
        // makes sure we never ever return null here
        return String.valueOf(model.artistName) + "#spotify";
    }

    @Override
    public InputStream loadData(Priority priority) throws Exception {
        if (!MusicUtil.isArtistNameUnknown(model.artistName) && Util.isAllowedToAutoDownload(context)) {
            Response<ArtistsPager> response = spotifyClient.getService().searchArtists(model.artistName, SpotifyUtil.createLimitSingleOptions(), model.skipOkHttpCache ? "no-cache" : null).execute();

            if (!response.isSuccessful()) {
                throw new IOException("Request failed with code: " + response.code());
            }

            List<Image> images = response.body().artists.items.get(0).images;

            if (isCancelled) return null;

            GlideUrl url = new GlideUrl(SpotifyUtil.getLargestArtistImageUrl(images));
            urlFetcher = urlLoader.getResourceFetcher(url, width, height);

            return urlFetcher.loadData(priority);
        }
        return null;
    }

    @Override
    public void cleanup() {
        if (urlFetcher != null) {
            urlFetcher.cleanup();
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
        if (urlFetcher != null) {
            urlFetcher.cancel();
        }
    }
}
