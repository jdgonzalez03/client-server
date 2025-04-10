package Models.DTOs.Request;

import java.time.LocalDateTime;
import Models.Entidades.Request.RequestEntity;
import Models.Entidades.Request.RequestStatus;

public class RequestDTO {
    private int id;
    private int senderId;
    private String senderName;  // Información adicional útil
    private int receiverId;
    private String receiverName;  // Información adicional útil
    private Integer channelId;
    private String channelName;  // Información adicional útil si hay canal
    private RequestStatus status;
    private LocalDateTime createdAt;

    public RequestDTO() {}

    public RequestDTO(int id, int senderId, String senderName, int receiverId, String receiverName,
                      Integer channelId, String channelName, RequestStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static RequestDTO fromEntity(RequestEntity entity, String senderName, String receiverName, String channelName) {
        return new RequestDTO(
                entity.getId(),
                entity.getSenderId(),
                senderName,
                entity.getReceiverId(),
                receiverName,
                entity.getChannelId(),
                channelName,
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public Integer getChannelId() { return channelId; }
    public void setChannelId(Integer channelId) { this.channelId = channelId; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
