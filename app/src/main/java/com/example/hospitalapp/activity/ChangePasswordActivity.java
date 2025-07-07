package com.example.hospitalapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalapp.db.DBManager;
import com.example.hospitalappointment.R;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;
    private Button btnChange;
    private DBManager dbManager;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        dbManager = new DBManager(this);
        username = getSharedPreferences("user_info", MODE_PRIVATE).getString("username", "");

        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnChange = findViewById(R.id.btn_change_password);

        btnChange.setOnClickListener(v -> {
            String oldPassword = etOldPassword.getText().toString().trim();
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                return;
            }

            // 验证旧密码
            if (!dbManager.verifyPassword(username, oldPassword)) {
                Toast.makeText(this, "旧密码错误", Toast.LENGTH_SHORT).show();
                return;
            }

            // 更新密码
            if (dbManager.updatePassword(username, newPassword)) {
                Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "密码修改失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}