package com.example.project_02_exercise_app.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;

import java.util.List;

public class StrottaRepository {

    private final StrottaDAO strottaDAO;
    private final UserDAO userDAO;

    public StrottaRepository(Application application) {
        StrottaDatabase db = StrottaDatabase.getDatabase(application);
        this.strottaDAO = db.strottaDAO();
        this.userDAO = db.userDAO();
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<List<Strotta>> getAllLogsByUser(int userId) {
        return strottaDAO.getAllLogsByUser(userId);
    }

    public void insertLog(Strotta strotta) {
        StrottaDatabase.databaseWriteExecutor.execute(() -> {
            strottaDAO.insert(strotta);
        });
    }
}