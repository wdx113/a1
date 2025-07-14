package com.back.a0625wangdixun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.back.a0625wangdixun.AddRecordActivity;
import com.back.a0625wangdixun.R;
import com.back.a0625wangdixun.adapter.RecordAdapter;
import com.back.a0625wangdixun.db.AppDatabase;
import com.back.a0625wangdixun.db.entity.Record;
import com.back.a0625wangdixun.utils.SessionManager;

import java.text.DecimalFormat;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvTotalIncome, tvTotalExpense, tvBalance;
    private Button btnAddRecord;
    private RecyclerView recyclerView;
    private RecordAdapter adapter;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 初始化视图
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        tvBalance = view.findViewById(R.id.tvBalance);
        btnAddRecord = view.findViewById(R.id.btnAddRecord);
        recyclerView = view.findViewById(R.id.recyclerView);

        // 初始化数据库和会话管理器
        db = AppDatabase.getInstance(getActivity());
        sessionManager = SessionManager.getInstance(getActivity());
        userId = sessionManager.getUserId();

        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        // 添加记录按钮点击事件
        btnAddRecord.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddRecordActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        // 获取收入和支出总额
        double totalIncome = db.recordDao().getTotalIncome(userId);
        double totalExpense = db.recordDao().getTotalExpense(userId);
        double balance = totalIncome - totalExpense;

        // 格式化金额
        DecimalFormat df = new DecimalFormat("0.00");
        tvTotalIncome.setText("¥" + df.format(totalIncome));
        tvTotalExpense.setText("¥" + df.format(totalExpense));
        tvBalance.setText("¥" + df.format(balance));

        // 加载记录列表
        List<Record> records = db.recordDao().getRecordsByUserId(userId);
        adapter = new RecordAdapter(getActivity(), records);
        recyclerView.setAdapter(adapter);
    }
} 