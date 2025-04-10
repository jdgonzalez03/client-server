package DAO.Interfaces;

import Models.Entidades.Attachment.AttachmentEntity;

import java.util.List;

public interface IAttachmentDAO {
    AttachmentEntity findById(int id);
    List<AttachmentEntity> findByMessageId(int messageId);
    void save(AttachmentEntity attachment);
    void delete(int id);
}