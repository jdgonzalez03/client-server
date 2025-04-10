package Config.DatabaseConfig;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;


public class DatabaseInitializer {
    public static void initialize() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DatabaseConfig.getInstance().getConnection();
            stmt = conn.createStatement();

            // Crear la base de datos si no existe
            stmt.execute("CREATE DATABASE IF NOT EXISTS chat_db");
            stmt.execute("USE chat_db");

            // Crear tablas
            createUserTable(stmt);
            createEventLogsTable(stmt);
            createChannelTable(stmt);
            createMessageTable(stmt);
            createAttachmentTable(stmt);
            createRequestTable(stmt);
            createChannelUserTable(stmt);

            System.out.println("Base de datos inicializada correctamente");

        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConfig.close(stmt, conn);
        }
    }

    private static void createUserTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS User (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL UNIQUE," +
                "passwordHashed VARCHAR(255) NOT NULL," +
                "photo LONGBLOB," +
                "ip VARCHAR(45)," +
                "createAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "updateAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";
        stmt.execute(sql);
    }

    private static void createEventLogsTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS EventLogs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "eventType ENUM('LOGIN', 'LOGOUT', 'MESSAGE_SENT', 'MESSAGE_RECEIVED', 'CHANNEL_CREATED') NOT NULL," +
                "userId INT NOT NULL," +
                "timeStamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (userId) REFERENCES User(id)" +
                ")";
        stmt.execute(sql);
    }

    private static void createChannelTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Channel (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "isPublic BOOLEAN DEFAULT TRUE," +
                "createAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        stmt.execute(sql);
    }

    private static void createMessageTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Message (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "content TEXT NOT NULL," +
                "status ENUM('SENT', 'DELIVERED', 'READ') DEFAULT 'SENT'," +
                "timeStamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "senderId INT NOT NULL," +
                "receiverId INT," +
                "channelId INT," +
                "FOREIGN KEY (senderId) REFERENCES User(id)," +
                "FOREIGN KEY (receiverId) REFERENCES User(id)," +
                "FOREIGN KEY (channelId) REFERENCES Channel(id)" +
                ")";
        stmt.execute(sql);
    }

    private static void createAttachmentTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Attachment (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "fileData LONGBLOB NOT NULL," +
                "fileName VARCHAR(255) NOT NULL," +
                "fileSize INT NOT NULL," +
                "type ENUM('IMAGE', 'VIDEO', 'DOCUMENT') NOT NULL," +
                "messageId INT NOT NULL," +
                "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (messageId) REFERENCES Message(id)" +
                ")";
        stmt.execute(sql);
    }

    private static void createRequestTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Request (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "senderId INT NOT NULL," +
                "receiverId INT NOT NULL," +
                "channelId INT," +
                "status ENUM('PENDING', 'ACCEPTED', 'REJECTED') DEFAULT 'PENDING'," +
                "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (senderId) REFERENCES User(id)," +
                "FOREIGN KEY (receiverId) REFERENCES User(id)," +
                "FOREIGN KEY (channelId) REFERENCES Channel(id)" +
                ")";
        stmt.execute(sql);
    }

    private static void createChannelUserTable(Statement stmt) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS ChannelUser (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "userId INT NOT NULL," +
                "channelId INT NOT NULL," +
                "role ENUM('MEMBER', 'ADMIN') DEFAULT 'MEMBER'," +
                "joinedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (userId) REFERENCES User(id)," +
                "FOREIGN KEY (channelId) REFERENCES Channel(id)" +
                ")";
        stmt.execute(sql);
    }
}