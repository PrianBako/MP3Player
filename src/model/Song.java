package model;

import java.io.Serializable;
import java.util.Objects;

public class Song implements Serializable {
    private String emri;
    private String artist;
    private String path;

    public Song(String e, String a) {
        emri=e;
        artist=a;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(emri, song.emri) && Objects.equals(artist, song.artist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emri, artist);
    }

    @Override
    public String toString() {
        return "model.Song Name: " + emri + ", Artist: " + artist;
    }
}