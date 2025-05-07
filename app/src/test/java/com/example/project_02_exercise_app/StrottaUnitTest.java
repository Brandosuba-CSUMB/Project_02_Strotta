package com.example.project_02_exercise_app;

import com.example.project_02_exercise_app.database.entities.Strotta;

import org.junit.Test;
import static org.junit.Assert.*;

public class StrottaUnitTest {
/*
Brandons Unit tests for Cardio
 */
    @Test
    public void testCardioConstructorAndGetters() {

        int userId = 123;
        double distanceKm = 7.5;
        int seconds = 2700; // 45 minutes

        Strotta cardioLog = new Strotta(userId, distanceKm, seconds);
        assertEquals(userId, cardioLog.getUserId());
        assertEquals(distanceKm, cardioLog.getDistanceKm(), 0.001);
        assertEquals(seconds, cardioLog.getSeconds());
        assertFalse(cardioLog.isCalisthenics());
        assertEquals("Activity", cardioLog.getTitle());
        assertNotNull(cardioLog.getDate());
    }

    @Test
    public void testCardioSetters() {
        Strotta cardioLog = new Strotta(1, 1.0, 60);
        int newUserId = 456;
        double newDistanceKm = 15.0;
        int newSeconds = 5400;
        String newTitle = "Morning Run";

        cardioLog.setUserId(newUserId);
        cardioLog.setDistanceKm(newDistanceKm);
        cardioLog.setSeconds(newSeconds);
        cardioLog.setTitle(newTitle);

        assertEquals(newUserId, cardioLog.getUserId());
        assertEquals(newDistanceKm, cardioLog.getDistanceKm(), 0.001);
        assertEquals(newSeconds, cardioLog.getSeconds());
        assertEquals(newTitle, cardioLog.getTitle());
    }

    @Test
    public void testPaceCalculation() {
        Strotta cardioLog1 = new Strotta(1, 10.0, 3600);
        assertEquals(6.0, cardioLog1.paceMinPerKm(), 0.001);

        Strotta cardioLog2 = new Strotta(1, 5.0, 1800);
        assertEquals(6.0, cardioLog2.paceMinPerKm(), 0.001);

        Strotta cardioLog3 = new Strotta(1, 7.5, 2700);
        assertEquals(6.0, cardioLog3.paceMinPerKm(), 0.001);

        Strotta cardioLogZeroDistance = new Strotta(1, 0.0, 1000);
        assertEquals(0.0, cardioLogZeroDistance.paceMinPerKm(), 0.001);

        Strotta cardioLogZeroSeconds = new Strotta(1, 5.0, 0);
        assertEquals(0.0, cardioLogZeroSeconds.paceMinPerKm(), 0.001);
    }
    /*
    Brandons unit tests End
     */
}