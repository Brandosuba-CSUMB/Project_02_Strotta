package com.example.project_02_exercise_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_02_exercise_app.databinding.ActivityMainBinding;


public class ActivityCardio extends AppCompatActivity {
    private ActivityMainBinding binding;

    public static Intent cardioIntentFactory(Context context){
        return new Intent(context, ActivityCardio.class);
    }
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
