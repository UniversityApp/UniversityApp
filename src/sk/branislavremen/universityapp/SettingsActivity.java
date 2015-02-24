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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
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
	String title;
	String name;
	String surname;
	String fakulta;
	String stupen;
	String forma;
	String druh;
	String program;
	String rocnik;
	
	boolean isStudent;
	boolean isAllFilled;

	String pictureString;
	Bitmap pictureBitMap;
	ParseFile pictureParseFile;

	/* ui variables */
	EditText usernameTextView;
	EditText titleTextView;
	EditText nameTextView;
	EditText surnameTextView;
	EditText emailTextView;
	
	EditText password1TextView;
	EditText password2TextView;
	
	TextView fakultaTextView;
	TextView stupenTextView;
	TextView formaTextView;
	TextView druhTextView;
	TextView programTextView;
	TextView rocnikTextView;

	ImageView avatarImageView;
	
	Button studySettingsButton;
	Button changePasswordButton;
	
	CheckBox isStudentCheckBox;

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
		usernameTextView = (EditText) findViewById(R.id.settings_username_edittext);
		titleTextView = (EditText) findViewById(R.id.settings_title_edittext);
		nameTextView = (EditText) findViewById(R.id.settings_name_edittext);
		surnameTextView = (EditText) findViewById(R.id.settings_surname_edittext);
		emailTextView = (EditText) findViewById(R.id.settings_email_edittext);
		password1TextView = (EditText) findViewById(R.id.settings_password_edittext);
		password2TextView = (EditText) findViewById(R.id.settings_password_again_edittext);
		
		fakultaTextView = (TextView) findViewById(R.id.settings_fakulta_tv);
		stupenTextView = (TextView) findViewById(R.id.settings_stupen_tv);
		formaTextView = (TextView) findViewById(R.id.settings_forma_tv);
		druhTextView = (TextView) findViewById(R.id.settings_druh_tv);
		programTextView = (TextView) findViewById(R.id.settings_program_tv);
		rocnikTextView = (TextView) findViewById(R.id.settings_rocnik_tv);

		avatarImageView = (ImageView) findViewById(R.id.settings_avatar_imageview);
		
		isStudentCheckBox = (CheckBox) findViewById(R.id.settings_studydata_checkbox);

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
		
		isStudentCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				setViewsEnabled(!isChecked);
			}
		});
		
		/* stiahnem data alebo ukoncim ak nie je prihlaseny */
		if (ParseUser.getCurrentUser() != null) {
			getUserData();
		} else {
			finish();
		}

	}
	
	/*pri stlaceni backbuttonu zistim ci je vsetko vyplnene*/
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		if(isAllFilled){
			finish();
		} else {
			Toast.makeText(this, "vyplnte vsetko", Toast.LENGTH_SHORT).show();
		}
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			// When an Image is picked
			if (resultCode == RESULT_OK	&& null != data) {
				if(requestCode == RESULT_LOAD_IMG){
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
				} /* {
					Toast.makeText(this, "You haven't picked Image",
							Toast.LENGTH_LONG).show();
				}*/
				
				if(requestCode == RESULT_STUDY_DATA){
					//navrat zo settings study activity
					fakulta = data.getStringExtra("fakulta");
					stupen = data.getStringExtra("stupen");
					forma = data.getStringExtra("forma");	
					druh = data.getStringExtra("druh");
					program = data.getStringExtra("program");
					rocnik = data.getStringExtra("rocnik");
					isStudent = true;
					
					setValuesToViews();
				}
				

			}  else {
				
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
				    	pictureBitMap = BitmapFactory.decodeByteArray(data, 0, data.length);
				    	avatarImageView.setImageBitmap(pictureBitMap);
				    } else {
				      // something went wrong
				    	Log.d("img", "e:" + e.getMessage());
				    }
				  }
				});

				isStudent = object.getBoolean("isStudent");
				isAllFilled = object.getBoolean("isAllFilled");
				
				username = object.getUsername();
				title = object.getString("Titul");
				name = object.getString("Meno");
				surname = object.getString("Priezvisko");
				email = object.getEmail();
				fakulta = object.getString("Fakulta");
				stupen = object.getString("Stupen");
				forma = object.getString("Forma");
				druh = object.getString("Druh");
				program = object.getString("StudyProgramme");
				rocnik = object.getString("Rocnik");
				
				setValuesToViews();
			}
		});
	}
	
	public void setValuesToViews(){
		
		setViewsEnabled(isStudent);
		
		usernameTextView.setText(username);
		titleTextView.setText(title);
		nameTextView.setText(name);
		surnameTextView.setText(surname);
		emailTextView.setText(email);
		fakultaTextView.setText(fakulta);
		stupenTextView.setText(stupen);
		formaTextView.setText(forma);
		druhTextView.setText(druh);
		programTextView.setText(program);
		rocnikTextView.setText(rocnik);
	}
	
	public void setViewsEnabled(boolean isStudent){
		if(isStudent){
			isStudentCheckBox.setChecked(!isStudent);
			studySettingsButton.setEnabled(isStudent);
		} else {
			isStudentCheckBox.setChecked(!isStudent);
			studySettingsButton.setEnabled(isStudent);
		}
	}

	/* nacitanie dat do UI - koniec */

	/* UPDATE PROFILU POUZIVATELA - ZACIATOK */

	public boolean isAllProfileFilled() {
		boolean result = true;

		if (titleTextView.getText() == null | titleTextView.getText().equals(titleTextView.getHint())) {
			result = false;
		}

		if (nameTextView.getText() == null | nameTextView.getText().equals(nameTextView.getHint())) {
			result = false;
		}
		
		if (surnameTextView.getText() == null | surnameTextView.getText().equals(surnameTextView.getHint())) {
			result = false;
		}
		
		if(!isStudentCheckBox.isChecked()){
			if(fakulta == null | fakulta.length() == 0){
				result = false;
			}
			
			if(stupen == null | stupen.length() == 0){
				result = false;
			}
			
			if(forma == null | forma.length() == 0){
				result = false;
			}
			
			if(druh == null | druh.length() == 0){
				result = false;
			}
			
			if(program == null | program.length() == 0){
				result = false;
			}
			
			if(rocnik == null | rocnik.length() == 0){
				result = false;
			}
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
			Toast.makeText(this, "vyplnte vsetko", Toast.LENGTH_SHORT).show();
		}
	}

	public void setUserData() {

		// tu riesim aj upload obrazku/avataru
		// avatar uploadnem do systemu

		ParseUser pu = ParseUser.getCurrentUser();
		
		pu.put("Titul", titleTextView.getText().toString());
		pu.put("Meno", nameTextView.getText().toString());
		pu.put("Priezvisko", surnameTextView.getText().toString());
		
		if(isStudentCheckBox.isChecked()){
			pu.put("Fakulta", "");
			pu.put("Stupen", "");
			pu.put("Forma", "");
			pu.put("Druh", "");
			pu.put("StudyProgramme", "");
			pu.put("Rocnik", "");
			
			pu.put("isStudent", false);
		} else {
			pu.put("Fakulta", fakulta);
			pu.put("Stupen", stupen);
			pu.put("Forma", forma);
			pu.put("Druh", druh);
			pu.put("StudyProgramme", program);
			pu.put("Rocnik", rocnik);
			
			pu.put("isStudent", true);
		}

		if (isNewAvatarLoaded) {
			pu.put("Picture", newImageParseFile());
		}
		
		pu.put("isAllFilled", true);
		
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
