/**
 * @file TaskDialogFragment.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import de.taracamp.familyplan.R;

/**
 * Created by wowa on 08.09.2017.
 */
public class TaskDialogFragment extends DialogFragment
{
	public interface TaskDialogListener
	{
		public void onDialogPositveClick(DialogFragment dialog);
		public void onDialogNegativeClick(DialogFragment dialog);
	}

	private TaskDialogListener TaskListener = null;

	@Override
	public void onAttach(Activity _activity)
	{
		super.onAttach(_activity);

		try
		{
			this.TaskListener = (TaskDialogListener) _activity;
		}
		catch (ClassCastException e)
		{
			throw new ClassCastException(_activity.toString() + " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		builder.setView(inflater.inflate(R.layout.dialog_task_add,null)).setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int id)
			{
				// Send the positive button event back to the host activity
				TaskListener.onDialogPositveClick(TaskDialogFragment.this);
			}

		}).setNegativeButton("Abbrechen", new DialogInterface.OnClickListener(){

			public void onClick(DialogInterface dialog, int id)
			{
				// Send the negative button event back to the host activity
				TaskListener.onDialogNegativeClick(TaskDialogFragment.this);
			}

		});

		return builder.create();
	}

}
