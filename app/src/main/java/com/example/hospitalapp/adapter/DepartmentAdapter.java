package com.example.hospitalapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalapp.model.Department;
import com.example.hospitalappointment.R;

import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.DepartmentViewHolder> {

    private Context context;
    private List<Department> departmentList;
    private LayoutInflater inflater;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Department department);
    }

    public DepartmentAdapter(Context context, List<Department> departmentList) {
        this.context = context;
        this.departmentList = departmentList;
        inflater = LayoutInflater.from(context);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public DepartmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout for each department
        View view = inflater.inflate(R.layout.item_department, parent, false);
        return new DepartmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartmentViewHolder holder, int position) {
        // Get the department for this position
        Department department = departmentList.get(position);

        // Bind the data to the views
        holder.tvDepartmentName.setText(department.getName());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(department);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }

    // Method to update the data in the adapter
    public void updateData(List<Department> newDepartmentList) {
        departmentList = newDepartmentList;
        notifyDataSetChanged();  // Notify RecyclerView to refresh the data
    }

    // ViewHolder class to hold references to the views
    public static class DepartmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvDepartmentName;

        public DepartmentViewHolder(View itemView) {
            super(itemView);
            tvDepartmentName = itemView.findViewById(R.id.tv_department_name);
        }
    }
}

//package com.example.hospitalapp.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.TextView;
//
//import com.example.hospitalapp.model.Department;
//import com.example.hospitalappointment.R;
//
//import java.util.List;
//
//public class DepartmentAdapter extends BaseAdapter {
//
//    private Context context;
//    private List<Department> departmentList;
//    private LayoutInflater inflater;
//
//    public DepartmentAdapter(Context context, List<Department> departmentList) {
//        this.context = context;
//        this.departmentList = departmentList;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return departmentList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return departmentList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return departmentList.get(position).getId();
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//
//        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.item_department, parent, false);
//            holder = new ViewHolder();
//            holder.tvDepartmentName = convertView.findViewById(R.id.tv_department_name);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        // 获取当前科室对象
//        Department department = departmentList.get(position);
//
//        // 设置科室名称
//        holder.tvDepartmentName.setText(department.getName());
//
//        return convertView;
//    }
//
//    // 用于优化性能的 ViewHolder 模式
//    static class ViewHolder {
//        TextView tvDepartmentName;
//    }
//
//    // 更新数据
//    public void updateData(List<Department> newDepartmentList) {
//        this.departmentList = newDepartmentList;
//        notifyDataSetChanged();
//    }
//}
