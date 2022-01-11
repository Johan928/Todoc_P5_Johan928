package com.cleanup.todoc.database;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    public abstract ProjectDao projectDao();

    public static volatile TodocDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    public static TodocDatabase getINSTANCE(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class, "todoc_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(taskDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static final RoomDatabase.Callback taskDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {


                ProjectDao projectDao = INSTANCE.projectDao();
                projectDao.deleteAll();
                Log.d(TAG, "onCreate: delete");

                Project project1 = new Project(1L, "Projet Tartampion", 0xFFEADAD1);
                Project project2 = new Project(2L, "Projet Lucidia", 0xFFB4CDBA) ;
                Project project3 = new Project(3L, "Projet Circus", 0xFFA3CED2);
                projectDao.insert(project1);
                projectDao.insert(project2);
                projectDao.insert(project3);

                TaskDao taskDao = INSTANCE.taskDao();
                taskDao.deleteAll();

                Task task = new Task(1L, "Nettoyer les vitres Salle 4", new Date().getTime());
                taskDao.insert(task);
                Task task1 = new Task(2L, "Laver le sol Salle 47", new Date().getTime());
                taskDao.insert(task1);

            });
        }
    };


}
