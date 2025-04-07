package model;

import java.io.Serializable;
import java.util.Objects;

public class Song extends Base implements Serializable {
    private String emri;
    private String artist;
    private String path;

    public Song(int id, String emri, String artist) {
        super(id);
        this.emri = emri;
        this.artist = artist;
    }

    public Song(String emri, String artist, String path) {
        this.emri=emri;

        this.artist=artist;
        this.path= path;
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