package gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import model.Song;
import service.DatabaseOperations;
import service.MP3PlayerImpl;
import service.Mp3Player;
import service.MySqlConnectionManager;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MP3PlayerGUI extends Application {


    private static ListView<String> playlistView = new ListView<>();
    private MediaPlayer mediaPlayer;
    private static MySqlConnectionManager connectionManager;
    private static DatabaseOperations dbOperations;
    private static Mp3Player prianplayer = new MP3PlayerImpl();


    @Override
    public void start(Stage primaryStage) {
        connectionManager = new MySqlConnectionManager(
                "127.0.0.1", 3306, "mp3db", "root", "Prian1234!"
        );
        dbOperations = new DatabaseOperations(connectionManager);


        primaryStage.setTitle("JavaFX MP3 Player");
        playlistView.setPrefWidth(300);

        // Buttons (all original methods)
        Button addButton = new Button("Add MP3");
        Button playButton = new Button("Play");
        Button nextButton = new Button("Next");
        Button backButton = new Button("Back");
        Button playLast = new Button("Play Last");
        Button playFirst = new Button("Play First");
        Button remove = new Button("Remove");

        addButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select MP3 Files");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
            );
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
            if (selectedFiles != null) {
                try {
                    for (File file : selectedFiles) {
                        String[] columns = {"title", "filepath"};
                        Object[] values = {file.getName(), file.getPath()};
                        dbOperations.insertRecord("songs", columns, values);
                    }
                    updatePlaylistView();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Play selected song
        playButton.setOnAction(e -> playCurrent());

        // Next song
        nextButton.setOnAction(e -> {
            Song nextSong = prianplayer.playNext();
            playSong(nextSong);
        });

        // Previous song
        backButton.setOnAction(e -> {
            Song backSong = prianplayer.playBack();
            playSong(backSong);
        });

        // Play first song
        playFirst.setOnAction(e -> {
            Song firstSong = prianplayer.playFirst();
            playSong(firstSong);
        });

        // Play last song
        playLast.setOnAction(e -> {
            Song lastSong = prianplayer.playLast();
            playSong(lastSong);
        });

        remove.setOnAction(e -> {
            Song SongToRemove = prianplayer.getCurrentSong();
            prianplayer.removesong(SongToRemove);
            prianplayer.saveSongOnExit();
            updatePlaylistView();
        });

        // Bottom controls
        HBox controlBar = new HBox(10, playFirst, backButton, playButton, nextButton, playLast, addButton, remove);
        controlBar.setPadding(new Insets(10));

        // Layout
        BorderPane root = new BorderPane();
        root.setCenter(playlistView);
        root.setBottom(controlBar);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Play the currently selected song
    private void playCurrent() {
        Song currentSong = prianplayer.getCurrentSong();
        playSong(currentSong);
    }

    // Play a specific song
    private void playSong(Song song) {
        if (song == null) {
            System.out.println("No song to play.");
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        File file = new File(song.getPath());
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();

        // Update UI selection
        playlistView.getSelectionModel().select(song.getEmri());
    }

    // Update playlist view
    private static void updatePlaylistView() {
        ObservableList<String> names = FXCollections.observableArrayList();
        for (Song song : prianplayer.listSongs()) {
            names.add(song.getEmri());
        }
        playlistView.setItems(names);
    }

    public static void main(String[] args) {
        launch(args);
        updatePlaylistView();
    }
}

