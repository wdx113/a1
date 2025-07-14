package com.back.a0625wangdixun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.back.a0625wangdixun.R;
import com.back.a0625wangdixun.db.entity.Record;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private List<Record> records;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public RecordAdapter(Context context, List<Record> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        Record record = records.get(position);
        
        holder.tvCategory.setText(record.getCategory());
        holder.tvDescription.setText(record.getDescription());
        holder.tvDate.setText(dateFormat.format(new Date(record.getDate())));
        
        String amount;
        if (record.getType() == 1) { // 收入
            amount = "+" + decimalFormat.format(record.getAmount());
            holder.tvAmount.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else { // 支出
            amount = "-" + decimalFormat.format(record.getAmount());
            holder.tvAmount.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
        }
        holder.tvAmount.setText(amount);
    }

    @Override
    public int getItemCount() {
        return records == null ? 0 : records.size();
    }

    public void updateData(List<Record> newRecords) {
        this.records = newRecords;
        notifyDataSetChanged();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvDescription, tvAmount, tvDate;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
} 