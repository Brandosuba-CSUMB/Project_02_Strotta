package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.R;
import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Strength;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.viewHolders.StrengthAdapter;
import com.example.project_02_exercise_app.database.viewHolders.StrottaAdapter;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;

public class StrengthLogActivity extends AppCompatActivity {

    private long elapsed;
    private StrottaRepository repository;
    private StrottaAdapter strottaAdapter;
    private StrengthAdapter strengthAdapter;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_log);

        EditText exerciseInput = findViewById(R.id.exerciseInputEditTextView);
        EditText weightInput = findViewById(R.id.weightInputEditTextView);
        EditText repsInput = findViewById(R.id.repsInputEditTextView);
        EditText setsInput = findViewById(R.id.setsInputEditTextView);
        TextView timeTv = findViewById(R.id.time_tv);
        Button logBtn = findViewById(R.id.log_btn);

        elapsed = getIntent().getLongExtra("duration_ms", 0);
        userId = getIntent().getIntExtra("user_id", -1);
        long s = elapsed / 1000;
        long mm = (s % 3600) / 60;
        long hh = s / 3600;
        long ss = s % 60;
        String timeStr = hh > 0 ? String.format("%d:%02d:%02d", hh, mm, ss) : String.format("%02d:%02d", mm, ss);
        timeTv.setText(timeStr);
        RecyclerView recyclerView = findViewById(R.id.strengthRecycler);
        strengthAdapter = new StrengthAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(strengthAdapter);
        StrottaDatabase.getDatabase(getApplicationContext()).strengthDAO().getStrengthLogsByUserId(userId).observe(this,logs ->strengthAdapter.submitList(logs));
        Button logbtn = findViewById(R.id.log_btn);
        logbtn.setOnClickListener(v -> {
            String exercise = exerciseInput.getText().toString().trim();
            String weightStr = weightInput.getText().toString().trim();
            String setsStr = setsInput.getText().toString().trim();
            String repsStr = repsInput.getText().toString().trim();

            if (exercise.isEmpty() || weightStr.isEmpty() || repsStr.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight = Float.parseFloat(weightStr);
            int sets = Integer.parseInt(setsStr);
            int reps = Integer.parseInt(repsStr);

            Strength strengthLog = new Strength(0, exercise, weight, sets, reps, elapsed);
            strengthLog.setDate(LocalDateTime.now());

            Executors.newSingleThreadExecutor().execute(() -> {
                StrottaDatabase.getDatabase(getApplicationContext()).strengthDAO().insert(strengthLog);
            });
        });
    }

//    private void loadLogs() {
//        StrottaDatabase.getDatabase(getApplicationContext())
//                .strottaDAO()
//                .getAllStrengthLogs()
//                .observe(this, logs -> strottaAdapter.submitList(logs));
//    }


    public static Intent strengthLogIntentFactory(Context context, long elapsedMs, int userId) {
        return new Intent(context, StrengthLogActivity.class)
                .putExtra("duration_ms", elapsedMs).putExtra("user_id",userId);
    }
}
