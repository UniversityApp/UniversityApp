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

	//public List<PlaceData> placeDataList;
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

		//placeDataList = new ArrayList<PlaceData>();
		eventDataList = new ArrayList<EventData>();

		itemAdapter = new EventItemAdapter(this, eventDataList);

		setListAdapter(itemAdapter);

		refreshEvents();
	}

	//nepouzivam
	public void refreshAll() {

		// ked sa pripnu vsetky miesta, zacne sa nacitavat eventlist
		refreshPlaces();

	}

	//nepouzivam
	public void refreshPlaces() {
		//placeDataList.clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
		setProgressBarIndeterminateVisibility(true);

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					pinAllPlaces(parseList);
				} else {
					Log.e(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
				}
			}
		});
	}

	public void refreshEvents() {
		eventDataList.clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Events");
		setProgressBarIndeterminateVisibility(true);

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					pinAllEvents(parseList);
				} else {
					Log.e(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
				}
			}
		});
	}

	public void pinAllEvents(List<ParseObject> parseList) {
		// pripnem ich do lokalnej pamate
		ParseObject.pinAllInBackground("localEvents", parseList,
				new SaveCallback() {

					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub

						if (e == null) {
							loadEventsFromLocalMemory();
						} else {
							Log.e(getClass().getSimpleName(),
									"Error: " + e.getMessage());
							// nie je pripojenie
						}

					}
				});
	}

	//nepouzivam
	public void pinAllPlaces(List<ParseObject> parseList) {
		// pripnem ich do lokalnej pamate
		ParseObject.pinAllInBackground("localPlaces", parseList,
				new SaveCallback() {

					@Override
					public void done(ParseException e) {
						// TODO Auto-generated method stub

						if (e == null) {

							refreshEvents();

						} else {
							Log.e(getClass().getSimpleName(),
									"Error: " + e.getMessage());
							// nie je pripojenie
						}

					}
				});
	}

	public void loadEventsFromLocalMemory() {
		ParseQuery<ParseObject> testQuery = ParseQuery.getQuery("Events");
		testQuery.fromPin("localEvents");
		// nacitam ich z lokalnej pamate
		testQuery.orderByDescending("StartDate");
		testQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated
				// method stub

				createEventDataList(objects);

			}
		});
	}

	public void createEventDataList(List<ParseObject> objects) {

		for (ParseObject object : objects) {

			//clearData();

			eventTitle = object.getString("Title");
			eventDescription = object.getString("Description");
			eventPlace = object.getString("Place");
			// Log.e("tag", "tit: " + eventTitle + " descr: " + eventDescription
			// + " objid: " + eventPlaceId);
			startEvent = object.getDate("StartDate");
			endEvent = object.getDate("EndDate");
			
			
			addNewEventData(eventTitle, eventDescription,
					startEvent, endEvent, eventPlace);
			
			 itemAdapter.notifyDataSetChanged();
				Log.e("tag", "done");}
			
			// if(eventPlaceId != null && eventPlaceId.length() > 0){
			// Log.e("tag", "event nie je null " + eventPlaceId);

			/*ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
			query.fromPin("localPlaces");
			query.getInBackground(eventPlaceId, new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if (e == null) {
						// object will be your game score
						eventPlaceData = new PlaceData(object.getObjectId(),
								object.getParseGeoPoint("Place"), object
										.getString("Title"), object
										.getString("Type"), object
										.getString("Detail"));
						Log.i("tag", "place tit: " + eventPlaceData.getTitle());
						Log.i("tag", "event tit: " + eventTitle + " descr: "
								+ eventDescription + " objid: " + eventPlaceId);
						
						
						// itemAdapter.notifyDataSetChanged();
					} else {
						// something went wrong
						Log.e("err", e.getMessage());
						Log.e("tag", "event tit: " + eventTitle + " descr: "
								+ eventDescription + " objid: " + eventPlaceId);
						addNewEventData(eventTitle, eventDescription,
								startEvent, endEvent, null);
					}
					itemAdapter.notifyDataSetChanged();
				}
			});
			/*
			 * } else { Log.e("tag", "event je null");
			 * addNewEventData(eventTitle, eventDescription, startEvent,
			 * endEvent, eventPlaceData); }
			 */

		//}
		
	}

	//nepouzivam
	public void clearData() {
		//eventPlaceData = null;
		eventTitle = "";
		eventDescription = "";
		eventPlace = "";
	}

	public void addNewEventData(String title, String descr, Date start,
			Date end, String place) {
		
		EventData event = new EventData(title, descr, start, end, place);
		eventDataList.add(event);
	}

}
