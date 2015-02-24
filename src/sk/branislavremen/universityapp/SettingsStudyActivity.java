package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.List;

import sk.branislavremen.universityapp.view.DropDown;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.R.bool;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;

public class SettingsStudyActivity extends Activity {

	String fakulta;
	String stupen;
	String forma;
	String druh;
	String program;
	String rocnik;

	ListView program_lv;

	DropDown fakulta_dd;
	DropDown stupen_dd;
	DropDown forma_dd;
	DropDown druh_dd;
	DropDown rocnik_dd;

	ScrollView sv;

	ListAdapter lv_adapter;
	ArrayAdapter<String> adapter;

	List<String> studyProgrammeTitlesList;

	boolean isPart1Visible;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_settings_study);

		isPart1Visible = true;

		program_lv = (ListView) findViewById(R.id.settings_studyprogramme);

		fakulta_dd = (DropDown) findViewById(R.id.dropdown_fakulta);
		stupen_dd = (DropDown) findViewById(R.id.dropdown_stupen);
		forma_dd = (DropDown) findViewById(R.id.dropdown_forma);
		druh_dd = (DropDown) findViewById(R.id.dropdown_druh);
		rocnik_dd = (DropDown) findViewById(R.id.dropdown_rocnik);

		sv = (ScrollView) findViewById(R.id.settings_scrollview);

		studyProgrammeTitlesList = new ArrayList<String>();
		lv_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, studyProgrammeTitlesList);

		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				studyProgrammeTitlesList);

		program_lv.setAdapter(adapter);

		program_lv
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> adapterView,
							View view, int i, long l) {

						program = adapter.getItem(i).toString();
						Log.d("selected", "p:" + program);
						
						for (int j = 0; j < adapterView.getChildCount(); j++)
							adapterView.getChildAt(j).setBackgroundColor(
									Color.TRANSPARENT);

						// change the background color of the selected element
						view.setBackgroundColor(Color.LTGRAY);
					}
				});

		

		setPart1();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (isPart1Visible) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			setPart1();
		}

	}

	/* nacitanie studijnych programov pre autocomplettextview - ZACIATOK */
	public void getStudyProgrammes() {
		// studyProgrammesList.clear();

		ParseQuery<ParseObject> query = ParseQuery.getQuery("StudyProgrammes");
		setProgressBarIndeterminateVisibility(true);

		query.whereEqualTo("Fakulta", fakulta);
		query.whereEqualTo("DruhStudia", druh);
		query.whereEqualTo("Forma", forma);
		query.whereEqualTo("Stupen", stupen);

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
		adapter.notifyDataSetChanged();
		Log.d("debug", "pocet:" + pocitadlo);

		// adapter refresh - !!!! HERE !!!!

	}

	/* nacitanie studijnych programov pre autocomplettextview - KONIEC */

	public boolean isAllSelected() {
		boolean b = false;

		if (fakulta_dd.getSelectedItemString() != null
				&& stupen_dd.getSelectedItemString() != null
				&& forma_dd.getSelectedItemString() != null
				&& druh_dd.getSelectedItemString() != null
				&& rocnik_dd.getSelectedItemString() != null) {
			b = true;
		}

		return b;
	}

	public boolean isProgrammeSelected() {
		boolean b = false;

		if (program != null) {
			b = true;
		}

		return b;
	}

	public void setPart2() {

		isPart1Visible = false;

		fakulta = fakulta_dd.getSelectedItemString();
		stupen = stupen_dd.getSelectedItemString();
		forma = forma_dd.getSelectedItemString();
		druh = druh_dd.getSelectedItemString();
		rocnik = rocnik_dd.getSelectedItemString();

		getStudyProgrammes();
		
		sv.setVisibility(View.GONE);
		program_lv.setVisibility(View.VISIBLE);
	}

	public void setPart1() {
		sv.setVisibility(View.VISIBLE);
		program_lv.setVisibility(View.GONE);
	}

	public void goBack() {
		Intent returnIntent = new Intent();
		returnIntent.putExtra("fakulta", fakulta);
		returnIntent.putExtra("stupen", stupen);
		returnIntent.putExtra("forma", forma);
		returnIntent.putExtra("druh", druh);
		returnIntent.putExtra("program", program);
		returnIntent.putExtra("rocnik", rocnik);
		setResult(RESULT_OK, returnIntent);
		finish();
	}

	/* MENU */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_study, menu);
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

		case R.id.action_save:

			if (isPart1Visible) {
				// part 1
				if (isAllSelected()) {
					setPart2();
				}
			} else {
				//part2
				if (program != null) {
					goBack();
				}
			}

			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
