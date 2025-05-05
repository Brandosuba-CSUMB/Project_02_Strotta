package com.example.project_02_exercise_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_02_exercise_app.database.entities.Calisthenics;

import java.util.List;

@Dao
public interface CalisthenicsDAO {

    @Insert
    void insert (Calisthenics c);

    @Query("SELECT * FROM calisthenics_table ORDER BY timeStamp DESC")
    LiveData<List<Calisthenics>> getAllRuns();

    @Query("DELETE FROM calisthenics_table")
    void deleteAll();
}
