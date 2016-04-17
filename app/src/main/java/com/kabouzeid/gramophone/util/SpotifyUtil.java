package com.kabouzeid.gramophone.util;

import com.kabouzeid.gramophone.spotify.rest.model.Image;
import com.kabouzeid.gramophone.spotify.rest.service.SpotifyService;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class SpotifyUtil {
    public static Map<String, Object> createLimitSingleOptions() {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 1);
        return options;
    }

    public static String getLargestArtistImageUrl(List<Image> images) {
        return Collections.max(images, new Comparator<Image>() {
            @Override
            public int compare(Image lhs, Image rhs) {
                return (lhs.height * lhs.width) - (rhs.height * rhs.width);
            }
        }).url;
    }
}
