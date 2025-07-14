package com.back.a0625wangdixun.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.back.a0625wangdixun.R;
import com.back.a0625wangdixun.adapter.CartListAdapter;
import com.back.a0625wangdixun.db.AppDatabase;
import com.back.a0625wangdixun.db.entity.CartItem;
import com.back.a0625wangdixun.db.entity.Record;
import com.back.a0625wangdixun.utils.SessionManager;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class CartFragment extends Fragment implements CartListAdapter.OnCartItemClickListener {

    private ListView listViewCart;
    private TextView tvTotalPrice, tvEmptyCart;
    private Button btnCheckout;
    private CartListAdapter adapter;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int userId;
    private List<CartItem> cartItems;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // 初始化视图
        listViewCart = view.findViewById(R.id.listViewCart);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        tvEmptyCart = view.findViewById(R.id.tvEmptyCart);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        // 初始化数据库和会话管理器
        db = AppDatabase.getInstance(getActivity());
        sessionManager = SessionManager.getInstance(getActivity());
        userId = sessionManager.getUserId();

        // 结算按钮点击事件
        btnCheckout.setOnClickListener(v -> checkout());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadCartItems();
    }

    private void loadCartItems() {
        // 从数据库获取购物车项目
        cartItems = db.cartItemDao().getCartItemsByUserId(userId);

        if (cartItems == null || cartItems.isEmpty()) {
            tvEmptyCart.setVisibility(View.VISIBLE);
            listViewCart.setVisibility(View.GONE);
            btnCheckout.setEnabled(false);
            tvTotalPrice.setText("¥0.00");
        } else {
            tvEmptyCart.setVisibility(View.GONE);
            listViewCart.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(true);

            // 计算总价
            double totalPrice = 0;
            for (CartItem item : cartItems) {
                totalPrice += item.getTotalPrice();
            }
            tvTotalPrice.setText("¥" + decimalFormat.format(totalPrice));

            // 设置适配器
            adapter = new CartListAdapter(getActivity(), cartItems, this);
            listViewCart.setAdapter(adapter);
        }
    }

    private void checkout() {
        if (cartItems == null || cartItems.isEmpty()) {
            Toast.makeText(getActivity(), "购物车为空", Toast.LENGTH_SHORT).show();
            return;
        }

        // 计算总价
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getTotalPrice();
        }

        // 创建支出记录
        Record record = new Record(userId, totalPrice, "购物", "购物车结算", new Date().getTime(), 2); // 2: 支出
        long recordId = db.recordDao().insert(record);

        if (recordId > 0) {
            // 清空购物车
            db.cartItemDao().deleteAllCartItemsByUserId(userId);
            Toast.makeText(getActivity(), "结算成功", Toast.LENGTH_SHORT).show();
            loadCartItems(); // 刷新界面
        } else {
            Toast.makeText(getActivity(), "结算失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleteClick(CartItem cartItem) {
        db.cartItemDao().delete(cartItem);
        Toast.makeText(getActivity(), "已从购物车移除", Toast.LENGTH_SHORT).show();
        loadCartItems(); // 刷新界面
    }

    @Override
    public void onQuantityChanged(CartItem cartItem, int quantity) {
        if (quantity <= 0) {
            db.cartItemDao().delete(cartItem);
        } else {
            cartItem.setQuantity(quantity);
            db.cartItemDao().update(cartItem);
        }
        loadCartItems(); // 刷新界面
    }
} 