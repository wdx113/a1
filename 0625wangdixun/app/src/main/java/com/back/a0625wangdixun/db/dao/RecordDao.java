package com.back.a0625wangdixun.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.back.a0625wangdixun.db.entity.Record;

import java.util.List;

@Dao
public interface RecordDao {
    @Insert
    long insert(Record record);

    @Update
    void update(Record record);

    @Delete
    void delete(Record record);

    @Query("SELECT * FROM records WHERE userId = :userId ORDER BY date DESC")
    List<Record> getRecordsByUserId(int userId);

    @Query("SELECT * FROM records WHERE id = :id")
    Record getRecordById(int id);

    @Query("SELECT * FROM records WHERE userId = :userId AND type = :type ORDER BY date DESC")
    List<Record> getRecordsByType(int userId, int type);

    @Query("SELECT SUM(amount) FROM records WHERE userId = :userId AND type = 1")
    double getTotalIncome(int userId);

    @Query("SELECT SUM(amount) FROM records WHERE userId = :userId AND type = 2")
    double getTotalExpense(int userId);
} 