package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;

import com.example.project_02_exercise_app.databinding.ActivityCardioBinding;
import com.example.project_02_exercise_app.databinding.ActivityStrengthBinding;

public class StrengthActivity extends FragmentActivity {

    private ActivityStrengthBinding binding;
    private int weight = 0;
    private String exerciseType = null;
    private int userId = 0;
    private static final String STRENGTH_ACTIVITY_USER_ID = "com.example.project_02_exercise_app";
    private long elapsedMs = 0;
    private long startRealtime = 0L;
    private TextView timeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStrengthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        timeTv = binding.timeTv;

        binding.recordBtn.setOnClickListener(v -> {
            startRealtime = SystemClock.elapsedRealtime();
            binding.recordBtn.setEnabled(false);
            binding.stopBtn.setEnabled(true);
            binding.statsBar.post(tick);
        });

        binding.stopBtn.setOnClickListener(v -> stopRecording());
    }

    private void stopRecording() {
        binding.recordBtn.setEnabled(true);
        binding.stopBtn.setEnabled(false);
        elapsedMs = 0;
        timeTv.setText("00:00");
        binding.statsBar.removeCallbacks(tick);
        startRealtime = 0L;
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

    public static Intent strengthIntentFactory(Context context) {
        return new Intent(context, StrengthActivity.class);
    }
}
