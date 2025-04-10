package Models.DTOs.ChannelUser;

import Models.Entidades.ChannelUser.UserEntity;

public class UserDTO {
    private int id;
    private String name;
    private String email;
    private byte[] photo;
    private String ip;

    public UserDTO() {}

    public UserDTO(int id, String name, String email, byte[] photo, String ip) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.ip = ip;
    }

    // Método estático para convertir de entidad a DTO
    public static UserDTO fromEntity(UserEntity entity) {
        return new UserDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPhoto(),
                entity.getIp()
        );
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }

    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
}