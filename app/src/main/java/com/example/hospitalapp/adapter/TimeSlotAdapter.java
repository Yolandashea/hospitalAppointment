package com.example.hospitalapp.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalapp.model.TimeSlot;
import com.example.hospitalappointment.R;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(TimeSlot slot);
    }

    private List<TimeSlot> timeSlotList;
    private OnItemClickListener listener;

    public TimeSlotAdapter(List<TimeSlot> list, OnItemClickListener listener) {
        this.timeSlotList = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotAdapter.ViewHolder holder, int position) {
        TimeSlot slot = timeSlotList.get(position);
        holder.tvDate.setText(slot.getDate());
        holder.tvTimePeriod.setText(slot.getTimePeriod());
        if (slot.isAvailable()) {
            holder.tvStatus.setText("可预约");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // 绿色
            holder.itemView.setEnabled(true);
        } else {
            holder.tvStatus.setText("已满");
            holder.tvStatus.setTextColor(Color.parseColor("#F44336")); // 红色
            holder.itemView.setEnabled(false);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null && slot.isAvailable()) {
                listener.onItemClick(slot);
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlotList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTimePeriod, tvStatus;

        ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTimePeriod = itemView.findViewById(R.id.tvTimePeriod);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
