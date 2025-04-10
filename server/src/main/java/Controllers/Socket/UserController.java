package Controllers.Socket;


import Controllers.Interfaces.IUserController;
import Models.DTOs.ChannelUser.UserDTO;
import Services.User.UserService;
import Utils.Logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UserController {
    private final UserService userService;
    private final Logger logger = Logger.getInstance();

    public UserController() {
        this.userService = new UserService();
    }

    public JSONObject loginUser(String email, String password) {
        JSONObject response = new JSONObject();
        logger.log("Login controller: " + email);

        try {
            UserDTO user = userService.loginUser(email, password);

            // Crear objeto JSON con datos del usuario
            JSONObject userJson = new JSONObject();
            userJson.put("id", user.getId());
            userJson.put("email", user.getEmail());
            userJson.put("name", user.getName());
            if (user.getIp() != null) {
                userJson.put("ip", user.getIp());
            }
            if (user.getPhoto() != null) {
                userJson.put("photo", user.getPhoto());
            }

            response.put("user", userJson);
            logger.log("Login exitoso: " + email);
        } catch (Exception e) {
            logger.log("Error al login usuario: " + e.getMessage());
        }
        return response;
    }

    public JSONObject registerUser(JSONObject userDTO, String password) {
        JSONObject response = new JSONObject();
        logger.log("Registro controller: " + userDTO.getString("email"));

        try {
            // Crear objeto DTO desde el JSON
            UserDTO newUser = new UserDTO();
            newUser.setName(userDTO.getString("name"));
            newUser.setEmail(userDTO.getString("email"));
            newUser.setIp(userDTO.optString("ip", null));

            // Registrar usuario en el servicio
            UserDTO registeredUser = userService.registerUser(newUser, password);

            // Crear objeto JSON con datos del usuario registrado
            JSONObject userJson = new JSONObject();
            userJson.put("id", registeredUser.getId());
            userJson.put("email", registeredUser.getEmail());
            userJson.put("name", registeredUser.getName());
            if (registeredUser.getIp() != null) {
                userJson.put("ip", registeredUser.getIp());
            }
            response.put("user", userJson);
            logger.log("Registro exitoso: " + registeredUser.getEmail());
        } catch (Exception e) {
            logger.log("Error al registrar usuario: " + e.getMessage());
        }

        return response;
    }

    public JSONObject getAllUsersExcept(String email) {
        JSONObject response = new JSONObject();
        logger.log("Obteniendo todas los usuarios");

        try {
            List<UserDTO> users = userService.getAllUsersExcept(email);
            JSONArray usersArray = new JSONArray();

            for (UserDTO user : users) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", user.getId());
                userJson.put("email", user.getEmail());
                userJson.put("name", user.getName());
                usersArray.put(userJson);
            }

            response.put("type", "connected-users");
            response.put("users", usersArray);
        } catch (Exception e) {
            response.put("type", "error");
            response.put("message", "Error al obtener usuarios: " + e.getMessage());
            logger.log("Algo salió mal obteniendo todas los usuarios" + e.getMessage());
        }

        return response;
    }
}
