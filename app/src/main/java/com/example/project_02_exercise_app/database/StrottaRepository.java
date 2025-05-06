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

    private final LiveData<List<Strotta>> allStrengthLogs;

    private ArrayList<Strotta> allLogs;

    private static StrottaRepository repository;

    public StrottaRepository(Application application) {
        StrottaDatabase db = StrottaDatabase.getDatabase(application);
        this.strottaDAO = db.strottaDAO();
        this.userDAO = db.userDAO();
        this.allStrengthLogs = strottaDAO.getAllStrengthLogs();
        this.allLogs = new ArrayList<>();
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

    public ArrayList<Strotta> getAllLogs() {
        Future<ArrayList<Strotta>> future = StrottaDatabase.databaseWriteExecution.submit(
                new Callable<ArrayList<Strotta>>() {
                    @Override
                    public ArrayList<Strotta> call() throws Exception {
                        return (ArrayList<Strotta>) strottaDAO.getAllRecords();
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
        }
        return null;
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

    public LiveData<List<Strotta>> getAllLogsByUserIdLiveData(int loggedInUserId) {
        return strottaDAO.getRecordsByUserIdLiveData(loggedInUserId);
    }

    public LiveData<List<Strotta>> getAllStrengthLogs() {
        return allStrengthLogs;
    }

    @Deprecated
    public ArrayList<Strotta> getAllLogsByUserId(int loggedInUserId) {
        Future<ArrayList<Strotta>> future = StrottaDatabase.databaseWriteExecution.submit(
                new Callable<ArrayList<Strotta>>() {
                    @Override
                    public ArrayList<Strotta> call() throws Exception {
                        return (ArrayList<Strotta>) strottaDAO.getRecordsByUserId(loggedInUserId);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.i(MainActivity.TAG, "Problem when getting all GymLogs in the repository");
        }
        return null;

    }
}