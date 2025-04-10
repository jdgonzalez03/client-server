package Services.Message;

import DAO.Interfaces.IMessageDAO;
import DAO.Repositories.Message.MessageDAO;

import Models.DTOs.Message.MessageDTO;
import Models.Entidades.Message.MessageEntity;
import Models.Entidades.Message.MessageStatus;

import Utils.Logger.Logger;

import com.google.protobuf.ServiceException;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class MessageService implements IMessageService {

    private final IMessageDAO messageDAO;
    private final Logger logger = Logger.getInstance();

    public MessageService() throws SQLException {
        this.messageDAO = new MessageDAO();
    }

    @Override
    public MessageDTO sendMessage(MessageDTO dto) throws ServiceException {
        try {
            logger.log("Enviando mensaje de " + dto.getSenderId() + " a " +
                    (dto.getReceiverId() != null ? "usuario " + dto.getReceiverId() : "canal " + dto.getChannelId()));

            MessageEntity entity = convertToEntity(dto);
            entity.setStatus(MessageStatus.SENT);
            entity.setTimeStamp(java.time.LocalDateTime.now());

            messageDAO.save(entity);

            dto.setId(entity.getId()); // Asignar el ID generado en la BD
            dto.setTimeStamp(entity.getTimeStamp());
            dto.setStatus(entity.getStatus());

            return dto;
        } catch (Exception e) {
            logger.log("Error al enviar mensaje: " + e.getMessage());
            throw new ServiceException("Error al enviar mensaje", e);
        }
    }

    @Override
    public List<MessageDTO> getMessagesByChannelId(int channelId) throws ServiceException {
        try {
            logger.log("Obteniendo mensajes del canal " + channelId);
            return messageDAO.findByChannelId(channelId).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al obtener mensajes del canal", e);
        }
    }

    @Override
    public List<MessageDTO> getMessagesBetweenUsers(int user1Id, int user2Id) throws ServiceException {
        try {
            logger.log("Obteniendo mensajes privados entre usuarios " + user1Id + " y " + user2Id);
            return messageDAO.findPrivateMessagesBetweenUsers(user1Id, user2Id).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new ServiceException("Error al obtener mensajes privados", e);
        }
    }

    @Override
    public void updateMessageStatus(int messageId, String newStatus) throws ServiceException {
        try {
            logger.log("Actualizando estado del mensaje " + messageId + " a " + newStatus);
            messageDAO.updateStatus(messageId, newStatus);
        } catch (Exception e) {
            throw new ServiceException("Error al actualizar estado del mensaje", e);
        }
    }

    // Conversores
    private MessageEntity convertToEntity(MessageDTO dto) {
        MessageEntity entity = new MessageEntity();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setSenderId(dto.getSenderId());
        entity.setReceiverId(dto.getReceiverId());
        entity.setChannelId(dto.getChannelId());
        entity.setTimeStamp(dto.getTimeStamp());
        entity.setStatus(dto.getStatus());
        return entity;
    }

    private MessageDTO convertToDTO(MessageEntity entity) {
        MessageDTO dto = new MessageDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setSenderId(entity.getSenderId());
        dto.setReceiverId(entity.getReceiverId());
        dto.setChannelId(entity.getChannelId());
        dto.setTimeStamp(entity.getTimeStamp());
        dto.setStatus(entity.getStatus());
        // senderName, receiverName y channelName los puede completar en el cliente o con joins si lo deseas
        return dto;
    }
}
