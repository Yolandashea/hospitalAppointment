package com.example.hospitalapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.hospitalapp.activity.ChangePasswordActivity;
import com.example.hospitalapp.activity.LoginActivity;
import com.example.hospitalapp.db.DBManager;
import com.example.hospitalapp.model.User;
import com.example.hospitalappointment.R;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvMobile, tvIdCard, tvGender, tvAge;
    private Button btnChangePassword, btnLogout;
    private DBManager dbManager;
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 初始化视图
        tvUsername = view.findViewById(R.id.tv_username);
        tvMobile = view.findViewById(R.id.tv_mobile);
        tvIdCard = view.findViewById(R.id.tv_idcard);
        tvGender = view.findViewById(R.id.tv_gender);
        tvAge = view.findViewById(R.id.tv_age);
        btnChangePassword = view.findViewById(R.id.btn_change_password);
        btnLogout = view.findViewById(R.id.btn_logout);

        // 初始化数据库管理器
        dbManager = new DBManager(getContext());

        // 初始化SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
        
        // 加载用户信息
        loadUserInfo();
        
        // 设置修改密码按钮点击事件
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        // 设置退出登录按钮点击事件
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // 清除登录信息
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    // 跳转到登录界面
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    
                    // 直接调用 finish()，无需 runOnUiThread
                    if (getActivity() != null) {
                                getActivity().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "退出登录失败: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 添加用户协议按钮点击事件
        Button btnUserAgreement = view.findViewById(R.id.btn_user_agreement);
        btnUserAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserAgreementDialog();
            }
        });
        
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次页面恢复时重新加载用户信息
        loadUserInfo();
    }

    private void loadUserInfo() {
        try {
            // 直接从SharedPreferences获取用户信息
            String username = sharedPreferences.getString("username", "");
            String mobile = sharedPreferences.getString("mobile", "");
            String idCard = sharedPreferences.getString("id_card", "");
            String gender = sharedPreferences.getString("gender", "");
            int age = sharedPreferences.getInt("age", 0);

            if (!username.isEmpty()) {
                tvUsername.setText(username);
                tvMobile.setText(mobile);
                tvIdCard.setText(idCard);
                tvGender.setText(gender);
                tvAge.setText(String.valueOf(age));
            } else {
                // 如果SharedPreferences中没有用户信息，说明用户未登录
                Toast.makeText(getContext(), "请先登录", Toast.LENGTH_SHORT).show();
                // 跳转到登录界面
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
        }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "加载用户信息失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void showUserAgreementDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_user_agreement);

        // 设置对话框宽度为屏幕的90%
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        // 设置协议内容
        TextView tvContent = dialog.findViewById(R.id.tv_agreement_content);
        String agreementContent = "你可以：\n\n" +
                "(1)根据该协议条款使用该软件;\n\n" +
                "(2)复制和备份;\n\n" +
                "(3)不对文档作任何增加或修改以文档下载的最初形式将此软件拷贝给他人;\n\n" +
                "(4)在网址上通过email或者其他任何物理媒体分发该软件。\n\n" +
                "你不可以：\n\n" +
                "(1)对本系统进行逆向工程、反汇编、解体拆卸或任何试图发现该软件工作程序获源代码的行为;\n\n" +
                "(2)未经_________有限公司的书面许可出售租赁该程序;\n\n" +
                "(3)创造派生性产品包括含有该系统的更大的系统或另外的程序和包裹;\n\n" +
                "(4)将该软件和与_________没有分离授权协议的产品或山版物捆绑销售;\n\n" +
                "(5)使用和拷贝没有_________授权的包含在_________之中的icons，1ogosorartwork;\n\n" +
                "(6)改变_________广告图案内部程序的运行，改变广告及其内容、广告出现的时间或使其他功能丧失的行为;\n\n" +
                "(7)使用该软件改进其他产品和服务;\n\n" +
                "(8)任何反相工程、反相编译和反汇编以及其他任何试图发现由该内程旗帜广告和该软件中网络共享功能所使用的协议的行为;\n\n" +
                "(9)在任何图像中使用该软件在任何情况下以任何方式对任何个人和商业实体的诋毁行为;或暗示由软件、软件供应商、软件许可商或其他在该软件中描绘的实体任何形式的认可;\n\n" +
                "(10)因软件拷贝控诉媒体和运输超过了合理成本。\n\n" +
                "3.条款\n\n" +
                "只要你使用该软件，该授权协议就持续有效，除非由于你不遵守该协议的条款而被终止使用权利。一旦协议终止，你必须同意毁掉你所有的该软件的所有拷贝。\n\n" +
                "4.有限的保证\n\n" +
                "_________不保证也不能保证你由于使用该软件或其他相关项目因此可以得到的结果的履行。_________公司对于第三方、商业行为能力和任何特殊的目的的非侵害和适当性不承担任何保证，包括明示和暗示的。没有_________公司给予的口头或者书面通知或者建议，他的分销商，代理商或者雇员将给予指证_________不对安全担保，也不对数据的丢失和由于在互联网上使用该软件带来的任何风险担保。美国有些州禁止排除暗示担保的限制，所以上述例外或限制也许不适用于你。\n\n" +
                "5.有限责任\n\n" +
                "_________的公司不对任何情况下带来的损失承担责任，包括使用该软件、不能使用该软件或使用该软件的结果。";

        tvContent.setText(agreementContent);

        // 设置确认按钮点击事件
        Button btnAgree = dialog.findViewById(R.id.btn_agree);
        btnAgree.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
