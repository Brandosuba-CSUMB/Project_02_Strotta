package com.example.project_02_exercise_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Calisthenics;
import com.example.project_02_exercise_app.database.entities.Run;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.database.viewHolders.StrottaAdapter;
import com.example.project_02_exercise_app.databinding.ActivityCalisthenicsLogBinding;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CalisthenicsLogActivity extends AppCompatActivity {
    private static final String MAIN_ACTIVITY_USER_ID = "com.example.project_02_exercise_app.MAIN_ACTIVITY_USER_ID";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.project_02_exercise_app.SAVED_INSTANCE_STATE_USERID_KEY";
    private static final int LOGGED_OUT = -1;
    private ActivityCalisthenicsLogBinding binding;
    private StrottaRepository repository;

    public static final String TAG = "DAC_STROTTA";
    private long elapsed;
    String exercise = null;
    double bodyWeight = 0.0;
    private int userId;
    private Calisthenics calisthenics;
    private  Strotta strotta;
    private User user;
    private EditText exerciseInput;
    private EditText weightInput;
    private TextView logHistoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityCalisthenicsLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = StrottaRepository.getRepository(getApplication());

        Intent i = getIntent();

        userId = i.getIntExtra("uid", -1);
        if (userId == -1) {
            SharedPreferences prefs = getSharedPreferences(
                    getString(R.string.preference_file_key), MODE_PRIVATE);
            userId = prefs.getInt(   getString(R.string.preference_userId_key), -1);
        }
        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        elapsed = i.getLongExtra ("duration_ms", 0L);

        TextView timeTv = findViewById(R.id.time_tv);
        Button logBtn = findViewById(R.id.log_btn);
        long secs = elapsed / 1000;
        long mm   = (secs % 3600) / 60;
        long ss   = secs % 60;
        timeTv.setText(
                String.format(Locale.US, "%02d:%02d", mm, ss));

        exerciseInput = findViewById(R.id.exerciseInputEditTextView);
        weightInput = findViewById(R.id.weightInputEditTextView);

        binding.logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exercise = exerciseInput.getText().toString();
                try {
                    bodyWeight = Double.parseDouble(weightInput.getText().toString());
                } catch (NumberFormatException e) {
                    Log.d(TAG, "Error reading body weight.");
                }
                Strotta s = new Strotta(userId, exercise, bodyWeight);
                String title = "Calisthenics Activity - " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM d, h:mm a"));
                s.setTitle(title);
                repository.insertStrottaRepository(s);
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.calisthenics_recycler);
        StrottaAdapter adapter = new StrottaAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        repository.getAllLogsByUserId(userId).observe(this, adapter ::submitList);

        adapter.setOnItemLongClick(log -> {
            android.widget.EditText input = new android.widget.EditText(this);
            input.setText(log.getTitle());

            new AlertDialog.Builder(this)
                    .setTitle("Rename log")
                    .setView(input)
                    .setPositiveButton("Save",(d,w) ->
                            repository.rename(log.getId(), input.getText().toString()))
                    .setNegativeButton("Cancel",null)
                    .show();
        });
    }

    private void loadRuns() {
        StrottaDatabase.getDatabase(getApplicationContext()).runDAO().getAllRuns()
                .observe(this, runs -> {
                    StringBuilder sb = new StringBuilder("Logged Runs:\n\n");
                    for (Run run : runs) {
                        sb.append(formatRun(run)).append("\n");
                    }
                    logHistoryTextView.setText(sb.toString());
                });
    }

    private String formatRun(Run run){
        long ms = run.getTotalTime();
        long totalSeconds = ms/1000;
        long mm = (totalSeconds % 3600)/60;
        long hh = totalSeconds / 3600;
        long ss = totalSeconds % 60;
        String timeStr = hh>0 ?
                String.format("%d:%02d:%02d",hh,mm,ss) : String.format("%02d:%02d",mm,ss);

        return String.format("* %.2f %s",timeStr);
    }

    private void appendRunToHistory(Run run){
        String current = logHistoryTextView.getText().toString();
        String New = formatRun(run) + "\n";
        logHistoryTextView.setText(current + New);
    }

    public static Intent calisthenicsIntentFactory(Context context, long elaspedMs){
        return new Intent(context,CalisthenicsLogActivity.class).putExtra("ELAPSED",elaspedMs);
    }

}
