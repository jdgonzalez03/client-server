package Models.DTOs.Event;

import java.time.LocalDateTime;
import Models.Entidades.Event.EventLogEntity;
import Models.Entidades.Event.EventLogType;

public class EventLogDTO {
    private int id;
    private EventLogType eventType;
    private int userId;
    private LocalDateTime timeStamp;

    public EventLogDTO() {}

    public EventLogDTO(int id, EventLogType eventType, int userId, LocalDateTime timeStamp) {
        this.id = id;
        this.eventType = eventType;
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    public static EventLogDTO fromEntity(EventLogEntity entity) {
        return new EventLogDTO(
                entity.getId(),
                entity.getEventType(),
                entity.getUserId(),
                entity.getTimeStamp()
        );
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public EventLogType getEventType() { return eventType; }
    public void setEventType(EventLogType eventType) { this.eventType = eventType; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
}
