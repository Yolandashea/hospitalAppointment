package com.example.hospitalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalapp.activity.NewsDetailActivity;
import com.example.hospitalappointment.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private Context context;
    private List<BannerItem> bannerItems;

    public BannerAdapter(Context context, List<BannerItem> bannerItems) {
        this.context = context;
        this.bannerItems = bannerItems;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        BannerItem item = bannerItems.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.titleView.setText(item.getTitle());
        
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("imageResId", item.getImageResId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("time", item.getTime());
            intent.putExtra("content", item.getContent());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bannerItems.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleView;

        BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_banner);
            titleView = itemView.findViewById(R.id.tv_banner_title);
        }
    }

    public static class BannerItem {
        private int imageResId;
        private String title;
        private String time;
        private String content;

        public BannerItem(int imageResId, String title, String time, String content) {
            this.imageResId = imageResId;
            this.title = title;
            this.time = time;
            this.content = content;
        }

        public int getImageResId() {
            return imageResId;
        }

        public String getTitle() {
            return title;
        }

        public String getTime() {
            return time;
        }

        public String getContent() {
            return content;
        }
    }
} 