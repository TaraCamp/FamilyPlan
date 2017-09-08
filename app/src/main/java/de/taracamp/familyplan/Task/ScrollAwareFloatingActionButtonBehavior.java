/**
 * @file ScrollAwareFloatingActionButtonBehavior.java
 * @version 0.1
 * @copyright 2017 TaraCamp Community
 * @author Wladimir Tarasov <wladimir.tarasov@tarakap.de>
 */
package de.taracamp.familyplan.Task;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wowa on 08.09.2017.
 */
public class ScrollAwareFloatingActionButtonBehavior extends FloatingActionButton.Behavior
{
	@Override
	public boolean onStartNestedScroll(CoordinatorLayout _coordinatorLayout,FloatingActionButton _child, View _directTargetChild, View _target, int _nestedScrollAxes)
	{
		return _nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL || super.onStartNestedScroll(_coordinatorLayout, _child, _directTargetChild, _target, _nestedScrollAxes);
	}

	@Override
	public void onNestedScroll(CoordinatorLayout _coordinatorLayout, FloatingActionButton _child, View _target, int _dxConsumed, int _dyConsumed, int _dxUnconsumed, int _dyUnconsumed)
	{
		super.onNestedScroll(_coordinatorLayout, _child, _target, _dxConsumed, _dyConsumed, _dxUnconsumed, _dyUnconsumed);

		if (_dyConsumed > 0 && _child.getVisibility() == View.VISIBLE)
		{
			_child.hide();
		}
		else if (_dyConsumed < 0 && _child.getVisibility() != View.VISIBLE)
		{
			_child.show();
		}
	}

	public ScrollAwareFloatingActionButtonBehavior(Context _context, AttributeSet _attrs)
	{
		super();
	}
}
