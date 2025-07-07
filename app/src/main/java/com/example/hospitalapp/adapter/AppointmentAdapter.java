package com.example.hospitalapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Intent;
import android.app.Activity;

import com.example.hospitalapp.activity.PaymentDialog;
import com.example.hospitalapp.model.Appointment;
import com.example.hospitalapp.db.DBManager;
import com.example.hospitalappointment.R;
import com.example.hospitalapp.activity.PaymentActivity;
import com.example.hospitalapp.activity.MainActivity;
import com.example.hospitalapp.fragment.AppointmentFragment;

import java.util.List;

public class AppointmentAdapter extends BaseAdapter {

    private Context context;
    private List<Appointment> appointmentList;
    private LayoutInflater inflater;
    private DBManager dbManager;

    public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
        inflater = LayoutInflater.from(context);
        dbManager = new DBManager(context); // 初始化 DBManager
    }

    @Override
    public int getCount() {
        return appointmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return appointmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return appointmentList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_appointment, parent, false);
            holder = new ViewHolder();
            holder.tvDoctorName = convertView.findViewById(R.id.tv_appointment_doctor);
            holder.tvDepartment = convertView.findViewById(R.id.tv_appointment_department);
            holder.tvTime = convertView.findViewById(R.id.tv_appointment_time);
            holder.tvStatus = convertView.findViewById(R.id.tv_appointment_status);
            holder.btnPay = convertView.findViewById(R.id.btn_pay);
            holder.btnCancel = convertView.findViewById(R.id.btn_cancel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 获取当前预约对象
        Appointment appointment = appointmentList.get(position);

        // 获取医生信息和科室信息
        String doctorName = dbManager.getDoctorNameById(appointment.getDoctorId());
        String departmentName = dbManager.getDepartmentNameByDoctorId(appointment.getDoctorId());

        // 设置视图中的数据
        holder.tvDoctorName.setText("医生：" + doctorName);
        holder.tvDepartment.setText("科室：" + departmentName);
        holder.tvTime.setText("时间：" + appointment.getDate() + " " + appointment.getTimeSlot());

        // 根据状态设置不同的颜色
        String status = appointment.getStatus();
        holder.tvStatus.setText("状态：" + status);
        switch (status) {
            case "已预约":
                holder.tvStatus.setTextColor(Color.parseColor("#FF0000")); // 红色
                break;
            case "已支付":
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // 绿色
                break;
            case "已取消":
                holder.tvStatus.setTextColor(Color.parseColor("#9E9E9E")); // 灰色
                break;
        }

        // 根据状态显示不同的操作按钮
        if (status.equals("待支付")) {
            holder.btnPay.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.VISIBLE);

            holder.btnPay.setOnClickListener(v -> {
                // 跳转到支付界面
                Intent intent = new Intent(context, PaymentActivity.class);
                intent.putExtra("appointmentId", appointment.getId());
                intent.putExtra("paymentMethod", "支付宝"); // 默认支付宝，实际支付方式在PaymentActivity中选择
                intent.putExtra("price", 20.0); // 设置价格，这里固定为20元
                
                // 使用Fragment的ActivityResultLauncher
                if (context instanceof MainActivity) {
                    MainActivity activity = (MainActivity) context;
                    AppointmentFragment fragment = (AppointmentFragment) activity.getSupportFragmentManager()
                            .findFragmentById(R.id.fragment_container);
                    if (fragment != null) {
                        fragment.getPaymentLauncher().launch(intent);
                    }
                }
            });

            holder.btnCancel.setOnClickListener(v -> {
                // 执行取消逻辑
                new AlertDialog.Builder(context)
                    .setTitle("确认取消")
                    .setMessage("确定要取消这个预约吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        appointment.setStatus("已取消");
                        dbManager.updateAppointmentStatus(appointment.getId(), "已取消");
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("取消", null)
                    .show();
            });
        } else {
            holder.btnPay.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
        }

        return convertView;
    }

    // 用于优化性能的 ViewHolder 模式
    static class ViewHolder {
        TextView tvDoctorName;
        TextView tvDepartment;
        TextView tvTime;
        TextView tvStatus;
        Button btnPay;
        Button btnCancel;
    }

    // 更新适配器的数据
    public void updateData(List<Appointment> newAppointmentList) {
        this.appointmentList = newAppointmentList;
        notifyDataSetChanged();
    }
}
