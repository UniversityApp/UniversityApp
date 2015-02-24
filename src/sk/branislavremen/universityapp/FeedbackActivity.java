package sk.branislavremen.universityapp;

import java.util.List;

import com.parse.ConfigCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FeedbackActivity extends Activity {

	ParseConfig pc;

	String otazka;
	boolean jeKratkaOdpoved;

	String odpoved_string;
	boolean odpoved_boolean;

	TextView otazka_tv;
	EditText kratka_odpoved_ed;
	Button odpovedat;
	Button ano;
	Button nie;

	LinearLayout kratka_odpoved_ll;
	LinearLayout ano_nie_ll;
	TextView nemoznoOdpovedat;

	OnClickListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		otazka_tv = (TextView) findViewById(R.id.feedback_otazka);
		kratka_odpoved_ed = (EditText) findViewById(R.id.feedback_shortansw_et);
		odpovedat = (Button) findViewById(R.id.feedback_shortansw_btn);
		ano = (Button) findViewById(R.id.feedback_yes_btn);
		nie = (Button) findViewById(R.id.feedback_no_btn);
		kratka_odpoved_ll = (LinearLayout) findViewById(R.id.feedback_shortansw_ll);
		ano_nie_ll = (LinearLayout) findViewById(R.id.feedback_yesno_ll);
		nemoznoOdpovedat = (TextView) findViewById(R.id.feedback_zakazane);

		odpovedat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				odpoved_string = kratka_odpoved_ed.getText().toString();
				odpovedat();
			}
		});
		ano.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				odpoved_boolean = true;
				odpovedat();
			}
		});
		nie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				odpoved_boolean = false;
				odpovedat();
			}
		});

		ParseConfig.getInBackground(new ConfigCallback() {

			@Override
			public void done(ParseConfig config, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					otazka = config.getString("otazka");
					jeKratkaOdpoved = config.getBoolean("jeKratkaOdpoved");

					otazka_tv.setText(otazka);

					ParseQuery<ParseObject> query = ParseQuery
							.getQuery("Feedback");
					query.clearCachedResult();

					query.whereEqualTo("user", ParseUser.getCurrentUser()
							.getObjectId());
					query.whereEqualTo("otazka", otazka);
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects,
								ParseException e) {
							// TODO Auto-generated method stub
							if (e == null) {
								Log.e("err", "nie je null");
								if (!objects.isEmpty()) {
									zobrazUzOdpovedal();
								} else if (jeKratkaOdpoved) {
									// kratka odpoved
									zobrazKratkuOdpoved();
								} else {
									// ano nie
									zobrazAnoNieOdpoved();
								}

							} else {
								Log.e("err", e.getMessage());
							}
						}
					});

					Log.e("TAG", "otazka: " + otazka + jeKratkaOdpoved);
				} else {
					Log.e("TAG", "Failed to fetch. Using Cached Config.");

				}
			}
		});

	}

	public void odpovedat() {
		ParseObject po = new ParseObject("Feedback");
		po.put("otazka", otazka);
		if (jeKratkaOdpoved) {
			po.put("kratkaOdpoved", odpoved_string);
		} else {
			po.put("logickaOdpoved", odpoved_boolean);
		}
		po.put("user", ParseUser.getCurrentUser().getObjectId());
		po.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					zobrazUzOdpovedal();
				} else {
					Log.e("feedback", e.getMessage());
				}
			}
		});
	}

	public void zobrazKratkuOdpoved() {
		kratka_odpoved_ll.setVisibility(View.VISIBLE);
		ano_nie_ll.setVisibility(View.GONE);
		nemoznoOdpovedat.setVisibility(View.GONE);
	}

	public void zobrazAnoNieOdpoved() {
		kratka_odpoved_ll.setVisibility(View.GONE);
		ano_nie_ll.setVisibility(View.VISIBLE);
		nemoznoOdpovedat.setVisibility(View.GONE);
	}

	public void zobrazUzOdpovedal() {
		kratka_odpoved_ll.setVisibility(View.GONE);
		ano_nie_ll.setVisibility(View.GONE);
		nemoznoOdpovedat.setVisibility(View.VISIBLE);
	}

	public boolean uzOdpovedal() {
		return false;
	}

}
