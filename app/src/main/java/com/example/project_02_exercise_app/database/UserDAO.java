package com.example.project_02_exercise_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.entities.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   long insertOne(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + StrottaDatabase.USER_TABLE + " ORDER BY username")
    LiveData<List<User>> getAllUsers();

    @Query("DELETE from " + StrottaDatabase.USER_TABLE)
    void deleteAll();

    @Query("SELECT * from " + StrottaDatabase.USER_TABLE + " WHERE username == :username")
    LiveData<User> getUserByUserName(String username);

    @Query("SELECT * from " + StrottaDatabase.USER_TABLE + " WHERE id == :userId")
    LiveData<User> getUserByUserId(int userId);

    @Query("SELECT * FROM " + StrottaDatabase.USER_TABLE + " WHERE username = :u AND password = :p LIMIT 1")
    LiveData<User> authenticate (String u, String p);
}

