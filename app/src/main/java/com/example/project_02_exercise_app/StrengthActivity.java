package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.viewHolders.StrottaAdapter;
import com.example.project_02_exercise_app.database.viewHolders.StrottaViewModel;
import com.example.project_02_exercise_app.databinding.ActivityStrengthBinding;

import java.util.ArrayList;
import java.util.List;

public class StrengthActivity extends FragmentActivity {

    private ActivityStrengthBinding binding;
    private long elapsedMs = 0;
    private long startRealtime = 0L;
    private TextView timeTv;
    private int userId = -1;

    private RecyclerView recyclerView;
    private StrottaAdapter strottaAdapter;
    private StrottaViewModel strottaViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStrengthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        timeTv = binding.timeTv;
        userId = getIntent().getIntExtra("user_id", -1);
        strottaAdapter = new StrottaAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(strottaAdapter);
        StrottaDatabase.getDatabase(getApplicationContext()).strengthDAO().getStrengthLogsByUserId(userId).observe(this,logs -> strottaAdapter.submitList(logs));

        binding.recordBtn.setOnClickListener(v -> {
            startRealtime = SystemClock.elapsedRealtime();
            binding.recordBtn.setEnabled(false);
            binding.stopBtn.setEnabled(true);
            binding.statsBar.post(tick);
        });

        binding.stopBtn.setOnClickListener(v -> stopRecording());

        RecyclerView recyclerView = findViewById(R.id.strengthRecycler);
        strottaAdapter = new StrottaAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(strottaAdapter);

        strottaViewModel = new ViewModelProvider(this).get(StrottaViewModel.class);
        strottaViewModel.getAllLogsById(userId).observe(this, strottas -> {
            // Update adapter with only strength logs
            strottaAdapter.submitList(strottas);
        });
    }

    private void stopRecording() {
        binding.recordBtn.setEnabled(true);
        binding.stopBtn.setEnabled(false);
        binding.statsBar.removeCallbacks(tick);
        elapsedMs = SystemClock.elapsedRealtime() - startRealtime;
        startRealtime = 0L;
        timeTv.setText("00:00");

        Intent intent = StrengthLogActivity.strengthLogIntentFactory(this, elapsedMs,userId);
        startActivity(intent);
    }


    private void updateStatsUI() {
        long s = elapsedMs / 1000;
        long mm = (s % 3600) / 60;
        long hh = s / 3600;
        long ss = s % 60;

        timeTv.setText(hh > 0
                ? String.format("%d:%02d:%02d", hh, mm, ss)
                : String.format("%02d:%02d", mm, ss));
    }

    private final Runnable tick = new Runnable() {
        @Override
        public void run() {
            if (startRealtime == 0L) return;
            elapsedMs = SystemClock.elapsedRealtime() - startRealtime;
            updateStatsUI();
            binding.statsBar.postDelayed(this, 1000);
        }
    };

    public static Intent strengthIntentFactory(Context context, int userId) {
        return new Intent(context, StrengthActivity.class).putExtra("user_id",userId);
    }
}
