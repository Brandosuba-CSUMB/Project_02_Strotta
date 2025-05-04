package com.example.project_02_exercise_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.databinding.ActivityLandingBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LandingActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.example.project_02_exercise_app.USER_ID_KEY";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.project_02_exercise_app.SAVED_INSTANCE_STATE_USERID_KEY";
    private static final int LOGGED_OUT = -1;
    private ActivityLandingBinding binding;
    private StrottaRepository repository;
    private int userId;
    private User user;

    public static Intent landingActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, LandingActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLandingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        //repository = new StrottaRepository(getApplication());
        repository = StrottaRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        userId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (userId == -1) {
            Toast.makeText(this, "Error: No user ID received!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        repository.getUserByUserId(userId).observe(this, u -> {
            if (u == null) return;

            this.user = u;
            invalidateOptionsMenu();
            binding.landingAdminButton.setVisibility(
                    u.isAdmin() ? View.VISIBLE : View.INVISIBLE);
        });

        binding.landingCardioButton.setOnClickListener(v ->
                startActivity(CardioActivity.cardioIntentFactory(this )));

        binding.landingStrengthButton.setOnClickListener(v ->
                startActivity(StrengthActivity.strengthIntentFactory(this )));
        binding.landingCalisthenicsButton.setOnClickListener(v -> logExercise("Calisthenics"));
       // binding.landingAdminButton.setVisibility(user.isAdmin() ? View.VISIBLE : View.INVISIBLE);
        binding.landingAdminButton.setOnClickListener(v ->
                startActivity(new Intent(this, AdminActivity.class)));
    }

    private void loginUser(Bundle savedInstanceState) {
        //check shared preference for logged in user
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        userId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        if (userId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)) {
            userId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if (userId == LOGGED_OUT) {
            userId = getIntent().getIntExtra(USER_ID_KEY, LOGGED_OUT);
        }
        if (userId == LOGGED_OUT) {
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(userId);
        userObserver.observe(this, user -> {
            this.user = user;
            if (this.user != null) {
                invalidateOptionsMenu();
            }
        });
    }

    private void logExercise(String type) {
        String duration = "30 minutes";  // or whatever you generate
        int minutes = Integer.parseInt(duration.split(" ")[0]);
        Strotta log = new Strotta(userId, minutes);
        repository.insertStrottaRepository(log);
        Toast.makeText(this, type + " logged!", Toast.LENGTH_SHORT).show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);
        if(user != null){
            item.setTitle(user.getUsername());
        }else {
            item.setTitle("Logout");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logoutMenuItem) {
            showLogoutDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        SharedPreferences prefs = getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        prefs.edit().remove(getString(R.string.preference_userId_key)).apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}