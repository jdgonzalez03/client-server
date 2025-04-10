package DAO.Interfaces;

import java.util.List;
import Models.Entidades.ChannelUser.UserEntity;

public interface IUserDAO {
    UserEntity save(UserEntity user);
    UserEntity findById(int id);
    List<UserEntity> findAll();
    void update(UserEntity user);
    void delete(int id);
    UserEntity findByEmail(String email);
}
