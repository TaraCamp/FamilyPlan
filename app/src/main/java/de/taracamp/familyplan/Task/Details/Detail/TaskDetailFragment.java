package de.taracamp.familyplan.Task.Details.Detail;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.TasksActivity;

public class TaskDetailFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
	private static String TAG = "familyplan.debug";

	private static final String TASK_KEY = "TASK_KEY";

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

	private String state = null; // Aufgaben Status
	private String taskKey = null; // Aufgaben Token

	private static FirebaseManager firebaseManager = null;

	public TaskDetailFragment()
	{
		// Required empty public constructor
	}

	/**
	 * Ein neues Fragment von Typ TaskDetailFragment wird erstellt.
	 */
	public static TaskDetailFragment newInstance(String _taskKey, FirebaseManager _firebaseManager)
	{
		TaskDetailFragment fragment = new TaskDetailFragment();

		Bundle args = new Bundle();
		args.putString(TASK_KEY,_taskKey);
		fragment.setArguments(args);

		firebaseManager = _firebaseManager;

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null)
		{
			taskKey = getArguments().getString(TASK_KEY);
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
				Intent intent = new Intent(getActivity().getApplicationContext(),TasksActivity.class);
				startActivity(AppUserManager.setAppUser(intent,firebaseManager.appUser));
			}
		});

		this.Firebase();

		return view;
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	private void Firebase()
	{
		// ./families/<token>/familyTasks/<token>/ -> get task
		this.firebaseManager.getCurrentTask(taskKey).addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				// Gibt die aktuelle Aufgabe zurück.
				Task task = dataSnapshot.getValue(Task.class);

				// Prüfft ob die Aufgabe vorhanden ist.
				if (task!=null)
				{
					// Füllt die Views mit Inhalt aus der Aufgabe.
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
		// ./families/<token>/ -> get family
		this.firebaseManager.getCurrentFamilyReference().addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				// Gibt die aktuelle Familie zurück
				Family family = dataSnapshot.getValue(Family.class);

				// Gibt die aktuellen Familien Mitglieder zurück.
				memberList = family.getFamilyMembers(); // Alle Member werden zwischengespeichert.


				multiSelectionSpinnerTaskRelatedUsers.setItems(getRelatedUserList(memberList));
				multiSelectionSpinnerTaskRelatedUsers.setSelection(getRelatedUserList(_task.getTaskRelatedUsers()));
				multiSelectionSpinnerTaskRelatedUsers.setListener(TaskDetailFragment.this);

				buttonUpdateTask.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v)
					{
						if (update(_task))
						{

						}
						else
						{
							Message.show(getContext().getApplicationContext(),"Die Aufgabe konnte nicht geändert werden.", Message.Mode.WARNING);
						}
					}
				});
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		this.editTextTaskCreator.setText(_task.getTaskCreator().getUserName());
		this.editTextTaskTitle.setText(_task.getTaskTitle());
		this.editTextTaskDescription.setText(_task.getTaskDescription());

		this.editTextTaskDate.setText(_task.getTaskDate());
		this.setDateListener();
		this.editTextTaskTime.setText(_task.getTaskTime());
		this.setTimeListener();

		if (_task.getTaskCreator().getUserToken().equals(firebaseManager.appUser.getUserToken())) enableViews(true);
		else enableViews(false);
	}

	private void setTimeListener()
	{
		this.editTextTaskTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus)
				{
					Log.d(TAG,":TaskAddActivity.onFocusChange() -> open time dialog");

					timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute)
						{
							timeCalendar = Calendar.getInstance();
							timeCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
							timeCalendar.set(Calendar.MINUTE,minute);

							editTextTaskTime.setText(DateUtils.formatDateTime(getContext(),timeCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_TIME));
						}

					},calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
					timePickerDialog.show();
				}
			}
		});
	}

	private void setDateListener()
	{
		this.editTextTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus)
				{
					Log.d(TAG,":TaskAddActivity.onFocusChange() -> open date dialog");

					datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
						{
							dateCalendar = Calendar.getInstance();
							dateCalendar.set(Calendar.YEAR,year);
							dateCalendar.set(Calendar.MONTH,month);
							dateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

							editTextTaskDate.setText(DateUtils.formatDateTime(getContext(),dateCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
						}

					},calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
					datePickerDialog.show();
				}
			}
		});
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
			Task updateTask = this.createTask(_task);
			this.firebaseManager.currentTaskReference.setValue(updateTask);
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
		//// TODO: 01.11.2017 Beim Ändern tritt ein Fehler manchmal auf.
		updateTask.setTaskRelatedUsers(listRel);

		updateTask.setTaskTitle(editTextTaskTitle.getText().toString()); // Setze Aufgabentitel
		updateTask.setTaskDescription(editTextTaskDescription.getText().toString()); // Setze Aufgabenbeschreibung

		updateTask.setTaskCreatedOn(DateUtils.formatDateTime(getContext(),calendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
		if (dateCalendar!=null) _task.setTaskDate(editTextTaskDate.getText().toString()); // Setzt Datum als String
		if (timeCalendar!=null) _task.setTaskTime(editTextTaskTime.getText().toString()); // Setzt Time als String

		return updateTask;
	}

	private void enableViews(boolean _enable)
	{
		//this.spinnerTaskState.setEnabled(_enable);
		this.editTextTaskTitle.setEnabled(_enable);
		this.editTextTaskDescription.setEnabled(_enable);
		this.editTextTaskDate.setEnabled(_enable);
		this.editTextTaskTime.setEnabled(_enable);
	}
}
