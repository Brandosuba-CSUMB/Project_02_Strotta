package com.example.project_02_exercise_app;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_02_exercise_app.database.StrottaDatabase;
import java.util.concurrent.Executors;

public class AdminActivity extends AppCompatActivity {
    private RunAdapter runAdapter;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_admin);

        RecyclerView recyclerView = findViewById(R.id.run_recycler_view);
        Button deleteAll = findViewById(R.id.delete_all_runs_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        runAdapter = new RunAdapter();
        recyclerView.setAdapter(runAdapter);

        StrottaDatabase.getDatabase(getApplicationContext()).runDAO().getAllRuns().observe(this,runs -> {
            runAdapter.setRuns(runs);
        });
        deleteAll.setOnClickListener(v ->{
            StrottaDatabase.databaseWriteExecution.execute(() -> {
                StrottaDatabase.getDatabase(getApplicationContext()).runDAO().deleteAll();
            });
        });
    }
}
