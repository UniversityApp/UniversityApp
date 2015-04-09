package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;





import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import sk.branislavremen.universityapp.adapter.EventItemAdapter;
import sk.branislavremen.universityapp.adapter.PlaceItemAdapter;
import sk.branislavremen.universityapp.vo.EventData;
import sk.branislavremen.universityapp.vo.PlaceData;
import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.SearchView;
import android.widget.Toast;

public class PlaceActivity extends ListActivity {

	PlaceData pd;
	ArrayList<PlaceData> pd_list;
	PlaceItemAdapter itemAdapter;

   	double lat = 0;
    double lon = 0;
    

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
			
			/*ParseFile f = null;
			if(object.getParseFile("Picture").isDataAvailable()){
				f = object.getParseFile("Picture");
				f.getDataInBackground();
			}*/
			
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
		getMenuInflater().inflate(R.menu.place, menu);
		
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
			getLocationAndStartActivity();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	/* KONIEC MENU */
	    private void getLocationAndStartActivity() {
	        // Get the location manager
	        LocationManager locationManager = (LocationManager) 
	                getSystemService(LOCATION_SERVICE);
	        Criteria criteria = new Criteria();
	        String bestProvider = locationManager.getBestProvider(criteria, false);
	        Location location = locationManager.getLastKnownLocation(bestProvider);
	        LocationListener loc_listener = new LocationListener() {

	            public void onLocationChanged(Location l) {}

	            public void onProviderEnabled(String p) {}

	            public void onProviderDisabled(String p) {}

	            public void onStatusChanged(String p, int status, Bundle extras) {}
	        };
	        locationManager
	                .requestLocationUpdates(bestProvider, 0l, 0f, loc_listener);
	        location = locationManager.getLastKnownLocation(bestProvider);
	        try {
	            lat = location.getLatitude();
	            lon = location.getLongitude();
	            
	            Intent intent = new Intent(PlaceActivity.this,
						ArActivity.class);
				startActivity(intent);
	        } catch (NullPointerException e) {
	            lat = -1.0;
	            lon = -1.0;
	            Toast.makeText(this, "Poloha GPS nie je dostupná, skúste to neskôr.", Toast.LENGTH_SHORT).show();
	        }
	        
	        Log.d("GPS", "Current gps: " + lat + " / " + lon);
	    }

}
