package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sk.branislavremen.universityapp.vo.PlaceData;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.view.BeyondarViewAdapter;
import com.beyondar.android.view.OnClickBeyondarObjectListener;
import com.beyondar.android.world.BeyondarObject;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class ArActivity extends FragmentActivity implements OnClickBeyondarObjectListener,
OnClickListener{

	private BeyondarFragmentSupport mBeyondarFragment;
	World sharedWorld;

	private List<BeyondarObject> showViewOn;

   	double lat = 0;
    double lon = 0;
    
    int pocet = 0;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_camera);
		showViewOn = Collections.synchronizedList(new ArrayList<BeyondarObject>());
		
		/* AR */

		mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager()
				.findFragmentById(R.id.beyondarFragment);
		Log.d("max distance", mBeyondarFragment.getDistanceFactor() + "");
		float f = 1000;
		mBeyondarFragment.setDistanceFactor(6);
		mBeyondarFragment.setMaxDistanceToRender(f);
		Log.d("max distance2", mBeyondarFragment.getDistanceFactor() + "");
		sharedWorld = new World(this);

		// The user can set the default bitmap. This is useful if you are
		// loading images form Internet and the connection get lost
		sharedWorld.setDefaultImage(R.drawable.ic_launcher);

		// User position (you can change it using the GPS listeners form Android
		// API)
		getLocation();
		
		mBeyondarFragment.setOnClickBeyondarObjectListener(this);

		CustomBeyondarViewAdapter customBeyondarViewAdapter = new CustomBeyondarViewAdapter(this);
		mBeyondarFragment.setBeyondarViewAdapter(customBeyondarViewAdapter);
		

		// Is it also possible to load the image asynchronously form internet
		/*
		 * GeoObject go2 = new GeoObject(2l); go2.setGeoPosition(48.309775,
		 * 18.073328); go2.setImageResource(R.drawable.ic_launcher);
		 * go2.setName("Online image");
		 * 
		 * GeoObject go3 = new GeoObject(22); go3.setGeoPosition(48.307775,
		 * 18.073028); go3.setImageResource(R.drawable.ic_launcher);
		 * go3.setName("Online image");
		 */

		// Add the GeoObjects to the world

		// sharedWorld.addBeyondarObject(go2);
		// sharedWorld.addBeyondarObject(go3);
		// sharedWorld.addBeyondarObject(go4);
		mBeyondarFragment.showFPS(true);

		mBeyondarFragment.setWorld(sharedWorld);
		
		

	}
	
	public void addObjectToAR(double lat, double lon, int iconResId, String name){
		GeoObject go = new GeoObject(2l); 
		go.setGeoPosition(lat, lon); 
		go.setImageResource(iconResId);
		go.setName(name);
		
		showViewOn.add(go);
		sharedWorld.addBeyondarObject(go);
		
		Log.d("objectAR", "object added " + name);
	}

    private void getLocation() {
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
            // lat = 48.308637;
            // lon = 18.073165;
            sharedWorld.setGeoPosition(lat, lon);
           
            refreshPlaces(lat, lon);

 

        } catch (NullPointerException e) {
            lat = -1.0;
            lon = -1.0;
        }
        
        Log.d("GPS", "Current gps: " + lat + " / " + lon);
    }
    
    

	public void refreshPlaces(double lat, double lon) {
		
		ParseGeoPoint point = new ParseGeoPoint(lat, lon);
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Places");
		setProgressBarIndeterminateVisibility(true);
		query.orderByAscending("Title");
		query.whereWithinKilometers("gps", point, 1d);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					for (ParseObject object : objects) {
						
						/*ParseFile f = null;
						if(object.getParseFile("Picture").isDataAvailable()){
							f = object.getParseFile("Picture");
							f.getDataInBackground();
						}*/
						
						pocet++;
						
						addObjectToAR(object.getParseGeoPoint("gps").getLatitude(), object.getParseGeoPoint("gps").getLongitude(), R.drawable.ic_launcher, object.getString("Nazov"));
						
						
					}
					
					Toast.makeText(getApplicationContext(), "V rádiuse 1km sa nachádza " + pocet + " miest.", Toast.LENGTH_SHORT).show();
				} else {
					Log.e(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
					Toast.makeText(getApplicationContext(), "Internetové pripojenie nie je k dispozicii", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		//Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	public void onClickBeyondarObject(ArrayList<BeyondarObject> beyondarObjects) {
		if (beyondarObjects.size() == 0) {
			return;
		}
		BeyondarObject beyondarObject = beyondarObjects.get(0);
		if (showViewOn.contains(beyondarObject)) {
			showViewOn.remove(beyondarObject);
		} else {
			showViewOn.add(beyondarObject);
		}
	}

	private class CustomBeyondarViewAdapter extends BeyondarViewAdapter {

		LayoutInflater inflater;

		public CustomBeyondarViewAdapter(Context context) {
			super(context);
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public View getView(BeyondarObject beyondarObject, View recycledView, ViewGroup parent) {
			if (!showViewOn.contains(beyondarObject)) {
				return null;
			}
			if (recycledView == null) {
				recycledView = inflater.inflate(R.layout.beyondar_object_view, null);
			}

			TextView textView = (TextView) recycledView.findViewById(R.id.titleTextView);
			textView.setText(beyondarObject.getName());
			TextView distTextView = (TextView) recycledView.findViewById(R.id.distanceTextView);
			int dist = (int) beyondarObject.getDistanceFromUser();
			distTextView.setText(dist + " m");
			
			//Button button = (Button) recycledView.findViewById(R.id.button);
			//button.setOnClickListener(ArActivity.this);

			// Once the view is ready we specify the position
			setPosition(beyondarObject.getScreenPositionTopRight());

			return recycledView;
		}

	}


}
