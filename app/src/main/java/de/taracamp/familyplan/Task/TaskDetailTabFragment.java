package de.taracamp.familyplan.Task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class TaskDetailTabFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
	private static String TAG = "familyplan.debug";

	private static final String TASK_KEY = "TASK_KEY";
	private static final String FAMILY_KEY = "FAMILY_KEY";

	private ImageView imageViewTaskHeader = null;
	private Spinner spinnerTaskState = null;
	private EditText editTextTaskCreator = null;
	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;
	private MultiSelectionSpinner multiSelectionSpinnerTaskRelatedUsers = null;
	private Button buttonUpdateTask = null;
	private Button buttonCancel = null;

	private ArrayAdapter<CharSequence> adapter = null;

	private TimePickerDialog timePickerDialog = null;
	private DatePickerDialog datePickerDialog = null;

	private Calendar calendar = Calendar.getInstance();
	private Calendar dateCalendar = null;
	private Calendar timeCalendar = null;

	private List<String> selectedUsersAsString = null;
	List<User> memberList = null;
	List<User> listRel = null;

	private String state = null;
	private String taskKey = null;
	private String familyToken = null;

	private FirebaseManager firebaseManager = null;

	public TaskDetailTabFragment()
	{
		// Required empty public constructor
	}

	public static TaskDetailTabFragment newInstance(String _taskKey,String _familyToken)
	{
		TaskDetailTabFragment fragment = new TaskDetailTabFragment();

		Bundle args = new Bundle();
		args.putString(TASK_KEY,_taskKey);
		args.putString(FAMILY_KEY,_familyToken);
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null)
		{
			taskKey = getArguments().getString(TASK_KEY);
			familyToken = getArguments().getString(FAMILY_KEY);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_task_detail_tab, container, false);

		this.imageViewTaskHeader = (ImageView) view.findViewById(R.id._imageView_detail);
		this.spinnerTaskState = (Spinner) view.findViewById(R.id._input_task_detail_taskStatus);
		this.editTextTaskCreator = (EditText) view.findViewById(R.id._input_task_detail_taskCreator);
		this.editTextTaskTitle = (EditText) view.findViewById(R.id._input_task_detail_taskTitle);
		this.editTextTaskDescription = (EditText) view.findViewById(R.id._input_task_detail_taskDescription);
		this.editTextTaskDate = (EditText) view.findViewById(R.id._input_task_detail_taskDate);
		this.editTextTaskTime = (EditText) view.findViewById(R.id._input_task_detail_taskTime);
		this.multiSelectionSpinnerTaskRelatedUsers = (MultiSelectionSpinner) view.findViewById(R.id._input_task_detail_taskRelatedUsers);
		this.buttonUpdateTask = (Button) view.findViewById(R.id._button_task_detail_updateTask);
		this.buttonCancel = (Button) view.findViewById(R.id._button_task_detail_closeDialog);
		this.buttonCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				//Intent intent = new Intent(getActivity().getApplicationContext(),TaskListActivity.class);
				//startActivity(intent);
			}
		});

		this.Firebase();

		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private void Firebase()
	{
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.currentTasksReference = this.firebaseManager.tasks(this.firebaseManager.families().child(familyToken));
		this.firebaseManager.currentTaskReference = this.firebaseManager.currentTasksReference.child(taskKey);
		this.firebaseManager.currentTaskReference.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				Task task = dataSnapshot.getValue(Task.class);

				if (task!=null)
				{
					fillViews(task);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	public static String[] getRelatedUserList(List<User> _members)
	{
		String[] array = new String[_members.size()];

		for (int i = 0;i<array.length;i++)
		{
			array[i] = _members.get(i).getUserName();
		}

		return array;

	}

	private void fillViews(final Task _task)
	{
		this.firebaseManager.currentFamilyReference = this.firebaseManager.families().child(familyToken);
		this.firebaseManager.currentFamilyReference.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				Family family = dataSnapshot.getValue(Family.class);

				memberList = family.getFamilyMembers(); // Alle Member werden zwischengespeichert.

				multiSelectionSpinnerTaskRelatedUsers.setItems(getRelatedUserList(family.getFamilyMembers()));
				multiSelectionSpinnerTaskRelatedUsers.setSelection(getRelatedUserList(_task.getTaskRelatedUsers()));
				multiSelectionSpinnerTaskRelatedUsers.setListener(TaskDetailTabFragment.this);

				buttonUpdateTask.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v)
					{
						if (update(_task))
						{
							Message.show(getContext().getApplicationContext(),"Die Aufgabe wurde geändert!","INFO");
						}
						else
						{
							Message.show(getContext().getApplicationContext(),"Die Aufgabe konnte nicht geändert werden.","ERROR");
						}
					}
				});
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		this.loadHeader(_task.getTaskState());
		this.loadSpinner(_task.getTaskState());

		this.editTextTaskCreator.setText(_task.getTaskCreator().getUserName());
		this.editTextTaskTitle.setText(_task.getTaskTitle());
		this.editTextTaskDescription.setText(_task.getTaskDescription());
		this.editTextTaskDate.setText(_task.getTaskDate());
		this.editTextTaskTime.setText(_task.getTaskTime());
	}

	private void loadSpinner(String _taskState)
	{
		adapter = ArrayAdapter.createFromResource(this.getContext(),R.array.state,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTaskState.setAdapter(adapter);

		if (_taskState.equals("OPEN")) spinnerTaskState.setSelection(0);
		else if (_taskState.equals("IN_PROCESS")) spinnerTaskState.setSelection(1);
		else if (_taskState.equals("FINISH")) spinnerTaskState.setSelection(2);

		spinnerTaskState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position==0)
				{
					state = "OPEN";
				}
				else if (position==1)
				{
					state = "IN_PROCESS";
				}
				else if (position==2)
				{
					state = "FINISH";
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
	}

	private void loadHeader(String taskState)
	{
		if (taskState.equals("FINISH"))
		{
			this.imageViewTaskHeader.setImageResource(R.drawable.ic_action_finish);
			this.imageViewTaskHeader.setBackgroundColor(Color.argb(255,204,255,153));
		}
		else if (taskState.equals("IN_PROCESS"))
		{
			this.imageViewTaskHeader.setImageResource(R.drawable.ic_action_in_process);
			this.imageViewTaskHeader.setBackgroundColor(Color.argb(255,255,253,175));
		}
	}

	@Override
	public void selectedIndices(List<Integer> indices) {}

	@Override
	public void selectedStrings(List<String> strings) {}

	private boolean update(Task _task)
	{
		if (valid())
		{
			this.firebaseManager.currentTaskReference.setValue(createTask(_task));
			return true;
		}
		else
		{
			return false;
		}
	}

	private boolean valid()
	{
		return true;
	}

	private Task createTask(Task _task)
	{
		Task updateTask = _task;

		//// TODO: 27.09.2017  Workaround für die Auswahl der Benutzer!!
		//List<User> allUsers = family.getFamilyMembers();
		if (selectedUsersAsString==null)
		{
			selectedUsersAsString = new ArrayList<>();
			selectedUsersAsString.add(multiSelectionSpinnerTaskRelatedUsers.getSelectedItemsAsString());
		}

		listRel = new ArrayList<>();

		for(User user : memberList)
		{
			for(String s : selectedUsersAsString)
			{
				if (user.getUserName().equals(s)) listRel.add(user);
			}
		}

		updateTask.setTaskRelatedUsers(listRel);
		updateTask.setTaskTitle(editTextTaskTitle.getText().toString()); // Setze Aufgabentitel
		updateTask.setTaskDescription(editTextTaskDescription.getText().toString()); // Setze Aufgabenbeschreibung
		updateTask.setTaskState(state);
		//updateTask.setTaskCreatedOn(DateUtils.formatDateTime(TaskAddActivity.this,calendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
		//if (dateCalendar!=null) task.setTaskDate(editTextTaskDate.getText().toString()); // Setzt Datum als String
		//if (timeCalendar!=null) task.setTaskTime(editTextTaskTime.getText().toString()); // Setzt Time als String

		return updateTask;
	}

	private void enableViews(boolean _enable)
	{
		this.spinnerTaskState.setEnabled(_enable);
		this.editTextTaskTitle.setEnabled(_enable);
		this.editTextTaskDescription.setEnabled(_enable);
		this.editTextTaskDate.setEnabled(_enable);
		this.editTextTaskTime.setEnabled(_enable);
	}
}
