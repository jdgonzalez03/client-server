package DAO.Repositories.User;

import Config.DatabaseConfig.DatabaseConfig;
import DAO.Interfaces.IUserDAO;
import Models.Entidades.ChannelUser.UserEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    @Override
    public UserEntity save(UserEntity user) {
        String sql = "INSERT INTO User (name, email, passwordHashed, ip) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHashed());
            stmt.setString(4, user.getIp());

            stmt.executeUpdate();

            // Obtener ID generado
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            System.err.println("Error al guardar usuario: " + e.getMessage());
            return null;
        }
    }

    @Override
    public UserEntity findById(int id) {
        String sql = "SELECT * FROM User WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("passwordHashed"),
                        null, // omitimos photo por ahora
                        rs.getString("ip"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por ID: " + e.getMessage());
        }

        return null;
    }

    @Override
    public UserEntity findByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new UserEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("passwordHashed"),
                        null, // omitimos photo por ahora
                        rs.getString("ip"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<UserEntity> findAll() {
        String sql = "SELECT * FROM User";
        List<UserEntity> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserEntity user = new UserEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("passwordHashed"),
                        null,
                        rs.getString("ip"),
                        rs.getTimestamp("createAt"),
                        rs.getTimestamp("updateAt")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener todos los usuarios: " + e.getMessage());
        }

        return users;
    }

    @Override
    public void update(UserEntity user) {
        String sql = "UPDATE User SET name = ?, email = ?, passwordHashed = ?, ip = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPasswordHashed());
            stmt.setString(4, user.getIp());
            stmt.setInt(5, user.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM User WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
        }
    }
}
