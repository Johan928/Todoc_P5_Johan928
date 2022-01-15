package com.cleanup.todoc.ui;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cleanup.todoc.R;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.Calendar;
import java.util.Locale;

/**
 * <p>Adapter which handles the list of tasks to display in the dedicated RecyclerView.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class TasksAdapter extends ListAdapter<Task, TasksAdapter.TaskViewHolder> {

    public static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getName().equals(newItem.getName()) && oldItem.getProjectId() == newItem.getProjectId() && oldItem.getId() == newItem.getId();
        }

    };
    /**
     * The listener for when a task needs to be deleted
     */

    private DeleteTaskListener deleteTaskListener;

    /**
     * Instantiates a new TasksAdapter.
     * <p>
     * // * @param tasks the list of tasks the adapter deals with to set
     */
    public TasksAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task, viewGroup, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int position) {
        Task currentTask = getItem(position);
        taskViewHolder.bind(currentTask);
    }

    public void setOnDeleteTaskListener(DeleteTaskListener deleteTaskListener) {
        this.deleteTaskListener = deleteTaskListener;
    }

    /**
     * Listener for deleting tasks
     * /*
     */
    public interface DeleteTaskListener {
        void onDeleteTask(Task task);
    }

    /**
     * <p>ViewHolder for task items in the tasks list</p>
     *
     * @author Gaëtan HERFRAY
     */
    class TaskViewHolder extends RecyclerView.ViewHolder {
        public static final String TASK_NAME = "com.cleanup.todoc.TASK_NAME";
        public static final String PROJECT_NAME = "com.cleanup.todoc.PROJECT_NAME";
        public static final String TASK_ID = "com.cleanup.todoc.TASK_ID";

        /**
         * The circle icon showing the color of the project
         */
        private final AppCompatImageView imgProject;

        /**
         * The TextView displaying the name of the task
         */
        private final TextView lblTaskName;

        /**
         * The TextView displaying the name of the project
         */
        private final TextView lblProjectName;

        /**
         * The delete icon
         */
        private final AppCompatImageView imgDelete;

        private final TextView lblTaskDate;

        /**
         * Instantiates a new TaskViewHolder.
         *
         * @param itemView the view of the task item
         *                 //  * @param deleteTaskListener the listener for when a task needs to be deleted to set
         *                 //  * @param deleteTaskListener
         */
        TaskViewHolder(@NonNull View itemView) {
            super(itemView);


            imgProject = itemView.findViewById(R.id.img_project);
            lblTaskName = itemView.findViewById(R.id.lbl_task_name);
            lblProjectName = itemView.findViewById(R.id.lbl_project_name);
            imgDelete = itemView.findViewById(R.id.img_delete);
            lblTaskDate = itemView.findViewById(R.id.textView_date_in_circle);


            imgDelete.setOnClickListener(view -> {
                final Object tag = view.getTag();
                if (tag instanceof Task) {

                    if (deleteTaskListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        deleteTaskListener.onDeleteTask(getItem(getAdapterPosition()));
                    }
                }
            });
            lblTaskName.setOnLongClickListener(v -> {

                Intent intent = new Intent(v.getContext(), ModifyTaskActivity.class);
                intent.putExtra(TASK_NAME, lblTaskName.getText());
                intent.putExtra(PROJECT_NAME, lblProjectName.getText());
                intent.putExtra(TASK_ID, getItem(getAdapterPosition()).getId());
                v.getContext().startActivity(intent);

                return true;
            });
        }


        /**
         * Binds a task to the item view.
         *
         * @param task the task to bind in the item view
         */
        void bind(Task task) {
            lblTaskName.setText(task.getName());

            lblTaskDate.setText(getFormattedDate(task.getCreationTimestamp()));

            imgDelete.setTag(task);

            final Project taskProject = task.getProjectId();
            if (taskProject != null) {
                imgProject.setSupportImageTintList(ColorStateList.valueOf(taskProject.getColor()));
                lblProjectName.setText(taskProject.getName());
            } else {
                imgProject.setVisibility(View.INVISIBLE);
                lblProjectName.setText("");
            }


        }

        private String getFormattedDate(long time) {
            Calendar cal = Calendar.getInstance(Locale.FRENCH);
            cal.setTimeInMillis(time);
            //Log.d(TAG, "getDate: " + cal.getTime().toString());
            return DateFormat.format("dd/MM\nyyyy", cal).toString();
        }
    }
}
