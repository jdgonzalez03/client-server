package Models.Entidades.Request;

import java.time.LocalDateTime;

public class RequestEntity {
    private int id;
    private int senderId;
    private int receiverId;
    private Integer channelId;  // Puede ser null
    private RequestStatus status;
    private LocalDateTime createdAt;



    public RequestEntity() {}

    public RequestEntity(int id, int senderId, int receiverId, Integer channelId) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.channelId = channelId;
        this.status = RequestStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public Integer getChannelId() { return channelId; }
    public void setChannelId(Integer channelId) { this.channelId = channelId; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}