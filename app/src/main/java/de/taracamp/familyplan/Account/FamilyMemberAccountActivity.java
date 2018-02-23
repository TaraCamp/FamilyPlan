package de.taracamp.familyplan.Account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;
import de.taracamp.familyplan.Task.List.TaskAddActivity;

public class FamilyMemberAccountActivity extends AppCompatActivity
{
	private FirebaseManager firebaseManager = null;

	private TextView textViewMemberName = null;
	private String userToken = null;
	private Button buttonSendNotification = null;
	private Button buttonNewTask = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_family_member_account);

		// Get Firebase App User.
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());
		this.userToken = getIntent().getStringExtra("MEMBER_ID");

		textViewMemberName = (TextView) findViewById(R.id.textview_familymember_name);

		this.firebaseManager.getUsersReference().child(this.userToken).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot)
			{
				User user = dataSnapshot.getValue(User.class);
				textViewMemberName.setText(user.getUserName());
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {}
		});

		buttonSendNotification = (Button) findViewById(R.id.button_familymember_notification);
		// check if selected user is ow user account ->if true then button for send notification is invisible.
		if (firebaseManager.appUser.getUserToken().equals(userToken)) buttonSendNotification.setVisibility(View.GONE);

		buttonSendNotification.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				intent.putExtra("MEMBER_ID",userToken);
				intent.putExtra("MEMBER_NAME",textViewMemberName.getText().toString());
				startActivity(intent);
			}
		});

		buttonNewTask = (Button) findViewById(R.id.button_familymember_task);
		buttonNewTask.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				Intent intent = new Intent(getApplicationContext(),TaskAddActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();

		Intent intent = new Intent(getApplicationContext(),AccountActivity.class);
		intent.putExtra("USER",firebaseManager.appUser);
		intent.putExtra("ACCOUNT_TAB_MENU",1);

		startActivity(intent);
	}
}
