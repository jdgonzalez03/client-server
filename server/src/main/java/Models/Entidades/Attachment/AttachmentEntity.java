package Models.Entidades.Attachment;

import java.time.LocalDateTime;

public class AttachmentEntity {
    private int id;
    private byte[] fileData;
    private String fileName;
    private int fileSize;
    private AttachmentType type;
    private int messageId;
    private LocalDateTime createdAt;


    public AttachmentEntity() {}

    public AttachmentEntity(int id, byte[] fileData, String fileName, int fileSize, AttachmentType type, int messageId) {
        this.id = id;
        this.fileData = fileData;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.type = type;
        this.messageId = messageId;
        this.createdAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public int getFileSize() { return fileSize; }
    public void setFileSize(int fileSize) { this.fileSize = fileSize; }

    public AttachmentType getType() { return type; }
    public void setType(AttachmentType type) { this.type = type; }

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
