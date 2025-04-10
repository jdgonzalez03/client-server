package DAO.Interfaces;

import Models.Entidades.ChannelUser.*;

import java.util.List;

public interface IChannelUserDAO {
    ChannelUserEntity findById(int id);
    List<ChannelUserEntity> findAll();
    List<ChannelUserEntity> findByChannelId(int id);
    List<UserEntity> findUsersByChannelId(int channelId);
    List<ChannelEntity> findChannelsByUserId(int userId);
    void save(ChannelUserEntity channelUser);
    void update(ChannelUserEntity channelUser);
    void delete(int id);
}
