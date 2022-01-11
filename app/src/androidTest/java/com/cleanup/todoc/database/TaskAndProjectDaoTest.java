package com.cleanup.todoc.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class TaskAndProjectDaoTest {

    // DATA SET FOR TEST
    private static final Task TASK_DEMO = new Task(1L, "TEST", new Date().getTime());
    private static final Task TASK_DEMO_2 = new Task(2L, "TEST2", new Date().getTime());
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    // FOR DATA
    private TodocDatabase database;

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                TodocDatabase.class)
                .allowMainThreadQueries()
                .build();
        fillProjectTable();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    /**
    * TESTING PROJECT TABLE

     */
    private void fillProjectTable() {
        Project project1 = new Project(1L, "Projet Tartampion", 0xFFEADAD1);
        Project project2 = new Project(2L, "Projet Lucidia", 0xFFB4CDBA);
        Project project3 = new Project(3L, "Projet Circus", 0xFFA3CED2);
        this.database.projectDao().insert(project1);
        this.database.projectDao().insert(project2);
        this.database.projectDao().insert(project3);
    }
    @Test
    public void deleteAndGetAllProjects() throws InterruptedException {
        List<Project> allProjects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects1());

        assertEquals(3,allProjects.size());
        this.database.projectDao().deleteAll();
        allProjects = LiveDataTestUtil.getValue(this.database.projectDao().getAllProjects1());
        assertEquals(0,allProjects.size());
        fillProjectTable(); // used to tests the following tasks
    }

    @Test
    public void insertAndGetTask() throws InterruptedException {
        // BEFORE : Adding a new task
        this.database.taskDao().insert(TASK_DEMO);
        // TEST
        Task task = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks()).get(0);
        assertTrue(task.getName().equals("TEST"));
    }

    @Test
    public void getTasksWhenNoTaskInserted() throws InterruptedException {
        // TEST
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertTrue(tasks.isEmpty());
    }

    @Test
    public void insertTaskAndDeleteSingleTask() throws InterruptedException {
        this.database.taskDao().insert(TASK_DEMO);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(1, tasks.size());
        this.database.taskDao().deleteTask(tasks.get(0));
        tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(0, tasks.size());
    }

    @Test
    public void insertTasksAndDeleteAllTasks() throws InterruptedException {
        this.database.taskDao().insert(TASK_DEMO);
        this.database.taskDao().insert(TASK_DEMO_2);
        List<Task> tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(2,tasks.size());
        this.database.taskDao().deleteAll();
        tasks = LiveDataTestUtil.getValue(this.database.taskDao().getAllTasks());
        assertEquals(0,tasks.size());


    }
}