package com.example.project_02_exercise_app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;
@Database(entities = {Strotta.class, User.class}, version = 1, exportSchema = false)
public abstract class StrottaDatabase {
    public static final String USER_TABLE = "usertable";
    public static final String DATABASE_NAME = "strottadatabase";
    private static volatile StrottaDatabase INSTANCE;
    static StrottaDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (StrottaDatabase.class){
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            StrottaDatabase.class,
                            DATABASE_NAME
                    )
                            .fallbackToDestructiveMigration().addCallback(addDefaultValues).build();
                }
            }
        }
        return INSTANCE;
    }
    public static final RoomDatabase.Callback addDefaultValues =
            super.onCreate(db)
}
