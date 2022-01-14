package com.cleanup.todoc.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.TaskDao;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Task;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;


    public TaskRepository(Application application) {
        TodocDatabase db = TodocDatabase.getINSTANCE(application);
        taskDao = db.taskDao();
        allTasks = taskDao.getAllTasks();
    }


    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public void insert(Task task){
        TodocDatabase.databaseWriteExecutor.execute(() -> taskDao.insert(task));

    }
    public void delete(Task task){
        TodocDatabase.databaseWriteExecutor.execute(() -> taskDao.deleteTask(task));
    }

    public void update(Task task){
        TodocDatabase.databaseWriteExecutor.execute(() -> taskDao.updateTask(task));
    }
}
