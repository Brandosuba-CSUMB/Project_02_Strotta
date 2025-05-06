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
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.viewHolders.StrottaAdapter;

import java.time.LocalDateTime;
import java.util.concurrent.Executors;

public class StrengthLogActivity extends AppCompatActivity {

    private long elapsed;
    private StrottaAdapter strottaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_log);

        EditText exerciseInput = findViewById(R.id.strengthExerciseInputEditTextView);
        EditText weightInput = findViewById(R.id.strengthWeightInputEditTextView);
        EditText repsInput = findViewById(R.id.strengthRepsInputEditTextView);
        EditText setsInput = findViewById(R.id.strengthSetsInputEditTextView);
        TextView timeTv = findViewById(R.id.time_tv);
        Button logBtn = findViewById(R.id.strengthLog_btn);

        RecyclerView recyclerView = findViewById(R.id.strengthRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        strottaAdapter = new StrottaAdapter(new StrottaAdapter.StrottaDiff());
        recyclerView.setAdapter(strottaAdapter);

        elapsed = getIntent().getLongExtra("duration_ms", 0);

        long s = elapsed / 1000;
        long mm = (s % 3600) / 60;
        long hh = s / 3600;
        long ss = s % 60;
        String timeStr = hh > 0 ? String.format("%d:%02d:%02d", hh, mm, ss) : String.format("%02d:%02d", mm, ss);
        timeTv.setText(timeStr);

        loadLogs();

        logBtn.setOnClickListener(v -> {
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

            Strotta strottaLog = new Strotta(0, exercise, weight, sets, reps, elapsed);
            strottaLog.setDate(LocalDateTime.now());

            Executors.newSingleThreadExecutor().execute(() -> {
                StrottaDatabase.getDatabase(getApplicationContext()).strottaDAO().insert(strottaLog);
            });
        });
    }

    private void loadLogs() {
        StrottaDatabase.getDatabase(getApplicationContext())
                .strottaDAO()
                .getAllStrengthLogs()
                .observe(this, logs -> strottaAdapter.submitList(logs));
    }


    public static Intent strengthLogIntentFactory(Context context, long elapsedMs) {
        return new Intent(context, StrengthLogActivity.class)
                .putExtra("duration_ms", elapsedMs);
    }
}
