package com.example.project_02_exercise_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.databinding.ActivitySignUpBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private StrottaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = StrottaRepository.getRepository(getApplication());

        binding.registerButton.setOnClickListener(v -> {
            createAccount();
        });
        binding.switchToLoginTextView.setOnClickListener(v ->{
            Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(intent);
        });
    }
    private void createAccount() {
        String user = binding.usernameEditText.getText().toString();
        String pass = binding.passwordEditText.getText().toString();
        String pass2 = binding.confirmPasswordEditText.getText().toString();
        if (user.isEmpty() || pass.isEmpty()) {
            toastMaker("Username and password cannot be empty");
            return;
        }
        if (!pass.equals(pass2)) {
            toastMaker("The password is not the same!");
            return;
        }
        User newUser = new User(user,pass);
        long uid = repository.insertUserSync(newUser);
        getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE).edit().
                putInt(getString(R.string.preference_userId_key),(int) uid).apply();

        startActivity(LandingActivity.landingActivityIntentFactory(this, (int) uid));
    }
    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    public static <T> void obsOnce(LiveData<T> live, LifecycleOwner owner, Observer<T> observer){
        live.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                live.removeObserver(this);
                observer.onChanged(t);
            }
        });
    }
}
