package com.example.hospitalapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalapp.db.DBManager;
import com.example.hospitalappointment.R;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvPaymentMethod;
    private TextView tvPrice;
    private RadioGroup rgPaymentMethod;
    private ImageView ivQRCode;
    private Button btnCancel;
    private Button btnConfirm;
    private DBManager dbManager;
    private int appointmentId;
    private String paymentMethod;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // 初始化视图
        tvPaymentMethod = findViewById(R.id.tv_payment_method);
        tvPrice = findViewById(R.id.tv_price);
        rgPaymentMethod = findViewById(R.id.rg_payment_method);
        ivQRCode = findViewById(R.id.iv_qr_code);
        btnCancel = findViewById(R.id.btn_cancel);
        btnConfirm = findViewById(R.id.btn_confirm);
        dbManager = new DBManager(this);

        // 获取传递的数据
        Intent intent = getIntent();
        if (intent != null) {
            appointmentId = intent.getIntExtra("appointmentId", -1);
            paymentMethod = intent.getStringExtra("paymentMethod");
            price = intent.getDoubleExtra("price", 20.0); // 默认20元

            // 设置支付方式和金额
            tvPrice.setText("￥" + String.format("%.2f", price));
        }

        // 设置支付方式选择监听
        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_alipay) {
                paymentMethod = "支付宝";
                ivQRCode.setImageResource(R.drawable.img_scan);
            } else if (checkedId == R.id.rb_wechat) {
                paymentMethod = "微信";
                ivQRCode.setImageResource(R.drawable.img_scan);
            }
            tvPaymentMethod.setText(paymentMethod + "扫码支付");
        });

        // 设置按钮点击事件
        btnCancel.setOnClickListener(v -> finish());

        btnConfirm.setOnClickListener(v -> {
            if (appointmentId != -1) {
                // 更新预约状态为已支付
                dbManager.payAppointment(appointmentId, paymentMethod);
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                
                // 返回预约成功的结果
                Intent resultIntent = new Intent();
                resultIntent.putExtra("appointmentId", appointmentId);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "支付失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

        // 默认选中支付宝
        rgPaymentMethod.check(R.id.rb_alipay);
    }
} 