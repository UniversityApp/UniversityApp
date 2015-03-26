package sk.branislavremen.universityapp;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PasswordResetActivity extends Activity {

	Button resetButton;
	EditText emailInputEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_reset);

		resetButton = (Button) findViewById(R.id.resetPasswordButton);
		emailInputEditText = (EditText) findViewById(R.id.emailField);

		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetPass(emailInputEditText.getText().toString());
			}
		});
	}

	private void resetPass(String userEmail) {
		ParseUser.requestPasswordResetInBackground(userEmail,
				new RequestPasswordResetCallback() {
					public void done(ParseException e) {
						if (e == null) {
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.reset_password_succesfull),
									Toast.LENGTH_SHORT).show();
							finish();
						} else {
							// Something went wrong. Look at the ParseException
							// to see what's up.
							Toast.makeText(
									getApplicationContext(),
									getResources().getString(
											R.string.reset_password_error),
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

}
