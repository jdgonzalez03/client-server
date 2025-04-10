package Services.User;

import Models.DTOs.ChannelUser.UserDTO;

import java.util.List;

public interface IUserService {
    UserDTO registerUser(UserDTO userDTO, String password) throws Exception;
    UserDTO loginUser(String email, String password) throws Exception;
    //UserDTO getUserById(int userId) throws Exception;
    List<UserDTO> getAllUsersExcept(String email) throws Exception;
}