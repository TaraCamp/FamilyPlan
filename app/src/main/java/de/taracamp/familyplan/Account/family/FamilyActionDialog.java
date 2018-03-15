package de.taracamp.familyplan.Account.family;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.taracamp.familyplan.Models.Family;
import de.taracamp.familyplan.Models.FirebaseHelper.FirebaseManager;
import de.taracamp.familyplan.Models.User;
import de.taracamp.familyplan.R;

public class FamilyActionDialog extends DialogFragment
{
	private static FirebaseManager firebaseManager;

	private RecyclerView recyclerView = null;
	private static List<Family> families = null;
	private static String userToken = null;
	private FamiliesRecyclerAdapter familiesRecyclerAdapter = null;

	public FamilyActionDialog(){}

	public static FamilyActionDialog newInstance(FirebaseManager _firebaseManager,List<Family> _families,String _userToken)
	{
		FamilyActionDialog fragment = new FamilyActionDialog();
		firebaseManager = _firebaseManager;
		families = _families;
		userToken = _userToken;
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
		View rootView = inflater.inflate(R.layout.dialog_families, container, false);

		recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_familydialog_families);
		familiesRecyclerAdapter = new FamiliesRecyclerAdapter(getActivity().getApplicationContext(),families,firebaseManager,userToken);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
		recyclerView.setAdapter(familiesRecyclerAdapter);

		getDialog().setTitle("Wähle eine Familie aus");

		return rootView;
	}

}
