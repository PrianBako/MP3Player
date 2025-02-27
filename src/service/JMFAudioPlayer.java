package service;

import model.Song;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;

public class JMFAudioPlayer {


    public static void main(String[] args) {
        // Use a valid URL format for your audio file.
        // For example, for a file on your system:
        String mediaFile = "file:///C://Users//use//Downloads//Adele-Hello.wav"; // Adjust path as needed

        try {
            // Create a MediaLocator for the file
            MediaLocator mediaLocator = new MediaLocator(mediaFile);

            // Create a player for the media
            Player player = Manager.createPlayer(mediaLocator);

            // Realize the player (optional, but sometimes needed to pre-load resources)
            player.realize();

            // Start playback
            player.start();

            // Optionally, you can add a listener to handle end-of-media events
            // or keep the application running until playback is complete.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playSong(Song song) {


        try {
            // Create a MediaLocator for the file
            MediaLocator mediaLocator = new MediaLocator(song.getPath());

            // Create a player for the media
            Player player = Manager.createPlayer(mediaLocator);

            // Realize the player (optional, but sometimes needed to pre-load resources)
            player.realize();

            // Start playback
            player.start();

            // Optionally, you can add a listener to handle end-of-media events
            // or keep the application running until playback is complete.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
