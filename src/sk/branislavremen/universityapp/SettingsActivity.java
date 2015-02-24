package sk.branislavremen.universityapp;

import java.io.ByteArrayOutputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import sk.branislavremen.universityapp.adapter.StudyProgrammesExpListViewAdapter;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.ContactsContract.PinnedPositions;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	// init
	final int PASSWORD_LENG = 6;
	final int RESULT_LOAD_IMG = 1;
	final String SPINNER_DEFAULT_VALUE = "Vyberte si z možností...";

	final String[] EMPTY_ARRAY = { SPINNER_DEFAULT_VALUE };
	final String[] FAKULTA_ARRAY = { "FPV", "FF", "PF", "FSVAZ", "FSS" };
	final String[] STUPEN_ARRAY = { "Bakalársky I. St.", "Magisterský II. St.",
			"Doktorandské III. St.", "Iné(RNDr.) N st.", "Iné(PaedDr.) N st.",
			"Iné N st." };
	final String[] FORMA_ARRAY = { "Denná forma", "Externá forma" };
	final String[] DRUH_ARRAY = { "Jednoodborové štúdium", "Uèite¾ské štúdium",
			"Rigorózne štúdium", "Rozširujúce štúdium" };

	String pictureString;
	Drawable pictureDraw;
	Bitmap pictureBitMap;
	ParseFile pictureParseFile;

	TextView usernameTextView;
	TextView titleTextView;
	TextView nameTextView;
	TextView surnameTextView;
	TextView emailTextView;
	
	TextView password1TextView;
	TextView password2TextView;

	ImageView avatarImageView;

	StudyProgrammesExpListViewAdapter studyProgrammesAdapter;
	ExpandableListView fakultaListView;
	ExpandableListView stupenListView;
	ExpandableListView formaListView;
	ExpandableListView druhListView;
	ExpandableListView programListView;
	
	EditText rocnikEditText;

	// Button saveButton;
	Button changePasswordButton;

	List<String> studyProgrammeTitlesList;
	List<String> studyProgrammeCodeList;




	boolean isNewAvatarLoaded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		isNewAvatarLoaded = false;
		
		studyProgrammeTitlesList = new ArrayList<String>();
		studyProgrammeCodeList = new ArrayList<String>();
		
		

		usernameTextView = (TextView) findViewById(R.id.settings_username_edittext);
		titleTextView = (TextView) findViewById(R.id.settings_title_edittext);
		nameTextView = (TextView) findViewById(R.id.settings_name_edittext);
		surnameTextView = (TextView) findViewById(R.id.settings_surname_edittext);
		emailTextView = (TextView) findViewById(R.id.settings_email_edittext);
		password1TextView = (TextView) findViewById(R.id.settings_password_edittext);
		password2TextView = (TextView) findViewById(R.id.settings_password_again_edittext);

		avatarImageView = (ImageView) findViewById(R.id.settings_avatar_imageview);

		rocnikEditText = (EditText) findViewById(R.id.settings_rocnik_edittext);

		changePasswordButton = (Button) findViewById(R.id.settings_change_password_button);

		avatarImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// open image picker
				// Create intent to Open Image applications like Gallery, Google
				// Photos
				Intent galleryIntent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				// Start the Intent
				startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
			}
		});

		changePasswordButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (isPasswordValidated(password1TextView.getText().toString(),
						password2TextView.getText().toString())) {
					changePassword(password1TextView.getText().toString());
				}

			}
		});

		if (ParseUser.getCurrentUser() != null) {
			getUserData();
		}

	}
	


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
					&& null != data) {
				// Get the Image from data
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				// Get the cursor
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				// Move to first row
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				pictureString = cursor.getString(columnIndex);
				cursor.close();

				pictureBitMap = BitmapFactory.decodeFile(pictureString);
				// Set the Image in ImageView after decoding the String
				avatarImageView.setImageBitmap(pictureBitMap);

				isNewAvatarLoaded = true;

			} else {
				Toast.makeText(this, "You haven't picked Image",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}

	}

	/* nacitanie studijnych programov pre autocomplettextview - ZACIATOK */
	public void getStudyProgrammes() {
		// studyProgrammesList.clear();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("StudyProgrammes");
		setProgressBarIndeterminateVisibility(true);
		/*query.whereEqualTo("Fakulta", fakultaSpinner.getSelectedItem()
				.toString());
		query.whereEqualTo("DruhStudia", druhStudiaSpinner.getSelectedItem()
				.toString());
		query.whereEqualTo("Forma", formaSpinner.getSelectedItem()
				.toString());
		query.whereEqualTo("Stupen", stupenStudiaSpinner.getSelectedItem()
				.toString());*/
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					Log.i("autocomplet", "done ok");
					getStudyProgrammesDataList(parseList);
				} else {
					Log.i("autocomplet", "error");
					Log.e(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
				}
			}
		});
	}

	public void getStudyProgrammesDataList(List<ParseObject> objects) {
		int pocitadlo = 0;
		for (ParseObject object : objects) {
			// pre autocomplet textview
			pocitadlo++;
			studyProgrammeTitlesList.add(object.getString("Skratka") + " - "
					+ object.getString("Popis"));
		}
		Log.d("debug", "pocet:" + pocitadlo);
		// autocompleteAdapter.notifyDataSetChanged();
		//adapter refresh !!!!!!!!!§§ HERE!!!!

	}

	/* nacitanie studijnych programov pre autocomplettextview - KONIEC */

	/* nacitanie dat do UI - zaciatok */
	public void getUserData() {

		ParseUser myParseUser = ParseUser.getCurrentUser();

		usernameTextView.setText(myParseUser.getUsername());
		titleTextView.setText(myParseUser.getString("Title"));
		nameTextView.setText(myParseUser.getString("Name"));
		surnameTextView.setText(myParseUser.getString("Surname"));
		emailTextView.setText(myParseUser.getString("email"));

		ParseFile imageFile = (ParseFile) myParseUser.get("Picture");
		imageFile.getDataInBackground(new GetDataCallback() {
		  public void done(byte[] data, ParseException e) {
		    if (e == null) {
		      // data has the bytes for the image
		    	pictureBitMap = BitmapFactory.decodeByteArray(data, 0, data.length);
		    	avatarImageView.setImageBitmap(pictureBitMap);
		    } else {
		      // something went wrong
		    }
		  }
		});
		
	}

	/* nacitanie dat do UI - koniec */

	/* UPDATE PROFILU POUZIVATELA - ZACIATOK */

	public boolean isAllProfileFilled() {
		boolean result = false;

		if (titleTextView.getText().toString().length() == 0) {
			result = true;
		}

		if (nameTextView.getText().toString().length() == 0) {
			result = true;
		}

		if (surnameTextView.getText().toString().length() == 0) {
			result = true;
		}

		if (!isProgrammeFromList()) {
			result = true;
		}

		return result;
	}

	/*
	 * aby som zarucil, ze nebudu pisat bludy pri studijnych programoch, budem
	 * overovat ci pridali popis co im ponuklo v autocomplete okne. musia vybrat
	 * moznost z toho okna inak ich to nepusti dalej
	 */
	public boolean isProgrammeFromList() {
		boolean result = false;
		/*
		 * String text = studyProgrammeTextView.getText().toString(); if
		 * (studyProgrammeTitlesList.contains(text) && text.length() > 0) {
		 * result = true; }
		 */
		return result;
	}

	public void save() {
		if (isAllProfileFilled()) {
			setUserData();
		} else {
			// upozornit ze treba vsetko vyplnit
		}
	}

	public void setUserData() {

		// tu riesim aj upload obrazku/avataru
		// avatar uploadnem do systemu

		ParseUser pu = ParseUser.getCurrentUser();

		pu.put("Titul", titleTextView.getText().toString());
		pu.put("Meno", nameTextView.getText().toString());
		pu.put("Priezvisko", surnameTextView.getText().toString());
		// pu.put("StudyProgramme",
		// studyProgrammeTextView.getText().toString());

		if (isNewAvatarLoaded) {
			pu.put("Picture", newImageParseFile());
		}

		pu.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					// ok
					Log.i("update profil", "updated ok!");
				} else {
					// error
					Log.e("update profil", "update error!");
				}
			}
		});

	}

	/* UPDATE PROFILU POUZIVATELA - KONIEC */

	/* VYBER OBRAZKA Z GALERIE - ZACIATOK */
	public ParseFile newImageParseFile() {
		// Drawable drawable =
		// getResources().getDrawable(R.drawable.ic_launcher);
		// Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		pictureBitMap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
		byte[] data = stream.toByteArray();

		ParseFile file = new ParseFile("avatar.png", data);
		file.saveInBackground();
		return file;
		// pu.put("Picture", file);
	}

	/* VYBER OBRAZKA Z GALERIE - KONIEC */

	/* ZMENA HESLA ZACIATOK */
	public boolean isPasswordValidated(String pass1, String pass2) {
		boolean result = false;
		if (pass1.equals(pass2)) {
			if (pass1.length() >= PASSWORD_LENG) {
				result = true;
			} else {
				Log.d("change password",
						"passwords is too short: " + pass1.length());
			}
		} else {
			Log.d("change password", "passwords are not equeal");
		}
		return result;
	}

	public void changePassword(String passwd) {
		ParseUser pu = ParseUser.getCurrentUser();
		pu.setPassword(passwd);
		pu.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e != null) {
					Log.i("change password", "password succesfully changed!");
				} else {
					Log.e("change password", "error: " + e.getMessage());
				}
			}
		});
	}

	/* ZMENA HESLA KONIEC */

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
		case R.id.action_logout:
			ParseUser.logOut();
			finish();
			break;

		case R.id.action_save:
			save();
			finish();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
