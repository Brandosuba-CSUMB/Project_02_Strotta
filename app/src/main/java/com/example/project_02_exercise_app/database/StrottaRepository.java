package com.example.project_02_exercise_app.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class StrottaRepository {

    private StrottaDAO strottaDAO;
    private final UserDAO userDAO;

    private ArrayList<Strotta> allLogs;

    private static StrottaRepository repository;

    private StrottaRepository(Application application) {
        StrottaDatabase db = StrottaDatabase.getDatabase(application);
        this.strottaDAO = db.strottaDAO();
        this.userDAO = db.userDAO();
        this.allLogs = (ArrayList<Strotta>) this.strottaDAO.getAllRecords();
    }


    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }
}
