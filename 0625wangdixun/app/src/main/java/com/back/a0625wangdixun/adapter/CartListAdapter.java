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
import com.back.a0625wangdixun.db.entity.CartItem;

import java.text.DecimalFormat;
import java.util.List;

public class CartListAdapter extends BaseAdapter {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemClickListener listener;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public CartListAdapter(Context context, List<CartItem> cartItems, OnCartItemClickListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return cartItems.size();
    }

    @Override
    public Object getItem(int position) {
        return cartItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cartItems.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_cart_list, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.imageProduct);
            holder.tvName = convertView.findViewById(R.id.tvProductName);
            holder.tvPrice = convertView.findViewById(R.id.tvProductPrice);
            holder.tvQuantity = convertView.findViewById(R.id.tvQuantity);
            holder.btnDecrease = convertView.findViewById(R.id.btnDecrease);
            holder.btnIncrease = convertView.findViewById(R.id.btnIncrease);
            holder.btnDelete = convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CartItem cartItem = cartItems.get(position);

        holder.tvName.setText(cartItem.getProductName());
        holder.tvPrice.setText("¥" + decimalFormat.format(cartItem.getPrice()));
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));

        // 为了简化，这里不加载图片
        holder.imageView.setImageResource(R.drawable.ic_product_placeholder);

        // 减少数量按钮
        holder.btnDecrease.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() - 1;
            if (listener != null) {
                listener.onQuantityChanged(cartItem, newQuantity);
            }
        });

        // 增加数量按钮
        holder.btnIncrease.setOnClickListener(v -> {
            int newQuantity = cartItem.getQuantity() + 1;
            if (listener != null) {
                listener.onQuantityChanged(cartItem, newQuantity);
            }
        });

        // 删除按钮
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(cartItem);
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView tvName;
        TextView tvPrice;
        TextView tvQuantity;
        Button btnDecrease;
        Button btnIncrease;
        Button btnDelete;
    }

    public interface OnCartItemClickListener {
        void onDeleteClick(CartItem cartItem);
        void onQuantityChanged(CartItem cartItem, int quantity);
    }
} 