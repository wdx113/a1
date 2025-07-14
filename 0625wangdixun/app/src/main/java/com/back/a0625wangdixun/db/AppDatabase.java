package com.back.a0625wangdixun.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.back.a0625wangdixun.db.dao.CartItemDao;
import com.back.a0625wangdixun.db.dao.ProductDao;
import com.back.a0625wangdixun.db.dao.RecordDao;
import com.back.a0625wangdixun.db.dao.UserDao;
import com.back.a0625wangdixun.db.entity.CartItem;
import com.back.a0625wangdixun.db.entity.Product;
import com.back.a0625wangdixun.db.entity.Record;
import com.back.a0625wangdixun.db.entity.User;

@Database(entities = {User.class, Record.class, Product.class, CartItem.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME = "account_book_db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    DATABASE_NAME)
                    .allowMainThreadQueries() // 仅用于示例，实际应用应使用异步操作
                    .build();
        }
        return instance;
    }

    public abstract UserDao userDao();
    public abstract RecordDao recordDao();
    public abstract ProductDao productDao();
    public abstract CartItemDao cartItemDao();
} 