package com.example.project_02_exercise_app;
import android.os.Bundle;
import android.widget.Button;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02_exercise_app.database.StrottaDAO;
import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.StrottaRepository;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.database.viewHolders.StrottaAdapter;

import com.example.project_02_exercise_app.database.viewHolders.UserAdapter;

public class AdminActivity extends AppCompatActivity {
    private RunAdapter runAdapter;
    private StrottaAdapter strottaAdapter;

    private UserAdapter userAdapter;
    private TextView selectedUserTitle;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_admin);

        Button deleteAll = findViewById(R.id.delete_all_runs_btn);
        RecyclerView recyclerView = findViewById(R.id.run_recycler_view);
        RecyclerView userRecycler = findViewById(R.id.user_recycler_view);
        selectedUserTitle = findViewById(R.id.selected_user_title);

        strottaAdapter = new StrottaAdapter();
        recyclerView.setAdapter(strottaAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        UserAdapter userAdapter = new UserAdapter(user -> {
            selectedUserTitle.setText("Logs for: " + user.getUsername());
            StrottaRepository.getRepository(getApplication()).getAllLogsByUserId(user.getId()).observe(this,strottaAdapter::submitList);
        });
        userRecycler.setAdapter(userAdapter);
        userRecycler.setLayoutManager(new LinearLayoutManager(this));

        StrottaRepository.getRepository(getApplication()).getAllLogs().observe(this, strottaAdapter::submitList);
        StrottaDatabase.getDatabase(getApplicationContext()).userDAO().getAllUsers().observe(this,userAdapter::submitList);


        deleteAll.setOnClickListener(v -> {
            StrottaDatabase.databaseWriteExecution.execute(() -> {
                StrottaDatabase.getDatabase(getApplicationContext())
                        .strottaDAO().deleteAll();
            });
        });
    }
}
