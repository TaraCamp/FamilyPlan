/**
 * @file TaskAddActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.Dummy;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
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
public class TaskAddActivity extends FragmentActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener
{
	private static final String TAG = "familyplan.debug";

	private EditText editTextTaskTitle = null;
	private EditText editTextTaskDescription = null;
	private EditText editTextTaskDate = null;
	private EditText editTextTaskTime = null;
	private MultiSelectionSpinner multiSelectionSpinner = null;

	private Button buttonAddTask = null;
	private Button buttonCloseDialog = null;

	private TimePickerDialog timePickerDialog = null;
	private DatePickerDialog datePickerDialog = null;

	private Calendar calendar = Calendar.getInstance();
	private Calendar dateCalendar = null;
	private Calendar timeCalendar = null;

	private List<String> selectedUsersAsString = null;

	private FirebaseDatabase database = null;
	private DatabaseReference databaseReference = null;
	private DatabaseReference taskReference = null;

	private android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

	private Family family = null;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void init()
	{
		editTextTaskTitle = (EditText) findViewById(R.id.text_task_add_taskName);
		editTextTaskTitle.requestFocus();
		editTextTaskDescription = (EditText) findViewById(R.id.text_task_add_taskDescription);
		editTextTaskDate = (EditText) findViewById(R.id.text_task_add_taskDate);
		editTextTaskDate.setShowSoftInputOnFocus(false);
		editTextTaskDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View _v, boolean _hasFocus)
			{
				if (_hasFocus)
				{
					Log.d(TAG,":TaskAddActivity.onFocusChange() -> open date dialog");

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
		editTextTaskTime = (EditText) findViewById(R.id.text_task_add_taskTime);
		editTextTaskTime.setShowSoftInputOnFocus(false);
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

		buttonAddTask = (Button) findViewById(R.id.button_task_add_addTask);
		buttonAddTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> add task");

				saveTask(); // Speichert die neue Aufgabe in der Datenbank.
			}
		});
		buttonCloseDialog = (Button) findViewById(R.id.button_task_add_closeDialog);
		buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> close dialog");

				Intent taskTabIntent = new Intent(getApplicationContext(),TaskListActivity.class);
				startActivity(taskTabIntent);
			}
		});
	}

	/**
	 * Das Family Object wird geladen und initialisiert.
	 */
	private void load()
	{
		Bundle extras = getIntent().getExtras();
		if (extras != null)
		{
			final String key = extras.getString("FAMILY_KEY");

			this.database = FirebaseDatabase.getInstance();

			this.databaseReference = this.database.getReference("families");
			this.databaseReference.addValueEventListener(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					DataSnapshot snap = dataSnapshot.child(key);
					family = snap.getValue(Family.class);

					// Der MultiSelectionSpinner wurde seperat anhand des Spinner Class erweitert und ist unter dem Ordner
					// Controls zu finden. Zum befüllen des Spinners wurde ein Workaround verwendet.
					// In Zukunft sollten Objekte abgekegt werden und keine Strings.
					multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.multiSpinner);
					String[] array = Dummy.newRelatedUserList(family.getFamilyMembers()); // // TODO: 27.09.2017 sollte aus der datenbank gefüllt werden
					multiSelectionSpinner.setItems(array);
					multiSelectionSpinner.setListener(TaskAddActivity.this);
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {}
			});
		}
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(@Nullable Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);

		Log.d(TAG,":TaskAddActivity.onCreate()");

		setContentView(R.layout.dialog_task_add);

		load();
		init();
	}

	/**
	 * Speichert eine neue Aufgabe in der Firebase Datenbank.
	 */
	private void saveTask()
	{
		// Es wird geprüft ob alle Pflichtfelder ausgefüllt sind
		if(checkValid())
		{
			/*
			* Creating new task node, which returns the unique key value,
			* new task node would be families/$familyId/familyTasks/$userid/
			*/
			this.taskReference = this.databaseReference.child(this.family.getKey()).child("familyTasks").getRef();
			String taskKey = this.taskReference.push().getKey(); // create a new taskKey

			this.taskReference.child(taskKey).setValue(this.createTask(taskKey)); // put to database

			Message.show(TaskAddActivity.this,"Die Aufgabe wurde angelegt!","SUCCES");

			Intent taskTabIntent = new Intent(getApplicationContext(),TaskListActivity.class);
			startActivity(taskTabIntent);
		}
	}

	/**
	 *
	 * Erstellt eine neue Aufgabe. Jede Aufgabe erhält einen eindeutigen Schlüssen (_key).
	 *
	 * Folgende Eigenschaften besitzt eine Aufgabe:
	 * - Aufgabenschlüssel -> wird aon firebase zurückgegeben.
	 * - Aufgaben Titel -> Pflichtfeld
	 * - Aufgaben Beschreibung -> Optional
	 * - Aufgaben Status -> Wird zu Begin auf OPEN gesetzt.
	 * - Aufgaben Erstelldatum -> automatisch
	 * - Aufgaben Ausführdatum -> optional / ansonsten today()
	 * - Aufgaben Ausführuhrzeit -> optional / ansonsten today()
	 * - Aufgaben Benutzer -> min. 1 musst selektiert sein.
	 *
	 */
	private Task createTask(String _key)
	{
		User creator = Dummy.newUser("Wowa","wowa@tarasov"); // // TODO: 27.09.2017  Dummy Ersteller
		Task task = new Task(creator);

		task.setId(_key);
		task.setFamilyKey(family.getKey());

		//// TODO: 27.09.2017  Workaround für die Auswahl der Benutzer!!
		List<User> allUsers = family.getFamilyMembers();
		if (selectedUsersAsString==null)
		{
			selectedUsersAsString = new ArrayList<>();
			selectedUsersAsString.add(multiSelectionSpinner.getSelectedItemsAsString());
		}
		List<User> listRel = new ArrayList<>();
		for(User user : allUsers)
		{
			for(String s : selectedUsersAsString)
			{
				//if (user.getUserName().equals(s)) task.addRelatedUser(user);
				if (user.getUserName().equals(s)) listRel.add(user);
			}
		}

		task.setTaskTitle(editTextTaskTitle.getText().toString()); // Setze Aufgabentitel
		task.setTaskDescription(editTextTaskDescription.getText().toString()); // Setze Aufgabenbeschreibung
		task.setTaskState("OPEN"); // // TODO: 27.09.2017 muss aus enum entnommen werden.
		task.setTaskCreatedOn(DateUtils.formatDateTime(TaskAddActivity.this,calendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
		task.setTaskRelatedUsers(listRel);
		if (dateCalendar!=null) task.setTaskDate(editTextTaskDate.getText().toString()); // Setzt Datum als String
		if (timeCalendar!=null) task.setTaskTime(editTextTaskTime.getText().toString()); // Setzt Time als String

		return task;
	}

	private boolean checkValid()
	{
		boolean isValid = false;

		if (!this.editTextTaskTitle.getText().toString().equals("")) isValid=true;
		else Message.show(TaskAddActivity.this,"Es muss ein Titel angegeben werden!","ERROR");

		if (multiSelectionSpinner.getCount()==0)
		{
			isValid=false;
			Message.show(TaskAddActivity.this,"Es muss mindestens einer ausgewählt sein!","ERROR");
		}

		return isValid;
	}

	@Override
	public void selectedIndices(List<Integer> indices)
	{
		Log.d(TAG,":TaskAddActivity.selectedIndices()");
	}

	@Override
	public void selectedStrings(List<String> strings)
	{
		Log.d(TAG,":TaskAddActivity.selectedStrings() -> " + strings.toString());

		// Weil die Selektierteliste immer wieder zurückgesetzt wird, müssen die ausgewählten Strings übergeben werden
		selectedUsersAsString = strings;
	}
}
