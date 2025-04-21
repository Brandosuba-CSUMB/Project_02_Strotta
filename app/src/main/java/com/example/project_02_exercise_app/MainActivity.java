package com.example.project_02_exercise_app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.example.project_02_exercise_app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.example.project_02_exercise_app.databinding.ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}