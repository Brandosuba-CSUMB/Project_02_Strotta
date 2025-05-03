package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Run;
import com.example.project_02_exercise_app.database.viewHolders.StrottaAdapter;

import java.util.Locale;

public class CalisthenicsLogActivity extends AppCompatActivity {
    private long elapsed;
    private int userId;
    private TextView logHistoryTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calisthenics_log);

        Intent i = getIntent();
        userId = i.getIntExtra("uid", -1);
        elapsed  = getIntent().getLongExtra ("duration_ms", 0L);

        TextView timeTv = findViewById(R.id.time_tv);
        Button logBtn = findViewById(R.id.log_btn);

        long secs = elapsed / 1000;
        long mm   = (secs % 3600) / 60;
        long ss   = secs % 60;
        timeTv.setText(
                String.format(Locale.US, "%02d:%02d", mm, ss));

        logBtn.setOnClickListener(v -> {
            finish();
        });
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

        RecyclerView recyclerView = findViewById(R.id.calisthenics_recycler);
        StrottaAdapter adapter = new StrottaAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        StrottaRepository repository = StrottaRepository.getRepository(getApplication());
        repository.getAllLogsByUserId(userId).observe(this, adapter ::submitList);
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
