/**
 * @file TaskActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Family.FamilyActivity;
import de.taracamp.familyplan.MainActivity;
import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.Enums.TaskState;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

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
 * Information: Die Liste ist eine RecyclerView und wird über einen Adapter (TasksRecyclerAdapter) geladen.
 * 				Je nach Gerät wird die Master/Detail Ansicht genutzt.
 *
 *
 * Events:
 *
 * - buttonAddFamily.onClick() // Add family to current user.
 * - floatingActionButton.onClick() // Open TaskAddActivity.
 * - buttonNoTasks.onClick() // Add first task. Open TaskAddActivity.
 */
public class TasksActivity extends AppCompatActivity
{
	public FirebaseManager firebaseManager = null;

	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "TasksActivity";

	// no family section.
	private LinearLayout sectionNoFamily = null;
	private TextView textViewNoFamily = null;
	private Button buttonAddFamily = null;

	// no tasks section.
	private LinearLayout sectionNoTasks = null;
	private TextView textViewNoTasks = null;
	private Button buttonNoTasks = null;

	private Toolbar toolbar = null;
	private FloatingActionButton floatingActionButton = null;
	private RecyclerView recyclerView = null;
	private TextView textViewMode = null;

	private ArrayList<Task> tasks = null;
	private TasksRecyclerAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_list);

		Log.d(TAG,CLASS+".onCreate()");

		firebaseManager = new FirebaseManager();
		firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		// check if current user has a family
		if (firebaseManager.appUser.isHasFamily())
		{
			// initialize toolbar
			toolbar = (Toolbar) findViewById(R.id.toolbar_task);
			setSupportActionBar(toolbar);
			getSupportActionBar().setLogo(R.drawable.ic_action_add);
			getSupportActionBar().setTitle("Übersicht");

			textViewMode = (TextView) findViewById(R.id.textview_tasks_mode);
			textViewMode.setText(firebaseManager.appUser.getUserName() + "'s Aufgaben");

			recyclerView = (RecyclerView) findViewById(R.id.item_list);
			recyclerView.setLayoutManager(new LinearLayoutManager(this));
			recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
			recyclerView.setHasFixedSize(true);

			sectionNoTasks = findViewById(R.id.section_tasks_noTasks);
			textViewNoTasks = findViewById(R.id.textView_tasks_noTasks);
			buttonNoTasks = findViewById(R.id.button_tasks_addTask);
			buttonNoTasks.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(getApplicationContext(),TaskAddActivity.class);
					intent.putExtra("USER",firebaseManager.appUser);
					startActivity(intent);
				}
			});

			// Der Runde Button um eine neue Aufgabe anzulegen
			this.floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton_task_openDialog);
			this.floatingActionButton.setVisibility(View.VISIBLE);
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

			this.loadTasks();
		}
		else
		{
			// no family section declaration
			sectionNoFamily = findViewById(R.id.section_tasks_nofamily);
			sectionNoFamily.setVisibility(View.VISIBLE);
			textViewNoFamily = findViewById(R.id.textView_tasks_nofamily);
			buttonAddFamily = findViewById(R.id.button_tasks_addFamily);
			buttonAddFamily.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(getApplicationContext(), FamilyActivity.class);
					intent.putExtra("USER",firebaseManager.appUser);
					startActivity(intent);
				}
			});
		}
	}

	/**
	 * Load tasks by firebase database
	 */
	private void loadTasks()
	{
		this.tasks = new ArrayList<>(); // Leere Aufgabenliste wird erstellt.
		this.firebaseManager.getTasksReference().addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				tasks.clear(); // clear list
				for (DataSnapshot taskSnap : dataSnapshot.getChildren())
				{
					tasks.add(taskSnap.getValue(Task.class));
				}

				loadTasksByCreator();
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
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
			textViewMode.setText(firebaseManager.appUser.getUserName()+ "'s Aufgaben");
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
		// check if list is empty. if true -> show no tasks section
		if (list.size()==0) sectionNoTasks.setVisibility(View.VISIBLE);
		else  sectionNoTasks.setVisibility(View.GONE);

		// Adapter wird geladen.
		adapter = new TasksRecyclerAdapter(TasksActivity.this, list); // Liste wird an Adapter weitergegeben
		recyclerView.setAdapter(adapter); // Liste wird durch Adapter befüllt
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
}
