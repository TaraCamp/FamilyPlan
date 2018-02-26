/**
 * @file ProfileFragment.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Account.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import de.hdodenhof.circleimageview.CircleImageView;
import de.taracamp.familyplan.Login.LoginActivity;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.R;

/**
 * Represent the profile page of a user.
 */
public class ProfileFragment extends Fragment
{
	private FirebaseAuth firebaseAuth = null;
	private static FirebaseManager firebaseManager;

	private CircleImageView circleImageViewUserPhoto = null;
	private TextView textViewUsername = null;
	private Button buttonLogout = null;

	public ProfileFragment(){}

	/**
	 * Create a new ProfileFragment instance and set arguments
	 *
	 * @param _firebaseManager Helper class for Firebase and AppUser
	 * @return fragment that be used.
	 */
	public static ProfileFragment newInstance(FirebaseManager _firebaseManager)
	{
		ProfileFragment fragment = new ProfileFragment();
		firebaseManager = _firebaseManager;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
	{
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_profile, container, false);

		firebaseAuth = FirebaseAuth.getInstance();

		circleImageViewUserPhoto = (CircleImageView) view.findViewById(R.id.circleimageview_profile_photo);

		textViewUsername = (TextView) view.findViewById(R.id.textview_profile_name);
		textViewUsername.setText(firebaseManager.appUser.getUserName());

		buttonLogout = (Button) view.findViewById(R.id.button_profile_logout);
		buttonLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view)
			{
				firebaseAuth.signOut(); // logout

				Intent intent = new Intent(container.getContext(), LoginActivity.class);
				startActivity(intent);
			}
		});

		return view;
	}

}
