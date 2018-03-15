package de.taracamp.familyplan.Task.detail.nodes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.fafaldo.fabtoolbar.widget.FABToolbarLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.History;
import de.taracamp.familyplan.Models.HistoryMessage;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;

/**
 * TaskNoteFragment:
 */
public class TaskNoteFragment extends Fragment
{
	private static FirebaseManager firebaseManager = null;
	private static Task selectedTask = null;

	private RecyclerView recyclerView = null;
	private List<HistoryMessage> messages = null;
	private MessagesRecyclerAdapter messagesRecyclerAdapter = null;

	private FABToolbarLayout fabAnimatedToolbar = null;
	private FloatingActionButton fab = null;

	private ImageView imageViewText = null;
	private ImageView imageViewImage = null;
	private ImageView imageViewPhoto = null;
	private ImageView imageViewCancel = null;

	public TaskNoteFragment() {}

	public static TaskNoteFragment newInstance(FirebaseManager _firebaseManager,Task task)
	{
		TaskNoteFragment fragment = new TaskNoteFragment();
		firebaseManager = _firebaseManager;
		selectedTask = task;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_task_note, container, false);

		recyclerView = view.findViewById(R.id.recyclerview_tasknodes_nodes);

		History history = selectedTask.getTaskHistory();
		messages = history.getMessages();

		Collections.reverse(messages); // Reverse an ArrayList.

		messagesRecyclerAdapter = new MessagesRecyclerAdapter(getActivity().getApplicationContext(),messages,firebaseManager);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
		recyclerView.setAdapter(messagesRecyclerAdapter);

		fabAnimatedToolbar = view.findViewById(R.id.fabtoolbar_tasknode);
		fab = view.findViewById(R.id.floatingactionbutton_tasknode_opentoolbar);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				fabAnimatedToolbar.show();
			}
		});

		imageViewText = view.findViewById(R.id.fabtoolbar_tasknode_addtext);
		imageViewText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				TextMessageActionDialog dialog = TextMessageActionDialog.newInstance(firebaseManager,selectedTask);
				dialog.show(getActivity().getFragmentManager(),"textmessageaction");
			}
		});

		imageViewImage = view.findViewById(R.id.fabtoolbar_tasknode_addimage);
		imageViewImage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				// Select photo in hardware storage and add it to chat
				Message.show(getActivity().getApplicationContext(),"Funktion noch nicht verfügbar", Message.Mode.INFO);
			}
		});

		imageViewPhoto = view.findViewById(R.id.fabtoolbar_tasknode_addphoto);
		imageViewPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				// take photo and add it to chat
				Message.show(getActivity().getApplicationContext(),"Funktion noch nicht verfügbar", Message.Mode.INFO);
			}
		});

		imageViewCancel = view.findViewById(R.id.fabtoolbar_tasknode_cancel);
		imageViewCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				fabAnimatedToolbar.hide();
			}
		});

		return view;
	}
}
