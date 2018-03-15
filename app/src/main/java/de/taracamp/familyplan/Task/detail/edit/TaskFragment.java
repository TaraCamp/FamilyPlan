package de.taracamp.familyplan.Task.detail.edit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.Enums.TaskState;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class TaskFragment extends Fragment implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
	private static FirebaseManager firebaseManager = null;

	private static Task selectedTask = null;

	private Button buttonStatus = null;
	private ImageButton imageButtonChangeTaskState = null;
	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;
	private MultiSelectionSpinner multiSelectionSpinnerRelatedUsers = null;
	private Button buttonSave = null;

	//date and time options
	private TimePickerDialog timePickerDialog = null;
	private DatePickerDialog datePickerDialog = null;
	private Calendar calendar = Calendar.getInstance();
	private Calendar dateCalendar = null;
	private Calendar timeCalendar = null;

	public TaskFragment() {}

	public static TaskFragment newInstance(FirebaseManager _firebaseManager,Task task)
	{
		TaskFragment fragment = new TaskFragment();
		firebaseManager = _firebaseManager;
		selectedTask = task;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_task, container, false);

		buttonStatus = view.findViewById(R.id.button_taskdetail_status);
		imageButtonChangeTaskState = view.findViewById(R.id.imagebutton_taskdetail_statuschange);
		editTextTaskTitle = view.findViewById(R.id.edittext_taskdetail_title);
		editTextTaskDescription = view.findViewById(R.id.edittext_taskdetail_description);
		editTextTaskDate = view.findViewById(R.id.edittext_taskdetail_date);
		editTextTaskTime = view.findViewById(R.id.edittext_taskdetail_time);
		buttonSave = view.findViewById(R.id.button_taskdetail_save);

		// checck state of selected task and set user information.
		if (selectedTask.getTaskState().equals(TaskState.OPEN)) buttonStatus.setText("Offen");
		else if (selectedTask.getTaskState().equals(TaskState.WAITING)) buttonStatus.setText("Warten");
		else if (selectedTask.getTaskState().equals(TaskState.IN_PROCESS)) buttonStatus.setText("In Bearbeitung");
		else if (selectedTask.getTaskState().equals(TaskState.FINISH)) buttonStatus.setText("Abgeschlossen");

		editTextTaskTitle.setText(selectedTask.getTaskTitle());
		editTextTaskDescription.setText(selectedTask.getTaskDescription());

		editTextTaskDate.setText(selectedTask.getTaskDate());
		editTextTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
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
		editTextTaskTime.setText(selectedTask.getTaskTime());
		editTextTaskTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus)
				{
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

		// check if current user is task creator. true -> enable save button
		if (firebaseManager.appUser.getUserToken().equals(selectedTask.getTaskCreator().getUserToken())) buttonSave.setEnabled(true);

		multiSelectionSpinnerRelatedUsers = view.findViewById(R.id.multiSpinner_taskdetail_relatedUsers);
		firebaseManager.getCurrentFamilyReference().addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				multiSelectionSpinnerRelatedUsers.setItems(getRelatedUserList(dataSnapshot.getValue(Family.class).getFamilyMembers()));
				multiSelectionSpinnerRelatedUsers.setSelection(getRelatedUserList(selectedTask.getTaskRelatedUsers()));
				multiSelectionSpinnerRelatedUsers.setListener(TaskFragment.this);

				buttonSave.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view)
					{
						selectedTask.setTaskTitle(editTextTaskTitle.getText().toString());
						selectedTask.setTaskDescription(editTextTaskDescription.getText().toString());
						selectedTask.setTaskDate(editTextTaskDate.getText().toString());
						selectedTask.setTaskTime(editTextTaskTime.getText().toString());

						// related users

						if (firebaseManager.saveObject(selectedTask))
							Message.show(getActivity().getApplicationContext(),"Aufgabe wurde aktualisiert!", Message.Mode.SUCCES);
						else
							Message.show(getActivity().getApplicationContext(),"Aufgabe konnte nicht aktualisiert!", Message.Mode.ERROR);
					}
				});
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		imageButtonChangeTaskState.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				TaskStateActionDialog dialog = TaskStateActionDialog.newInstance(firebaseManager,selectedTask);
				dialog.show(getActivity().getFragmentManager(),"taskstateaction");
			}
		});

		return view;
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

	@Override
	public void selectedIndices(List<Integer> indices) {}

	@Override
	public void selectedStrings(List<String> strings) {}
}
