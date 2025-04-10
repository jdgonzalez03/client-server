package Models.Entidades.Message;

import java.time.LocalDateTime;

public class MessageEntity {
    private int id;
    private String content;
    private MessageStatus status;
    private LocalDateTime timeStamp;
    private int senderId;
    private Integer receiverId;  // Puede ser null
    private Integer channelId;   // Puede ser null

    public MessageEntity() {}

    public MessageEntity(int id, String content, int senderId, Integer receiverId, Integer channelId) {
        this.id = id;
        this.content = content;
        this.status = MessageStatus.SENT;
        this.timeStamp = LocalDateTime.now();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.channelId = channelId;
    }

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

    public Integer getReceiverId() { return receiverId; }
    public void setReceiverId(Integer receiverId) { this.receiverId = receiverId; }

    public Integer getChannelId() { return channelId; }
    public void setChannelId(Integer channelId) { this.channelId = channelId; }
}
