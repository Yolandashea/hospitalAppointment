package com.example.hospitalapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalapp.adapter.TimeSlotAdapter;
import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.Appointment;
import com.example.hospitalapp.model.TimeSlot;
import com.example.hospitalapp.model.User;
import com.example.hospitalappointment.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MakeAppointmentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TimeSlotAdapter adapter;
    private List<TimeSlot> timeSlotList = new ArrayList<>();
    private TextView tvDoctorName, tvDepartmentName, tvDoctorIntro;
    private String doctorName, departmentName;
    private DBManager dbManager;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_appointment);

        // 初始化DBManager（移到最前面）
        dbManager = new DBManager(this);

        // 初始化视图
        tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvDepartmentName = findViewById(R.id.tv_department_name);
        tvDoctorIntro = findViewById(R.id.tv_doctor_intro);
        recyclerView = findViewById(R.id.recyclerView);

        // 获取传递过来的医生信息
        Intent intent = getIntent();
        if (intent != null) {
            doctorId = intent.getIntExtra("doctorId", -1);
            doctorName = intent.getStringExtra("doctorName");
            departmentName = intent.getStringExtra("department");

            // 验证必要参数
            if (doctorId == -1 || doctorName == null || departmentName == null) {
                Toast.makeText(this, "医生信息获取失败", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // 设置医生信息
            tvDoctorName.setText("医生姓名：" + doctorName);
            tvDepartmentName.setText("科室：" + departmentName);
            
            // 获取医生简介
            String doctorIntro = dbManager.getDoctorIntro(doctorId);
            if (doctorIntro != null) {
                tvDoctorIntro.setText("擅长方向：" + doctorIntro);
            } else {
                tvDoctorIntro.setText("擅长方向：暂无信息");
            }
        } else {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 生成时间段
        generateTimeSlots();

        // 初始化适配器
        adapter = new TimeSlotAdapter(timeSlotList, slot -> {
            // 点击时间段，弹出预约确认或者支付
            showAppointmentDialog(slot);
        });

        recyclerView.setAdapter(adapter);
    }

    private void generateTimeSlots() {
        timeSlotList.clear();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 0; i <= 5; i++) { // 今天 + 5天
            String date = sdf.format(calendar.getTime());

            // 判断当前时间，上午/下午是否过期
            boolean morningAvailable = isTimeAvailable(date, "上午");
            boolean afternoonAvailable = isTimeAvailable(date, "下午");

            timeSlotList.add(new TimeSlot(date, "上午", morningAvailable));
            timeSlotList.add(new TimeSlot(date, "下午", afternoonAvailable));

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * 判断该日期该时间段是否可预约
     */
    private boolean isTimeAvailable(String date, String timePeriod) {
        // 先判断是不是预约过去的时间段（当天上午时间点过了，则上午不可预约）
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String todayStr = sdf.format(now.getTime());

        if (date.equals(todayStr)) {
            int hour = now.get(Calendar.HOUR_OF_DAY);
            if (timePeriod.equals("上午") && hour >= 12) {
                return false; // 上午时间已经过了
            }
            if (timePeriod.equals("下午") && hour >= 18) {
                return false; // 下午时间已经过了
            }
        }

        // 查询数据库，判断是否已满（你需要实现dbManager查询预约状态方法）
        int bookedCount = dbManager.getAppointmentCount(doctorName, date, timePeriod);

        // 这里假设每个时间段最多可预约5个
        int maxCapacity = 5;
        return bookedCount < maxCapacity;
    }

    private void showAppointmentDialog(TimeSlot slot) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认预约");
        builder.setMessage("预约时间: " + slot.getDate() + " " + slot.getTimePeriod() +
                "\n医生: " + doctorName + "\n科室: " + departmentName);

        builder.setPositiveButton("确认预约", (dialog, which) -> {
            // 保存预约记录
            int appointmentId = saveAppointment(slot);
            if (appointmentId != -1) {
                // 显示支付方式选择对话框
                showPaymentMethodDialog(appointmentId);
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void showPaymentMethodDialog(int appointmentId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_payment_method, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();

        RadioGroup rgPaymentMethod = view.findViewById(R.id.rg_payment_method);
        Button btnConfirm = view.findViewById(R.id.btn_confirm_payment);
        Button btnCancel = view.findViewById(R.id.btn_cancel_payment);

        final String[] selectedPaymentMethod = {null};

        // 设置支付方式选择监听
        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_alipay) {
                selectedPaymentMethod[0] = "支付宝";
            } else if (checkedId == R.id.rb_wechat) {
                selectedPaymentMethod[0] = "微信";
            }
        });

        // 设置确认按钮点击事件
        btnConfirm.setOnClickListener(v -> {
            if (selectedPaymentMethod[0] == null) {
                Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
                return;
            }
            
            // 关闭选择对话框
            dialog.dismiss();
            
            // 跳转到支付界面
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("appointmentId", appointmentId);
            intent.putExtra("paymentMethod", selectedPaymentMethod[0]);
            intent.putExtra("price", 20.0); // 设置价格，这里固定为20元
            startActivityForResult(intent, 1);
        });

        // 设置取消按钮点击事件
        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private int saveAppointment(TimeSlot slot) {
        // 从SharedPreferences获取当前登录用户信息
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        
        if (username.isEmpty()) {
            Toast.makeText(this, "用户信息获取失败，请重新登录", Toast.LENGTH_SHORT).show();
            return -1;
        }

        // 获取用户ID
        User user = dbManager.getUserByUsername(username);
        if (user == null) {
            Toast.makeText(this, "用户信息获取失败，请重新登录", Toast.LENGTH_SHORT).show();
            return -1;
        }

        int currentUserId = user.getId();
        
        if (doctorId == -1) {
            Toast.makeText(this, "医生信息获取失败", Toast.LENGTH_SHORT).show();
            return -1;
        }

        // 保存预约到数据库，状态初始为"待支付"
        Appointment appointment = new Appointment(currentUserId, doctorId, slot.getDate(), slot.getTimePeriod(), "待支付");
        int appointmentId = dbManager.insertAppointment(appointment);
        
        if (appointmentId != -1) {
            Toast.makeText(this, "预约成功，请选择支付方式", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "预约失败，请重试", Toast.LENGTH_SHORT).show();
        }
        
        return appointmentId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 支付成功，返回上一页
            finish();

        }
    }
}
