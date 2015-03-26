package sk.branislavremen.universityapp;

import java.io.ByteArrayOutputStream;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends Activity {

	protected EditText usernameEditText;
	protected EditText passwordEditText;
	protected EditText emailEditText;
	protected Button signUpButton;
	
	String username;
    String password;
    String email;
    ParseFile file;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	        getActionBar().setDisplayHomeAsUpEnabled(true);
	        setContentView(R.layout.activity_sign_up);
	 
	        usernameEditText = (EditText)findViewById(R.id.usernameField);
	        passwordEditText = (EditText)findViewById(R.id.passwordField);
	        emailEditText = (EditText)findViewById(R.id.emailField);
	        signUpButton = (Button)findViewById(R.id.signupButton);
	 
	        signUpButton.setOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                username = usernameEditText.getText().toString();
	                password = passwordEditText.getText().toString();
	                email = emailEditText.getText().toString();
	 
	                username = username.trim();
	                password = password.trim();
	                email = email.trim();
	 
	                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
	                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
	                    builder.setMessage(R.string.signup_error_message)
	                        .setTitle(R.string.signup_error_title)
	                        .setPositiveButton(android.R.string.ok, null);
	                    AlertDialog dialog = builder.create();
	                    dialog.show();
	                }
	                else {
	                    setProgressBarIndeterminateVisibility(true);
	                    
	                	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	                	Bitmap pictureBitMap = BitmapFactory.decodeResource(getResources(), R.drawable.default_avatar);
	            		pictureBitMap.compress(Bitmap.CompressFormat.JPEG, 75, stream);
	            		byte[] data = stream.toByteArray();

	            		file = new ParseFile("default_picture.png", data);
	            		file.saveInBackground(new SaveCallback() {
							
							@Override
							public void done(ParseException e) {
								// TODO Auto-generated method stub
								  ParseUser newUser = new ParseUser();
				                    newUser.setUsername(username);
				                    newUser.setPassword(password);
				                    newUser.setEmail(email);
				                    newUser.put("Picture", file);
				                    newUser.put("Role", "visitor");
				                    newUser.signUpInBackground(new SignUpCallback() {
				                        @Override
				                        public void done(ParseException e) {
				                            setProgressBarIndeterminateVisibility(false);
				 
				                            if (e == null) {
				                                // Success!
				                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
				                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				                                startActivity(intent);
				                            }
				                            else {
				                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
				                                builder.setMessage(e.getMessage())
				                                    .setTitle(R.string.signup_error_title)
				                                    .setPositiveButton(android.R.string.ok, null);
				                                AlertDialog dialog = builder.create();
				                                dialog.show();
				                            }
				                        }
				                    });
							}
						});
	                    
	                    
	 
	            		 
		                }
		            }
		        });
	}

	
}
