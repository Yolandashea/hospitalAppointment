package com.example.hospitalapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.hospitalapp.model.User;
import com.example.hospitalapp.model.Doctor;
import com.example.hospitalapp.model.Department;
import com.example.hospitalapp.model.Appointment;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private DBHelper dbHelper;

    public DBManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    // 注册用户
    public boolean registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.query("user", null, "username=?", new String[]{user.getUsername()}, null, null, null);
        if (cursor.moveToFirst()) {
            cursor.close();
            return false; // 用户名已存在
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("mobile", user.getMobile());
        values.put("id_card", user.getIdCard());
        values.put("gender", user.getGender());
        values.put("age", user.getAge());

        db.insert("user", null, values);
        return true;
    }

    // 登录验证
    public User login(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "username=? AND password=?", new String[]{username, password}, null, null, null);

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setMobile(cursor.getString(cursor.getColumnIndexOrThrow("mobile")));
            user.setIdCard(cursor.getString(cursor.getColumnIndexOrThrow("id_card")));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

    // 获取所有科室
    public List<Department> getAllDepartments() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("department", null, null, null, null, null, null);

        List<Department> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Department d = new Department();
            d.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            d.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            list.add(d);
        }
        cursor.close();
        return list;
    }

    // 根据科室ID获取医生
    public List<Doctor> getDoctorsByDepartment(int departmentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Doctor> doctorList = new ArrayList<>();

        Cursor cursor = db.query("doctor", null, "department_id=?", new String[]{String.valueOf(departmentId)}, null, null, null);

        while (cursor.moveToNext()) {
            Doctor doctor = new Doctor();
            doctor.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            doctor.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            doctor.setDepartmentId(cursor.getInt(cursor.getColumnIndexOrThrow("department_id")));
            doctor.setSpecialty(cursor.getString(cursor.getColumnIndexOrThrow("specialty")));
            doctorList.add(doctor);
        }

        cursor.close();
        return doctorList;
    }

    // 创建预约
    public int insertAppointment(Appointment appt) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 防止重复预约同一时段
        Cursor cursor = db.query("appointment", null,
                "doctor_id=? AND date=? AND time_slot=? AND status != '已取消'",
                new String[]{String.valueOf(appt.getDoctorId()), appt.getDate(), appt.getTimeSlot()},
                null, null, null);

        if (cursor.moveToFirst()) {
            cursor.close();
            return -1; // 已有预约
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put("user_id", appt.getUserId());
        values.put("doctor_id", appt.getDoctorId());
        values.put("date", appt.getDate());
        values.put("time_slot", appt.getTimeSlot());
        values.put("status", "待支付");
        values.put("pay_channel", ""); // 初始为空

        long id = db.insert("appointment", null, values);
        return (int) id;
    }

    // 获取用户预约记录
    public List<Appointment> getAppointmentsByUser(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("appointment", null, "user_id=?", new String[]{String.valueOf(userId)}, null, null, "date DESC");

        List<Appointment> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            Appointment a = new Appointment();
            a.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            a.setUserId(userId);
            a.setDoctorId(cursor.getInt(cursor.getColumnIndexOrThrow("doctor_id")));
            a.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            a.setTimeSlot(cursor.getString(cursor.getColumnIndexOrThrow("time_slot")));
            a.setStatus(cursor.getString(cursor.getColumnIndexOrThrow("status")));
            a.setPayChannel(cursor.getString(cursor.getColumnIndexOrThrow("pay_channel")));
            list.add(a);
        }
        cursor.close();
        return list;
    }

    // 更新预约支付状态
    public void payAppointment(int appointmentId, String channel) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "已支付");
        values.put("pay_channel", channel);
        db.update("appointment", values, "id=?", new String[]{String.valueOf(appointmentId)});
    }

    // 取消预约
    public void cancelAppointment(int appointmentId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", "已取消");
        db.update("appointment", values, "id=?", new String[]{String.valueOf(appointmentId)});
    }

    // 验证用户名和旧密码是否匹配
    public boolean checkUserPassword(String username, String oldPassword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "username=? AND password=?", new String[]{username, oldPassword}, null, null, null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    // 更新用户密码
    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("user", values, "username=?", new String[]{username});
        return rows > 0;
    }


    // 在 DBManager 中添加该方法
    public int getAppointmentCount(String doctorName, String date, String timeSlot) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询数据库中符合条件的预约记录
        Cursor cursor = db.query("appointment", new String[]{"COUNT(*)"},
                "doctor_id = (SELECT id FROM doctor WHERE name = ?) AND date = ? AND time_slot = ? AND status != '已取消'",
                new String[]{doctorName, date, timeSlot}, null, null, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);  // 获取计数结果
        }
        cursor.close();
        return count;
    }

    public void updateAppointmentPaid(int appointmentId, boolean isPaid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // 更新支付状态为已支付
        values.put("status", isPaid ? "已支付" : "未支付");

        // 如果需要，可以更新支付渠道
        // values.put("pay_channel", isPaid ? "支付宝" : "微信");

        db.update("appointment", values, "id=?", new String[]{String.valueOf(appointmentId)});
    }

    // 判断用户名是否已存在
    public boolean isUserExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "username=?", new String[]{username}, null, null, null);
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // 插入新用户
    public void insertUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("mobile", user.getMobile());
        values.put("id_card", user.getIdCard());
        values.put("gender", user.getGender());
        values.put("age", user.getAge());

        db.insert("user", null, values);
    }

    public List<Doctor> searchDoctorInDepartment(String departmentName, String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Doctor> doctorList = new ArrayList<>();

        // 查询符合条件的医生
        Cursor cursor = db.rawQuery("SELECT * FROM doctor WHERE department_id = (SELECT id FROM department WHERE name = ?) AND name LIKE ?",
                new String[]{departmentName, "%" + keyword + "%"});

        while (cursor.moveToNext()) {
            Doctor doctor = new Doctor();
            doctor.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            doctor.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            doctor.setDepartmentId(cursor.getInt(cursor.getColumnIndexOrThrow("department_id")));
            doctor.setSpecialty(cursor.getString(cursor.getColumnIndexOrThrow("specialty")));
            doctorList.add(doctor);
        }

        cursor.close();
        return doctorList;
    }

    // 添加 searchDepartments 方法
    public List<Department> searchDepartments(String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Department> departmentList = new ArrayList<>();

        // 使用 LIKE 查询来模糊匹配科室名称
        Cursor cursor = db.rawQuery("SELECT * FROM department WHERE name LIKE ?", new String[]{"%" + keyword + "%"});

        while (cursor.moveToNext()) {
            Department department = new Department();
            department.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            department.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            departmentList.add(department);
        }

        cursor.close();
        return departmentList;
    }

    // 根据用户ID获取用户信息
    public User getUserById(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "id=?", new String[]{String.valueOf(userId)}, null, null, null);

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setMobile(cursor.getString(cursor.getColumnIndexOrThrow("mobile")));
            user.setIdCard(cursor.getString(cursor.getColumnIndexOrThrow("id_card")));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }


    // 在 DBManager 中获取医生的名字
    public String getDoctorNameById(int doctorId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("doctor", new String[]{"name"}, "id=?", new String[]{String.valueOf(doctorId)}, null, null, null);

        String doctorName = null;
        if (cursor.moveToFirst()) {
            doctorName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        cursor.close();
        return doctorName;
    }

    // 在 DBManager 中根据医生 ID 获取科室名称
    public String getDepartmentNameByDoctorId(int doctorId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT d.name FROM department d JOIN doctor doc ON d.id = doc.department_id WHERE doc.id = ?",
                new String[]{String.valueOf(doctorId)});

        String departmentName = null;
        if (cursor.moveToFirst()) {
            departmentName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        cursor.close();
        return departmentName;
    }

    // 获取医生简介
    public String getDoctorIntro(int doctorId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("doctor", new String[]{"specialty"}, "id=?", new String[]{String.valueOf(doctorId)}, null, null, null);

        String specialty = null;
        if (cursor.moveToFirst()) {
            specialty = cursor.getString(cursor.getColumnIndexOrThrow("specialty"));
        }
        cursor.close();
        return specialty;
    }

    // 更新预约状态
    public void updateAppointmentStatus(int appointmentId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        db.update("appointment", values, "id=?", new String[]{String.valueOf(appointmentId)});
    }

    // 验证密码
    public boolean verifyPassword(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "username=? AND password=?", new String[]{username, password}, null, null, null);
        boolean result = cursor.moveToFirst();
        cursor.close();
        return result;
    }

    // 更新密码
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        int rows = db.update("user", values, "username=?", new String[]{username});
        return rows > 0;
    }

    // 获取医生ID
    public int getDoctorIdByName(String doctorName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("doctor", new String[]{"id"}, "name=?", new String[]{doctorName}, null, null, null);

        int doctorId = -1;
        if (cursor.moveToFirst()) {
            doctorId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return doctorId;
    }

    // 获取科室ID
    public int getDepartmentIdByName(String departmentName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("department", new String[]{"id"}, "name=?", new String[]{departmentName}, null, null, null);

        int departmentId = -1;
        if (cursor.moveToFirst()) {
            departmentId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return departmentId;
    }

    // 检查时间段是否可预约
    public boolean isTimeSlotAvailable(int doctorId, String date, String timeSlot) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("appointment", new String[]{"COUNT(*)"},
                "doctor_id=? AND date=? AND time_slot=? AND status != '已取消'",
                new String[]{String.valueOf(doctorId), date, timeSlot},
                null, null, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count < 5; // 假设每个时间段最多可预约5个
    }

    // 获取科室名称
    public String getDepartmentNameById(int departmentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("department", new String[]{"name"}, "id=?", new String[]{String.valueOf(departmentId)}, null, null, null);

        String departmentName = null;
        if (cursor.moveToFirst()) {
            departmentName = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        }
        cursor.close();
        return departmentName;
    }

    // 在科室中搜索医生
    public List<Doctor> searchDoctorInDepartment(int departmentId, String keyword) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Doctor> doctorList = new ArrayList<>();

        Cursor cursor = db.query("doctor", null, "department_id=? AND name LIKE ?",
                new String[]{String.valueOf(departmentId), "%" + keyword + "%"}, null, null, null);

        while (cursor.moveToNext()) {
            Doctor doctor = new Doctor();
            doctor.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            doctor.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            doctor.setDepartmentId(cursor.getInt(cursor.getColumnIndexOrThrow("department_id")));
            doctor.setSpecialty(cursor.getString(cursor.getColumnIndexOrThrow("specialty")));
            doctorList.add(doctor);
        }

        cursor.close();
        return doctorList;
    }

    // 根据用户名获取用户信息
    public User getUserByUsername(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("user", null, "username=?", new String[]{username}, null, null, null);

        if (cursor.moveToFirst()) {
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow("username")));
            user.setMobile(cursor.getString(cursor.getColumnIndexOrThrow("mobile")));
            user.setIdCard(cursor.getString(cursor.getColumnIndexOrThrow("id_card")));
            user.setGender(cursor.getString(cursor.getColumnIndexOrThrow("gender")));
            user.setAge(cursor.getInt(cursor.getColumnIndexOrThrow("age")));
            cursor.close();
            return user;
        }

        cursor.close();
        return null;
    }

}
