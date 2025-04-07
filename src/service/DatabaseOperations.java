package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Song;

public class DatabaseOperations {
    private MySqlConnectionManager connectionManager;

    public DatabaseOperations(MySqlConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void insertRecord(String tableName, String[] columns, Object[] values) throws SQLException {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");
        for (String column : columns) {
            query.append(column).append(",");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(") VALUES (");
        for (int i = 0; i < values.length; i++) {
            query.append("?,");
        }
        query.deleteCharAt(query.length() - 1);
        query.append(")");

        System.out.println("Executing insert: " + query); // Debug
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < values.length; i++) {
                preparedStatement.setObject(i + 1, values[i]);
            }
            preparedStatement.executeUpdate();
        }
    }

    public void deleteRecord(String tableName, String condition) throws SQLException {
        String query = "DELETE FROM " + tableName + " WHERE " + condition;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.executeUpdate();
        }
    }

    public List<Song> getAllSongs() throws SQLException {
        List<Song> songs = new ArrayList<>();
        String query = "SELECT id, title, filepath FROM songs ORDER BY id";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                songs.add(new Song(rs.getInt("id"),
                        rs.getString("title"),
                        null,
                        rs.getString("filepath")));
            }
        }
        return songs;
    }

    public Song getSongByName(String title) throws SQLException {
        String query = "SELECT id, title, filepath FROM songs WHERE title = ? LIMIT 1";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Song song = new Song(rs.getInt("id"),
                            rs.getString("title"),
                            null,
                            rs.getString("filepath"));
                    System.out.println("Retrieved: " + song.getEmri() + " at " + song.getPath());
                    return song;
                }
            }
        }
        return null;
    }

    public Song getNextSong(int currentId) throws SQLException {
        String query = "SELECT id, title, filepath FROM songs WHERE id > ? ORDER BY id LIMIT 1";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, currentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Song(rs.getInt("id"),
                            rs.getString("title"),
                            null,
                            rs.getString("filepath"));
                }
            }
        }
        return null;
    }

    public Song getPreviousSong(int currentId) throws SQLException {
        String query = "SELECT id, title, filepath FROM songs WHERE id < ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, currentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Song(rs.getInt("id"),
                            rs.getString("title"),
                            null,
                            rs.getString("filepath"));
                }
            }
        }
        return null;
    }

    public Song getFirstSong() throws SQLException {
        String query = "SELECT id, title, filepath FROM songs ORDER BY id LIMIT 1";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Song(rs.getInt("id"),
                        rs.getString("title"),
                        null,
                        rs.getString("filepath"));
            }
        }
        return null;
    }

    public Song getLastSong() throws SQLException {
        String query = "SELECT id, title, filepath FROM songs ORDER BY id DESC LIMIT 1";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Song(rs.getInt("id"),
                        rs.getString("title"),
                        null,
                        rs.getString("filepath"));
            }
        }
        return null;
    }
}