package com.cleanup.todoc.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.model.Project;

import java.util.List;

@Dao
public interface ProjectDao {

    @Query("SELECT * FROM project_table")
    LiveData<List<Project>> getAllProjects1();

    @Insert
    void insert(Project project);

    @Query("DELETE FROM project_table")
    void deleteAll();


}
