package com.example.mynavdrawer;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT username FROM user WHERE userID=:userID and pwd=:pwd")
    String getusername(String userID,String pwd);

    @Query("SELECT COUNT(*) FROM user WHERE userID=:userID and pwd=:pwd")
    int getDataCount(String userID,String pwd);

    @Insert
    void insert(User... users);
    @Update
    void update(User... users);
    @Delete
    void delete(User... users);
}