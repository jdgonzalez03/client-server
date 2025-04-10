package DAO.Repositories.Attachment;

import DAO.Interfaces.IAttachmentDAO;
import Models.Entidades.Attachment.AttachmentEntity;
import Models.Entidades.Attachment.AttachmentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttachmentDAO implements IAttachmentDAO {
    private final Connection connection;

    public AttachmentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AttachmentEntity findById(int id) {
        String sql = "SELECT * FROM Attachment WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEntity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<AttachmentEntity> findByMessageId(int messageId) {
        List<AttachmentEntity> attachments = new ArrayList<>();
        String sql = "SELECT * FROM Attachment WHERE messageId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                attachments.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attachments;
    }

    @Override
    public void save(AttachmentEntity attachment) {
        String sql = "INSERT INTO Attachment (fileData, fileName, fileSize, type, messageId, createdAt) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBytes(1, attachment.getFileData());
            stmt.setString(2, attachment.getFileName());
            stmt.setInt(3, attachment.getFileSize());
            stmt.setString(4, attachment.getType().name());
            stmt.setInt(5, attachment.getMessageId());
            stmt.setTimestamp(6, Timestamp.valueOf(attachment.getCreatedAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Attachment WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private AttachmentEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        AttachmentEntity entity = new AttachmentEntity();
        entity.setId(rs.getInt("id"));
        entity.setFileData(rs.getBytes("fileData"));
        entity.setFileName(rs.getString("fileName"));
        entity.setFileSize(rs.getInt("fileSize"));
        entity.setType(AttachmentType.valueOf(rs.getString("type")));
        entity.setMessageId(rs.getInt("messageId"));
        entity.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return entity;
    }
}