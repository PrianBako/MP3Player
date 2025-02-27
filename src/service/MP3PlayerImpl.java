package service;

import model.Song;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MP3PlayerImpl implements Mp3Player{
    public static final String Mp3Info= "Ky eshte nje Mp3 player Spotify";
    private static JMFAudioPlayer audioPlayer = new JMFAudioPlayer();
    private final ListStorage objectStorage = new ListStorage();

    private static List<Song> songs = new ArrayList<>();
    private static Set<String> artists = new HashSet<>();
    private static int current= 0;


    @Override
    public void addsong(Song a) {
        songs.add(a);
        artists.add(a.getArtist());

    }

    @Override
    public void removesong(int a) {
        songs.remove(a);

    }

    @Override
    public void showplaylist() {
        for (Song a : songs) System.out.println(a);
    }


    @Override
    public Song playNext() {

        current=current+1;

        if(current >= songs.size()) {
            current = 0;
        }
        audioPlayer.playSong(songs.get(current));
        return songs.get(current);
    }

    @Override
    public void stopSong() {
        System.out.println("No song currently playing");

    }

    @Override
    public Song playFirst() {
        current=0;
        return songs.get(current);

    }

    @Override
    public Song playLast() {
        current=songs.size()-1;
        return songs.get(current);

    }

    @Override
    public List<Song> listSongs() {
        return songs;

    }

    @Override
    public Song findByTitle(String title) {

        for (int i=0; i< songs.size(); i++){

            Song song = songs.get(i);
            if(song.getEmri().equals(title)){
                return song;
            }
        }
        return null;
    }

    @Override
    public Song getCurrentSong() {
        return songs.get(current);
    }

    @Override
    public void loadSong() {
        songs=ListStorage.readList("mp3.data");
        if (songs == null){
            songs = new ArrayList<>();
        }
    }

    @Override
    public void saveSongOnExit() {
        ListStorage.storeList("mp3.data",songs);

    }


}
