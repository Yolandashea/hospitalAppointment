package com.example.hospitalapp.model;

public class Appointment {
    private int id;
    private int userId;
    private int doctorId;
    private String date;       // 格式: yyyy-MM-dd
    private String timeSlot;   // “上午” 或 “下午”
    private String status;     // "待支付" / "已支付" / "已取消" / "已满"

    public Appointment() {
    }

    public Appointment(int userId, int doctorId, String date, String timeSlot, String status) {
        this.userId = userId;
        this.doctorId = doctorId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status;
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPayChannel(String payChannel) {
    }
    public byte[] getPayChannel() {
        return new byte[0];
    }

    public void setAppointmentTime(String appointmentTime) {
    }

    public byte[] getAppointmentTime() {
        return new byte[0];
    }
}
