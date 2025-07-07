package com.example.hospitalapp.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String mobile;
    private String idCard;
    private String gender;
    private int age;

    // 构造方法
    public User() {
    }

    public User(String username, String password, String mobile, String idCard, String gender, int age) {
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.idCard = idCard;
        this.gender = gender;
        this.age = age;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
