package com.example.project_02_exercise_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project_02_exercise_app.database.StrottaDatabase;

@Entity(tableName = StrottaDatabase.STROTTA_TABLE)
public class Strotta {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int userId;
    private String exerciseType;
    private String duration; // e.g., "30 minutes"
    private String date;

    public Strotta(int userId, String exerciseType, String duration, String date) {
        this.userId = userId;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getExerciseType() { return exerciseType; }
    public void setExerciseType(String exerciseType) { this.exerciseType = exerciseType; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
}
