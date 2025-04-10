package DAO.Repositories.Request;

import DAO.Interfaces.IRequestDAO;
import Models.Entidades.Request.RequestEntity;
import Models.Entidades.Request.RequestStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO implements IRequestDAO {
    private final Connection connection;

    public RequestDAO(Connection connection) {
        this.connection = connection;
    }

    private RequestEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        RequestEntity request = new RequestEntity();
        request.setId(rs.getInt("id"));
        request.setSenderId(rs.getInt("senderId"));
        request.setReceiverId(rs.getInt("receiverId"));
        int channelId = rs.getInt("channelId");
        request.setChannelId(rs.wasNull() ? null : channelId);
        request.setStatus(RequestStatus.valueOf(rs.getString("status")));
        request.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return request;
    }

    @Override
    public RequestEntity findById(int id) {
        String sql = "SELECT * FROM Request WHERE id = ?";
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
    public List<RequestEntity> findAll() {
        String sql = "SELECT * FROM Request";
        List<RequestEntity> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<RequestEntity> findByReceiverId(int receiverId) {
        String sql = "SELECT * FROM Request WHERE receiverId = ?";
        List<RequestEntity> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, receiverId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<RequestEntity> findBySenderId(int senderId) {
        String sql = "SELECT * FROM Request WHERE senderId = ?";
        List<RequestEntity> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, senderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<RequestEntity> findByChannelId(int channelId) {
        String sql = "SELECT * FROM Request WHERE channelId = ?";
        List<RequestEntity> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, channelId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public List<RequestEntity> findByStatus(RequestStatus status) {
        String sql = "SELECT * FROM Request WHERE status = ?";
        List<RequestEntity> requests = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                requests.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public void save(RequestEntity request) {
        String sql = "INSERT INTO Request (senderId, receiverId, channelId, status, createdAt) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, request.getSenderId());
            stmt.setInt(2, request.getReceiverId());
            if (request.getChannelId() != null) {
                stmt.setInt(3, request.getChannelId());
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setString(4, request.getStatus().name());
            stmt.setTimestamp(5, Timestamp.valueOf(request.getCreatedAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateStatus(int requestId, RequestStatus newStatus) {
        String sql = "UPDATE Request SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newStatus.name());
            stmt.setInt(2, requestId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Request WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}