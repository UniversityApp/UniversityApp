package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import sk.branislavremen.universityapp.adapter.EventItemAdapter;
import sk.branislavremen.universityapp.vo.EventData;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

public class EventsActivity extends ListActivity {

	public ArrayList<EventData> eventDataList;
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

	public List<EventData> getEventDataList() {
		return eventDataList;
	}

	public void setEventDataList(ArrayList<EventData> eventDataList) {
		this.eventDataList = eventDataList;
	}

	public EventItemAdapter getItemAdapter() {
		return itemAdapter;
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.events, menu);

		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.search)
				.getActionView();
		if (null != searchView) {
			searchView.setSearchableInfo(searchManager
					.getSearchableInfo(getComponentName()));
			searchView.setIconifiedByDefault(false);
		}

		SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
			public boolean onQueryTextChange(String newText) {
				// this is your adapter that will be filtered
				Log.i("search", ">>" + newText);

				itemAdapter.getFilter().filter(newText);
				return true;
			}

			public boolean onQueryTextSubmit(String query) {
				// Here u can get the value "query" which is entered in the
				// search box.
				return false;
			}
		};
		searchView.setOnQueryTextListener(queryTextListener);

		return true;
	}

}
