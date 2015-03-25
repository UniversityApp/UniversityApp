package sk.branislavremen.universityapp;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class SettingsActivity extends Activity {

	/* init */
	final int PASSWORD_LENG = 6;
	final int RESULT_LOAD_IMG = 1;
	final int RESULT_STUDY_DATA = 2;

	/* values on parse */
	String username;
	String email;
	String name;
	String role;
	String program;
	String rocnik;
	
	LinearLayout studySection;

	Boolean isTeacherConfirmed;

	String pictureString;
	Bitmap pictureBitMap;
	ParseFile pictureParseFile;

	/* ui variables */
	EditText usernameTextView;
	EditText nameTextView;
	EditText emailTextView;

	EditText password1TextView;
	EditText password2TextView;

	TextView teacherStatusTextView;

	TextView programTextView;
	TextView rocnikTextView;

	ImageView avatarImageView;

	Button studySettingsButton;
	Button changePasswordButton;

	Spinner roleSpinner;
	ArrayAdapter<CharSequence> adapter;

	/* helper variables */
	boolean isNewAvatarLoaded;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_settings);

		/* init */
		isNewAvatarLoaded = false;

		/* ui init */
		studySection = (LinearLayout) findViewById(R.id.settings_study_data_linearlayout);
		
		usernameTextView = (EditText) findViewById(R.id.settings_username_edittext);
		nameTextView = (EditText) findViewById(R.id.settings_name_edittext);
		emailTextView = (EditText) findViewById(R.id.settings_email_edittext);
		password1TextView = (EditText) findViewById(R.id.settings_password_edittext);
		password2TextView = (EditText) findViewById(R.id.settings_password_again_edittext);

		teacherStatusTextView = (TextView) findViewById(R.id.settings_teacher_status);
		programTextView = (TextView) findViewById(R.id.settings_program_tv);
		rocnikTextView = (TextView) findViewById(R.id.settings_rocnik_tv);

		avatarImageView = (ImageView) findViewById(R.id.settings_avatar_imageview);

		roleSpinner = (Spinner) findViewById(R.id.settings_role_spinner);

		studySettingsButton = (Button) findViewById(R.id.settings_setStudyData_button);
		changePasswordButton = (Button) findViewById(R.id.settings_change_password_button);

		/* listeners */
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

		studySettingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// start intent
				Intent intent = new Intent(SettingsActivity.this,
						SettingsStudyActivity.class);
				startActivityForResult(intent, RESULT_STUDY_DATA);

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

		adapter = ArrayAdapter.createFromResource(this, R.array.roles,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		roleSpinner.setAdapter(adapter);
		roleSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		        // your code here
		    	role = roleSpinner.getSelectedItem().toString();
		    	setViewsEnabled(role);
		    }

		    @Override
		    public void onNothingSelected(AdapterView<?> parentView) {
		        // your code here
		    }

		});

		/* stiahnem data alebo ukoncim ak nie je prihlaseny */
		if (ParseUser.getCurrentUser() != null) {
			getUserData();
		} else {
			finish();
		}

	}

	/* pri stlaceni backbuttonu zistim ci je vsetko vyplnene */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();

		finish();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (resultCode == RESULT_OK && null != data) {
				if (requestCode == RESULT_LOAD_IMG) {
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
				} /*
				 * { Toast.makeText(this, "You haven't picked Image",
				 * Toast.LENGTH_LONG).show(); }
				 */

				if (requestCode == RESULT_STUDY_DATA) {
					// navrat zo settings study activity
					program = data.getStringExtra("program");
					rocnik = data.getStringExtra("rocnik");
					setValuesToViews();
				}

			} else {

			}
		} catch (Exception e) {
			Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
					.show();
		}

	}

	/* nacitanie dat do UI - zaciatok */
	public void getUserData() {

		ParseUser myParseUser = ParseUser.getCurrentUser();
		setProgressBarIndeterminateVisibility(true);
		myParseUser.fetchInBackground(new GetCallback<ParseUser>() {

			@Override
			public void done(ParseUser object, ParseException e) {
				// TODO Auto-generated method stub

				ParseFile imageFile = (ParseFile) object.get("Picture");
				imageFile.getDataInBackground(new GetDataCallback() {
					public void done(byte[] data, ParseException e) {
						setProgressBarIndeterminateVisibility(false);
						if (e == null) {
							// data has the bytes for the image
							Log.d("img", "e je null");
							pictureBitMap = BitmapFactory.decodeByteArray(data,
									0, data.length);
							avatarImageView.setImageBitmap(pictureBitMap);
						} else {
							// something went wrong
							Log.d("img", "e:" + e.getMessage());
						}
					}
				});

				username = object.getUsername();
				name = object.getString("Meno");
				email = object.getEmail();
				role = object.getString("Role");
				program = object.getString("StudyProgramme");
				rocnik = object.getString("Rocnik");
				isTeacherConfirmed = object.getBoolean("teacherConfirmation");

				setValuesToViews();
			}
		});
	}

	public void setValuesToViews() {
		if(role.equalsIgnoreCase("admin")){
			teacherStatusTextView.setText("ADMIN prihlaseny");
			teacherStatusTextView.setVisibility(View.VISIBLE);
		} else {
			
		int position = adapter.getPosition(role);
		roleSpinner.setSelection(position);
		setViewsEnabled(roleSpinner.getSelectedItem().toString());
		
		}
		
		usernameTextView.setText(username);
		nameTextView.setText(name);
		emailTextView.setText(email);
		programTextView.setText(program);
		rocnikTextView.setText(rocnik);
	}

	public void setViewsEnabled(String role) {
		if (role.equalsIgnoreCase("visitor")) {
			teacherStatusTextView.setVisibility(View.GONE);
			studySettingsButton.setVisibility(View.GONE);
			studySection.setVisibility(View.GONE);
		}

		if (role.equalsIgnoreCase("student")) {
			teacherStatusTextView.setVisibility(View.GONE);
			studySettingsButton.setVisibility(View.VISIBLE);
			studySection.setVisibility(View.VISIBLE);
		}

		if (role.equalsIgnoreCase("teacher")) {
			teacherStatusTextView.setVisibility(View.VISIBLE);
			studySettingsButton.setVisibility(View.GONE);
			studySection.setVisibility(View.GONE);
			if(isTeacherConfirmed){
				teacherStatusTextView.setText("schválený");
			} else {
				teacherStatusTextView.setText("èaká na schválenie");
			}
		}
	}

	/* nacitanie dat do UI - koniec */

	public void save() {

		setUserData();

	}

	public void setUserData() {

		// tu riesim aj upload obrazku/avataru
		// avatar uploadnem do systemu
		role = roleSpinner.getSelectedItem().toString();

		ParseUser pu = ParseUser.getCurrentUser();

		pu.put("Meno", nameTextView.getText().toString());
		pu.put("Role", role);

		if (role.equalsIgnoreCase("visitor")) {
			pu.put("StudyProgramme", "");
			pu.put("Rocnik", "");
		}

		if (role.equalsIgnoreCase("student")) {
			pu.put("StudyProgramme", program);
			pu.put("Rocnik", rocnik);
		}

		if (role.equalsIgnoreCase("teacher")) {
			pu.put("StudyProgramme", "");
			pu.put("Rocnik", "");
		}

		if (isNewAvatarLoaded) {
			pu.put("Picture", newImageParseFile());
		}

		setProgressBarIndeterminateVisibility(true);
		pu.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// ok
					Log.i("update profil", "updated ok!");
					finish();
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
		setProgressBarIndeterminateVisibility(true);
		pu.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				setProgressBarIndeterminateVisibility(false);
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

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
