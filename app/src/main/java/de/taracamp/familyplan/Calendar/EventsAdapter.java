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

import java.util.List;

import de.taracamp.familyplan.Models.Enums.EventCategory;
import de.taracamp.familyplan.Models.Event;
import de.taracamp.familyplan.R;

/**
 * Created by wowa on 20.11.2017.
 */

public class EventsAdapter  extends RecyclerView.Adapter<EventsAdapter.ViewHolder>
{
	private static final String TAG = "familyplan.debug";
	private static final String CLASS = "EventsAdapter";

	private List<Event> events;
	private Context ThisContext;
	private EventsFragment eventsfragment;

	public EventsAdapter(EventsFragment _thisContext,List<Event> _events)
	{
		this.events = _events;
		this.ThisContext = _thisContext.getContext();
		this.eventsfragment = _thisContext;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup _parent, int _viewType)
	{
		View view = LayoutInflater.from(_parent.getContext()).inflate(R.layout.item_event,_parent,false);

		ViewHolder viewHolder = new ViewHolder(view, eventsfragment);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position)
	{
		final Event event = this.events.get(position);

		final ImageView imageViewEventIcon = holder.imageViewEventIcon;
		final TextView textViewEventName = holder.textViewEventName;
		final TextView textViewEventDescription = holder.textViewEventDescription;
		final TextView textViewEventDate = holder.textViewEventDate;

		initializeEventIcon(event.getEventCategory(),imageViewEventIcon);

		textViewEventName.setText(event.getEventName());
		textViewEventDate.setText(event.getEventTime());
		textViewEventDescription.setText(event.getEventDescription());

		holder.itemView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v)
			{
				Log.d(TAG,CLASS+".onCLick() -> open event");

				Intent intent = new Intent(eventsfragment.getActivity().getApplicationContext(),EventDetailActivity.class);
				intent.putExtra("EVENT_TOKEN",event.getEventToken());
				intent.putExtra("USER",holder.eventsfragment.firebaseManager.appUser);
				holder.eventsfragment.getActivity().startActivity(intent);
			}
		});

		holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v)
			{
				Log.d(TAG,CLASS+".onLongClick()");

				EventsActionDialog dialog = EventsActionDialog.newInstance(holder.eventsfragment.firebaseManager,event);
				dialog.show(holder.eventsfragment.getActivity().getFragmentManager(),"action");

				return true;
			}
		});
	}

	private void initializeEventIcon(EventCategory category, ImageView image)
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
		private EventsFragment eventsfragment;

		private ImageView imageViewEventIcon;
		private TextView textViewEventName;
		private TextView textViewEventDescription;
		private TextView textViewEventDate;

		public ViewHolder(View _itemView, EventsFragment _fragment)
		{
			super(_itemView);

			eventsfragment = _fragment;

			imageViewEventIcon = (ImageView) _itemView.findViewById(R.id.imageView_eventItem);
			textViewEventName = (TextView) _itemView.findViewById(R.id.item_event_name);
			textViewEventDescription = (TextView) _itemView.findViewById(R.id.item_event_description);
			textViewEventDate = (TextView) _itemView.findViewById(R.id.item_event_date);
		}
	}
}
