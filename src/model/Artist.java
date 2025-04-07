package model;

import java.io.Serializable;

public class Artist extends Base implements Serializable {
    private String emriA;

    public Artist(int id, String emri) {
        super(id);
        this.emriA = emri;

    }

    public String getArtistEmer() {
        return emriA;
    }

    public void setArtistEmer(String emri) {
        this.emriA = emri;
    }

}

