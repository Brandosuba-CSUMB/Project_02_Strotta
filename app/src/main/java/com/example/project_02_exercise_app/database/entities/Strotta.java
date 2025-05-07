package com.example.project_02_exercise_app.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.project_02_exercise_app.LoginActivity;
import com.example.project_02_exercise_app.database.StrottaDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Entity(tableName = StrottaDatabase.STROTTA_TABLE, foreignKeys = @ForeignKey(
        entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = ForeignKey.CASCADE
),
indices = @Index("userId"))
public class Strotta {
    @PrimaryKey(autoGenerate = true)
    private int cardio;
    private double bodyWeight;
    private String calisthenicsExercise;

    private boolean isCalisthenics = false;
    private int userId;
    private int id;
    public String title = "Activity";
  
    private LocalDateTime date = LocalDateTime.now();
    private double distanceKm;
    private int seconds;

    @Ignore                                    // ← tell Room to ignore this one
    public Strotta(int userId, int cardioMin) {
        this.userId     = userId;
        this.distanceKm = 0;                   // unknown
        this.seconds    = cardioMin * 60;      // at least duration is correct
    }

    /* --- canonical 3-arg ctor used by GPS screen --- */
    public Strotta(int userId, double distanceKm, int seconds) {
        this.userId     = userId;
        this.distanceKm = distanceKm;
        this.seconds    = seconds;
    }

    //This is for calisthenics
    public Strotta(int userId, String exercise, double bodyWeight) {
        this.userId = userId;
        this.calisthenicsExercise = exercise;
        this.bodyWeight = bodyWeight;
        this.isCalisthenics = true;
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        return "Cardio: " + cardio
                + "km\n" + "Date: " +"\n" +
                date.format(formatter);
    }


    public int getCardio() {
        return cardio;
    }

    public void setCardio(int cardio) {
        this.cardio = cardio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public double paceMinPerKm(){
        return distanceKm == 0? 0:(seconds /60.0)/distanceKm;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public double getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(double bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

    public String getCalisthenicsExercise() {
        return calisthenicsExercise;
    }

    public void setCalisthenicsExercise(String calisthenicsExercise) {
        this.calisthenicsExercise = calisthenicsExercise;
    }

    public boolean isCalisthenics() {
        return isCalisthenics;
    }

    public void setCalisthenics(boolean calisthenics) {
        isCalisthenics = calisthenics;
    }

    public String getTitle() { return title; }
    public void setTitle(String t){ this.title = t; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Strotta strotta = (Strotta) o;
        return cardio == strotta.cardio && Double.compare(bodyWeight, strotta.bodyWeight) == 0 && isCalisthenics == strotta.isCalisthenics && userId == strotta.userId && Double.compare(distanceKm, strotta.distanceKm) == 0 && seconds == strotta.seconds && Objects.equals(calisthenicsExercise, strotta.calisthenicsExercise) && Objects.equals(date, strotta.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cardio, bodyWeight, calisthenicsExercise, isCalisthenics, userId, date, distanceKm, seconds);
    }
}