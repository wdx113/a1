package com.back.a0625wangdixun.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.back.a0625wangdixun.R;
import com.back.a0625wangdixun.db.entity.CartItem;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemClickListener listener;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public interface OnCartItemClickListener {
        void onDeleteClick(CartItem cartItem);
        void onQuantityChanged(CartItem cartItem, int quantity);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemClickListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);

        holder.tvName.setText(cartItem.getProductName());
        holder.tvPrice.setText("¥" + decimalFormat.format(cartItem.getPrice()));
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.tvTotalPrice.setText("¥" + decimalFormat.format(cartItem.getTotalPrice()));

        // 在实际应用中，这里应该使用图片加载库（如Glide或Picasso）来加载图片
        // 这里简化处理，使用默认图片
        holder.ivProduct.setImageResource(R.drawable.ic_launcher_foreground);

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(cartItem);
            }
        });

        holder.btnIncrease.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuantityChanged(cartItem, cartItem.getQuantity() + 1);
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (listener != null && cartItem.getQuantity() > 1) {
                listener.onQuantityChanged(cartItem, cartItem.getQuantity() - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
    }

    public void updateData(List<CartItem> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice, tvQuantity, tvTotalPrice;
        Button btnDelete, btnIncrease, btnDecrease;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }
    }
}