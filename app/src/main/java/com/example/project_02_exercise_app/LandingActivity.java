package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project_02_exercise_app.databinding.ActivityLandingBinding;

public class LandingActivity extends AppCompatActivity {

    private ActivityLandingBinding binding;

    public static Intent landingActivityIntentFactory(Context applicationContext, int id) {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        binding.landingCardioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Woo! Cardio!", Toast.LENGTH_SHORT).show();
            }
        });
        
        binding.landingStrengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Wow! Strength!", Toast.LENGTH_SHORT).show();
            }
        });
        
        binding.landingCalisthenicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Awesome! Calisthenics!", Toast.LENGTH_SHORT).show();
            }
        });
        
        binding.landingAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LandingActivity.this, "Boss in the house! Admin Page!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}