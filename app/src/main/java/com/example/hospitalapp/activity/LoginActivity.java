package com.example.hospitalapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.User;
import com.example.hospitalappointment.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnToRegister;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);  // 使用登录界面布局

        dbManager = new DBManager(this);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        btnLogin = findViewById(R.id.btn_login);
        btnToRegister = findViewById(R.id.btn_to_register);

        btnLogin.setOnClickListener(v -> loginUser());
        btnToRegister.setOnClickListener(v -> {
            // 跳转到注册页面
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = dbManager.login(username, password);

        if (user != null) {
            // 保存完整的用户信息到SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", user.getUsername());
            editor.putString("mobile", user.getMobile());
            editor.putString("id_card", user.getIdCard());
            editor.putString("gender", user.getGender());
            editor.putInt("age", user.getAge());
            editor.putInt("user_id", user.getId());
            editor.apply();

            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            // 登录成功后跳转到主页面
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
        }
    }
}
