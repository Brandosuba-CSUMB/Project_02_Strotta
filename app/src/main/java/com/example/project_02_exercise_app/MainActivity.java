package com.example.project_02_exercise_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.project_02_exercise_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    public static final String PREFERENCE_NAME = "user_preference";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME,MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);
        if(username == null){
            startActivity(new Intent(this, LoginActivity.class));
        }else{
            startActivity(new Intent(this, LandingActivity.class));
        }
        binding.logButton.setOnClickListener(v -> {
            Toast.makeText(this,"Works",Toast.LENGTH_SHORT).show();
        });
    }
}