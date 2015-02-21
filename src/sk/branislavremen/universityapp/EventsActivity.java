package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.branislavremen.universityapp.adapter.EventItemAdapter;
import sk.branislavremen.universityapp.vo.EventData;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class EventsActivity extends ListActivity {

	public List<EventData> eventDataList;
	public EventItemAdapter itemAdapter;

	String eventTitle;
	String eventDescription;
	String eventPlace;
	Date startEvent;
	Date endEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_events);

		eventDataList = new ArrayList<EventData>();

		itemAdapter = new EventItemAdapter(this, eventDataList);

		setListAdapter(itemAdapter);

		refreshEvents();
	}

	public void refreshEvents() {
		eventDataList.clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
		setProgressBarIndeterminateVisibility(true);
		query.orderByDescending("StartDate");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {

					createEventDataList(parseList);
				} else {
					Log.e(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
				}
			}
		});
	}

	public void createEventDataList(List<ParseObject> objects) {

		eventDataList.clear();

		for (ParseObject object : objects) {

			eventTitle = object.getString("Title");
			eventDescription = object.getString("Description");
			eventPlace = object.getString("Place");
			startEvent = object.getDate("StartDate");
			endEvent = object.getDate("EndDate");

			addNewEventData(eventTitle, eventDescription, startEvent, endEvent,
					eventPlace);

			itemAdapter.notifyDataSetChanged();
			Log.e("tag", "done");
		}

	}

	public void addNewEventData(String title, String descr, Date start,
			Date end, String place) {

		EventData event = new EventData(title, descr, start, end, place);
		eventDataList.add(event);
	}

}
