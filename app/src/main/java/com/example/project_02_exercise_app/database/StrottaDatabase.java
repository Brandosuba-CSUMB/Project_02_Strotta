package com.example.project_02_exercise_app.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project_02_exercise_app.MainActivity;
import com.example.project_02_exercise_app.database.entities.Run;
import com.example.project_02_exercise_app.database.entities.Strotta;
import com.example.project_02_exercise_app.database.entities.User;
import com.example.project_02_exercise_app.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {User.class, Strotta.class, Run.class}, version = 2, exportSchema = false)

public abstract class StrottaDatabase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    private static final String DATABASE_NAME = "StrottaDatabase";
    public static final String STROTTA_TABLE = "strottaTable";

    public static final String STRENGTH_TABLE = "strengthTable";

    private static volatile StrottaDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecution = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static StrottaDatabase getDatabase(final Context context){
        if (INSTANCE == null) {
            synchronized (StrottaDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    StrottaDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED");
            databaseWriteExecution.execute(()->{
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();

                User admin = new User("admin1","admin1");
                admin.setAdmin(true);
                dao.insert(admin);

                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
            });

        }
    };
    public abstract StrottaDAO strottaDAO();

    public abstract UserDAO userDAO();
    public abstract RunDAO runDAO();

}