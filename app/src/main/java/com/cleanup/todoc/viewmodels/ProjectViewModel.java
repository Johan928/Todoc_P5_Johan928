package com.cleanup.todoc.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.repository.ProjectRepository;

import java.util.List;

public class ProjectViewModel extends AndroidViewModel {

    private final LiveData<List<Project>> allProjects;
    private final ProjectRepository repository;

    public ProjectViewModel(Application application) {
        super(application);
        repository = new ProjectRepository(application);
        allProjects = repository.getAllProjects1();
    }

    public LiveData<List<Project>> getAllProjects1() {
        return allProjects;
    }

}
