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
import service.MySqlConnectionManager;
import service.DatabaseOperations;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MP3PlayerGUI extends Application {
    private ListView<String> playlistView = new ListView<>();
    private MediaPlayer mediaPlayer;
    private MySqlConnectionManager connectionManager;
    private DatabaseOperations dbOperations;
    private int currentSongId = -1;

    @Override
    public void start(Stage primaryStage) {
        connectionManager = new MySqlConnectionManager(
                "127.0.0.1", 3306, "mp3db", "root", "Prian1234!"
        );
        dbOperations = new DatabaseOperations(connectionManager);

        primaryStage.setTitle("JavaFX MP3 Player");
        playlistView.setPrefWidth(300);

        Button addButton = new Button("Add MP3");
        Button playButton = new Button("Play");
        Button nextButton = new Button("Next");
        Button backButton = new Button("Back");
        Button playLast = new Button("Play Last");
        Button playFirst = new Button("Play First");
        Button remove = new Button("Remove");

        addButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
            );
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);
            if (selectedFiles != null) {
                try {
                    for (File file : selectedFiles) {
                        String[] columns = {"title", "filepath"}; // Match table
                        Object[] values = {file.getName(), file.getAbsolutePath()};
                        dbOperations.insertRecord("songs", columns, values);
                    }
                    updatePlaylistView();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        playButton.setOnAction(e -> playCurrent());

        nextButton.setOnAction(e -> {
            try {
                Song nextSong = dbOperations.getNextSong(currentSongId);
                if (nextSong != null) {
                    currentSongId = nextSong.getId();
                    playSong(nextSong);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(e -> {
            try {
                Song prevSong = dbOperations.getPreviousSong(currentSongId);
                if (prevSong != null) {
                    currentSongId = prevSong.getId();
                    playSong(prevSong);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        playFirst.setOnAction(e -> {
            try {
                Song firstSong = dbOperations.getFirstSong();
                if (firstSong != null) {
                    currentSongId = firstSong.getId();
                    playSong(firstSong);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        playLast.setOnAction(e -> {
            try {
                Song lastSong = dbOperations.getLastSong();
                if (lastSong != null) {
                    currentSongId = lastSong.getId();
                    playSong(lastSong);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        remove.setOnAction(e -> {
            try {
                String selectedSong = playlistView.getSelectionModel().getSelectedItem();
                if (selectedSong != null) {
                    dbOperations.deleteRecord("songs", "title = '" + selectedSong + "'");
                    updatePlaylistView();
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }
                    currentSongId = -1;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        HBox controlBar = new HBox(10, playFirst, backButton, playButton, nextButton, playLast, addButton, remove);
        controlBar.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(playlistView);
        root.setBottom(controlBar);

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

        updatePlaylistView();
    }

    private void playCurrent() {
        String selectedSong = playlistView.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            try {
                Song song = dbOperations.getSongByName(selectedSong);
                if (song != null) {
                    currentSongId = song.getId();
                    playSong(song);
                } else {
                    System.out.println("Song not found: " + selectedSong);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No song selected.");
        }
    }

    private void playSong(Song song) {
        if (song == null || song.getPath() == null) {
            return;
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        try {
            File file = new File(song.getPath());
            if (!file.exists()) {
                return;
            }
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            playlistView.getSelectionModel().select(song.getEmri());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePlaylistView() {
        try {
            ObservableList<String> titles = FXCollections.observableArrayList();
            List<Song> songs = dbOperations.getAllSongs();
            for (Song song : songs) {
                titles.add(song.getEmri());
            }
            playlistView.setItems(titles);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}