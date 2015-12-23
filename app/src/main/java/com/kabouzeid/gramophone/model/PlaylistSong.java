package com.kabouzeid.gramophone.model;

import android.os.Parcel;

public class PlaylistSong extends Song {

    public final int playlistId;
    public final int idInPlayList;

    public PlaylistSong(final int id, final int albumId, final int artistId, final String title, final String artistName,
                        final String albumName, final long duration, final int trackNumber, final String data, final int playlistId, final int idInPlayList) {
        super(id, albumId, artistId, title, artistName, albumName, duration, trackNumber, data);
        this.playlistId = playlistId;
        this.idInPlayList = idInPlayList;
    }

    public PlaylistSong() {
        super();
        playlistId = -1;
        idInPlayList = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PlaylistSong that = (PlaylistSong) o;

        if (playlistId != that.playlistId) return false;
        return idInPlayList == that.idInPlayList;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + playlistId;
        result = 31 * result + idInPlayList;
        return result;
    }

    @Override
    public String toString() {
        return super.toString() +
                "PlaylistSong{" +
                "playlistId=" + playlistId +
                ", idInPlayList=" + idInPlayList +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.playlistId);
        dest.writeInt(this.idInPlayList);
    }

    protected PlaylistSong(Parcel in) {
        super(in);
        this.playlistId = in.readInt();
        this.idInPlayList = in.readInt();
    }

    public static final Creator<PlaylistSong> CREATOR = new Creator<PlaylistSong>() {
        public PlaylistSong createFromParcel(Parcel source) {
            return new PlaylistSong(source);
        }

        public PlaylistSong[] newArray(int size) {
            return new PlaylistSong[size];
        }
    };
}
