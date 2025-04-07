package service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


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
            query.deleteCharAt(query.length() - 1); // Remove trailing comma
            query.append(") VALUES (");
            for (int i = 0; i < values.length; i++) {
                query.append("?,");
            }
            query.deleteCharAt(query.length() - 1); // Remove trailing comma
            query.append(")");

            try (Connection connection = connectionManager.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(query.toString())) {
                for (int i = 0; i < values.length; i++) {
                    preparedStatement.setObject(i + 1, values[i]);
                }
                preparedStatement.executeUpdate();
            }
        }

        public void updateRecord(String tableName, String[] columns, Object[] values, String condition) throws SQLException {
            StringBuilder query = new StringBuilder("UPDATE ");
            query.append(tableName).append(" SET ");
            for (String column : columns) {
                query.append(column).append(" = ?,");
            }
            query.deleteCharAt(query.length() - 1); // Remove trailing comma
            query.append(" WHERE ").append(condition);

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
}
