package de.taracamp.familyplan.Task;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;


public class TaskDetailFragment extends Fragment
{
	private static final String TAG = "familyplan.debug";

	public static final String ARG_ITEM_ID = "item_id";
	public static final String TASK_KEY = "TASK_KEY";
	public static final String FAMILY_KEY = "FAMILY_KEY";

	private ImageView imageViewDetailHeader = null;
	private EditText editTextTaskCreator = null;
	private EditText editTextTaskStatus = null;
	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;
	private MultiSelectionSpinner multiSelectionSpinnerRelatedUser = null;
	private Button buttonTaskUpdate = null;
	private Button buttonTaskCancel = null;

	private FirebaseDatabase database = null;
	private DatabaseReference tasksReference = null;

	private Task task = null;
	private Task updateTask = null;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TaskDetailFragment(){}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		this.loadTask();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		final View rootView = inflater.inflate(R.layout.activity_task_detail, container, false);

		this.imageViewDetailHeader = (ImageView) rootView.findViewById(R.id.imageView_detail);
		this.editTextTaskCreator = (EditText) rootView.findViewById(R.id.input_task_detail_taskCreator);
		this.editTextTaskStatus = (EditText) rootView.findViewById(R.id.input_task_detail_taskStatus);
		this.editTextTaskTitle = (EditText) rootView.findViewById(R.id.input_task_detail_taskTitle);
		this.editTextTaskDescription = (EditText) rootView.findViewById(R.id.input_task_detail_taskDescription);
		this.editTextTaskDate = (EditText) rootView.findViewById(R.id.input_task_detail_taskDate);
		this.editTextTaskTime = (EditText) rootView.findViewById(R.id.input_task_detail_taskTime);
		this.multiSelectionSpinnerRelatedUser = (MultiSelectionSpinner) rootView.findViewById(R.id.input_task_detail_taskRelatedUsers);
		this.buttonTaskUpdate = (Button) rootView.findViewById(R.id.button_task_detail_updateTask);
		this.buttonTaskUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskDetailFragment.onClick -> update task");

				updateTask.setTaskTitle(editTextTaskTitle.getText().toString());
				updateTask.setTaskDescription(editTextTaskDescription.getText().toString());
				updateTask.setTaskState(editTextTaskStatus.getText().toString());

				tasksReference.child(task.getTaskToken()).setValue(updateTask);

				Message.show(rootView.getContext(),"Aufgabe wurde aktualisiert!","INFO");
			}
		});
		this.buttonTaskCancel = (Button) rootView.findViewById(R.id.button_task_detail_closeDialog);
		this.buttonTaskCancel.setVisibility(View.GONE);

		return rootView;
	}

	private void loadTask()
	{
		final String task_key = getArguments().getString("TASK_KEY");
		final String family_key = getArguments().getString("FAMILY_KEY");

		Log.d(TAG,":TaskDetailFragment.onCreate with: " + task_key);

		this.database = FirebaseDatabase.getInstance();

		this.tasksReference = this.database.getReference("families").child(family_key).child("familyTasks").getRef();
		this.tasksReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				Log.d(TAG,":TaskDetailFragment.readDatabase() -> onDataChange with id=" + task_key);

				DataSnapshot taskSnap = dataSnapshot.child(task_key);
				task = taskSnap.getValue(Task.class);

				updateTask = task;

				//enableViews("Wowa",task.getTaskCreator().getUserName());

				fillViews(task);
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

	}

	private void fillViews(Task _task)
	{
		if (_task.getTaskState().equals("FINISH"))
		{
			this.imageViewDetailHeader.setImageResource(R.drawable.ic_action_finish);
			this.imageViewDetailHeader.setBackgroundColor(Color.argb(255,204,255,153));
		}
		else if (_task.getTaskState().equals("IN_PROCESS"))
		{
			this.imageViewDetailHeader.setImageResource(R.drawable.ic_action_in_process);
			this.imageViewDetailHeader.setBackgroundColor(Color.argb(255,255,253,175));
		}

		this.editTextTaskTitle.setText(_task.getTaskTitle());
		this.editTextTaskDescription.setText(_task.getTaskDescription());
		this.editTextTaskDate.setText(_task.getTaskDate());
		this.editTextTaskTime.setText(_task.getTaskTime());
		this.editTextTaskCreator.setText(_task.getTaskCreator().getUserName());
		this.editTextTaskStatus.setText(_task.getTaskState());
	}

	private void enableViews(String _loginUser, String _creator)
	{
		if (_loginUser.equals(_creator))
		{
			this.editTextTaskTitle.setEnabled(true);
			this.editTextTaskDescription.setEnabled(true);
			this.editTextTaskDate.setEnabled(true);
			this.editTextTaskTime.setEnabled(true);
		}
	}
}
