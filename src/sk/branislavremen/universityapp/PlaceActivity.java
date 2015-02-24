package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import sk.branislavremen.universityapp.adapter.EventItemAdapter;
import sk.branislavremen.universityapp.adapter.PlaceItemAdapter;
import sk.branislavremen.universityapp.vo.EventData;
import sk.branislavremen.universityapp.vo.PlaceData;
import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class PlaceActivity extends ListActivity {

	PlaceData pd;
	ArrayList<PlaceData> pd_list;
	PlaceItemAdapter itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_place);

		pd_list = new ArrayList<PlaceData>();
		itemAdapter = new PlaceItemAdapter(this, pd_list);
		setListAdapter(itemAdapter);

		refreshPlaces();

	}

	public void refreshPlaces() {
		pd_list.clear();
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
		setProgressBarIndeterminateVisibility(true);
		query.orderByAscending("Title");
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					createPlaceDataList(parseList);
				} else {
					Log.e(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
				}
			}
		});
	}

	public void createPlaceDataList(List<ParseObject> objects) {
		for (ParseObject object : objects) {

			PlaceData place = new PlaceData(object.getString("Nazov"),
					object.getString("Adresa"), object.getString("Typ"),
					object.getParseGeoPoint("gps"), object.getString("Detail"),
					object.getParseFile("Picture"));
			pd_list.add(place);

		}
		itemAdapter.notifyDataSetChanged();
		Log.e("tag", "done");

	}
	
	/* MENU */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		/*
		 * if (id == R.id.action_settings) { return true; }
		 */

		switch (id) {

		case R.id.action_ar:
			//open ar view
		
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}
