/**
 * @file NotificationsFragment.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Account.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Family.FamilyActivity;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.Notification;
import de.taracamp.familyplan.R;

/**
 * NotificationsFragment : Represent a fragment for write a notification to other user.
 */
public class NotificationsFragment extends Fragment
{
	private static FirebaseManager firebaseManager = null;

	private RecyclerView recyclerView = null;
	private List<Notification> notifications = null;
	private NotificationsRecyclerAdapter notificationsRecyclerAdapter = null;

	private LinearLayout linearLayoutNoFamily = null;
	private Button buttonAddFamily = null;
	private TextView textViewNoNotifications = null;

	public NotificationsFragment(){}

	public static NotificationsFragment newInstance(FirebaseManager _firebaseManager)
	{
		NotificationsFragment fragment = new NotificationsFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view = inflater.inflate(R.layout.fragment_notifications, container, false);

		// Information Section
		linearLayoutNoFamily = view.findViewById(R.id.linearlayout_notification_noFamily);

		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_notification_members);
		notifications = new ArrayList<>();

		// Check if user doesn't have a family
		if (!firebaseManager.appUser.isHasFamily())
		{
			linearLayoutNoFamily.setVisibility(View.VISIBLE);

			buttonAddFamily = (Button) view.findViewById(R.id.button_notification_addFamily);
			buttonAddFamily.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view)
				{
					Intent intent = new Intent(getActivity().getApplicationContext(), FamilyActivity.class);
					intent.putExtra("USER",firebaseManager.appUser);
					getActivity().startActivity(intent);
				}
			});
		}
		else
		{
			firebaseManager.getCurrentUserReference().child("userNotifications").addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot dataSnapshot)
				{
					for (DataSnapshot memberSnap : dataSnapshot.getChildren())
					{
						Notification notification = memberSnap.getValue(Notification.class);
						notifications.add(notification);
					}

					// Check if notifications list is empty and show message for user
					if (notifications.size()==0)
					{
						linearLayoutNoFamily.setVisibility(View.VISIBLE);
						textViewNoNotifications = (TextView) view.findViewById(R.id.textView_notification_nofamily);
						buttonAddFamily = (Button) view.findViewById(R.id.button_notification_addFamily);
						buttonAddFamily.setVisibility(View.GONE);
						textViewNoNotifications.setText("Keine Nachrichten");
					}
					else
					{
						notificationsRecyclerAdapter = new NotificationsRecyclerAdapter(getActivity().getApplicationContext(),notifications,firebaseManager);

						recyclerView.setHasFixedSize(true);
						recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
						recyclerView.setAdapter(notificationsRecyclerAdapter);
					}
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {}
			});
		}

		return view;
	}

}
