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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Controls.MultiSelectionSpinner;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FamilyUserHelper;
import de.taracamp.familyplan.Models.FirebaseManager;
import de.taracamp.familyplan.Models.History;
import de.taracamp.familyplan.Models.HistoryManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.List.TaskListActivity;

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
	List<User> memberList = null;
	List<User> listRel = null;

	private FirebaseManager firebaseManager = null;

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Override
	protected void onCreate(@Nullable Bundle _savedInstanceState)
	{
		super.onCreate(_savedInstanceState);
		setContentView(R.layout.dialog_task_add);

		this.Firebase();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void init()
	{
		editTextTaskTitle = (EditText) findViewById(R.id.input_task_add_taskTitle);
		editTextTaskTitle.requestFocus();
		editTextTaskDescription = (EditText) findViewById(R.id.input_task_add_taskDescription);
		editTextTaskDate = (EditText) findViewById(R.id.input_task_add_taskDate);
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
		editTextTaskTime = (EditText) findViewById(R.id.input_task_add_taskTime);
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

				// Prüft ob das Anlegen einer neuen Aufgabe erfolgreich war.
				if (saveTask())
				{
					Message.show(TaskAddActivity.this,"Eine neue Aufgabe wurde angelegt!", Message.Mode.SUCCES);

					Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
					startActivity(FamilyUserHelper.setAppUser(intent,firebaseManager.appUser));
				}
				else
				{
					Message.show(TaskAddActivity.this,"Aufgabe konnte nicht hinzugefügt werden", Message.Mode.INFO);
				}
			}
		});
		buttonCloseDialog = (Button) findViewById(R.id.button_task_add_closeDialog);
		buttonCloseDialog.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v)
			{
				Log.d(TAG,":TaskAddActivity.onClick() -> close dialog");

				Intent intent = new Intent(getApplicationContext(),TaskListActivity.class);
				startActivity(FamilyUserHelper.setAppUser(intent,firebaseManager.appUser));
			}
		});
	}

	private void Firebase()
	{
		// Der Multiselektion Spinner
		this.multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.multiSpinner); //// TODO: 28.10.2017 vorher schon

		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = FamilyUserHelper.getFamilyUser(getIntent()); //current user

		// ./families/token
		this.firebaseManager.currentFamilyReference = this.firebaseManager.families().child(this.firebaseManager.appUser.getUserFamilyToken()).getRef();
		this.firebaseManager.currentFamilyReference.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				Family family = dataSnapshot.getValue(Family.class);

				memberList = family.getFamilyMembers(); // Alle Member werden zwischengespeichert.

				multiSelectionSpinner.setItems(getRelatedUserList(family.getFamilyMembers()));
				multiSelectionSpinner.setListener(TaskAddActivity.this);

				init();
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

	/**
	 * Speichert eine neue Aufgabe in der Firebase Datenbank.
	 */
	private boolean saveTask()
	{
		try
		{
			// Es wird geprüft ob alle Pflichtfelder ausgefüllt sind
			if(checkValid())
			{
				String taskKey = this.firebaseManager.tasks(this.firebaseManager.families().child(this.firebaseManager.appUser.getUserFamilyToken())).push().getKey(); // create a new taskKey

				Task task = this.createTask(taskKey);
				// ./families/<token>/familyTasks/token -> add object
				this.firebaseManager.tasks(this.firebaseManager.families().child(this.firebaseManager.appUser.getUserFamilyToken())).child(taskKey).setValue(task);

				return true;
			}

			return false;
		}
		catch(Exception exception)
		{
			return false;
		}
	}

	/**
	 *
	 * Erstellt eine neue Aufgabe. Jede Aufgabe erhält einen eindeutigen Schlüssen (_key).
	 */
	private Task createTask(String _key)
	{
		Task task = new Task();

		task.setTaskToken(_key); // Aufgaben Token
		task.setTaskCreator(FamilyUserHelper.getUserByFamilyUser(this.firebaseManager.appUser)); // Aufgaben Ersteller
		task.setTaskFamilyToken(this.firebaseManager.appUser.getUserFamilyToken()); // Aufgaben Familien Token
		task.setTaskRelatedUsers(getRelatedUsers()); // Aufgaben Benutzer festlegen
		task.setTaskTitle(editTextTaskTitle.getText().toString()); // Setze Aufgabentitel
		task.setTaskDescription(editTextTaskDescription.getText().toString()); // Setze Aufgabenbeschreibung
		task.setTaskState("OPEN"); // // TODO: 27.09.2017 muss aus enum entnommen werden.
		task.setTaskCreatedOn(DateUtils.formatDateTime(TaskAddActivity.this,calendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_DATE));
		if (dateCalendar!=null) task.setTaskDate(editTextTaskDate.getText().toString()); // Setzt Datum als String
		if (timeCalendar!=null) task.setTaskTime(editTextTaskTime.getText().toString()); // Setzt Time als String

		// Eine History wird eingefügt.
		HistoryManager historyManager = new HistoryManager(task);
		historyManager.addMessage(historyManager.getMessageByNewHistory());
		task.setTaskHistory(historyManager.getHistory()); // Eine neue History wird an die Aufgabe übergeben.

		return task;
	}

	private List<User> getRelatedUsers()
	{
		//// TODO: 27.09.2017  Workaround für die Auswahl der Benutzer!!
		if (selectedUsersAsString==null)
		{
			selectedUsersAsString = new ArrayList<>();
			selectedUsersAsString.add(multiSelectionSpinner.getSelectedItemsAsString());
		}
		listRel = new ArrayList<>();
		for(User user : memberList)
		{
			for(String username : selectedUsersAsString)
			{
				if (user.getUserName().equals(username)) listRel.add(user);
			}
		}

		return listRel;
	}

	private boolean checkValid()
	{
		boolean isValid = false;

		if (!this.editTextTaskTitle.getText().toString().equals("")) isValid=true;
		else Message.show(TaskAddActivity.this,"Es muss ein Titel angegeben werden!", Message.Mode.ERROR);

		if (multiSelectionSpinner.getCount()==0)
		{
			isValid=false;
			Message.show(TaskAddActivity.this,"Es muss mindestens einer ausgewählt sein!",Message.Mode.ERROR);
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
