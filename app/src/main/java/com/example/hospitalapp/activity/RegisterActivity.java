package com.example.hospitalapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.User;
import com.example.hospitalappointment.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etMobile, etIdCard, etGender, etAge;
    private Button btnRegister, btnToLogin;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);  // 使用注册界面布局

        dbManager = new DBManager(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etMobile = findViewById(R.id.et_mobile);
        etIdCard = findViewById(R.id.et_idcard);
        etGender = findViewById(R.id.et_gender);
        etAge = findViewById(R.id.et_age);

        btnRegister = findViewById(R.id.btn_register);
        btnToLogin = findViewById(R.id.btn_to_login);

        btnRegister.setOnClickListener(v -> registerUser());
        btnToLogin.setOnClickListener(v -> finish());  // 返回到登录页面
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String mobile = etMobile.getText().toString().trim();
        String idCard = etIdCard.getText().toString().trim();
        String gender = etGender.getText().toString().trim();
        String ageStr = etAge.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || mobile.isEmpty() || idCard.isEmpty() ||
                gender.isEmpty() || ageStr.isEmpty()) {
            Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
            return;
        }

        int age = Integer.parseInt(ageStr);
        User user = new User(username, password, mobile, idCard, gender, age);

        if (dbManager.registerUser(user)) {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();  // 注册成功后返回登录页面
        } else {
            Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
        }
    }
}
