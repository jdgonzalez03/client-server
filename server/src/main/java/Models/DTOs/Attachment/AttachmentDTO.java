package Models.DTOs.Attachment;


import java.time.LocalDateTime;
import Models.Entidades.Attachment.AttachmentEntity;
import Models.Entidades.Attachment.AttachmentType;

public class AttachmentDTO {
    private int id;
    private String fileName;
    private int fileSize;
    private AttachmentType type;
    private int messageId;
    private LocalDateTime createdAt;
    // No incluye fileData para evitar enviar archivos pesados innecesariamente
    // Se podría agregar una URL para acceder al archivo si es necesario
    private String fileUrl;

    public AttachmentDTO() {}

    public AttachmentDTO(int id, String fileName, int fileSize, AttachmentType type,
                         int messageId, LocalDateTime createdAt, String fileUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.type = type;
        this.messageId = messageId;
        this.createdAt = createdAt;
        this.fileUrl = fileUrl;
    }

    public static AttachmentDTO fromEntity(AttachmentEntity entity, String baseUrl) {
        //TODO: Construir URL para acceder al archivo
        String fileUrl = baseUrl + "/attachments/" + entity.getId();

        return new AttachmentDTO(
                entity.getId(),
                entity.getFileName(),
                entity.getFileSize(),
                entity.getType(),
                entity.getMessageId(),
                entity.getCreatedAt(),
                fileUrl
        );
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
}
