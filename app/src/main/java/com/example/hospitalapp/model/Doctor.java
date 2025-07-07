package com.example.hospitalapp.model;

public class Doctor {
    private int id;
    private String name;
    private int departmentId;
    private String specialty;

    // 构造方法
    public Doctor() {
    }

    public Doctor(String name, int departmentId, String specialty) {
        this.name = name;
        this.departmentId = departmentId;
        this.specialty = specialty;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public void setIntroduction(String introduction) {
    }

    public void setTitle(String title) {
    }
}
