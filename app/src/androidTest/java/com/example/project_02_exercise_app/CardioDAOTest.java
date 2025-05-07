package com.example.project_02_exercise_app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.project_02_exercise_app.database.StrottaDAO;
import com.example.project_02_exercise_app.database.StrottaDatabase;
import com.example.project_02_exercise_app.database.UserDAO;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User; // Import User entity

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CardioDAOTest {

    private StrottaDAO dao;
    private UserDAO userDAO;
    private StrottaDatabase db;

    @Before
    public void setup() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, StrottaDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.strottaDAO();
        User dummyUser = new User("Test User", "testpass");
        dummyUser.setId(1);
        db.userDAO().insert(dummyUser);
    }

    @After
    public void teardown() {
        db.close();
    }
    @Test
    public void testInsertCardioLog() throws Exception {
        Strotta log = new Strotta(1, 3.5, 600);
        dao.insert(log);
        List<Strotta> logs = dao.getAllLogsByUserIdSync(1);
        assertEquals(1, logs.size());
        assertEquals(3.5, logs.get(0).getDistanceKm(), 0.01);
    }
    @Test
    public void testUpdateCardioLog() throws Exception {
        Strotta log = new Strotta(1, 2.0, 300);
        dao.insert(log);
        List<Strotta> logs = dao.getAllLogsByUserIdSync(1);
        Strotta inserted = logs.get(0);
        inserted.setDistanceKm(4.2);
        dao.update(inserted);
        Strotta updated = dao.getIdSync(inserted.getId());
        assertEquals(4.2, updated.getDistanceKm(), 0.01);
    }
    @Test
    public void testDeleteCardioLog() throws Exception {
        Strotta log = new Strotta(1, 5.0, 900);
        dao.insert(log);
        List<Strotta> logs = dao.getAllLogsByUserIdSync(1);
        Strotta inserted = logs.get(0);
        dao.delete(inserted);
        List<Strotta> afterDelete = dao.getAllLogsByUserIdSync(1);
        assertTrue(afterDelete.isEmpty());
    }
}
