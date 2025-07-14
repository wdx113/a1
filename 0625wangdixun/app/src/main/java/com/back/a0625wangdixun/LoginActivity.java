package com.back.a0625wangdixun;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.back.a0625wangdixun.db.AppDatabase;
import com.back.a0625wangdixun.db.entity.User;
import com.back.a0625wangdixun.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private CheckBox cbRememberPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private AppDatabase db;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbRememberPassword = findViewById(R.id.cbRememberPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // 初始化数据库和会话管理器
        db = AppDatabase.getInstance(this);
        sessionManager = SessionManager.getInstance(this);

        // 检查是否已经登录
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
            return;
        }

        // 检查是否有记住密码的用户
        User rememberedUser = db.userDao().getRememberedUser();
        if (rememberedUser != null) {
            etUsername.setText(rememberedUser.getUsername());
            etPassword.setText(rememberedUser.getPassword());
            cbRememberPassword.setChecked(true);
        }

        // 登录按钮点击事件
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                etUsername.setError("请输入用户名");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                etPassword.setError("请输入密码");
                return;
            }

            // 验证用户
            User user = db.userDao().login(username, password);
            if (user != null) {
                // 记住密码
                boolean rememberPassword = cbRememberPassword.isChecked();
                if (rememberPassword) {
                    user.setRememberPassword(true);
                } else {
                    user.setRememberPassword(false);
                }
                db.userDao().update(user);

                // 创建会话
                sessionManager.createLoginSession(user.getId(), user.getUsername(), rememberPassword);

                // 跳转到主页
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        // 注册文本点击事件
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }
} 