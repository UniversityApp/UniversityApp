package sk.branislavremen.universityapp;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import sk.branislavremen.universityapp.vo.StudyProgrammeData;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
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
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	// init
	final int PASSWORD_LENG = 6;
	final int RESULT_LOAD_IMG = 1;

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

	Button saveButton;
	Button changePasswordButton;

	AutoCompleteTextView studyProgrammeTextView;
	
	final private String[] DEGREE_ARRAY = { "BC", "MGR" };
	final private String[] TYPE_ARRAY = { "denné štúdium", "externé štúdium" };

	List<StudyProgrammeData> studyProgrammesList;
	List<String> studyProgrammeTitlesList;
	List<String> studyProgrammeCodeList;
	
	ArrayAdapter<String> autocompleteAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		studyProgrammesList = new ArrayList<StudyProgrammeData>();
		studyProgrammeTitlesList = new ArrayList<String>();

		usernameTextView = (TextView) findViewById(R.id.settings_username_edittext);
		titleTextView = (TextView) findViewById(R.id.settings_title_edittext);
		nameTextView = (TextView) findViewById(R.id.settings_name_edittext);
		surnameTextView = (TextView) findViewById(R.id.settings_surname_edittext);
		emailTextView = (TextView) findViewById(R.id.settings_email_edittext);
		password1TextView = (TextView) findViewById(R.id.settings_password_edittext);
		password2TextView = (TextView) findViewById(R.id.settings_password_again_edittext);

		avatarImageView = (ImageView) findViewById(R.id.settings_avatar_imageview);

		saveButton = (Button) findViewById(R.id.settings_save_button);
		changePasswordButton = (Button) findViewById(R.id.settings_change_password_button);

		studyProgrammeTextView = (AutoCompleteTextView) findViewById(R.id.settings_programme_textview);
		
		//autocomplet studijne odbory
		autocompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studyProgrammeTitlesList);
		studyProgrammeTextView.setAdapter(autocompleteAdapter);
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

		saveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

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
		
		//get study programmes 
		Log.i("autocomplet", "started");
		getStudyProgrammes();

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
               
                // Set the Image in ImageView after decoding the String
                avatarImageView.setImageBitmap(BitmapFactory
                        .decodeFile(pictureString));
 
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }
 
    }
	
	public void getStudyProgrammes(){
		studyProgrammesList.clear();
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("StudyProgrammes");
		setProgressBarIndeterminateVisibility(true);
		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> parseList, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					Log.i("autocomplet", "done ok");
					Log.i("autocomplet", "start pin");
					//pin in bg
					pinAllStudyProgrammes(parseList);
				} else {
					Log.i("autocomplet", "error");
					Log.e(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
				}
			}
		});
	}
	
	public void pinAllStudyProgrammes(List<ParseObject> parseList){
	
		// pripnem ich do lokalnej pamate
				ParseObject.pinAllInBackground("localStudyProgrammes", parseList,
						new SaveCallback() {

							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub

								if (e == null) {
									Log.i("autocomplet", "done ok");
									Log.i("autocomplet", "start get from local");
									getStudyProgrammesFromLocalMemory();
								} else {
									Log.i("autocomplet", "error");
									Log.e(getClass().getSimpleName(),
											"Error: " + e.getMessage());
								}
							}
						});
	}
	
	public void getStudyProgrammesFromLocalMemory() {
		ParseQuery<ParseObject> testQuery = ParseQuery.getQuery("StudyProgrammes");
		testQuery.fromPin("localStudyProgrammes");
		// nacitam ich z lokalnej pamate
		testQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {
				// TODO Auto-generated
				// method stub
				if(e == null){
					Log.i("autocomplet", "done ok");
					getStudyProgrammesDataList(objects);
				} else {
					Log.i("autocomplet", "error");
				}
				

			}
		});
	}

	public void getStudyProgrammesDataList(List<ParseObject> objects){
		for(ParseObject object:objects){
			// pre autocomplet textview
			studyProgrammeTitlesList.add(object.getString("Skratka") + " - " + object.getString("Popis"));
			
			//do lokalnej pamate
		}
		autocompleteAdapter.notifyDataSetChanged();
	}
	
	public void getUserData() {

		ParseUser pu = ParseUser.getCurrentUser();

		usernameTextView.setText(pu.getUsername());
		titleTextView.setText(pu.getString("Titul"));
		nameTextView.setText(pu.getString("Meno"));
		surnameTextView.setText(pu.getString("Priezvisko"));
		emailTextView.setText(pu.getString("email"));

	}
	
	public void setUserData() {

		ParseUser pu = ParseUser.getCurrentUser();

/*		usernameTextView.setText(pu.getUsername());
		titleTextView.setText(pu.getString("Titul"));
		nameTextView.setText(pu.getString("Meno"));
		surnameTextView.setText(pu.getString("Priezvisko"));
		emailTextView.setText(pu.getString("email"));
*/
	}

	

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

	public void getImage() {
		Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] data = stream.toByteArray();

		ParseFile file = new ParseFile("avatar.png", data);
		file.saveInBackground();

		// pu.put("Picture", file);
	}

}
