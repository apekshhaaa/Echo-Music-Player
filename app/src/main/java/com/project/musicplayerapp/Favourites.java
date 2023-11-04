package com.project.musicplayerapp;

public class Favourites {
    String song_name;
    String artist_name;
    public String getSongName() {
        return song_name;
    }
    public String getArtistName() {
        return artist_name;
    }
    public Favourites(String song_name, String artist_name) {
        this.song_name = song_name;
        this.artist_name = artist_name;
    }
}
