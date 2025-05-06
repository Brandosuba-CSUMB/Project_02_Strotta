package com.example.project_02_exercise_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;

import java.util.List;

@Dao
public interface StrottaDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Strotta strotta);

    @Delete
        // swipe‑to‑delete
    void delete(Strotta strotta);

    @Query("UPDATE " + StrottaDatabase.STROTTA_TABLE + " SET title = :title WHERE id = :id")
    void rename(int id, String title);

    @Query("SELECT * FROM " + StrottaDatabase.STROTTA_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    LiveData<List<Strotta>> getRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM " + StrottaDatabase.STROTTA_TABLE + " ORDER BY date DESC")
    LiveData<List<Strotta>> getAll();
    @Query("DELETE FROM " + StrottaDatabase.STROTTA_TABLE)
    void deleteAll();


}
