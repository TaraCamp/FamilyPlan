package de.taracamp.familyplan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;

public class TaskActivity extends AppCompatActivity {
    private static final String TASG = "familyplan.debug";

   List<Task> TaskList;

    private Button buttonAddTask;

    private void init(){
        buttonAddTask = (Button) findViewById(R.id.button_task_addTask);
        buttonAddTask.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AddTask("Erste Aufgabe");
            }
        });

        this.TaskList = new ArrayList<>();
    }

    private void AddTask(String _taskName){
        Task newTask = new Task(_taskName,new User("wowa","wowa@tarasov"));
        this.TaskList.add(newTask);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

    }
}
