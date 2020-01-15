package com.example.mynavdrawer;
import android.view.SurfaceControl;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface Transaction_Dao {
    @Query("SELECT total FROM Transaction_record WHERE username=:username ORDER BY id DESC LIMIT 1 ")
    String gettotal(String username);

    @Query("SELECT * FROM Transaction_record WHERE username=:username ORDER BY id ASC LIMIT 50 ")
    List<Transaction_record> getdata(String username);

    @Insert
    void insert(Transaction_record... transaction_records);
    @Update
    void update(Transaction_record... transaction_records);
    @Delete
    void delete(Transaction_record... transaction_records);
}
