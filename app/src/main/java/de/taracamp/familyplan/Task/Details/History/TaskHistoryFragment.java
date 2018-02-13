package de.taracamp.familyplan.Task.Details.History;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.HistoryManager;
import de.taracamp.familyplan.Models.HistoryMessage;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

/**
 * Stellt eine Aufgabenübesicht dar. Der Benutzer kann die Daten abändern und
 * den Status bearbeiten.
 */
public class TaskHistoryFragment extends Fragment
{
	private static final String TAG = "familyplan.debug";
	private static final String TASK_KEY = "TASK_KEY";

	private RecyclerView recyclerViewMessages = null;
	private FloatingActionButton floatingActionButtonAddMessage = null;
	private EditText editTextInputMessage = null;

	private HistoryListAdapter historyListAdapter = null;
	private static FirebaseManager firebaseManager = null;

	private HistoryManager historyManager = null;

	private String taskKey = null; // Aufgaben Token

	public TaskHistoryFragment() {
		// Required empty public constructor
	}

	public static TaskHistoryFragment newInstance(String _taskKey, FirebaseManager _firebaseManager)
	{
		TaskHistoryFragment fragment = new TaskHistoryFragment();

		Bundle args = new Bundle();
		args.putString(TASK_KEY,_taskKey);
		fragment.setArguments(args);

		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		if (getArguments() != null)
		{
			taskKey = getArguments().getString(TASK_KEY);
		}
	}

	private void Firebase()
	{
		this.firebaseManager.currentTasksReference = this.firebaseManager.tasks(this.firebaseManager.families().child(this.firebaseManager.appUser.getUserFamilyToken()));
		this.firebaseManager.currentTaskReference = this.firebaseManager.currentTasksReference.child(taskKey);
		this.firebaseManager.currentTaskReference.addListenerForSingleValueEvent(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				final Task task = dataSnapshot.getValue(Task.class);

				if (task!=null)
				{
					historyManager = new HistoryManager(task);
					loadRecyclerView(historyManager.getMessages());

					floatingActionButtonAddMessage.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v)
						{
							sendMessage(task);
						}
					});
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});
	}

	private void sendMessage(Task _task)
	{
		String message = editTextInputMessage.getText().toString();
		historyManager.addMessage(historyManager.getMessageByInput(message));

		_task.setTaskHistory(historyManager.getHistory());
		this.firebaseManager.currentTaskReference.setValue(_task);

		loadRecyclerView(historyManager.getMessages());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_task_history_tab, container, false);

		this.recyclerViewMessages = (RecyclerView) view.findViewById(R.id.list_of_messages);
		this.floatingActionButtonAddMessage = (FloatingActionButton) view.findViewById(R.id.floatingActionButton_task_addHistory);
		this.editTextInputMessage = (EditText) view.findViewById(R.id.input_task_history);

		this.floatingActionButtonAddMessage = (FloatingActionButton) view.findViewById(R.id.floatingActionButton_task_addHistory);
		this.editTextInputMessage = (EditText) view.findViewById(R.id.input_task_history);

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

	private void loadRecyclerView(List<HistoryMessage> messages)
	{
		this.recyclerViewMessages.setLayoutManager(new LinearLayoutManager(getContext()));
		this.recyclerViewMessages.setHasFixedSize(true);

		historyListAdapter = new HistoryListAdapter(getContext(),messages);
		recyclerViewMessages.setAdapter(historyListAdapter);
	}
}
