package de.taracamp.familyplan.Task.detail.nodes;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.History;
import de.taracamp.familyplan.Models.HistoryManager;
import de.taracamp.familyplan.Models.HistoryMessage;
import de.taracamp.familyplan.Models.HistoryMode;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Task;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.detail.TaskActivity;

public class TextMessageActionDialog extends DialogFragment
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "TasksActionDialog";

	private EditText editTextMessage = null;
	private Button buttonSend = null;

	private static FirebaseManager firebaseManager = null;
	private static Task selectedTask = null;

	public TextMessageActionDialog(){}

	public static TextMessageActionDialog newInstance(FirebaseManager _firebaseManager,Task _selectedTask)
	{
		TextMessageActionDialog fragment = new TextMessageActionDialog();
		firebaseManager = _firebaseManager;
		selectedTask = _selectedTask;

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.dialog_textmessage_action, container, false);

		editTextMessage = rootView.findViewById(R.id.edittext_textmessage_message);
		buttonSend = rootView.findViewById(R.id.button_textmessage_send);
		buttonSend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				if (selectedTask!=null)
				{
					// create a new text message
					HistoryMessage message = new HistoryMessage();
					message.setMessageData(editTextMessage.getText().toString());
					message.setMessageDate(new SimpleDateFormat("dd.MM.yyyy").format(new Date()));
					message.setMessageMode(HistoryMode.TEXT);
					message.setMessageTime(new SimpleDateFormat("HH:mm:ss").format(new Date()));
					message.setMessageUser(AppUserManager.getUserByAppUser(firebaseManager.appUser));

					// get history by task
					History history = selectedTask.getTaskHistory();
					List<HistoryMessage> messages = history.getMessages();
					messages.add(message);
					history.setMessages(messages);

					selectedTask.setTaskHistory(history);

					firebaseManager.saveObject(selectedTask);

					Message.show(getActivity().getApplicationContext(),"Notiz wurde hinterlegt", Message.Mode.SUCCES);

					getDialog().cancel();
				}
			}
		});

		getDialog().setTitle("Notiz hinterlegen");

		return rootView;
	}
}
