package DAO.Interfaces;

import Models.Entidades.Event.EventLogEntity;
import Models.Entidades.Event.EventLogType;

import java.util.List;

public interface IEventLogDAO {
    void save(EventLogEntity eventLog);
    List<EventLogEntity> findAll();
    List<EventLogEntity> findByUserId(int userId);
    List<EventLogEntity> findByType(EventLogType type);
}