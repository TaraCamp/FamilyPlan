/**
 * @file TaskAddActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Family.FamilyActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Enums.TaskState;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.HistoryManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.Notifications.AppNotification;
import de.taracamp.familyplan.R;

/**
 * Diese Activity wird genutzt eine neue Aufgabe zu erstellen. Folgende Felder sind anzugeben:
 *
 * - Aufgabentitel: Kurzbeschreibung der Aufgabe (Pflicht)
 * - Aufgabenbeschreibung: (Optional)
 * - Aufgabendatum: Wird bei nicht auswahl automatisch auf den nächsten Tag gesetzt.
 * - Aufgabenuhrzeit: (Optional)
 * - Aufgaben Verantwrtliche: Mindestens einer muss gewählt werden. (Pflicht)
 * - Weitere Angaben -> ....
 *
 */
public class TaskAddActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "TaskAddActivity";

	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;
	private MultiSelectionSpinner mSpinnerRelatedUsers = null;

	private Toolbar toolbar;

	private TimePickerDialog timePickerDialog = null;
	private DatePickerDialog datePickerDialog = null;

	private Calendar calendar = Calendar.getInstance();
	private Calendar dateCalendar = null;
	private Calendar timeCalendar = null;

	private List<String> selectedUsersAsString = null;

	private FirebaseManager firebaseManager = null;

	private Family currentFamily;

	private int year;
	private int month;
	private int day;
	private String time;

	AppNotification notification = null;

	@Override
	protected void onCreate(@Nullable Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.dialog_task_add);

		Log.d(TAG,CLASS+".onCreate()");

		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());
		if (firebaseManager.appUser.isHasFamily())
		{
			Log.d(TAG,CLASS+": has family");

			initializeViews();
			getFamily();
		}
		else
		{
			Log.d(TAG,CLASS+": has no family!");

			openNoFamilyDialog();
		}
	}

	@Override
	protected void onStart()
	{
		super.onStart();

		initializeDate();
		getIntentDate();

		Log.d(TAG,CLASS+".onStart() -> load time = " + year + "." + month + "." + day + ": " + time);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_event_add,menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem _item)
	{
		if (_item.getItemId()==R.id.item_event_add)
		{
			Log.d(TAG,CLASS+".onClick() -> add task");

			saveTask();
		}
		else if(_item.getItemId()==R.id.item_event_add_cancel)
		{
			Log.d(TAG,CLASS+".onClick() -> cancel");

			cancel();
		}

		return true;
	}

	private void cancel()
	{
		Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
		intent.putExtra("USER",firebaseManager.appUser);
		startActivity(intent);
	}

	private void getIntentDate()
	{
		Intent intent = getIntent();
		if (intent.hasExtra("YEAR"))
		{
			year = intent.getIntExtra("YEAR",0);
		}
		if (intent.hasExtra("MONTH"))
		{
			month = intent.getIntExtra("MONTH",0);
		}
		if (intent.hasExtra("DAY"))
		{
			day = intent.getIntExtra("DAY",0);
		}
		if (intent.hasExtra("TIME"))
		{
			time = intent.getStringExtra("TIME");
		}

		if (time!=null)
		{
			dateCalendar = Calendar.getInstance();
			dateCalendar.set(year,month,day);
			editTextTaskDate.setText(time);
		}
	}

	private void initializeDate()
	{
		editTextTaskDate = (EditText) findViewById(R.id.input_task_add_taskDate);
		editTextTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					Log.d(TAG,CLASS+" -> open date dialog");

					datePickerDialog = new DatePickerDialog(TaskAddActivity.this, new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
						{
							dateCalendar = Calendar.getInstance();
							dateCalendar.set(Calendar.YEAR,year);
							dateCalendar.set(Calendar.MONTH,month);
							dateCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

							editTextTaskDate.setText(DateUtils.formatDateTime(TaskAddActivity.this,dateCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
						}
					},calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
					datePickerDialog.show();
				}
			}
		});
	}

	private void initializeViews()
	{
		editTextTaskTitle = (EditText) findViewById(R.id.input_task_add_taskTitle);
		editTextTaskDescription = (EditText) findViewById(R.id.input_task_add_taskDescription);
		editTextTaskTime = (EditText) findViewById(R.id.input_task_add_taskTime);
		mSpinnerRelatedUsers = (MultiSelectionSpinner) findViewById(R.id.multiSpinner_task_add_relatedUsers);

		initializeToolbar();
	}

	public void initializeToolbar()
	{
		toolbar = (Toolbar) findViewById(R.id.toolbar_add);
		setSupportActionBar(toolbar);

		getSupportActionBar().setTitle("Aufgabe Erstellen");
		getSupportActionBar().setLogo(R.drawable.ic_action_owntasks);
	}

	private void initializeEvents()
	{
		editTextTaskTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus)
				{
					Log.d(TAG,":TaskAddActivity.onFocusChange() -> open time dialog");

					timePickerDialog = new TimePickerDialog(TaskAddActivity.this, new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay, int minute)
						{
							timeCalendar = Calendar.getInstance();
							timeCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
							timeCalendar.set(Calendar.MINUTE,minute);

							editTextTaskTime.setText(DateUtils.formatDateTime(TaskAddActivity.this,timeCalendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_TIME));
						}

					},calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(TaskAddActivity.this));
					timePickerDialog.show();
				}
			}
		});
	}

	private void getFamily()
	{
		// ./families/<token>
		firebaseManager.getCurrentFamilyReference().addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				currentFamily = dataSnapshot.getValue(Family.class);

				Log.d(TAG,CLASS+": found family with token: " + currentFamily.getFamilyToken());

				initializeEvents();
				initializeRelatedUsersSpinner();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	private void initializeRelatedUsersSpinner()
	{
		mSpinnerRelatedUsers.setItems(getRelatedUserList(currentFamily.getFamilyMembers()));
		mSpinnerRelatedUsers.setListener(TaskAddActivity.this);
	}

	private void openNoFamilyDialog()
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskAddActivity.this);
		alertDialogBuilder.setTitle("Familie Anlegen!");
		alertDialogBuilder.setIcon(R.drawable.logo);
		alertDialogBuilder
				.setMessage("Sie müssen in einer Familie sein um eine Aufgabe zu erstellen. Wollen Sie einer Familie beitreten?")
				.setCancelable(false)
				.setPositiveButton("Ja",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
						intent.putExtra("USER",firebaseManager.appUser);
						startActivity(intent);
					}
				})
				.setNegativeButton("Nein",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();

						Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
						intent.putExtra("USER",firebaseManager.appUser);
						startActivity(intent);
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
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

	/**
	 * Speichert eine neue Aufgabe in der Firebase Datenbank.
	 */
	private void saveTask()
	{
		if (isValid())
		{
			if (firebaseManager.saveObject(createTask()))
			{
				Message.show(getApplicationContext(),"Eine neue Aufgabe wurde angelegt", Message.Mode.SUCCES);
				Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		}
	}

	/**
	 * Erstellt eine neue Aufgabe. Jede Aufgabe erhält einen eindeutigen Schlüssen (_key).
	 */
	private Task createTask()
	{
		Task task = new Task();

		String taskTitle = editTextTaskTitle.getText().toString();
		String taskDescription = editTextTaskDescription.getText().toString();
		String taskToken = firebaseManager.getTasksReference().push().getKey();
		User taskCreator = AppUserManager.getUserByAppUser(firebaseManager.appUser);
		String taskFamilyToken = firebaseManager.appUser.getUserFamilyToken();
		String taskDate = editTextTaskDate.getText().toString();
		String taskTime = editTextTaskTime.getText().toString();
		TaskState taskState = TaskState.OPEN;
		List<User> taskRelatedUsers = getSelectedUsers();
		String taskCreatedOn = DateUtils.formatDateTime(TaskAddActivity.this,calendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE);

		task.setTaskTitle(taskTitle);
		task.setTaskDescription(taskDescription);
		task.setTaskToken(taskToken);
		task.setTaskCreator(taskCreator);
		task.setTaskFamilyToken(taskFamilyToken); // Aufgaben Familien Token
		task.setTaskRelatedUsers(taskRelatedUsers); // Aufgaben Benutzer festlegen
		task.setTaskState(taskState); // // TODO: 27.09.2017 muss aus enum entnommen werden.
		task.setTaskCreatedOn(taskCreatedOn);
		task.setTaskDate(taskDate); // Setzt Datum als String
		task.setTaskTime(taskTime); // Setzt Time als String

		// Eine History wird eingefügt.
		HistoryManager historyManager = new HistoryManager(task);
		historyManager.addMessage(historyManager.getMessageByNewHistory());
		task.setTaskHistory(historyManager.getHistory()); // Eine neue History wird an die Aufgabe übergeben.

		return task;
	}

	private List<User> getSelectedUsers()
	{
		List<User> familyMembers = currentFamily.getFamilyMembers();
		List<User> relatedUsers = new ArrayList<>();

		selectedUsersAsString = mSpinnerRelatedUsers.getSelectedStrings();
		if (selectedUsersAsString.size()!=0)
		{
			for (String username : selectedUsersAsString)
			{
				for (User user : familyMembers)
				{
					if (username.equals(user.getUserName()))
					{
						relatedUsers.add(user);
					}
				}
			}
		}

		return relatedUsers;
	}

	private boolean isValid()
	{
		String name = editTextTaskTitle.getText().toString();
		int relatedUsersCount = getSelectedUsers().size();

		if (name.isEmpty())
		{
			editTextTaskTitle.setError("Es muss ein Titel eingetragen werden!");
			return false;
		}

		if (relatedUsersCount==0)
		{
			Message.show(getApplicationContext(),"Es muss ein Bearbeiter für die Aufgabe ausgewählt werden!", Message.Mode.ERROR);
			return false;
		}

		return true;
	}

	@Override
	public void selectedIndices(List<Integer> indices) {}

	@Override
	public void selectedStrings(List<String> strings) {}
}
