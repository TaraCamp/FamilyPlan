/**
 * @file Message.java
 * @version 1.0
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Models;

import android.content.Context;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

/**
 * Repräsentiert eine Meldung
 * Link: https://github.com/GrenderG/Toasty
 */
public class Message
{
	public static void show(Context _context, String _message, Message.Mode _state)
	{
		if (_state.equals(Mode.ERROR))
		{
			Toasty.error(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
		else if (_state.equals(Mode.SUCCES))
		{
			Toasty.success(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
		else if (_state.equals(Mode.INFO))
		{
			Toasty.info(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
		else if (_state.equals(Mode.WARNING))
		{
			Toasty.warning(_context, _message, Toast.LENGTH_SHORT, true).show();
		}
	}

	/**
	 * Verschiede
	 */
	public enum Mode {
		ERROR,
		SUCCES,
		INFO,
		WARNING
	}
}
