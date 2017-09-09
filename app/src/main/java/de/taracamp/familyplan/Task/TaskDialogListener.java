/**
 * @file TaskDialogListener.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import de.taracamp.familyplan.Models.Task;

/**
 * Created by wowa on 08.09.2017.
 */
public interface TaskDialogListener
{
	public void onFinishTaskDialog(Task _newTask);
}
