package com.back.a0625wangdixun.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.back.a0625wangdixun.R;
import com.back.a0625wangdixun.adapter.ProductGridAdapter;
import com.back.a0625wangdixun.db.AppDatabase;
import com.back.a0625wangdixun.db.entity.CartItem;
import com.back.a0625wangdixun.db.entity.Product;
import com.back.a0625wangdixun.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment implements ProductGridAdapter.OnProductClickListener {

    private GridView gridViewProducts;
    private ProductGridAdapter adapter;
    private AppDatabase db;
    private SessionManager sessionManager;
    private int userId;
    private List<Product> products;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);

        // 初始化视图
        gridViewProducts = view.findViewById(R.id.gridViewProducts);

        // 初始化数据库和会话管理器
        db = AppDatabase.getInstance(getActivity());
        sessionManager = SessionManager.getInstance(getActivity());
        userId = sessionManager.getUserId();

        // 加载商品数据
        loadProducts();

        return view;
    }

    private void loadProducts() {
        // 从数据库获取商品列表
        products = db.productDao().getAllProducts();

        // 如果没有商品，则添加一些示例商品
        if (products == null || products.isEmpty()) {
            addSampleProducts();
            products = db.productDao().getAllProducts();
        }

        // 设置适配器
        adapter = new ProductGridAdapter(getActivity(), products, this);
        gridViewProducts.setAdapter(adapter);
    }

    private void addSampleProducts() {
        List<Product> sampleProducts = new ArrayList<>();
        sampleProducts.add(new Product("笔记本电脑", 5999.00, "高性能笔记本电脑", "https://example.com/laptop.jpg", "电子产品"));
        sampleProducts.add(new Product("智能手机", 3999.00, "最新款智能手机", "https://example.com/phone.jpg", "电子产品"));
        sampleProducts.add(new Product("耳机", 299.00, "无线蓝牙耳机", "https://example.com/headphones.jpg", "电子产品"));
        sampleProducts.add(new Product("运动鞋", 499.00, "舒适运动鞋", "https://example.com/shoes.jpg", "服装"));
        sampleProducts.add(new Product("T恤", 99.00, "纯棉T恤", "https://example.com/tshirt.jpg", "服装"));
        sampleProducts.add(new Product("牛仔裤", 199.00, "修身牛仔裤", "https://example.com/jeans.jpg", "服装"));
        sampleProducts.add(new Product("书籍", 59.00, "畅销小说", "https://example.com/book.jpg", "图书"));
        sampleProducts.add(new Product("水杯", 39.00, "保温水杯", "https://example.com/cup.jpg", "日用品"));

        for (Product product : sampleProducts) {
            db.productDao().insert(product);
        }
    }

    @Override
    public void onProductClick(Product product) {
        // 点击商品时的操作，可以跳转到商品详情页
        Toast.makeText(getActivity(), "点击了商品：" + product.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddToCartClick(Product product) {
        // 添加到购物车
        CartItem existingItem = db.cartItemDao().getCartItemByUserIdAndProductId(userId, product.getId());

        if (existingItem != null) {
            // 如果已经在购物车中，增加数量
            existingItem.setQuantity(existingItem.getQuantity() + 1);
            db.cartItemDao().update(existingItem);
        } else {
            // 如果不在购物车中，添加新项目
            CartItem newItem = new CartItem(userId, product.getId(), 1, product.getPrice(), product.getName(), product.getImageUrl());
            db.cartItemDao().insert(newItem);
        }

        Toast.makeText(getActivity(), "已添加到购物车", Toast.LENGTH_SHORT).show();
    }
} 