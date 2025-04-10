package Models.Entidades.ChannelUser;

import java.time.LocalDateTime;

public class ChannelEntity {
    private int id;
    private String name;
    private boolean isPublic;
    private LocalDateTime createAt;

    public ChannelEntity() {}

    public ChannelEntity(int id, String name, boolean isPublic) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.createAt = LocalDateTime.now();
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