package de.taracamp.familyplan.Models;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class Message
{
	public static void show(Context _context, String _message, String _state)
	{
		if (_state.equals("ERROR"))
		{
			Toasty.error(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
		else if (_state.equals("SUCCES"))
		{
			Toasty.success(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
		else if (_state.equals("INFO"))
		{
			Toasty.info(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
		else if (_state.equals("WARNING"))
		{
			Toasty.warning(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
	}
}
