package com.example.project_02_exercise_app.database.viewHolders;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Strotta;

import java.util.List;

public class StrottaViewModel extends AndroidViewModel {
    private final StrottaRepository repository;
    public StrottaViewModel(Application application){
        super(application);
        repository = StrottaRepository.getRepository(application);

    }
    public LiveData<List<Strotta>> getAllLogsById(int userId){
        return repository.getAllLogsByUserId(userId);
    }
    public void insert(Strotta re){
        repository.insertStrottaRepository(re);
    }
}
