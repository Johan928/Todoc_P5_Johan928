package com.cleanup.todoc.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final LiveData<List<Task>> allTasks;
    private final TaskRepository taskRepository;

    public TaskViewModel(Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllTasks();
    }

    public void insert(Task task) {
        taskRepository.insert(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

}
