package com.example.project_02_exercise_app.database.viewHolders;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Strotta;

import java.util.List;

public class StrottaViewModel extends AndroidViewModel {
    private final StrottaRepository repository;
    private final LiveData<List<Strotta>> allStrengthLogs;
    //    private final LiveData<List<GymLog>> allLogsById;
    public StrottaViewModel(Application application){
        super(application);
        repository = StrottaRepository.getRepository(application);
        allStrengthLogs = repository.getAllStrengthLogs();
//        allLogsById = repository.getAllLogsByUserIdLiveData(userId);

    }

    public LiveData<List<Strotta>> getAllStrengthLogs() {
        return allStrengthLogs;
    }
    public LiveData<List<Strotta>> getAllLogsById(int userId){
        return repository.getAllLogsByUserIdLiveData(userId);
    }
    public void insert(Strotta strotta){
        repository.insertStrottaRepository(strotta);
    }
}
