package com.example.hospitalapp.model;

public class TimeSlot {
    private String date;       // yyyy-MM-dd格式日期
    private String timePeriod; // 上午 / 下午
    private boolean isAvailable; // 是否可预约

    public TimeSlot(String date, String timePeriod, boolean isAvailable) {
        this.date = date;
        this.timePeriod = timePeriod;
        this.isAvailable = isAvailable;
    }

    public String getDate() {
        return date;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
