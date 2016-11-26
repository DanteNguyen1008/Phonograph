package com.kabouzeid.gramophone.helper.playingnotificationhelper;

import android.support.annotation.NonNull;

import com.kabouzeid.gramophone.service.MusicService;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */

public class PlayingNotificationHelperFactory {
    public static PlayingNotificationHelper from(@NonNull MusicService service) {
        return new PlayingNotificationHelperImpl(service);
    }

    private PlayingNotificationHelperFactory() {
    }
}
