package service;

import model.Song;
import java.util.List;

public interface Mp3Player {

    void addsong(Song a);
    void removesong(int a);
    void showplaylist();
    Song playNext();
    void stopSong();
    Song playFirst();
    Song playLast();

    List<Song> listSongs();
    Song findByTitle(String title);

    Song getCurrentSong();

    void loadSong();
    void saveSongOnExit();

}
