package de.taracamp.familyplan.Calendar;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import de.taracamp.familyplan.Models.Enums.EventCategory;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.R;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder>
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "EventListAdapter";

	private List<Event> events;
	private Context ThisContext;
	private EventsActivity eventsActivity;

	public EventListAdapter(Context _thisContext,List<Event> _events)
	{
		this.events = _events;
		this.ThisContext = _thisContext;

		this.eventsActivity = (EventsActivity) this.ThisContext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType)
	{
		View view = LayoutInflater.from(_parent.getContext()).inflate(R.layout.item_event,_parent,false);

		ViewHolder viewHolder = new ViewHolder(view, eventsActivity);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder _holder, int _position)
	{
		// Select task
		final Event event = this.events.get(_position);

		final ImageView imageViewEventIcon = _holder.imageViewEventIcon;
		final TextView textViewEventName = _holder.textViewEventName;
		final TextView textViewEventDescription = _holder.textViewEventDescription;
		final TextView textViewEventDate = _holder.textViewEventDate;

		initializeEventIcon(event.getEventCategory(),imageViewEventIcon);

		textViewEventName.setText(event.getEventName());
		textViewEventDate.setText(event.getEventTime());
		textViewEventDescription.setText(event.getEventDescription());

		_holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onCLick() -> open event");

				Intent intent = new Intent(eventsActivity.getApplicationContext(),EventDetailActivity.class);
				intent.putExtra("EVENT_TOKEN",event.getEventToken());
				intent.putExtra("USER",_holder.eventsActivity.firebaseManager.appUser);
				eventsActivity.startActivity(intent);
			}
		});

		_holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v)
			{
				Log.d(TAG,CLASS+".onLongClick()");

				EventsActionDialog dialog = EventsActionDialog.newInstance(_holder.eventsActivity.firebaseManager,event);
				dialog.show(_holder.eventsActivity.getFragmentManager(),"action");

				return true;
			}
		});
	}

	private void initializeEventIcon(EventCategory category,ImageView image)
	{
		if (category.equals(EventCategory.NOTHING))
		{
			image.setImageResource(R.drawable.ic_action_calendar);
		}
		else if (category.equals(EventCategory.SCHOOL))
		{
			image.setImageResource(R.drawable.school);
		}
		else if (category.equals(EventCategory.PARTY))
		{
			image.setImageResource(R.drawable.party);
		}
		else if (category.equals(EventCategory.JOB))
		{
			image.setImageResource(R.drawable.work);
		}
		else if (category.equals(EventCategory.BIRTHDAY))
		{
			image.setImageResource(R.drawable.birthday);
		}
		else if (category.equals(EventCategory.EXCURSION))
		{
			image.setImageResource(R.drawable.excursion);
		}
		else if (category.equals(EventCategory.DATE))
		{
			image.setImageResource(R.drawable.birthday);
		}
		else if (category.equals(EventCategory.SPORT))
		{
			image.setImageResource(R.drawable.excursion);
		}
	}

	@Override
	public int getItemCount()
	{
		if(this.events!=null)
		{
			return this.events.size();
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Eine Innere Klasse, mit dieser wird ein Behälter für die Steuerelemente eines Items definiert.
	 */
	public class ViewHolder extends RecyclerView.ViewHolder
	{
		private EventsActivity eventsActivity;

		private ImageView imageViewEventIcon;
		private TextView textViewEventName;
		private TextView textViewEventDescription;
		private TextView textViewEventDate;

		public ViewHolder(View _itemView, EventsActivity _eventsActivity)
		{
			super(_itemView);

			eventsActivity = _eventsActivity;

			imageViewEventIcon = (ImageView) _itemView.findViewById(R.id.imageView_eventItem);
			textViewEventName = (TextView) _itemView.findViewById(R.id.item_event_name);
			textViewEventDescription = (TextView) _itemView.findViewById(R.id.item_event_description);
			textViewEventDate = (TextView) _itemView.findViewById(R.id.item_event_date);
		}
	}
}
