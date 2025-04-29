package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.entities.Run;

import java.util.concurrent.Executors;

public class CardioLogActivity extends AppCompatActivity {
    private float distance;
    private long elapsed;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardio_log);

        TextView distanceTv = findViewById(R.id.distance_tv);
        TextView timeTv = findViewById(R.id.time_tv);
        TextView paceTv = findViewById(R.id.pace_tv);
        Button logBtn = findViewById(R.id.log_btn);

        distance = getIntent().getFloatExtra("distance_m",0f);
        elapsed = getIntent().getLongExtra("duration_ms",0);

        long s = elapsed / 1000;
        long mm = (s%3600) / 60;
        long hh = s/ 3600;
        long ss = s%60;

        String timeStr = hh > 0 ? String.format("%d:%02d:%02d",hh,mm,ss) : String.format("%02d:%02d",mm,ss);

        float paceSec = (elapsed/1000f)/(distance/1000f);
        int pMin = (int) (paceSec/60);
        int pSec = Math.round(paceSec) % 60;
        String paceStr = distance>20 ? String.format("%d:%02d min/km",pMin,pSec) : "- min/km";

        distanceTv.setText(String.format("%.2f km",distance/1000f));
        timeTv.setText(timeStr);
        paceTv.setText(paceStr);

        logBtn.setOnClickListener(v -> {
            Run run = new Run(distance,elapsed,System.currentTimeMillis());

            Executors.newSingleThreadExecutor().execute(() -> {
                StrottaDatabase.getDatabase(getApplicationContext()).runDAO().insert(run);
                runOnUiThread(this::finish);
            });
        });

    }
    public static Intent cardioRunIntentFactory(Context context, float distM,long elaspedMs){
        return new Intent(context,CardioLogActivity.class).putExtra("DISTANCE",distM).putExtra("ELAPSED",elaspedMs);
    }

}
