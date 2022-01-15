package com.cleanup.todoc.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.database.ProjectDao;
import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.model.Project;

import java.util.List;

public class ProjectRepository {
    private LiveData<List<Project>> allProjects;
    private ProjectDao projectDao;

    public ProjectRepository(Application application) {
        TodocDatabase db = TodocDatabase.getINSTANCE(application);
        projectDao = db.projectDao();
        allProjects = projectDao.getAllProjects1();
    }

    public LiveData<List<Project>> getAllProjects1(){return allProjects;}



}
