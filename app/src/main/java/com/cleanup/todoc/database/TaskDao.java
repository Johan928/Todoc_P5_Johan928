package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.cleanup.todoc.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();

    @Delete
    void deleteTask(Task task);

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();
}
