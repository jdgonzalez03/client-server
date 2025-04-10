package DAO.Repositories.Channel;

import DAO.Interfaces.IChannelDAO;
import Models.Entidades.ChannelUser.ChannelEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChannelDAO implements IChannelDAO {
    private final Connection connection;

    public ChannelDAO(Connection connection) {
        this.connection = connection;
    }

    private ChannelEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        ChannelEntity channel = new ChannelEntity();
        channel.setId(rs.getInt("id"));
        channel.setName(rs.getString("name"));
        channel.setPublic(rs.getBoolean("isPublic"));
        channel.setCreateAt(rs.getTimestamp("createAt").toLocalDateTime());
        return channel;
    }

    @Override
    public ChannelEntity findById(int id) {
        String sql = "SELECT * FROM Channel WHERE id = ?";
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
    public List<ChannelEntity> findAll() {
        String sql = "SELECT * FROM Channel";
        List<ChannelEntity> channels = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                channels.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return channels;
    }

    @Override
    public List<ChannelEntity> findPublicChannels() {
        String sql = "SELECT * FROM Channel WHERE isPublic = true";
        List<ChannelEntity> publicChannels = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                publicChannels.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return publicChannels;
    }

    @Override
    public void save(ChannelEntity channel) {
        String sql = "INSERT INTO Channel (name, isPublic, createAt) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, channel.getName());
            stmt.setBoolean(2, channel.isPublic());
            stmt.setTimestamp(3, Timestamp.valueOf(channel.getCreateAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChannelEntity channel) {
        String sql = "UPDATE Channel SET name = ?, isPublic = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, channel.getName());
            stmt.setBoolean(2, channel.isPublic());
            stmt.setInt(3, channel.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Channel WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}