package Services.User;

import DAO.Interfaces.IUserDAO;
import DAO.Repositories.User.UserDAO;

import Models.DTOs.ChannelUser.UserDTO;
import Models.Entidades.ChannelUser.UserEntity;

import Utils.Logger.Logger;
import Utils.Security.PasswordUtils;

import Services.User.IUserService;

import com.google.protobuf.ServiceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserService  implements IUserService {
    private final IUserDAO userDAO;
    private final Logger logger = Logger.getInstance();
    private final PasswordUtils passwordUtils = PasswordUtils.getInstance();

    public UserService() {
        this.userDAO = new UserDAO();
    }
    @Override
    public UserDTO registerUser(UserDTO userDTO, String password) throws ServiceException {
        try {
            logger.log("Registrando usuario: " + userDTO.getName());

            // Validar que no exista un usuario con el mismo email
            Optional<UserEntity> existingUser = Optional.ofNullable(userDAO.findByEmail(userDTO.getEmail()));
            if (existingUser.isPresent()) {
                throw new ServiceException("Ya existe un usuario con el email: " + userDTO.getEmail());
            }

            // Convertir DTO a entidad
            UserEntity user = new UserEntity();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            password = passwordUtils.hashPassword(password);
            user.setPasswordHashed(password);
            user.setIp(userDTO.getIp());

            // Guardar en la base de datos
            UserEntity savedUser = userDAO.save(user);

            // Convertir entidad guardada a DTO para retornar
            logger.log("Usuario registrado: " + userDTO.getName());
            return convertToDTO(savedUser);
        } catch (Exception e) {
            logger.log("Error al registrar usuario: " + e.getMessage());
            throw new ServiceException("Error al registrar usuario: " + e.getMessage(), e);
        }
    }
    @Override
    public UserDTO loginUser(String email, String password) throws ServiceException {
        UserEntity user = userDAO.findByEmail(email);

        if (user == null) {
            logger.log("Error al buscar el usuario: " + email);
            throw new ServiceException("No existe un usuario con el email: " + email);
        }

        boolean isMatchPassword = passwordUtils.verifyPassword(password, user.getPasswordHashed());

        if (!isMatchPassword) {
            logger.log("Contraseña Incorrecta: " + email + ", " + password);
            throw new ServiceException("Contraseña incorrecta");
        }
        logger.log("Usuario logueado: " + user.getName() + ", " + user.getEmail());
        return convertToDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsersExcept(String email) throws ServiceException {
        logger.log("Obteniendo todas los usuarios");
        List<UserEntity> users = userDAO.findAll();

        return users.stream()
                .filter(u -> !u.getEmail().equals(email))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(UserEntity entity) {
        return UserDTO.fromEntity(entity);
    }
}
