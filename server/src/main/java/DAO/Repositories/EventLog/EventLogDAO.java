package DAO.Repositories.EventLog;

import DAO.Interfaces.IEventLogDAO;
import Models.Entidades.Event.EventLogEntity;
import Models.Entidades.Event.EventLogType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventLogDAO implements IEventLogDAO {
    private final Connection connection;

    public EventLogDAO(Connection connection) {
        this.connection = connection;
    }

    private EventLogEntity mapResultSetToEntity(ResultSet rs) throws SQLException {
        EventLogEntity log = new EventLogEntity();
        log.setId(rs.getInt("id"));
        log.setEventType(EventLogType.valueOf(rs.getString("eventType")));
        log.setUserId(rs.getInt("userId"));
        log.setTimeStamp(rs.getTimestamp("timeStamp").toLocalDateTime());
        return log;
    }

    @Override
    public void save(EventLogEntity eventLog) {
        String sql = "INSERT INTO EventLog (eventType, userId, timeStamp) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eventLog.getEventType().name());
            stmt.setInt(2, eventLog.getUserId());
            stmt.setTimestamp(3, Timestamp.valueOf(eventLog.getTimeStamp()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<EventLogEntity> findAll() {
        String sql = "SELECT * FROM EventLog ORDER BY timeStamp DESC";
        List<EventLogEntity> logs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logs.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public List<EventLogEntity> findByUserId(int userId) {
        String sql = "SELECT * FROM EventLog WHERE userId = ? ORDER BY timeStamp DESC";
        List<EventLogEntity> logs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logs.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    @Override
    public List<EventLogEntity> findByType(EventLogType type) {
        String sql = "SELECT * FROM EventLog WHERE eventType = ? ORDER BY timeStamp DESC";
        List<EventLogEntity> logs = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, type.name());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                logs.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}