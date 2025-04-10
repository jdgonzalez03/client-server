package DAO.Repositories.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import DAO.Interfaces.IChannelUserDAO;
import Models.Entidades.ChannelUser.*;

public class ChannelUserDAO implements IChannelUserDAO {

    private final Connection connection;

    public ChannelUserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ChannelUserEntity findById(int id) {
        String sql = "SELECT * FROM ChannelUser WHERE id = ?";
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
    public List<ChannelUserEntity> findAll() {
        List<ChannelUserEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM ChannelUser";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ChannelUserEntity> findByChannelId(int channelId) {
        List<ChannelUserEntity> list = new ArrayList<>();
        String sql = "SELECT * FROM ChannelUser WHERE channelId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, channelId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<ChannelEntity> findChannelsByUserId(int userId){
        List<ChannelEntity> channels = new ArrayList<>();
        String sql = "SELECT c.* FROM Channel c " +
                "JOIN ChannelUser cu ON c.id = cu.channelId " +
                "WHERE cu.userId = ?";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChannelEntity channel = new ChannelEntity();
                    channel.setId(rs.getInt("id"));
                    channel.setName(rs.getString("name"));
                    channel.setPublic(rs.getBoolean("isPublic"));
                    channel.setCreateAt(rs.getTimestamp("createAt").toLocalDateTime());
                    channels.add(channel);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return channels;
    }

    @Override
    public List<UserEntity> findUsersByChannelId(int channelId) {
        List<UserEntity> users = new ArrayList<>();
        String sql = "SELECT u.* FROM User u " +
                "JOIN ChannelUser cu ON u.id = cu.userId " +
                "WHERE cu.channelId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, channelId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    UserEntity user = new UserEntity();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPasswordHashed(rs.getString("passwordHashed"));
                    user.setPhoto(rs.getBytes("photo"));
                    user.setIp(rs.getString("ip"));
                    user.setCreateAt(rs.getTimestamp("createAt").toLocalDateTime());
                    user.setUpdateAt(rs.getTimestamp("updateAt").toLocalDateTime());
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    @Override
    public void save(ChannelUserEntity channelUser) {
        String sql = "INSERT INTO ChannelUser (userId, channelId, role, joinedAt) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, channelUser.getUserId());
            stmt.setInt(2, channelUser.getChannelId());
            stmt.setString(3, channelUser.getRole().name());
            stmt.setTimestamp(4, Timestamp.valueOf(channelUser.getJoinedAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ChannelUserEntity channelUser) {
        String sql = "UPDATE ChannelUser SET userId = ?, channelId = ?, role = ?, joinedAt = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, channelUser.getUserId());
            stmt.setInt(2, channelUser.getChannelId());
            stmt.setString(3, channelUser.getRole().name());
            stmt.setTimestamp(4, Timestamp.valueOf(channelUser.getJoinedAt()));
            stmt.setInt(5, channelUser.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM ChannelUser WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ChannelUserEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        ChannelUserEntity entity = new ChannelUserEntity();
        entity.setId(rs.getInt("id"));
        entity.setUserId(rs.getInt("userId"));
        entity.setChannelId(rs.getInt("channelId"));
        entity.setRole(UserRole.valueOf(rs.getString("role")));
        entity.setJoinedAt(rs.getTimestamp("joinedAt").toLocalDateTime());
        return entity;
    }
}
