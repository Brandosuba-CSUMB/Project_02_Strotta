package com.example.project_02_exercise_app.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project_02_exercise_app.LoginActivity;
import com.example.project_02_exercise_app.database.StrottaDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity(tableName = StrottaDatabase.STROTTA_TABLE)
public class Strotta {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int cardio;

    private int userId;

    // Fields from Strength
    private String strengthExerciseName;
    private int strengthReps;

    private int strengthSets;

    private double strengthWeight;
    private long strengthElapsedMs;
    private LocalDateTime date;

    public Strotta(int userId, String strengthExerciseName, double strengthWeight, int strengthSets,int strengthReps, long strengthElapsedMs) {
        this.userId = userId;
        this.strengthExerciseName = strengthExerciseName;
        this.strengthWeight = strengthWeight;
        this.strengthSets = strengthSets;
        this.strengthReps = strengthReps;
        this.strengthElapsedMs = strengthElapsedMs;
        date = LocalDateTime.now();
    }
    public Strotta(int userId, int cardioMinutes) {
        this.userId = userId;
        this.cardio = cardioMinutes;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        long totalSeconds = strengthElapsedMs / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

        return "Strength Exercise: " + strengthExerciseName +
                "\nWeight: " + strengthWeight +
                "\nSets: " + strengthSets +
                "\nReps: " + strengthReps +
                "\nElapsed Time: " + timeFormatted +
                "\nDate: " + date.format(formatter);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCardio() {
        return cardio;
    }

    public void setCardio(int cardio) {
        this.cardio = cardio;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStrengthExerciseName() {
        return strengthExerciseName;
    }
    public void setStrengthExerciseName(String strengthExerciseName) {
        this.strengthExerciseName = strengthExerciseName;
    }

    public int getStrengthReps() {
        return strengthReps;
    }
    public void setStrengthReps(int strengthReps) {
        this.strengthReps = strengthReps;
    }

    public int getStrengthSets() {
        return strengthSets;
    }
    public void setStrengthSets(int strengthSets) {
        this.strengthSets = strengthSets;
    }
    public double getStrengthWeight() {
        return strengthWeight;
    }
    public void setStrengthWeight(double strengthWeight) {
        this.strengthWeight = strengthWeight;
    }

    public long getStrengthElapsedMs() {
        return strengthElapsedMs;
    }
    public void setStrengthElapsedMs(long strengthElapsedMs) {
        this.strengthElapsedMs = strengthElapsedMs;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Strotta strotta = (Strotta) o;
        return id == strotta.id &&
                cardio == strotta.cardio &&
                userId == strotta.userId &&
                Objects.equals(strengthExerciseName, strotta.strengthExerciseName) &&
                strengthSets == strotta.strengthSets &&
                strengthReps == strotta.strengthReps &&
                Double.compare(strengthWeight, strotta.strengthWeight) == 0 &&
                strengthElapsedMs == strotta.strengthElapsedMs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardio, userId, strengthExerciseName, strengthReps, strengthSets, strengthWeight, strengthElapsedMs);
    }
}