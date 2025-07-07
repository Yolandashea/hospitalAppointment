package com.example.hospitalapp.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.hospitalapp.db.DBManager;
import com.example.hospitalappointment.R;

public class PaymentDialog {

    public interface OnPaymentResultListener {
        void onPaymentSuccess();
    }

    public static void show(Context context, int appointmentId, OnPaymentResultListener listener) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_payment, null);
        RadioButton rbAliPay = view.findViewById(R.id.rb_alipay);
        RadioButton rbWeChat = view.findViewById(R.id.rb_wechat);
        Button btnPay = view.findViewById(R.id.btn_pay);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("请选择支付方式")
                .setView(view)
                .create();

        btnPay.setOnClickListener(v -> {
            if (!rbAliPay.isChecked() && !rbWeChat.isChecked()) {
                Toast.makeText(context, "请选择一种支付方式", Toast.LENGTH_SHORT).show();
                return;
            }

            // 模拟支付成功逻辑
            DBManager dbManager = new DBManager(context);
            dbManager.updateAppointmentPaid(appointmentId, true);

            Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
            dialog.dismiss();

            if (listener != null) {
                listener.onPaymentSuccess();
            }
        });

        dialog.show();
    }
}