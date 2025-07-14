package com.back.a0625wangdixun;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.back.a0625wangdixun.db.AppDatabase;
import com.back.a0625wangdixun.db.entity.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 初始化视图
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);

        // 初始化数据库
        db = AppDatabase.getInstance(this);

        // 注册按钮点击事件
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                etUsername.setError("请输入用户名");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("请输入密码");
                return;
            }

            if (TextUtils.isEmpty(confirmPassword)) {
                etConfirmPassword.setError("请确认密码");
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("两次密码不一致");
                return;
            }

            // 检查用户名是否已存在
            User existingUser = db.userDao().getUserByUsername(username);
            if (existingUser != null) {
                etUsername.setError("用户名已存在");
                return;
            }

            // 创建新用户
            User newUser = new User(username, password);
            long userId = db.userDao().insert(newUser);

            if (userId > 0) {
                Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "注册失败，请重试", Toast.LENGTH_SHORT).show();
            }
        });

        // 登录文本点击事件
        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }
} 