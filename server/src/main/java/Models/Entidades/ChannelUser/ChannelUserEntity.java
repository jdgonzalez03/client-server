package Models.Entidades.ChannelUser;

import java.time.LocalDateTime;

public class ChannelUserEntity {
    private int id;
    private int userId;
    private int channelId;
    private UserRole role;
    private LocalDateTime joinedAt;

    public ChannelUserEntity() {}

    public ChannelUserEntity(int id, int userId, int channelId, UserRole role) {
        this.id = id;
        this.userId = userId;
        this.channelId = channelId;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getChannelId() { return channelId; }
    public void setChannelId(int channelId) { this.channelId = channelId; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getJoinedAt() { return joinedAt; }
    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }
}