package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private StrottaRepository repository;
    private int loggedInUserId = -1;
    public static final String PREFERENCE_NAME = "user_preference";
    public static final String USER_ID_KEY = "logged_user_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = new StrottaRepository(getApplication());
        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(USER_ID_KEY, -1);

        if (loggedInUserId == -1) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        binding.logButton.setOnClickListener(v -> {
            String exercise = binding.cardioInputEditText.getText().toString().trim();
            if (exercise.isEmpty()) {
                Toast.makeText(this, "Enter an exercise name", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            Strotta log = new Strotta(loggedInUserId, exercise, "30 minutes", date);
            repository.insertLog(log);
            Toast.makeText(this, "Exercise logged!", Toast.LENGTH_SHORT).show();
        });

        binding.logDisplayTextView.setOnClickListener(v -> showLogoutDialog());
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Logout?")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> logout())
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logout() {
        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(USER_ID_KEY);
        editor.apply();
        startActivity(LoginActivity.loginIntentFactory(this));
        finish();
    }
    public static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}