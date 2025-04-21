package com.example.project_02_exercise_app;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    private StrottaRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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


        if (username.isEmpty()) {
            toastMaker("Username may not be blank.");
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserName(username);
        userObserver.observe(this, user -> {
            if (user != null) {
                String password = binding.passswordLoginEditText.getText().toString();
                if (password.equals(user.getPassword())) {
                    startActivity(LandingActivity.landingActivityIntentFactory(getApplicationContext(), user.getId()));
                } else {
                    toastMaker("Invalid password");
                    binding.passswordLoginEditText.setSelection(0);
                }
            } else {
                toastMaker(String.format("%s is not a valid username.", username));
                binding.userNameLoginEditText.setSelection(0);
            }
        });

        if (password.isEmpty()) {
            toastMaker("Password may not be blank.");
            return;
        }


//        LiveData<User> userObserver = repository.getUserByUserName(username);
//        userObserver.observe(this, user -> {
//            if (user != null) {
//                String password = binding.passswordLoginEditText.getText().toString();
//                if (password.equals(user.getPassword())) {
//                    startActivity(LandingActivity.landingActivityIntentFactory(getApplicationContext(), user.getId()));
//                } else {
//                    toastMaker("Invalid password");
//                    binding.passswordLoginEditText.setSelection(0);
//                }
//            } else {
//                toastMaker(String.format("%s is not a valid username.", username));
//                binding.userNameLoginEditText.setSelection(0);
//            }
//        });
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
