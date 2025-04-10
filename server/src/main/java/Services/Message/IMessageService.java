package Services.Message;

import Models.DTOs.Message.MessageDTO;
import com.google.protobuf.ServiceException;

import java.util.List;

public interface IMessageService {
    MessageDTO sendMessage(MessageDTO messageDTO) throws ServiceException;
    List<MessageDTO> getMessagesByChannelId(int channelId) throws ServiceException;
    List<MessageDTO> getMessagesBetweenUsers(int user1Id, int user2Id) throws ServiceException;
    void updateMessageStatus(int messageId, String newStatus) throws ServiceException;
}