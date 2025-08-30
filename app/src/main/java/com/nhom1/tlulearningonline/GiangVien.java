package com.nhom1.tlulearningonline;


public class GiangVien {
    private String id;
    private String username;
    private String password;
    private String role;
    private String avatarUrl;
    private String fullname;
    private String status;
    private String createdAt;
    private String updatedAt;

    public GiangVien(String id, String username, String password, String role, String avatarUrl,
                     String fullname, String status, String createdAt, String updatedAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.avatarUrl = avatarUrl;
        this.fullname = fullname;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getFullname() { return fullname; }
    public String getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
}


