package com.project.musicplayerapp;

import android.os.Parcelable;
import android.os.Parcel;
public class Allsongs implements Parcelable {
    String song_name;
    String artist_name;
    int song_resource_id;
    boolean isFavorite;
    public Allsongs(String song_name,String artist_name,int song_resource_id) {
        this.song_name = song_name;
        this.artist_name=artist_name;
        this.song_resource_id=song_resource_id;
        this.isFavorite = false;
    }
    protected Allsongs(Parcel in) {
        song_name = in.readString();
        artist_name = in.readString();
        song_resource_id = in.readInt();
        isFavorite = in.readByte() != 0;
    }
    public static final Creator<Allsongs> CREATOR = new Creator<Allsongs>() {
        @Override
        public Allsongs createFromParcel(Parcel in) {
            return new Allsongs(in);
        }

        @Override
        public Allsongs[] newArray(int size) {
            return new Allsongs[size];
        }
    };
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(song_name);
        dest.writeString(artist_name);
        dest.writeInt(song_resource_id);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
    public int getResourceId() {
        return song_resource_id;
    }
    public String getSongName() {
        return song_name;
    }
    public String getArtistName() {
        return artist_name;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
