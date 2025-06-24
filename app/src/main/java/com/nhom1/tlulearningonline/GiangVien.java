package com.nhom1.tlulearningonline;


public class GiangVien {
    private String ten;
    private String boMon;
    private int avatarResId; // hoặc String avatarUrl nếu dùng ảnh online

    public GiangVien(String ten, String boMon, int avatarResId) {
        this.ten = ten;
        this.boMon = boMon;
        this.avatarResId = avatarResId;
    }

    public String getTen() {
        return ten;
    }

    public String getBoMon() {
        return boMon;
    }

    public int getAvatarResId() {
        return avatarResId;
    }
}

