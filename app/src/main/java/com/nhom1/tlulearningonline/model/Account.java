package com.nhom1.tlulearningonline.model;

import java.io.Serializable;

public class Account implements Serializable {
    private String username;
    private String password;
    private String fullName;
    private String role;
    private String email;
    private String phoneNumber;
    private String msv;
    private String className;

    public Account(String msv, String fullName, String role, String className) {
        this.msv = msv;
        this.fullName = fullName;
        this.role = role;
        this.className = className;
    }

    public Account(String username, String password, String fullName, String role, String email, String phoneNumber, String msv, String className) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.msv = msv;
        this.className = className;
    }

    public Account(String number, String phạmNhậtAnh, String sv, String s, String mail, String number1) {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMsv() {
        return msv;
    }

    public void setMsv(String msv) {
        this.msv = msv;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
