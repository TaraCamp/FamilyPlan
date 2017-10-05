/**
 * @file TaskActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.Dummy;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

import java.util.ArrayList;

/**
 * In dieser Activity wird die Aufgabenliste gezeigt. Zudem kann man folgende Optionen nutzen:
 *
 * - Eine neue Aufgabe erstellen, über einen FloatingActionButton -> TaskAddActivity.
 * - Eigene Aufgaben anzeigen, über den ActionBarButton wird die Liste gefiltert.
 * - Erstellte Aufgaben anzeigen, über den ActionBarButton wird die Liste gefiltert.
 * - Nach Aufgaben suchen, über die Suche kann man die Liste filtern.
 * - ActionMode aktivieren. Über einen LongPress auf einem Item aktiviert man den ActionMode:
 * 	-> ActionMode: Aufgaben als Erledigt setzen. Über die gewählten Aufgaben.
 * 	-> ActionMode: ...
 * - Aufgaben Bearbeiten, über einen Klick auf ein Item -> TaskDetailActivity.
 *
 * Information: Die Liste ist eine RecyclerView und wird über einen Adapter (TaskListAdapter) geladen.
 * 				Je nach Gerät wird die Master/Detail Ansicht genutzt.
 *
 */
public class TaskListActivity extends AppCompatActivity implements View.OnLongClickListener
{
	private static final String TAG = "familyplan.debug";

	private TaskListActivity thisActivity = null;

	private Toolbar toolbar = null;
	private FloatingActionButton floatingActionButton = null;
	private RecyclerView recyclerView = null;
	private TextView textViewSelectedCounter = null;
	private TextView textViewInformation = null;

	private ArrayList<Task> list = null;
	private ArrayList<Task> selectedList = null;

	private TaskListAdapter adapter = null;
	private int selectedTasksCounter = 0;
	public boolean isActionModeEnable = false;

	private FirebaseDatabase database = null;
	private DatabaseReference familiesReference = null;
	private DatabaseReference tasksReference = null;

	public Family family = null;

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	public boolean isMasterDetailEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		toolbar = (Toolbar) findViewById(R.id.toolbar_task);
		setSupportActionBar(toolbar);

		// Anzeige für die Anzahl der selektierten Aufgaben
		this.textViewSelectedCounter = (TextView) findViewById(R.id.counter_task);
		this.textViewSelectedCounter.setVisibility(View.GONE);

		// Anzeige für die Information wenn keine AUfgaben vorhanden sind.
		this.textViewInformation = (TextView) findViewById(R.id.textView_task_noWork2);

		this.thisActivity = this;

		// Dei Datenbank wird initialisiert.
		this.database = FirebaseDatabase.getInstance();

		this.list = new ArrayList<>(); // Leere Aufgabenliste wird erstellt.
		this.selectedList = new ArrayList<>(); // Leere Selektierte Aufgabenliste wird erstellt.

		// Der Runde Button um eine neue Aufgabe anzulegen
		this.floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_task_openDialog);
		this.floatingActionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskActivity.click()-> open new task window");

				Intent intentAddTask = new Intent(getApplicationContext(),TaskAddActivity.class);
				intentAddTask.putExtra("FAMILY_KEY",family.getKey());
				startActivity(intentAddTask);
			}
		});

		this.recyclerView = (RecyclerView) findViewById(R.id.item_list);
		this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
		this.recyclerView.setHasFixedSize(true);

		load();

		if (findViewById(R.id.item_detail_container) != null)
		{
			isMasterDetailEnable = true;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_task,menu);
		return true;
	}

	@Override
	public void onBackPressed()
	{
		// Es wird geprüft on der actio mode aktiviert ist
		if (this.isActionModeEnable)
		{
			Log.d(TAG,":TaskActivity.onBackPressed() -> close action mode");

			clearActionMode(); // Der Action Mode wird deaktiviert
			this.adapter.notifyDataSetChanged();
		}
		else
		{
			Log.d(TAG,":TaskActivity.onBackPressed() -> close task menu");

			super.onBackPressed();

			Intent intentMain = new Intent(getApplicationContext(),MainActivity.class);
			startActivity(intentMain);
		}
	}


	private void load()
	{
		this.familiesReference = this.database.getReference("families");
		this.familiesReference.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				long foundFamiles = dataSnapshot.getChildrenCount();

				if (foundFamiles==0)
				{
					family = Dummy.newFamily(familiesReference.push().getKey());
					familiesReference.child(family.getKey()).setValue(family);
				}
				else
				{
					//// TODO: 26.09.2017 muss noch dynamisch implementiert werden
					family = dataSnapshot.child("-Kux_ITnsJUDHqqQdZXU").getValue(Family.class);
				}

				tasksReference = familiesReference.child(family.getKey()).child("familyTasks").getRef();
				tasksReference.addValueEventListener(new ValueEventListener() {
					@Override
					public void onDataChange(DataSnapshot dataSnapshot)
					{
						long foundTasks = dataSnapshot.getChildrenCount();

						Log.d(TAG,":TaskActivity.readDatabase() -> onDataChange, found records = " + foundTasks);

						list.clear(); // Liste entleeren

						// Jedes Element im Datenbankknoten wird durchlaufen und der Liste hinzugefügt
						for(DataSnapshot taskSnap : dataSnapshot.getChildren())
						{
							Task readTask = taskSnap.getValue(Task.class);
							if (readTask.getTaskState().equals("OPEN")) list.add(readTask); // Aufgabe der Liste anhefeten
						}

						adapter = new TaskListAdapter(thisActivity,list); // Liste wird an Adapter weitergegeben
						recyclerView.setAdapter(adapter); // Liste wird durch Adapter befüllt

						if (list.size()!=0 && isMasterDetailEnable)
						{
							Bundle arguments = new Bundle();
							arguments.putString(TaskDetailFragment.TASK_KEY,list.get(0).getId());
							arguments.putString(TaskDetailFragment.FAMILY_KEY,family.getKey());

							TaskDetailFragment taskDetailFragment = new TaskDetailFragment();
							taskDetailFragment.setArguments(arguments);

							getSupportFragmentManager()
									.beginTransaction()
									.replace(R.id.item_detail_container,taskDetailFragment)
									.commit();

							textViewInformation.setVisibility(View.GONE);
						}

					}

					@Override
					public void onCancelled(DatabaseError databaseError) {}
				});
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	public void prepareSelection(View _view,int _position)
	{
		if (((CheckBox)_view).isChecked())
		{
			this.selectedList.add(list.get(_position));
			selectedTasksCounter++;
			updateCounter(selectedTasksCounter);
		}
		else
		{
			this.selectedList.remove(list.get(_position));
			selectedTasksCounter--;
			updateCounter(selectedTasksCounter);
		}
	}

	@Override
	public boolean onLongClick(View v)
	{
		Log.d(TAG,":TaskActivity.onLongClick() -> action mode on");

		this.toolbar.getMenu().clear(); // Das normale menu wird deaktiviert.
		this.toolbar.inflateMenu(R.menu.menu_task_action_mode); // Action mode menu wird angezeigt
		this.textViewSelectedCounter.setVisibility(View.VISIBLE);
		this.floatingActionButton.setVisibility(View.GONE); // Floatingbutton wird unsichtbar
		this.isActionModeEnable = true; // ActionMode wird aktiviert
		this.adapter.notifyDataSetChanged();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		return true;
	}

	/**
	 * Ausgabe 'Selektierte Aufgaben' wird aktualisiert.
	 * @param _counter
	 */
	public void updateCounter(int _counter)
	{
		if (_counter==0)
		{
			textViewSelectedCounter.setText("0 Aufgaben ausgewählt");
		}
		if (_counter==1)
		{
			textViewSelectedCounter.setText("1 Aufgabe ausgewählt");
		}
		else
		{
			textViewSelectedCounter.setText(_counter + " Aufgaben ausgewählt");
		}
	}

	/**
	 *
	 * Kontrolliert alle Elemente innerhalb des Menus.
	 *
	 * - Erledigt Funktion - Ändert den Status aller ausgewählten Aufgaben (onClick)
	 * - Home Button - Beendet das Auswahlmenü (onClick)
	 * - Suche - In der Liste kann nach Aufgaben gesucht werden. (onClick)
	 * - Eigene Aufgaben - Zeigt Aufgaben an die man bearbeiten muss (onClick)
	 * - Erstellte Aufgaben - Zeigt Aufgaben an die Erstellt wurden (onClick)
	 *
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem _item)
	{
		if (_item.getItemId()==R.id.item_task_done)
		{
			Log.d(TAG,":TaskActivity.onClick() -> finish tasks");

			finishTasks();
		}
		else if(_item.getItemId()==R.id.item_task_search)
		{
			Log.d(TAG,":TaskActivity.onClick() -> search tasks");
		}
		else if(_item.getItemId()==R.id.item_task_ownTasks)
		{
			Log.d(TAG,":TaskActivity.onClick() -> own tasks");
		}
		else if(_item.getItemId()==R.id.item_task_createTasks)
		{
			Log.d(TAG,":TaskActivity.onClick() -> created tasks");
		}
		else if (_item.getItemId()==android.R.id.home)
		{
			Log.d(TAG,":TaskActivity.onClick() -> home");

			clearActionMode();
			this.adapter.notifyDataSetChanged();
		}

		return true;
	}

	/**
	 * Beendet alle ausgewählten Aufgaben und setzt den Status auf FINISH.
	 */
	private void finishTasks()
	{
		TaskListAdapter taskListAdapter = this.adapter;
		taskListAdapter.updateAdapter(selectedList);

		for (Task task : selectedList)
		{
			tasksReference.child(task.getId()).child("taskState").setValue("FINISH"); // Aufgabe wird als erledigt markiert
		}

		clearActionMode();
	}

	/**
	 * Deaktiviert den Action Mode
	 */
	public void clearActionMode()
	{
		Log.d(TAG,":TaskActivity -> action mode off");

		this.isActionModeEnable = false; // Action Mode wird ausgeschaltet

		this.toolbar.getMenu().clear(); // Das menu wird geleert
		this.toolbar.inflateMenu(R.menu.menu_task); // Die Standart Actionbar menu wird geladen

		this.floatingActionButton.setVisibility(View.VISIBLE); // FloatingActionButton wird aktiviert

		getSupportActionBar().setDisplayHomeAsUpEnabled(false); // Der Homebutton in der Actionbar wird deaktiviert

		// Die Ausgabe für Anzahl selektierter Aufgaben wird zurückgesetzt und unsichtbar
		this.textViewSelectedCounter.setVisibility(View.GONE);
		this.textViewSelectedCounter.setText("0 Aufgaben ausgewählt");

		this.selectedTasksCounter = 0; // Counter für Selektierte Aufgaben wird zurückgesetzt.
		this.selectedList.clear(); // Selektierte Aufgabenliste wird geleert.
	}
}
