package com.example.hospitalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalapp.adapter.DoctorAdapter;
import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.Doctor;
import com.example.hospitalappointment.R;

import java.util.ArrayList;
import java.util.List;

public class SelectDoctorActivity extends AppCompatActivity {

    private ListView lvDoctor;
    private DoctorAdapter adapter;
    private List<Doctor> doctorList = new ArrayList<>();
    private DBManager dbManager;
    private int departmentId;
    private TextView tvDepartmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

        lvDoctor = findViewById(R.id.lv_doctor);
        tvDepartmentTitle = findViewById(R.id.tv_department_title);
        dbManager = new DBManager(this);

        departmentId = getIntent().getIntExtra("department", -1);
        if (departmentId == -1) {
            finish();
            return;
        }

        // 设置科室名称
        String departmentName = dbManager.getDepartmentNameById(departmentId);
        if (departmentName != null) {
            tvDepartmentTitle.setText(departmentName + " - 选择医生");
        }

        // 获取医生列表
        doctorList = dbManager.getDoctorsByDepartment(departmentId);
        adapter = new DoctorAdapter(this, doctorList);
        lvDoctor.setAdapter(adapter);

        // 搜索医生
        SearchView searchView = findViewById(R.id.search_doctor);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterDoctors(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDoctors(newText);
                return true;
            }
        });

        // 点击医生项跳转到预约界面
        lvDoctor.setOnItemClickListener((parent, view, position, id) -> {
            Doctor doctor = doctorList.get(position);
            Intent intent = new Intent(this, MakeAppointmentActivity.class);
            intent.putExtra("doctorId", doctor.getId());
            intent.putExtra("doctorName", doctor.getName());
            intent.putExtra("department", departmentName);
            intent.putExtra("doctorSpecialty", doctor.getSpecialty());
            startActivity(intent);
        });
    }

    private void filterDoctors(String keyword) {
        List<Doctor> filteredList;
        if (keyword == null || keyword.isEmpty()) {
            filteredList = dbManager.getDoctorsByDepartment(departmentId);
        } else {
            filteredList = dbManager.searchDoctorInDepartment(departmentId, keyword);
        }
        adapter.updateData(filteredList);
    }
}