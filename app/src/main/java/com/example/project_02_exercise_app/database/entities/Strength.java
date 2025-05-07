package com.example.project_02_exercise_app.database.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Strength {
    private String strengthExerciseName;
    private int strengthReps;
    private int userId;
    private int strengthSets;

    private double strengthWeight;
    private long strengthElapsedMs;
    private LocalDateTime date;

    public Strength(int userId, String strengthExerciseName, double strengthWeight, int strengthSets,int strengthReps, long strengthElapsedMs) {
        this.userId = userId;
        this.strengthExerciseName = strengthExerciseName;
        this.strengthWeight = strengthWeight;
        this.strengthSets = strengthSets;
        this.strengthReps = strengthReps;
        this.strengthElapsedMs = strengthElapsedMs;
        date = LocalDateTime.now();
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
        Strength strength = (Strength) o;
        return strengthReps == strength.strengthReps && userId == strength.userId && strengthSets == strength.strengthSets && Double.compare(strengthWeight, strength.strengthWeight) == 0 && strengthElapsedMs == strength.strengthElapsedMs && Objects.equals(strengthExerciseName, strength.strengthExerciseName) && Objects.equals(date, strength.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strengthExerciseName, strengthReps, userId, strengthSets, strengthWeight, strengthElapsedMs, date);
    }
}
