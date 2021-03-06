package example.codeclan.com.todolist.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import example.codeclan.com.todolist.Adapters.TaskListAdapter;
import example.codeclan.com.todolist.Models.Task;
import example.codeclan.com.todolist.Models.TaskList;
import example.codeclan.com.todolist.R;

public class TaskListActivity extends AppCompatActivity {

    private TaskListAdapter taskListAdapter;
    private TaskList tasksToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        // JSON STRING
        String currentlySavedTasks = sharedPref.getString("AllTasks", new ArrayList<Task>().toString());

        Log.d("All tasks", currentlySavedTasks);

        //gson setup

        Gson gson = new Gson();
        TypeToken<ArrayList<Task>> taskArrayListToken = new TypeToken<ArrayList<Task>>() {
        };

        //USING the Json string to put into gson ARRAYLIST
        ArrayList<Task> gsonTasks = gson.fromJson(currentlySavedTasks, taskArrayListToken.getType());

//        TaskList tasksToShow = new TaskList(gsonTasks);
        tasksToShow = new TaskList(gsonTasks);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {

            Bundle extras = intent.getExtras();
            String filter = extras.getString("filter");

            switch (filter) {
                case "priority":
                    tasksToShow = tasksToShow.outstandingPriorityTasks();
                    break;
                case "outstanding":
                    tasksToShow = tasksToShow.outstandingTasks();
                    break;
                case "completed":
                    tasksToShow = tasksToShow.completedTasks();
                    break;
                case "all":
                    break;
            }
        }
        else {
                tasksToShow = tasksToShow.outstandingTasks();
            }

        taskListAdapter = new TaskListAdapter(this, tasksToShow.getList());


        ListView listView = findViewById(R.id.task_list_view);
        listView.setAdapter(taskListAdapter);
//        taskListAdapter.notifyDataSetChanged();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.task_list_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_task) {
            addTask();
            return true;
        }

        if (item.getItemId() == R.id.completed_tasks) {
            completedTasks();
            return true;
        }

        if (item.getItemId() == R.id.priority_tasks) {
            priorityTasks();
            return true;
        }

        if (item.getItemId() == R.id.outstanding_tasks) {
            outstandingTasks();
            return true;
        }

        if (item.getItemId() == R.id.all_tasks) {
            allTasks();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void completedTasks() {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter", "completed");
        startActivity(intent);
    }

    private void priorityTasks() {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter", "priority");

        startActivity(intent);
//        tasksToShow = tasksToShow.outstandingPriorityTasks();
//
//        taskListAdapter.notifyDataSetChanged();

    }

    private void outstandingTasks() {

        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter", "outstanding");
        startActivity(intent);
    }

    private void allTasks() {
        Intent intent = new Intent(this, TaskListActivity.class);
        intent.putExtra("filter", "all");
        startActivity(intent);
    }

    public void addTask(){
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }

    public void addTaskStart(View view){
        addTask();

    }

    public void getTask(View listItem){

        int taskIndex  = (int) listItem.getTag();

        Log.d(getClass().toString(), "Task Index" + taskIndex + " is selected");

        Intent intent = new Intent(this, EditTaskActivity.class);

        intent.putExtra("task_index", taskIndex);

        startActivity(intent);


    }

}





