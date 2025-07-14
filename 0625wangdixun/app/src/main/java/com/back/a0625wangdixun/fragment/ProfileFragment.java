package com.back.a0625wangdixun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.back.a0625wangdixun.LoginActivity;
import com.back.a0625wangdixun.R;
import com.back.a0625wangdixun.db.AppDatabase;
import com.back.a0625wangdixun.db.entity.User;
import com.back.a0625wangdixun.utils.SessionManager;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private Button btnLogout;
    private SessionManager sessionManager;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 初始化视图
        tvUsername = view.findViewById(R.id.tvUsername);
        btnLogout = view.findViewById(R.id.btnLogout);

        // 初始化数据库和会话管理器
        db = AppDatabase.getInstance(getActivity());
        sessionManager = SessionManager.getInstance(getActivity());

        // 显示用户名
        String username = sessionManager.getUsername();
        tvUsername.setText("当前用户: " + username);

        // 登出按钮点击事件
        btnLogout.setOnClickListener(v -> {
            // 清除记住密码标记
            User user = db.userDao().getUserByUsername(username);
            if (user != null) {
                user.setRememberPassword(false);
                db.userDao().update(user);
            }
            
            // 清除会话
            sessionManager.logoutUser();
            
            // 跳转到登录页面
            Toast.makeText(getActivity(), "已退出登录", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return view;
    }
} 