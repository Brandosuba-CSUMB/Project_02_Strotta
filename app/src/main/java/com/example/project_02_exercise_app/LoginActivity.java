package com.example.project_02_exercise_app;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private StrottaRepository repository;

    private ActivityLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = new StrottaRepository(getApplication());
        binding.loginButton.setOnClickListener(v -> verifyUser());
        binding.passswordLoginEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });
    }

    private void verifyUser() {
        String username = binding.userNameLoginEditText.getText().toString();
        String password = binding.passswordLoginEditText.getText().toString();

        if (username.isEmpty() || password.isEmpty()) {
            toastMaker("Username and password cannot be blank.");
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null && password.equals(user.getPassword())) {
                SharedPreferences prefs = getSharedPreferences(MainActivity.PREFERENCE_NAME, MODE_PRIVATE);
                prefs.edit().putInt(MainActivity.USER_ID_KEY, user.getId()).apply();
                if (user.isAdmin()) {
                    startActivity(LandingActivity.landingActivityIntentFactory(this, user.getId()));
                } else {
                    startActivity(MainActivity.mainActivityIntentFactory(this, user.getId()));
                }

                finish();
            } else {
                toastMaker("Incorrect username or password.");
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
