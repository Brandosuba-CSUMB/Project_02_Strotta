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

        SharedPreferences prefs = getSharedPreferences(getString((R.string.preference_file_key)),MODE_PRIVATE);
        int userId = prefs.getInt(getString(R.string.preference_userId_key),-1);

        if(userId != -1){
            Intent intent = LandingActivity.landingActivityIntentFactory(this,userId);
            startActivity(intent);
            finish();
            return;
        }
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
        String username = binding.userNameLoginEditText.getText().toString().trim();
        String password = binding.passwordLoginEditText.getText().toString();

        if (username.isEmpty()) {
            toastMaker("Username should not be blank");
            return;
        }

        //LiveData<User> userObserver = repository.getUserByUserName(username);
        LiveData<User> userLiveData = repository.getUserByUserName(username);
        obsOnce(userLiveData, user ->{
            if(user !=null){
                if(password.equals(user.getPassword())){
                    SharedPreferences prefs = getSharedPreferences(
                            getString(R.string.preference_file_key), MODE_PRIVATE
                    );
                    prefs.edit().putInt(getString(R.string.preference_userId_key), user.getId()).apply();

                    Intent intent = LandingActivity.landingActivityIntentFactory(getApplicationContext(), user.getId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
//        userObserver.observe(this, user -> {
//            if (user != null) {
//                if (password.equals(user.getPassword())) {
//                    SharedPreferences prefs = getSharedPreferences(
//                            getString(R.string.preference_file_key), MODE_PRIVATE
//                    );
//                    prefs.edit().putInt(getString(R.string.preference_userId_key), user.getId()).apply();
//
//                    Intent intent = LandingActivity.landingActivityIntentFactory(getApplicationContext(), user.getId());
////                    Intent intent = user.isAdmin()
////                            ? LandingActivity.landingActivityIntentFactory(getApplicationContext(), user.getId())
////                            : MainActivity.mainActivityIntentFactory(getApplicationContext(), user.getId());
//
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    toastMaker("Invalid password");
//                    binding.passwordLoginEditText.setSelection(0);
//                }
//            } else {
//                toastMaker(username + " is not a valid username.");
//                binding.userNameLoginEditText.setSelection(0);
//            }
//        });
    }
    private <T> void obsOnce(final LiveData<T> liveData, final androidx.lifecycle.Observer<T> observer){
        liveData.observe(this,new androidx.lifecycle.Observer<T>(){
            @Override
            public void onChanged(T t){
                liveData.removeObserver(this);
                observer.onChanged(t);
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