package MainServer.SocketServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import Controllers.Socket.MessageController;
import Controllers.Socket.UserController;
import DAO.Repositories.User.UserDAO;
import Utils.Logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final UserController userController;
    private final UserDAO userDAO;
    private final MessageController messageController;
    private String email;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.userController = new UserController();
        this.messageController = new MessageController();
        this.userDAO = new UserDAO();
    }

    @Override
    public void run() {
        Logger logger = Logger.getInstance();

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = in.readLine()) != null) {
                JSONObject message = new JSONObject(line);
                JSONObject response = new JSONObject();

                switch (message.getString("type")) {
                    case "register":
                        try {
                            String registerEmail = message.getString("email");
                            String password = message.getString("password");
                            String name = message.optString("name", registerEmail.split("@")[0]); // Usa parte del email como nombre si no se proporciona
                            String ip = socket.getInetAddress().getHostAddress();

                            // Crear DTO para registro
                            JSONObject userDTO = new JSONObject();
                            userDTO.put("name", name);
                            userDTO.put("email", registerEmail);
                            userDTO.put("ip", ip);

                            // Registrar usuario
                            JSONObject registeredUser = userController.registerUser(userDTO, password);

                            // Crear respuesta
                            response.put("type", "register-response");
                            response.put("success", true);
                            response.put("user", registeredUser.getJSONObject("user"));

                            //Notificacion a todos los clientes
                            notifyAllClientsUserConnected(registeredUser.getJSONObject("user"));

                            logger.log("Usuario registrado: " + registerEmail);
                        } catch (Exception e) {
                            response.put("type", "register-response");
                            response.put("success", false);
                            response.put("message", "Error al registrar: " + e.getMessage());
                            logger.log("Error al registrar usuario: " + e.getMessage());
                        }
                        out.println(response.toString());
                        break;

                    case "login":
                        try {
                            String loginEmail = message.getString("email");
                            String password = message.getString("password");

                            // Iniciar sesión
                            JSONObject loggedInUser = userController.loginUser(loginEmail, password);

                            // Guardar email del usuario conectado
                            this.email = loginEmail;
                            ConnectedClients.add(email, socket);

                            // Crear respuesta
                            response.put("type", "login-response");
                            response.put("success", true);
                            response.put("user", loggedInUser.getJSONObject("user"));
                            logger.log("Usuario logueado - Respuesta socket: " + loginEmail);

                            // Notificar a todos los clientes sobre la nueva lista de usuarios
                            notifyAllClientsUserConnected(loggedInUser.getJSONObject("user"));
                        } catch (Exception e) {
                            response.put("type", "login-response");
                            response.put("success", false);
                            response.put("message", "Error al iniciar sesión: " + e.getMessage());
                            logger.log("Error al iniciar sesión: " + e.getMessage());
                        }
                        out.println(response.toString());
                        break;

                    case "get-users":
                        try {
                            JSONObject usersResponse = userController.getAllUsersExcept(this.email);

                            JSONArray users = usersResponse.getJSONArray("users");

                            for (int i = 0; i < users.length(); i++) {
                                JSONObject user = users.getJSONObject(i);
                                String userEmail = user.getString("email");

                                if (ConnectedClients.isOnline(userEmail)) {
                                    user.put("status", "online");
                                } else {
                                    user.put("status", "away");
                                }
                            }

                            usersResponse.put("type", "connected-users");
                            out.println(usersResponse.toString());
                        } catch (Exception e) {
                            JSONObject error = new JSONObject();
                            error.put("type", "error");
                            error.put("message", "Error al cargar usuarios: " + e.getMessage());
                            out.println(error.toString());
                        }
                        break;
                    case "send-message":
                        try {

                            // El JSONObject message ya contiene todos los datos necesarios
                            // No es necesario extraer cada campo individualmente
                            JSONObject messageResponse = messageController.processMessage(message);

                            if (messageResponse.getBoolean("success")) {
                                JSONObject msgData = messageResponse.getJSONObject("message");

                                // Notificar al destinatario si es un mensaje privado y está online
                                if (msgData.has("receiverId")) {
                                    int receiverId = msgData.getInt("receiverId");
                                    String receiverEmail = userDAO.findById(receiverId).getEmail();

                                    if (ConnectedClients.isOnline(receiverEmail)) {
                                        Socket receiverSocket = ConnectedClients.getSocketByEmail(receiverEmail);
                                        PrintWriter receiverOut = new PrintWriter(receiverSocket.getOutputStream(), true);

                                        JSONObject notification = new JSONObject();
                                        notification.put("type", "new-message");
                                        notification.put("message", msgData);

                                        receiverOut.println(notification.toString());

                                        // Actualizar estado a DELIVERED
                                        JSONObject updateStatus = new JSONObject();
                                        updateStatus.put("messageId", msgData.getInt("id"));
                                        updateStatus.put("status", "DELIVERED");
                                        messageController.updateMessageStatus(updateStatus);
                                    }
                                }
                                // Si es un mensaje a un canal, notificar a todos los miembros del canal
                                else if (msgData.has("channelId")) {
                                    int channelId = msgData.getInt("channelId");
                                    List<String> channelMemberEmails =  new ArrayList<>();;

                                    for (String memberEmail : channelMemberEmails) {
                                        if (!memberEmail.equals(this.email) && ConnectedClients.isOnline(memberEmail)) {
                                            Socket memberSocket = ConnectedClients.getSocketByEmail(memberEmail);
                                            PrintWriter memberOut = new PrintWriter(memberSocket.getOutputStream(), true);

                                            JSONObject notification = new JSONObject();
                                            notification.put("type", "new-channel-message");
                                            notification.put("message", msgData);

                                            memberOut.println(notification.toString());
                                        }
                                    }
                                }

                                // Responder al remitente
                                response.put("type", "message-sent");
                                response.put("success", true);
                                response.put("message", msgData);
                            } else {
                                response.put("type", "message-sent");
                                response.put("success", false);
                                response.put("message", messageResponse.getString("error"));
                            }
                        } catch (Exception e) {
                            response.put("type", "message-sent");
                            response.put("success", false);
                            response.put("message", "Error al enviar mensaje: " + e.getMessage());
                            logger.log("Error al enviar mensaje: " + e.getMessage());
                        }
                        out.println(response.toString());
                        break;

                    case "get-private-messages":
                        try {
                            int user1Id =  userDAO.findByEmail(this.email).getId();
                            int user2Id = message.getInt("userId");

                            JSONObject requestData = new JSONObject();
                            requestData.put("user1Id", user1Id);
                            requestData.put("user2Id", user2Id);

                            MessageController messageController = new MessageController();
                            JSONObject historyResponse = messageController.getPrivateMessages(requestData);

                            if (historyResponse.getBoolean("success")) {
                                // Marcar mensajes como leídos si somos el destinatario
                                JSONArray messages = historyResponse.getJSONArray("messages");
                                for (int i = 0; i < messages.length(); i++) {
                                    JSONObject msg = messages.getJSONObject(i);
                                    if (msg.getInt("receiverId") == user1Id &&
                                            !msg.getString("status").equals("READ")) {

                                        JSONObject updateRequest = new JSONObject();
                                        updateRequest.put("messageId", msg.getInt("id"));
                                        updateRequest.put("status", "READ");
                                        messageController.updateMessageStatus(updateRequest);
                                    }
                                }
                            }

                            // Añadir el tipo para el cliente
                            historyResponse.put("type", "private-messages");
                            out.println(historyResponse.toString());

                        } catch (Exception e) {
                            JSONObject error = new JSONObject();
                            error.put("type", "error");
                            error.put("message", "Error al obtener mensajes privados: " + e.getMessage());
                            out.println(error.toString());
                            logger.log("Error al obtener mensajes privados: " + e.getMessage());
                        }
                        break;

                    case "get-channel-messages":
                        try {
                            int channelId = message.getInt("channelId");

                            JSONObject requestData = new JSONObject();
                            requestData.put("channelId", channelId);

                            MessageController messageController = new MessageController();
                            JSONObject historyResponse = messageController.getChannelMessages(requestData);

                            // Añadir el tipo para el cliente
                            historyResponse.put("type", "channel-messages");
                            out.println(historyResponse.toString());

                        } catch (Exception e) {
                            JSONObject error = new JSONObject();
                            error.put("type", "error");
                            error.put("message", "Error al obtener mensajes del canal: " + e.getMessage());
                            out.println(error.toString());
                            logger.log("Error al obtener mensajes del canal: " + e.getMessage());
                        }
                        break;

                    default:
                        response.put("type", "error");
                        response.put("message", "Tipo de mensaje no reconocido");
                        out.println(response.toString());
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (email != null) {
                ConnectedClients.remove(email);
                // Notificar a todos los clientes que este usuario se desconectó
                //notifyAllClientsAboutUserList();
            }
        }
    }

    private void notifyAllClientsUserConnected(JSONObject user) {
        for (String clientEmail : ConnectedClients.getAllEmails()) {
            try {
                if (!clientEmail.equals(email)) { // No enviar al cliente actual
                    Socket clientSocket = ConnectedClients.getSocketByEmail(clientEmail);
                    PrintWriter clientOut = new PrintWriter(clientSocket.getOutputStream(), true);

                    JSONObject notification = new JSONObject();
                    notification.put("type", "user-connected");

                    // Clona el usuario y le agrega estado
                    JSONObject userCopy = new JSONObject(user.toString());
                    userCopy.put("status", "online");

                    notification.put("user", userCopy);

                    clientOut.println(notification.toString());

                    Logger.getInstance().log("Notificado cliente " + clientEmail + " de nuevo usuario conectado: " + userCopy.getString("email"));
                }
            } catch (Exception e) {
                Logger.getInstance().log("Error al notificar a " + clientEmail + ": " + e.getMessage());
            }
        }
    }
}