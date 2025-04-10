package DAO.Interfaces;

import Models.Entidades.Message.MessageEntity;

import java.util.List;

public interface IMessageDAO {
    MessageEntity findById(int id);
    List<MessageEntity> findAll();
    List<MessageEntity> findByChannelId(int channelId);
    List<MessageEntity> findBySenderId(int senderId);
    List<MessageEntity> findPrivateMessagesBetweenUsers(int user1Id, int user2Id);
    void save(MessageEntity message);
    void updateStatus(int messageId, String newStatus); // 'SENT', 'DELIVERED', 'READ'
    void delete(int id);
}
