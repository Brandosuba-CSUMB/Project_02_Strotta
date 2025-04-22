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
    private LocalDateTime date;

    public Strotta(int userId, int cardio) {
        this.userId = userId;
        this.cardio = cardio;
        date = LocalDateTime.now();
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - hh:mm a");

        return "Cardio" + cardio
                + "\n" + "Date: " +"\n" +
                date.format(formatter);
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
        return id == strotta.id && cardio == strotta.cardio && userId == strotta.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardio, userId);
    }
}