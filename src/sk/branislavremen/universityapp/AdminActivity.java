package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminActivity extends Activity {
	
	List<ParseUser> teachersList;
	
	LinearLayout main_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		main_ll = (LinearLayout) findViewById(R.id.admin_linear_layout);
		
		teachersList = new ArrayList<ParseUser>();
		
		downloadList();
	}

	/* Stiahnut zoznam ucitelov */
	public void downloadList(){
	ParseQuery<ParseUser> query = ParseQuery.getUserQuery();
	query.whereEqualTo("Role", "teacher");
	query.findInBackground(new FindCallback<ParseUser>() {
		
		@Override
		public void done(List<ParseUser> objects, ParseException e) {
			// TODO Auto-generated method stub
			if( e == null ){
				for(ParseUser user : objects){
					teachersList.add(user);
					Log.d("teacher", user.getEmail());
				}
				
				// vypisem ucitelov
				vypisUcitelov();
			}
			
		}
	});
	
	}
	
	
	/* Vypisat ucitelov */
	public void vypisUcitelov(){
		
		for(ParseUser user : teachersList){
			LinearLayout newlinearlayout = new LinearLayout(this);
			
			TextView emailtt = new TextView(this);
			emailtt.setText(user.getEmail());
			newlinearlayout.addView(emailtt);
			
			Button confirmBtn = new Button(this);
			confirmBtn.setGravity(Gravity.RIGHT);
			if(!user.getBoolean("teacherConfirmation")){
				//true
				confirmBtn.setText("Potvrdiù");
				confirmBtn.setBackgroundColor(getResources().getColor(R.color.primary));
				
			} else {
				//false & null
				confirmBtn.setText("Potvrden˝");
				confirmBtn.setBackgroundColor(getResources().getColor(R.color.secondary));
			}
			newlinearlayout.addView(confirmBtn);
			
			main_ll.addView(newlinearlayout);
		}
		
	}
	
	/* Potvrdenie ucitela */
	public void potvrdUcitela(ParseUser user){
		
	}
}
