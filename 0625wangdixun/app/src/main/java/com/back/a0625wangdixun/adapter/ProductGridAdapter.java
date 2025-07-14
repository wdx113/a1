package com.back.a0625wangdixun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.back.a0625wangdixun.R;
import com.back.a0625wangdixun.db.entity.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductGridAdapter extends BaseAdapter {

    private Context context;
    private List<Product> products;
    private OnProductClickListener listener;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ProductGridAdapter(Context context, List<Product> products, OnProductClickListener listener) {
        this.context = context;
        this.products = products;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_product_grid, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageProduct);
            holder.tvName = convertView.findViewById(R.id.tvProductName);
            holder.tvPrice = convertView.findViewById(R.id.tvProductPrice);
            holder.btnAddToCart = convertView.findViewById(R.id.btnAddToCart);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product product = products.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText("¥" + decimalFormat.format(product.getPrice()));

        // 为了简化，这里不加载图片
        holder.imageView.setImageResource(R.drawable.ic_product_placeholder);

        // 点击整个商品项
        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });

        // 添加到购物车按钮点击
        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddToCartClick(product);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvPrice;
        Button btnAddToCart;
    }

    public interface OnProductClickListener {
        void onProductClick(Product product);
        void onAddToCartClick(Product product);
    }
} 