package Models.Entidades.ChannelUser;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class UserEntity {
    private int id;
    private String name;
    private String email;
    private String passwordHashed;
    private byte[] photo;
    private String ip;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public UserEntity() {}

    public UserEntity(int id, String name, String email, String passwordHashed, byte[] photo, String ip, Timestamp createAt, Timestamp updateAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHashed = passwordHashed;
        this.photo = photo;
        this.ip = ip;
        this.createAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHashed() { return passwordHashed; }
    public void setPasswordHashed(String passwordHashed) { this.passwordHashed = passwordHashed; }

    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }

    public LocalDateTime getCreateAt() { return createAt; }
    public void setCreateAt(LocalDateTime createAt) { this.createAt = createAt; }

    public LocalDateTime getUpdateAt() { return updateAt; }
    public void setUpdateAt(LocalDateTime updateAt) { this.updateAt = updateAt; }
}