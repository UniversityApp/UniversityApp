package sk.branislavremen.universityapp;

import java.util.List;

import com.beyondar.android.fragment.BeyondarFragmentSupport;
import com.beyondar.android.world.GeoObject;
import com.beyondar.android.world.World;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ArActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, LocationListener{

	private BeyondarFragmentSupport mBeyondarFragment;
	World sharedWorld;
	List<GeoObject> geoList;
	Location mCurrentLocation;
	
	protected GoogleApiClient mGoogleApiClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_camera);
		// mBeyondarFragment =
		// (BeyondarFragmentSupport)getFragmentManager().findFragmentById(R.id.beyondarFragment);
		
		
		
		mBeyondarFragment = (BeyondarFragmentSupport) getSupportFragmentManager()
				.findFragmentById(R.id.beyondarFragment);
		Log.d("max distance", mBeyondarFragment.getDistanceFactor() + "");
		float f = 10000;
		mBeyondarFragment.setDistanceFactor(2);
		mBeyondarFragment.setMaxDistanceToRender(f);
		Log.d("max distance2", mBeyondarFragment.getDistanceFactor() + "");
		sharedWorld = new World(this);

		// The user can set the default bitmap. This is useful if you are
		// loading images form Internet and the connection get lost
		sharedWorld.setDefaultImage(R.drawable.ic_launcher);

		// User position (you can change it using the GPS listeners form Android
		// API)
		sharedWorld.setGeoPosition(48.308637, 18.073165);
		
		// Is it also possible to load the image asynchronously form internet
		GeoObject go2 = new GeoObject(2l);
		go2.setGeoPosition(48.309775, 18.073328);
		go2.setImageResource(R.drawable.ic_launcher);
		go2.setName("Online image");

		GeoObject go3 = new GeoObject(22);
		go3.setGeoPosition(48.307775, 18.073028);
		go3.setImageResource(R.drawable.ic_launcher);
		go3.setName("Online image");
		
		// Add the GeoObjects to the world

		sharedWorld.addBeyondarObject(go2);
		sharedWorld.addBeyondarObject(go3);
		//sharedWorld.addBeyondarObject(go4);
		mBeyondarFragment.showFPS(true);

		mBeyondarFragment.setWorld(sharedWorld);
		
		buildGoogleApiClient();
	}
	
	protected synchronized void buildGoogleApiClient() {
	    mGoogleApiClient = new GoogleApiClient.Builder(this)
	        .addConnectionCallbacks(this)
	        .addOnConnectionFailedListener(this)
	        .addApi(LocationServices.API)
	        .build();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		 mCurrentLocation = location;
		 Toast.makeText(this, "gps:"+mCurrentLocation.getLatitude() + " / " + mCurrentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
		 //sharedWorld.setGeoPosition(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
