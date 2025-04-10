package DAO.Interfaces;

import Models.Entidades.Request.RequestEntity;
import Models.Entidades.Request.RequestStatus;

import java.util.List;

public interface IRequestDAO {
    RequestEntity findById(int id);
    List<RequestEntity> findAll();
    List<RequestEntity> findByReceiverId(int receiverId);
    List<RequestEntity> findBySenderId(int senderId);
    List<RequestEntity> findByChannelId(int channelId);
    List<RequestEntity> findByStatus(RequestStatus status);
    void save(RequestEntity request);
    void updateStatus(int requestId, RequestStatus newStatus);
    void delete(int id);
}