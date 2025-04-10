package Models.DTOs.ChannelUser;

import java.time.LocalDateTime;
import Models.Entidades.ChannelUser.ChannelUserEntity;
import Models.Entidades.ChannelUser.UserRole;

public class ChannelUserDTO {
    private int id;
    private int userId;
    private String userName;  // Información adicional útil
    private int channelId;
    private String channelName;  // Información adicional útil
    private UserRole role;
    private LocalDateTime joinedAt;

    public ChannelUserDTO() {}

    public ChannelUserDTO(int id, int userId, String userName, int channelId, String channelName,
                          UserRole role, LocalDateTime joinedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.role = role;
        this.joinedAt = joinedAt;
    }

    public static ChannelUserDTO fromEntity(ChannelUserEntity entity, String userName, String channelName) {
        return new ChannelUserDTO(
                entity.getId(),
                entity.getUserId(),
                userName,
                entity.getChannelId(),
                channelName,
                entity.getRole(),
                entity.getJoinedAt()
        );
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public int getChannelId() { return channelId; }
    public void setChannelId(int channelId) { this.channelId = channelId; }

    public String getChannelName() { return channelName; }
    public void setChannelName(String channelName) { this.channelName = channelName; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
}