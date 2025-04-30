package com.example.project_02_exercise_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_02_exercise_app.database.entities.Run;

import java.util.List;
@Dao
public interface RunDAO {
    @Insert
    void insert(Run run);
    @Query("SELECT * FROM run_table ORDER BY timeStamp DESC")
    LiveData<List<Run>> getAllRuns();
    @Query("DELETE FROM run_table")
    void deleteAll();
}
