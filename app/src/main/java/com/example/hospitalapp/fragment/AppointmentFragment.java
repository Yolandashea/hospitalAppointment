package com.example.hospitalapp.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.example.hospitalapp.adapter.AppointmentAdapter;
import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.Appointment;
import com.example.hospitalappointment.R;

import java.util.List;

public class AppointmentFragment extends Fragment {

    private ListView lvAppointments;
    private AppointmentAdapter adapter;
    private DBManager dbManager;
    private ActivityResultLauncher<Intent> paymentLauncher;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 初始化支付结果处理器
        paymentLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    // 支付成功，刷新预约列表
                    refreshAppointmentList();
                }
            }
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);

        // 绑定 ListView
        lvAppointments = rootView.findViewById(R.id.lv_appointments);

        // 初始化 DBManager
        dbManager = new DBManager(getContext());

        // 获取当前用户ID
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (!username.isEmpty()) {
            refreshAppointmentList();
        }

        return rootView;
    }

    private void refreshAppointmentList() {
        // 获取当前用户ID
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (!username.isEmpty()) {
            int userId = dbManager.getUserByUsername(username).getId();
            List<Appointment> appointmentList = dbManager.getAppointmentsByUser(userId);
            
            if (adapter == null) {
                adapter = new AppointmentAdapter(getContext(), appointmentList);
                lvAppointments.setAdapter(adapter);
            } else {
                adapter.updateData(appointmentList);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshAppointmentList();
    }

    public ActivityResultLauncher<Intent> getPaymentLauncher() {
        return paymentLauncher;
    }
}
