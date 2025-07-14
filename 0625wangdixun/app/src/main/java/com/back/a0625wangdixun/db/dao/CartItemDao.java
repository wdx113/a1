package com.back.a0625wangdixun.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.back.a0625wangdixun.db.entity.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert
    long insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    List<CartItem> getCartItemsByUserId(int userId);

    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId")
    CartItem getCartItemByUserIdAndProductId(int userId, int productId);

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    void deleteAllCartItemsByUserId(int userId);

    @Query("SELECT SUM(quantity * price) FROM cart_items WHERE userId = :userId")
    double getTotalPrice(int userId);

    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    int getCartItemCount(int userId);
} 