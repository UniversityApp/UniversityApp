package sk.branislavremen.universityapp;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button rssNewsButton;
	Button eventsButton;
	Button placesButton;
	Button chatButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			loadLoginView();
		} else {
			currentUser.fetchInBackground(new GetCallback<ParseUser>() {

				@Override
				public void done(ParseUser object, ParseException e) {
					// TODO Auto-generated method stub
					if(e == null){
					if(!currentUser.getBoolean("isAllFilled")){
						loadSettingsView();
					}
					} else {
						ParseUser.logOut();
						loadLoginView();
					}
				}
			});
		}

		rssNewsButton = (Button) findViewById(R.id.rssNewsButton);
		eventsButton = (Button) findViewById(R.id.eventsButton);
		placesButton = (Button) findViewById(R.id.placesButton);
		chatButton = (Button) findViewById(R.id.chatButton);

		rssNewsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						PostsActivity.class);
				startActivity(intent);
			}
		});

		eventsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						EventsActivity.class);
				startActivity(intent);
			}
		});

		placesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						PlaceActivity.class);
				startActivity(intent);
			}
		});

		
		chatButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						ChatActivity.class);
				startActivity(intent);
			}
		});
		
		
	}

	private void loadLoginView() {
		Intent intent = new Intent(this, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}

	private void loadSettingsView() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
		case R.id.action_logout:
			ParseUser.logOut();
			loadLoginView();
			break;

		case R.id.action_settings:
			loadSettingsView();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
