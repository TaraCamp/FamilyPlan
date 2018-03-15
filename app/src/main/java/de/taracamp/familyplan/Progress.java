package de.taracamp.familyplan;

import android.app.ProgressDialog;
import android.content.Context;

/**
 *
 * Usage: create a new object of this class and use the follow two methods to show and hide the progressDialog
 * Important! : implement in onStop this line -> if (progress!=null) progress.hideProgressDialog();
 */
public class Progress
{
	public ProgressDialog mProgressDialog = null;

	public Progress(Context context)
	{
		this.mProgressDialog = new ProgressDialog(context);
	}

	public void showProgressDialog(String message)
	{
		mProgressDialog.setMessage(message);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.show();
	}

	public void hideProgressDialog()
	{
		if (mProgressDialog != null && mProgressDialog.isShowing()) mProgressDialog.dismiss();
	}
}
