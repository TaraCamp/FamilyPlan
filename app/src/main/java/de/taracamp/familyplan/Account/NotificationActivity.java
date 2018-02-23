/**
 * @file NotificationActivity.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Account;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import de.taracamp.familyplan.Models.AppUserManager;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Message;
import de.taracamp.familyplan.Models.Notification;
import de.taracamp.familyplan.R;

/**
 * NotificationActivity : Represent the logic behind of notifications layout.
 * The user can send a message to a other user in the family.
 */
public class NotificationActivity extends AppCompatActivity
{
	private FirebaseManager firebaseManager = null;
	private String memberToken = null;

	private TextView textViewId = null;
	private EditText editTextMessage = null;
	private Button buttonSendNotification = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);

		// Get Firebase App User.
		this.firebaseManager = new FirebaseManager();
		this.firebaseManager.appUser = AppUserManager.getIntentAppUser(getIntent());

		final String memberToken = getIntent().getStringExtra("MEMBER_ID");
		final String memberName = getIntent().getStringExtra("MEMBER_NAME");

		textViewId = (TextView) findViewById(R.id.textview_notification_id);
		textViewId.setText(textViewId.getText().toString() + " " + memberName);
		editTextMessage = (EditText) findViewById(R.id.edittext_notification_message);
		buttonSendNotification = (Button) findViewById(R.id.button_notification_send);
		buttonSendNotification.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view)
			{
				Notification notification = new Notification();
				notification.setNotifificationMessage(editTextMessage.getText().toString());
				notification.setNotifificationFrom(firebaseManager.appUser.getUserToken());
				notification.setNotificationTo(memberToken);
				notification.setNotificationToken(firebaseManager.createToken());

				// Needs for search the user.
				firebaseManager.transferData = memberToken;

				// save notification in /users/{token}/userNotifications/{token}
				if (firebaseManager.saveObject(notification)) Message.show(getApplicationContext(),"Nachricht wurde versendet", Message.Mode.SUCCES);
				else Message.show(getApplicationContext(),"Nachricht konnte nicht versendet werden", Message.Mode.ERROR);

				Intent intent = new Intent(getApplicationContext(),FamilyMemberAccountActivity.class);
				intent.putExtra("USER",firebaseManager.appUser);
				intent.putExtra("MEMBER_ID",memberToken);
				startActivity(intent);
			}
		});
	}
}
