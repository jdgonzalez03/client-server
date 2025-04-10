package Models.DTOs.ChannelUser;

import java.time.LocalDateTime;
import Models.Entidades.ChannelUser.ChannelEntity;

public class ChannelDTO {
    private int id;
    private String name;
    private boolean isPublic;
    private LocalDateTime createAt;

    public ChannelDTO() {}

    public ChannelDTO(int id, String name, boolean isPublic, LocalDateTime createAt) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.createAt = createAt;
    }

    public static ChannelDTO fromEntity(ChannelEntity entity) {
        return new ChannelDTO(
                entity.getId(),
                entity.getName(),
                entity.isPublic(),
                entity.getCreateAt()
        );
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isPublic() { return isPublic; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }

    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }
}
