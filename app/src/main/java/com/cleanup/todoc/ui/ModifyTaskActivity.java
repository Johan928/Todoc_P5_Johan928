package com.cleanup.todoc.ui;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.viewmodels.TaskViewModel;

import java.util.List;


public class ModifyTaskActivity extends AppCompatActivity {

    public static final String TASK_NAME = "com.cleanup.todoc.TASK_NAME";
    public static final String PROJECT_NAME = "com.cleanup.todoc.PROJECT_NAME";
    public static final String TASK_ID = "com.cleanup.todoc.TASK_ID";

    EditText editText;
    Spinner spinner;
    Button buttonValidate;
    Button buttonCancel;
    List<Project> allProjects;
    Long taskID;
    Task currentTask;
    TaskViewModel taskViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_task);
        editText = findViewById(R.id.txt_task_name);
        spinner = findViewById(R.id.project_spinner);
        buttonValidate = findViewById(R.id.modify_task_button);
        buttonCancel = findViewById(R.id.cancel_button);

        allProjects = Project.projectsList;
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        if (getIntent() != null) {

            buttonValidate.setVisibility(View.VISIBLE);
            Intent intent = getIntent();
            if (intent.hasExtra(TASK_NAME)) {
                editText.setText(intent.getStringExtra(TASK_NAME));
            }

            final ArrayAdapter<Project> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, allProjects);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            String projectName = "";
            if (intent.hasExtra(PROJECT_NAME)) {
                projectName = intent.getStringExtra(PROJECT_NAME);
                setSpinnerPosition(projectName, adapter);
            }

            if (intent.hasExtra(TASK_ID)) {
                taskID = intent.getLongExtra(TASK_ID, 0L);

                for (Task task : Task.tasksList) {
                    if (task.getId() == taskID) {
                        currentTask = task;

                        break;
                    }
                }
            }
            buttonValidate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTask != null) {
                        Project project = Project.getProjectByName(spinner.getSelectedItem().toString());
                        currentTask.setName(editText.getText().toString());
                        currentTask.setProjectId(project.getId());
                        taskViewModel.update(currentTask);
                        Toast.makeText(v.getContext(), "Task Updated.", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(v.getContext(), "Error updating the Task!!!", Toast.LENGTH_SHORT).show();

                    finish();
                }
            });
            buttonCancel.setOnClickListener(v -> {
                Toast.makeText(v.getContext(), "Operation canceled by user.", Toast.LENGTH_SHORT).show();
                finish();
            });

        }

    }

    private void setSpinnerPosition(String projectName, ArrayAdapter<Project> adapter) {
        Project project = null;
        if (allProjects != null) {
            for (Project project1 : allProjects) {
                if (project1.getName().equals(projectName.toString())) {
                    project = project1;
                    break;
                }
            }
            if (project != null) {
                spinner.setSelection(adapter.getPosition(project));
            }
        } else {
            Log.d(TAG, "onCreate: NULL");
        }
    }


}
