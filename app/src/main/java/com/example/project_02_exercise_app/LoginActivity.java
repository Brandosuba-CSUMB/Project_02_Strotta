package com.example.project_02_exercise_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.MainActivity;
import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private StrottaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        repository = StrottaRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
    });
    TextView signup = findViewById(R.id.switchToSignUpTextView);
    signup.setOnClickListener(v ->{
        Intent  intent = new Intent(LoginActivity.this,SignupActivity.class);
        startActivity(intent);
    });
}
    private void verifyUser(){
        String username = binding.userNameLoginEditText.getText().toString();

        if(username.isEmpty()){
            toastMaker("username should not be blank");
            return;
        }
        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passwordLoginEditText.getText().toString();
                if (password.equals(user.getPassword())) {
                    Intent intent;
                    if (user.isAdmin()) {
                        intent = LandingActivity.landingActivityIntentFactory(
                                getApplicationContext(), user.getId());
                    } else {
                        intent = MainActivity.mainActivityIntentFactory(
                                getApplicationContext(), user.getId());
                    }
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    );
                    startActivity(intent);
                    finish();
                } else {
                    toastMaker("Invalid password");
                    binding.passwordLoginEditText.setSelection(0);
                }
            } else {
                toastMaker(username + " is not a valid username.");
                binding.userNameLoginEditText.setSelection(0);
            }
        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public static Intent loginIntentFactory(Context context){
        return new Intent(context, LoginActivity.class );
    }
}