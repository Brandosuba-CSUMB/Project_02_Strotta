package com.example.project_02_exercise_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Run;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.viewHolders.StrottaAdapter;
import java.util.Locale;

public class CardioLogActivity extends AppCompatActivity {
    private float distance;
    private long elapsed;
    private int userId;

    private Run run;
    private TextView logHistoryTextView;
    private TextView distTv;
    private TextView timeTv;
    private TextView paceTv;
    private Button logBtn;
    private RecyclerView recyclerView;
    private StrottaAdapter adapter;
    private StrottaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_log);
        getIntentData();
        authenticateUser();
        initializeUI();
        displayRunData();
        logButton();
        createRecyclerView();
        observeRunLogs();
        swipeToDelete();
        renameLog();
    }

    private void getIntentData() {
        Intent i = getIntent();
        userId = i.getIntExtra("uid", -1);
        distance = getIntent().getFloatExtra("distance_m", 0f);
        elapsed = getIntent().getLongExtra("duration_ms", 0L);
    }

    private void initializeUI() {
        distTv = findViewById(R.id.distance_tv);
        timeTv = findViewById(R.id.time_tv);
        paceTv = findViewById(R.id.pace_tv);
        logBtn = findViewById(R.id.log_btn);
        recyclerView = findViewById(R.id.run_recycler);
    }

    private void displayRunData() {
        distTv.setText(String.format(Locale.US, "%.2f km", distance / 1000f));
        long secs = elapsed / 1000;
        long mm = (secs % 3600) / 60;
        long ss = secs % 60;
        timeTv.setText(
                String.format(Locale.US, "%02d:%02d", mm, ss));
        float pace = distance > 0 ? (elapsed / 1000f) / (distance / 1000f) : 0;
        int pMin = (int) pace / 60;
        int pSec = Math.round(pace) % 60;
        paceTv.setText(
                String.format(Locale.US, "%d:%02d min/km", pMin, pSec));
    }

    private void logButton() {
        logBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void authenticateUser() {

        if (userId == -1) {
            SharedPreferences prefs = getSharedPreferences(
                    getString(R.string.preference_file_key), MODE_PRIVATE);
            userId = prefs.getInt(getString(R.string.preference_userId_key), -1);
        }
        if (userId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
    }

    private void createRecyclerView() {
        adapter = new StrottaAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void observeRunLogs() {
        repository = StrottaRepository.getRepository(getApplication());
        repository.getAllLogsByUserId(userId).observe(this, adapter::submitList);
    }

    private void swipeToDelete() {
        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(
                        0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int d) {
                        int pos = viewHolder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            Strotta doomed = adapter.getCurrentList().get(pos);
                            repository.delete(doomed);
                            Toast.makeText(CardioLogActivity.this,
                                    "Log deleted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        helper.attachToRecyclerView(recyclerView);
    }

    private void renameLog() {
        adapter.setOnItemLongClick(log -> {
            android.widget.EditText input = new android.widget.EditText(this);
            input.setText(log.getTitle());

            new AlertDialog.Builder(this)
                    .setTitle("Rename log")
                    .setView(input)
                    .setPositiveButton("Save", (d, w) ->
                            repository.rename(log.getId(), input.getText().toString()))
                    .setNegativeButton("Cancel", null)
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

    private void appendRunToHistory(Run run) {
        String current = logHistoryTextView.getText().toString();
        String New = formatRun(run) + "\n";
        logHistoryTextView.setText(current + New);
    }

    private String formatRun(Run run) {
        float km = run.getDistantMeters() / 1000f;
        long ms = run.getTotalTime();
        long totalSeconds = ms / 1000;
        long mm = (totalSeconds % 3600) / 60;
        long hh = totalSeconds / 3600;
        long ss = totalSeconds % 60;
        String timeStr = hh > 0 ?
                String.format("%d:%02d:%02d", hh, mm, ss) : String.format("%02d:%02d", mm, ss);

        float pace = (ms / 1000f) / km;
        int pMin = (int) (pace / 60);
        int pSec = Math.round(pace) % 60;
        String paceStr = String.format("%d:%02d min/km", pMin, pSec);
        return String.format("* %.2f km | %s | %s", km, timeStr, paceStr);
    }

    public static Intent cardioRunIntentFactory(Context context, float distM, long elaspedMs) {
        return new Intent(context, CardioLogActivity.class).putExtra("DISTANCE", distM).putExtra("ELAPSED", elaspedMs);
    }

}