package com.example.project_02_exercise_app.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.MainActivity;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class StrottaRepository {
    public final StrottaDAO strottaDAO;
    private final UserDAO userDAO;
    private static StrottaRepository repository;

    public StrottaRepository(Application application) {
        StrottaDatabase db = StrottaDatabase.getDatabase(application);
        this.strottaDAO = db.strottaDAO();
        this.userDAO = db.userDAO();
    }

    public static synchronized StrottaRepository getRepository(Application application) {
        if (repository == null) {
            repository = new StrottaRepository(application);
        }
        return repository;
    }

    public static void setRepository(StrottaRepository repository) {
        StrottaRepository.repository = repository;
    }

    public void insertStrottaRepository(Strotta strotta) {
        StrottaDatabase.databaseWriteExecution.execute(() -> {
            strottaDAO.insert(strotta);
        });
    }

    public void insertUser(User... user) {
        StrottaDatabase.databaseWriteExecution.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUserName(String username) {
        return userDAO.getUserByUserName(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }
    public void delete(Strotta s){
        StrottaDatabase.databaseWriteExecution.execute(() -> strottaDAO.delete(s));
    }


    public void rename(int id, String t){
        StrottaDatabase.databaseWriteExecution.execute(() -> strottaDAO.rename(id, t));
    }
  
    public LiveData<List<Strotta>> getAllLogsByUserId(int loggedInUserId) {
        return strottaDAO.getRecordsByUserId(loggedInUserId);
    }
    public LiveData<List<Strotta>> getAllLogs() {
        return strottaDAO.getAll();
    }

    public long insertUserSync(User u) {
        Future<Long> f = StrottaDatabase.databaseWriteExecution.submit(
                () -> userDAO.insertOne(u));
        try {
            return f.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LiveData<User> login(String u, String p) {
        return userDAO.authenticate(u, p);
    }
}