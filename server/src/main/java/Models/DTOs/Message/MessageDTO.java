package Models.DTOs.Message;

import java.time.LocalDateTime;
import Models.Entidades.Message.MessageEntity;
import Models.Entidades.Message.MessageStatus;

public class MessageDTO {
    private int id;
    private String content;
    private MessageStatus status;
    private LocalDateTime timeStamp;
    private int senderId;
    private String senderName;  // Información adicional útil para el cliente
    private Integer receiverId;
    private String receiverName;  // Información adicional útil para el cliente
    private Integer channelId;
    private String channelName;  // Información adicional útil para el cliente

    public MessageDTO() {}

    public MessageDTO(int id, String content, MessageStatus status, LocalDateTime timeStamp,
                      int senderId, String senderName, Integer receiverId, String receiverName,
                      Integer channelId, String channelName) {
        this.id = id;
        this.content = content;
        this.status = status;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.channelId = channelId;
        this.channelName = channelName;
    }

    // Método de conversión básico (necesitarías información adicional para completarlo)
    public static MessageDTO fromEntity(MessageEntity entity, String senderName, String receiverName, String channelName) {
        return new MessageDTO(
                entity.getId(),
                entity.getContent(),
                entity.getStatus(),
                entity.getTimeStamp(),
                entity.getSenderId(),
                senderName,
                entity.getReceiverId(),
                receiverName,
                entity.getChannelId(),
                channelName
        );
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageStatus getStatus() { return status; }
    public void setStatus(MessageStatus status) { this.status = status; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Integer getReceiverId() { return receiverId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public Integer getChannelId() { return channelId; }
    public void setChannelId(Integer channelId) { this.channelId = channelId; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }
}
