package com.example.project_02_exercise_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_02_exercise_app.database.entities.Strotta;

import java.util.List;

@Dao
public interface StrottaDAO {

    @Insert
    void insert(Strotta log);

    @Query("SELECT * FROM strottaTable WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Strotta>> getAllLogsByUser(int userId);
}
