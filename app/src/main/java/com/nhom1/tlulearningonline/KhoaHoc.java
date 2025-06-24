package com.nhom1.tlulearningonline;


public class KhoaHoc {
    private String tenKhoaHoc;
    private String tenGiangVien;
    private String boMon;
    private int soBai;

    public KhoaHoc(String tenKhoaHoc, String tenGiangVien, String boMon, int soBai) {
        this.tenKhoaHoc = tenKhoaHoc;
        this.tenGiangVien = tenGiangVien;
        this.boMon = boMon;

        this.soBai = soBai;
    }

    public String getTenKhoaHoc() {
        return tenKhoaHoc;
    }

    public String getTenGiangVien() {
        return tenGiangVien;
    }

    public String getBoMon() {
        return boMon;
    }


    public int getSoBai() {
        return soBai;
    }
}

