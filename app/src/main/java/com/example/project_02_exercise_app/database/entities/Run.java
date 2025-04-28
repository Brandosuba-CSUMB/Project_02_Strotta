package com.example.project_02_exercise_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_table")
public class Run {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private float distantMeters;
    private long totalTime;
    private String path;
    private long timeStamp;

    public Run(int id, float distantMeters, long totalTime, String path, long timeStamp) {
        this.id = id;
        this.distantMeters = distantMeters;
        this.totalTime = totalTime;
        this.path = path;
        this.timeStamp = timeStamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDistantMeters() {
        return distantMeters;
    }

    public void setDistantMeters(float distantMeters) {
        this.distantMeters = distantMeters;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
