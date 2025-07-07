package com.example.hospitalapp.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalappointment.R;

public class NewsDetailActivity extends AppCompatActivity {

    private ImageView ivNewsImage;
    private TextView tvNewsTitle;
    private TextView tvNewsTime;
    private TextView tvNewsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // 初始化视图
        ivNewsImage = findViewById(R.id.iv_news_image);
        tvNewsTitle = findViewById(R.id.tv_news_title);
        tvNewsTime = findViewById(R.id.tv_news_time);
        tvNewsContent = findViewById(R.id.tv_news_content);

        // 获取传递的数据
        int imageResId = getIntent().getIntExtra("imageResId", R.drawable.img01);
        String title = getIntent().getStringExtra("title");
        String time = getIntent().getStringExtra("time");
        String content = getIntent().getStringExtra("content");

        // 设置数据
        ivNewsImage.setImageResource(imageResId);
        if (title != null) tvNewsTitle.setText(title);
        if (time != null) tvNewsTime.setText(time);
        if (content != null) tvNewsContent.setText(content);
    }
} 