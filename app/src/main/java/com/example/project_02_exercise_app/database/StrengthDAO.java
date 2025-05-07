package com.example.project_02_exercise_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.project_02_exercise_app.database.entities.Strength;

import java.util.List;

@Dao
public interface StrengthDAO {
    @Insert
    void insert(Strength strength);
    @Update
    void update (Strength strength);
    @Delete
    void delete(Strength strength);
    @Query("SELECT * FROM strength_table ORDER BY date DESC")
    LiveData<List<Strength>>getAllStrength();

    @Query("SELECT * FROM strength_table WHERE userId = :userId ORDER BY date DESC")
    LiveData<List<Strength>> getStrengthLogsByUserId(int userId);

}
