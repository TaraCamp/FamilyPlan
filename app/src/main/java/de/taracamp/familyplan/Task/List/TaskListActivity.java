/**
 * @file TaskActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task.List;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Enums.TaskState;
import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.Details.Detail.TaskDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * In dieser Activity wird die Aufgabenliste gezeigt. Zudem kann man folgende Optionen nutzen:
 *
 * - Eine neue Aufgabe erstellen, über einen FloatingActionButton -> TaskAddActivity.
 * - Eigene Aufgaben anzeigen, über den ActionBarButton wird die Liste gefiltert.
 * - Erstellte Aufgaben anzeigen, über den ActionBarButton wird die Liste gefiltert.
 * - Nach Aufgaben suchen, über die Suche kann man die Liste filtern.
 * - ActionMode aktivieren. Über einen LongPress auf einem Item aktiviert man den ActionMode:
 * 	-> ActionMode: Aufgaben als Erledigt setzen. Über die gewählten Aufgaben.
 * 	-> ActionMode: In bearbeitungsmodus wechseln.
 * - Aufgaben Bearbeiten, über einen Klick auf ein Item -> TaskDetailActivity.
 *
 * Information: Die Liste ist eine RecyclerView und wird über einen Adapter (TaskListAdapter) geladen.
 * 				Je nach Gerät wird die Master/Detail Ansicht genutzt.
 *
 */
public class TaskListActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "TaskListActivity";

	private Toolbar toolbar = null;
	private FloatingActionButton floatingActionButton = null;
	private RecyclerView recyclerView = null;
	private TextView textViewInformation = null;
	private TextView textViewMode = null;

	private ArrayList<Task> tasks = null;
	private TaskListAdapter adapter = null;

	public FirebaseManager firebaseManager = null;

	public Family family = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		Log.d(TAG,CLASS+".onCreate()");

		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		loadTasks();
	}

	private void loadTasks()
	{
		tasks = new ArrayList<>(); // Leere Aufgabenliste wird erstellt.
		firebaseManager.getTasksReference().addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				tasks.clear();

				for (DataSnapshot taskSnap : dataSnapshot.getChildren())
				{
					tasks.add(taskSnap.getValue(Task.class));
				}

				initializeViews();
				loadTasksByCreator();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	private void initializeViews()
	{
		initializeToolbar();

		// Anzeige für die Information wenn keine Aufgaben vorhanden sind.
		textViewInformation = (TextView) findViewById(R.id.textView_task_noWork2);
		textViewInformation.setVisibility(View.GONE);

		textViewMode = (TextView) findViewById(R.id.textview_tasks_mode);

		recyclerView = (RecyclerView) findViewById(R.id.item_list);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
		recyclerView.setHasFixedSize(true);

		initializeActionButton();
	}

	private void initializeToolbar()
	{
		toolbar = (Toolbar) findViewById(R.id.toolbar_task);
		setSupportActionBar(toolbar);

		getSupportActionBar().setLogo(R.drawable.ic_action_add);
		getSupportActionBar().setTitle("Aufgaben Übersicht");
	}

	private void initializeActionButton()
	{
		// Der Runde Button um eine neue Aufgabe anzulegen
		this.floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_task_openDialog);
		this.floatingActionButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":TaskActivity.click()-> open new task window");

				Intent intent = new Intent(getApplicationContext(),TaskAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu_task,menu);

		final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.item_task_search));

		SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
		searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
		searchView.setMaxWidth(Integer.MAX_VALUE);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query)
			{
				loadTasksBySearch(query);
				textViewMode.setText("Gesuchte Aufgaben");
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText)
			{
				return false;
			}
		});

		return true;
	}

	@Override
	public void onBackPressed()
	{
		Log.d(TAG,":TaskActivity.onBackPressed() -> close task menu");

		super.onBackPressed();

		Intent intent = new Intent(getApplicationContext(),MainActivity.class);
		intent.putExtra("USER",firebaseManager.appUser);
		startActivity(intent);

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
		if(_item.getItemId()==R.id.item_task_ownTasks)
		{
			loadTasksByRelated();
			textViewMode.setText("Zugeteilte Aufgaben");
		}
		else if(_item.getItemId()==R.id.item_task_createTasks)
		{
			loadTasksByCreator();
			textViewMode.setText("Erstellte Aufgaben");
		}
		else if (_item.getItemId()==android.R.id.home)
		{
			this.adapter.notifyDataSetChanged();
		}

		return true;
	}

	/**
	 * Alle Aufgaben die vom Ersteller werden geladen und angezeigt.
	 */
	private void loadTasksByCreator()
	{
		List<Task> selectTasks = new ArrayList<>();

		for (Task task : tasks)
		{
			if (task.getTaskCreator().getUserToken().equals(firebaseManager.appUser.getUserToken())) selectTasks.add(task);
		}

		setAdapter(selectTasks);
	}

	// Zeigt alle Aufgaben an die einem zugeordnet sind.
	private void loadTasksByRelated()
	{
		List<Task> selectTasks = new ArrayList<>();

		for (Task task : tasks)
		{
			List<User> members = task.getTaskRelatedUsers();
			for (User user : members)
			{
				if (user.getUserToken().equals(firebaseManager.appUser.getUserToken()) && !task.getTaskState().equals(TaskState.FINISH)) selectTasks.add(task);
			}
		}

		setAdapter(selectTasks);
	}

	// zeigt alle Aufgaben an die gesucht wurden.
	private void loadTasksBySearch(final String _query)
	{
		List<Task> selectTasks = new ArrayList<>();

		for (Task task : tasks)
		{
			if (task.getTaskTitle().contains(_query) || task.getTaskDescription().contains(_query)) selectTasks.add(task);
		}

		setAdapter(selectTasks);
	}

	private void setAdapter(List<Task> list)
	{
		// Wenn die Liste leer ist wird eine Meldung angezeigt.
		if (list.size()==0) textViewInformation.setText("Keine Aufgaben gefunden");
		else textViewInformation.setVisibility(View.GONE);

		// Adapter wird geladen.
		adapter = new TaskListAdapter(TaskListActivity.this, list); // Liste wird an Adapter weitergegeben
		recyclerView.setAdapter(adapter); // Liste wird durch Adapter befüllt
	}
}
