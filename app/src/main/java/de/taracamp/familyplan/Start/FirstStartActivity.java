/**
 * @file FirstStartActivity.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Start;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import de.taracamp.familyplan.R;

public class FirstStartActivity extends AppCompatActivity
{
	private static final String TAG = "familyplan.debug";

	private Button buttonCreateFamily = null;
	private Button buttonSearchFamily = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_start);

		this.buttonCreateFamily = (Button) findViewById(R.id.button_start_create);
		this.buttonCreateFamily.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":FirstStartActivity.click()-> open dialog for new family");

				Intent intent = new Intent(getApplicationContext(),FamilyCreatorActivity.class);
				startActivity(intent);
			}
		});

		this.buttonSearchFamily = (Button) findViewById(R.id.button_start_search);
		this.buttonSearchFamily.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,":FirstStartActivity.click()-> open dialog for searching family");

				Intent intent = new Intent(getApplicationContext(),FamilySearchActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		super.onBackPressed();
	}
}
