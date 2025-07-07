package com.example.hospitalapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.Doctor;
import com.example.hospitalappointment.R;

import java.util.List;

public class DoctorAdapter extends BaseAdapter {
    private Context context;
    private List<Doctor> doctorList;
    private DBManager dbManager;

    public DoctorAdapter(Context context, List<Doctor> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        this.dbManager = new DBManager(context);
    }

    @Override
    public int getCount() {
        return doctorList.size();
    }

    @Override
    public Object getItem(int position) {
        return doctorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return doctorList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);
            holder = new ViewHolder();
            holder.doctorName = convertView.findViewById(R.id.tv_doctor_name);
            holder.doctorDepartment = convertView.findViewById(R.id.tv_doctor_department);
            holder.doctorSpecialty = convertView.findViewById(R.id.tv_doctor_specialty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Doctor doctor = doctorList.get(position);
        
        // 设置医生姓名
        holder.doctorName.setText(doctor.getName());
        
        // 设置科室名称
        String departmentName = dbManager.getDepartmentNameById(doctor.getDepartmentId());
        holder.doctorDepartment.setText("科室：" + departmentName);
        
        // 设置专业特长
        String specialty = doctor.getSpecialty();
        if (specialty != null && !specialty.isEmpty()) {
            holder.doctorSpecialty.setText("擅长：" + specialty);
        } else {
            holder.doctorSpecialty.setText("暂无特长信息");
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView doctorName;
        TextView doctorDepartment;
        TextView doctorSpecialty;
    }

    // 刷新数据
    public void updateData(List<Doctor> newDoctorList) {
        this.doctorList = newDoctorList;
        notifyDataSetChanged();
    }
}
