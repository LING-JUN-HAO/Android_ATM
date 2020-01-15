package com.example.mynavdrawer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

@Database(entities = { User.class, Transaction_record.class }, version = 1,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static final String DB_NAME = "user_mformation.db";
    private static volatile UserDatabase instance;
    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }
    private static UserDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                UserDatabase.class,
                DB_NAME).build();
    }
    public abstract UserDao getUserDao();
    public abstract Transaction_Dao getTransactionDao();
}