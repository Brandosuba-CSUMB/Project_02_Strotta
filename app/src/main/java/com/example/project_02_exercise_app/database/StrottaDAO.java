package com.example.project_02_exercise_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_02_exercise_app.database.entities.Strotta;

import java.util.List;

@Dao
public interface StrottaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Strotta strotta);
    @Query("SELECT * FROM " + StrottaDatabase.STROTTA_TABLE + " ORDER BY date DESC")
    List<Strotta> getAllRecords();

    @Query("SELECT * FROM " + StrottaDatabase.STROTTA_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<Strotta> getRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM " + StrottaDatabase.STROTTA_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    LiveData<List<Strotta>> getRecordsByUserIdLiveData(int loggedInUserId);

    @Query("SELECT * FROM " + StrottaDatabase.STROTTA_TABLE + " WHERE strengthExerciseName IS NOT NULL ORDER BY date DESC")
    LiveData<List<Strotta>> getAllStrengthLogs();

    @Query("DELETE FROM " + StrottaDatabase.STROTTA_TABLE + " WHERE strengthExerciseName IS NOT NULL")
    void deleteAllStrengthLogs();
    //@Query("SELECT * FROM " + StrottaDatabase.STROTTA_TABLE + " ORDER BY date DESC")
    //LiveData<List<Strotta>> getAllLogsLiveData();

}
