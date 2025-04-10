package Models.Entidades.Event;

import java.time.LocalDateTime;

public class EventLogEntity {
    private int id;
    private EventLogType eventType;
    private int userId;
    private LocalDateTime timeStamp;



    public EventLogEntity() {}

    public EventLogEntity(int id, EventLogType eventType, int userId) {
        this.id = id;
        this.eventType = eventType;
        this.userId = userId;
        this.timeStamp = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public EventLogType getEventType() { return eventType; }
    public void setEventType(EventLogType eventType) { this.eventType = eventType; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
}
