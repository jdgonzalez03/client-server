package Controllers.Socket;

import Controllers.Interfaces.IMessageObserver;
import Services.Message.IMessageService;
import Services.Message.MessageService;
import Models.DTOs.Message.MessageDTO;
import Models.Entidades.Message.MessageStatus;
import Utils.Logger.Logger;

import com.google.protobuf.ServiceException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageController {
    private final IMessageService messageService;
    private final Logger logger;
    private final List<IMessageObserver> observers = new ArrayList<>();

    public MessageController() {
        this.logger = Logger.getInstance();
        try {
            this.messageService = new MessageService();
        } catch (SQLException e) {
            logger.log("Error al inicializar MessageService: " + e.getMessage());
            throw new RuntimeException("Error al inicializar MessageService", e);
        }
    }

    /**
     * Procesa un mensaje recibido del socket y lo envía al destinatario
     * @param messageJson JSONObject con los datos del mensaje
     * @return JSONObject con el resultado de la operación
     */
    public JSONObject processMessage(JSONObject messageJson) {
        JSONObject response = new JSONObject();

        try {
            // Convertir JSONObject a DTO
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setContent(messageJson.getString("content"));
            messageDTO.setSenderId(messageJson.getInt("senderId"));

            // El mensaje puede ser para un usuario o para un canal
            if (messageJson.has("receiverId")) {
                messageDTO.setReceiverId(messageJson.getInt("receiverId"));
            } else if (messageJson.has("channelId")) {
                messageDTO.setChannelId(messageJson.getInt("channelId"));
            } else {
                throw new ServiceException("El mensaje debe tener un destinatario (usuario o canal)");
            }

            // Enviar mensaje a través del servicio
            MessageDTO savedMessage = messageService.sendMessage(messageDTO);

            // Convertir el DTO guardado de vuelta a JSONObject para la respuesta
            JSONObject messageResponse = convertDTOToJSON(savedMessage);

            response.put("success", true);
            response.put("message", messageResponse);
            notifyObservers(messageResponse);

        } catch (Exception e) {
            logger.log("Error al procesar mensaje: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Actualiza el estado de un mensaje
     * @param messageIdJson JSONObject con el ID del mensaje y el nuevo estado
     * @return JSONObject con el resultado de la operación
     */
    public JSONObject updateMessageStatus(JSONObject messageIdJson) {
        JSONObject response = new JSONObject();

        try {
            int messageId = messageIdJson.getInt("messageId");
            String newStatus = messageIdJson.getString("status");

            messageService.updateMessageStatus(messageId, newStatus);

            response.put("success", true);

        } catch (Exception e) {
            logger.log("Error al actualizar estado del mensaje: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene los mensajes entre dos usuarios
     * @param requestJson JSONObject con los IDs de los usuarios
     * @return JSONObject con la lista de mensajes
     */
    public JSONObject getPrivateMessages(JSONObject requestJson) {
        JSONObject response = new JSONObject();

        try {
            int user1Id = requestJson.getInt("user1Id");
            int user2Id = requestJson.getInt("user2Id");

            List<MessageDTO> messages = messageService.getMessagesBetweenUsers(user1Id, user2Id);

            JSONArray messagesArray = new JSONArray();
            for (MessageDTO message : messages) {
                messagesArray.put(convertDTOToJSON(message));
            }

            response.put("success", true);
            response.put("messages", messagesArray);

        } catch (Exception e) {
            logger.log("Error al obtener mensajes privados: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Obtiene los mensajes de un canal
     * @param requestJson JSONObject con el ID del canal
     * @return JSONObject con la lista de mensajes
     */
    public JSONObject getChannelMessages(JSONObject requestJson) {
        JSONObject response = new JSONObject();

        try {
            int channelId = requestJson.getInt("channelId");

            List<MessageDTO> messages = messageService.getMessagesByChannelId(channelId);

            JSONArray messagesArray = new JSONArray();
            for (MessageDTO message : messages) {
                messagesArray.put(convertDTOToJSON(message));
            }

            response.put("success", true);
            response.put("messages", messagesArray);

        } catch (Exception e) {
            logger.log("Error al obtener mensajes del canal: " + e.getMessage());
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Convierte un DTO a JSONObject
     * @param dto MessageDTO a convertir
     * @return JSONObject con los datos del mensaje
     */
    private JSONObject convertDTOToJSON(MessageDTO dto) {
        JSONObject json = new JSONObject();
        json.put("id", dto.getId());
        json.put("content", dto.getContent());
        json.put("senderId", dto.getSenderId());
        if (dto.getReceiverId() != null) {
            json.put("receiverId", dto.getReceiverId());
        }
        if (dto.getChannelId() != null) {
            json.put("channelId", dto.getChannelId());
        }
        json.put("timestamp", dto.getTimeStamp().toString());
        json.put("status", dto.getStatus().toString());

        // Si en el DTO tienes datos adicionales como nombres, agrégalos aquí
        if (dto.getSenderName() != null) {
            json.put("senderName", dto.getSenderName());
        }
        if (dto.getReceiverName() != null) {
            json.put("receiverName", dto.getReceiverName());
        }
        if (dto.getChannelName() != null) {
            json.put("channelName", dto.getChannelName());
        }

        return json;
    }


    // Patron Observer
    public void addObserver(IMessageObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(IMessageObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(JSONObject messageJson) {
        for (IMessageObserver observer : observers) {
            observer.onMessageReceived(messageJson);
        }
    }
}