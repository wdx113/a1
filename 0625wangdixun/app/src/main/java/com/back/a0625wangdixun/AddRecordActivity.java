package com.back.a0625wangdixun;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.back.a0625wangdixun.db.AppDatabase;
import com.back.a0625wangdixun.db.entity.Record;
import com.back.a0625wangdixun.utils.SessionManager;

import java.util.Date;

public class AddRecordActivity extends AppCompatActivity {

    private RadioGroup rgType;
    private RadioButton rbIncome, rbExpense;
    private EditText etAmount, etDescription;
    private Spinner spinnerCategory;
    private Button btnSave;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int userId;

    // 收入和支出的分类
    private String[] incomeCategories = {"工资", "奖金", "投资", "兼职", "其他收入"};
    private String[] expenseCategories = {"餐饮", "购物", "交通", "住房", "娱乐", "医疗", "教育", "其他支出"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);

        // 初始化视图
        rgType = findViewById(R.id.rgType);
        rbIncome = findViewById(R.id.rbIncome);
        rbExpense = findViewById(R.id.rbExpense);
        etAmount = findViewById(R.id.etAmount);
        etDescription = findViewById(R.id.etDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnSave = findViewById(R.id.btnSave);

        // 初始化数据库和会话管理器
        db = AppDatabase.getInstance(this);
        sessionManager = SessionManager.getInstance(this);
        userId = sessionManager.getUserId();

        // 设置默认分类为收入
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, incomeCategories);
        spinnerCategory.setAdapter(adapter);

        // 类型切换监听
        rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbIncome) {
                ArrayAdapter<String> incomeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, incomeCategories);
                spinnerCategory.setAdapter(incomeAdapter);
            } else {
                ArrayAdapter<String> expenseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, expenseCategories);
                spinnerCategory.setAdapter(expenseAdapter);
            }
        });

        // 保存按钮点击事件
        btnSave.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();
            int type = rbIncome.isChecked() ? 1 : 2; // 1: 收入, 2: 支出

            if (TextUtils.isEmpty(amountStr)) {
                etAmount.setError("请输入金额");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                etAmount.setError("金额格式不正确");
                return;
            }

            if (amount <= 0) {
                etAmount.setError("金额必须大于0");
                return;
            }

            // 创建记录
            Record record = new Record(userId, amount, category, description, new Date().getTime(), type);
            long recordId = db.recordDao().insert(record);

            if (recordId > 0) {
                Toast.makeText(AddRecordActivity.this, "记录添加成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(AddRecordActivity.this, "记录添加失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });
    }
} 