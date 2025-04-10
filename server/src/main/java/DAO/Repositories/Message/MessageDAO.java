package DAO.Repositories.Message;

import Config.DatabaseConfig.DatabaseConfig;
import DAO.Interfaces.IMessageDAO;
import Models.Entidades.ChannelUser.ChannelUserEntity;
import Models.Entidades.Message.MessageEntity;
import Models.Entidades.Message.MessageStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO implements IMessageDAO {
    private final Connection connection = DatabaseConfig.getInstance().getConnection();

    public MessageDAO() throws SQLException {
    }

    @Override
    public MessageEntity findById(int id) {
        String sql = "SELECT * FROM Message WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
                PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return mapResultSetToEntity(rs);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public List<MessageEntity> findAll() {
        String sql = "SELECT * FROM Message";
        List<MessageEntity> messages = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                messages.add(mapResultSetToEntity(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<MessageEntity> findByChannelId(int channelId) {
        String sql = "SELECT * FROM Message WHERE channelId = ? ORDER BY timeStamp ASC";
        List<MessageEntity> messages = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                messages.add(mapResultSetToEntity(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<MessageEntity> findBySenderId(int senderId) {
        String sql = "SELECT * FROM Message WHERE senderId = ? ORDER BY timeStamp ASC";
        List<MessageEntity> messages = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                messages.add(mapResultSetToEntity(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public List<MessageEntity> findPrivateMessagesBetweenUsers(int senderId, int receiverId) {
        String sql = "SELECT * FROM Message WHERE " +
                "((senderId = ? AND receiverId = ?) OR (senderId = ? AND receiverId = ?)) " +
                "AND channelId IS NULL " + // Para asegurarse que no son mensajes de canal
                "ORDER BY timeStamp ASC";

        List<MessageEntity> messages = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            stmt.setInt(2, receiverId);
            stmt.setInt(3, receiverId); // inverso
            stmt.setInt(4, senderId);   // inverso

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

    @Override
    public void save(MessageEntity message) {
        String sql = "INSERT INTO Message (content, status, timeStamp, senderId, receiverId, channelId) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, message.getContent());
            stmt.setString(2, message.getStatus().name());
            stmt.setTimestamp(3, Timestamp.valueOf(message.getTimeStamp()));
            stmt.setInt(4, message.getSenderId());

            if (message.getReceiverId() != null) {
                stmt.setInt(5, message.getReceiverId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            if (message.getChannelId() != null) {
                stmt.setInt(6, message.getChannelId());
            } else {
                stmt.setNull(6, Types.INTEGER);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(int messageId, String newStatus) {
        String sql = "UPDATE Message SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, messageId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Message WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private MessageEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        MessageEntity entity = new MessageEntity();
        entity.setId(rs.getInt("id"));
        entity.setContent(rs.getString("content"));
        entity.setStatus(MessageStatus.valueOf(rs.getString("status")));
        entity.setTimeStamp(rs.getTimestamp("timeStamp").toLocalDateTime());
        entity.setSenderId(rs.getInt("senderId"));
        entity.setReceiverId(rs.getInt("receiverId"));
        entity.setChannelId(rs.getInt("channelId"));

        return entity;
    }
}
