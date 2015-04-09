package sk.branislavremen.universityapp;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button rssNewsButton;
	Button eventsButton;
	Button placesButton;
	Button chatButton;
	Button feedbackButton;
	Button settingsButton;
	Button adminButton;

	String role = "visitor";
	Boolean isTeacherConfirmed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		rssNewsButton = (Button) findViewById(R.id.rssNewsButton);
		eventsButton = (Button) findViewById(R.id.eventsButton);
		placesButton = (Button) findViewById(R.id.placesButton);
		chatButton = (Button) findViewById(R.id.chatButton);
		feedbackButton = (Button) findViewById(R.id.feedbackButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		adminButton = (Button) findViewById(R.id.adminButton);

		checkUser();

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
				Log.d("role", "role: " + role);
				if (role.equalsIgnoreCase("visitor")) {
					Toast.makeText(
							getApplicationContext(),
							"N·vötevnÌk nemÙûe pouûÌvaù t˙to funkciu.\nVyplÚte svoj profil.",
							Toast.LENGTH_LONG).show();
				}

				if (role.equalsIgnoreCase("student")) {
					loadChatView();
				}

				if (role.equalsIgnoreCase("teacher")) {

					if (isTeacherConfirmed) {
						loadChatView();
					} else {
						Toast.makeText(
								getApplicationContext(),
								"NemÙûete pouûÌvaù t˙to funkciu.\nV·ö profil Ëak· na schv·lenie.",
								Toast.LENGTH_LONG).show();
					}
				}

				if (role.equalsIgnoreCase("admin")) {
					Toast.makeText(getApplicationContext(),
							"Admin nemoze pouzivat tuto funkciu",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		feedbackButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						FeedbackActivity.class);
				startActivity(intent);
			}
		});

		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadSettingsView();
			}
		});

		adminButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						AdminActivity.class);
				startActivity(intent);
			}
		});

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		checkUser();
	}

	private void checkUser() {
		final ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser == null) {
			loadLoginView();
		} else {
			currentUser.fetchInBackground(new GetCallback<ParseUser>() {

				@Override
				public void done(ParseUser object, ParseException e) {

					role = object.getString("Role");
					isTeacherConfirmed = object
							.getBoolean("teacherConfirmation");

					if (role.equalsIgnoreCase("visitor")) {
						adminButton.setVisibility(View.GONE);
					}

					if (role.equalsIgnoreCase("student")) {
						adminButton.setVisibility(View.GONE);
					}

					if (role.equalsIgnoreCase("teacher")) {
						if (isTeacherConfirmed) {
							adminButton.setVisibility(View.GONE);
						} else {
							adminButton.setVisibility(View.GONE);
						}
					}

					if (role.equalsIgnoreCase("admin")) {
						adminButton.setVisibility(View.VISIBLE);
					}
			} }); } }

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

	private void loadChatView() {
		Intent intent = new Intent(MainActivity.this, ChatActivity.class);
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
