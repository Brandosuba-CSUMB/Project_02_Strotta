package com.example.project_02_exercise_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "run_table")
public class Run {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private float distantMeters;
    private long totalTime;
    private long timeStamp;

    public Run(float distantMeters, long totalTime, long timeStamp) {
        this.distantMeters = distantMeters;
        this.totalTime = totalTime;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
