package DAO.Interfaces;


import Models.Entidades.ChannelUser.ChannelEntity;

import java.util.List;

public interface IChannelDAO {
    ChannelEntity findById(int id);
    List<ChannelEntity> findAll();
    List<ChannelEntity> findPublicChannels();
    void save(ChannelEntity channel);
    void update(ChannelEntity channel);
    void delete(int id);
}