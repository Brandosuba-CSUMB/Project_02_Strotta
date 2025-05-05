package com.example.project_02_exercise_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Calisthenics;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.databinding.ActivityCalisthenicsBinding;
import com.example.project_02_exercise_app.tracking.CardioTrackingService;

public class CalisthenicsActivity extends FragmentActivity {

    private ActivityCalisthenicsBinding binding;

    private long startRealTime = 0;

    private long elapsedMs = 0;
    private int userId = -1;
    private TextView timeTv;

    private final ActivityResultLauncher<String[]> permissions =
            registerForActivityResult(
                    new ActivityResultContracts.RequestMultiplePermissions(),
                    res -> { if (!res.containsValue(false)) enableRecording(); });

    @Override protected void onCreate(Bundle s){
        super.onCreate(s);
        binding = ActivityCalisthenicsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userId = getIntent().getIntExtra("uid", -1);
        timeTv = findViewById(R.id.timeTv);

        binding.recordBtn.setOnClickListener(v -> {
            permissions.launch(new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACTIVITY_RECOGNITION
            });
        });

        binding.stopBtn.setOnClickListener(v -> stopRecording());
    }

//    @Override protected void onStart(){
//        super.onStart();
//        LocalBroadcastManager.getInstance(this).registerReceiver(
//                locReceiver,new IntentFilter(CardioTrackingService.ACTION_UPDATE));
//    }
//
//    @Override protected void onStop(){
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(locReceiver);
//        super.onStop();
//    }

    private void enableRecording(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        ContextCompat.startForegroundService(this,
                new Intent(this,CardioTrackingService.class)
                        .setAction(CardioTrackingService.ACTION_START));

        binding.recordBtn.setEnabled(false);
        binding.stopBtn.setEnabled(true);
        startRealTime = SystemClock.elapsedRealtime();
        binding.statsBar.post(tick);
    }

    private void stopRecording(){
//        stopService(new Intent(this,CardioTrackingService.class)
//                .setAction(CardioTrackingService.ACTION_STOP));
//
//
//        Intent logIntent = new Intent(this, CalisthenicsLogActivity.class)
//                .putExtra("duration_ms",elapsedMs)
//                .putExtra("uid", userId);
//        startActivity(logIntent);

        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);
        int userId = prefs.getInt(getString(R.string.preference_userId_key), -1);

        Intent log = new Intent(this, CalisthenicsLogActivity.class)
                .putExtra("uid",          userId)
                .putExtra("duration_ms",  elapsedMs);
        startActivity(log);

        int seconds = (int) (elapsedMs /1000);
        StrottaRepository repository = StrottaRepository.getRepository(getApplication());
        repository.insertStrottaRepository(new Strotta(userId, seconds));

        /* reset UI */
        binding.recordBtn.setEnabled(true);
        binding.stopBtn.setEnabled(false);
        elapsedMs = 0;
        timeTv.setText("00:00");
        binding.statsBar.removeCallbacks(tick);
        startRealTime = 0L;
    }

    private void updateStatsUI() {
        long s  = elapsedMs / 1000;
        long mm = (s % 3600) / 60;
        long hh =  s / 3600;
        long ss =  s % 60;
        timeTv.setText(hh > 0
                ? String.format("%d:%02d:%02d", hh, mm, ss)
                : String.format("%02d:%02d", mm, ss));
    }

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (startRealTime == 0L) return;  // not recording

            elapsedMs = SystemClock.elapsedRealtime() - startRealTime;
            updateStatsUI();// refresh timer & pace even if no GPS
            binding.statsBar.postDelayed(this, 1);   // tick every 1 s
        }
    };

    public static Intent calisthenicsIntentFactory(Context c, int uid){
        return new Intent(c, CalisthenicsActivity.class).putExtra("uid", uid);
    }


}
