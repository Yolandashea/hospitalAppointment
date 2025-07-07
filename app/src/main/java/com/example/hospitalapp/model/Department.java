package com.example.hospitalapp.model;

public class Department {
    private int id;
    private String name;

    public Department() {
    }

    public Department(String name) {
        this.name = name;
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

    public void setDescription(String description) {
    }
}
