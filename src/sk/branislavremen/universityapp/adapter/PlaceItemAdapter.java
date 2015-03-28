package sk.branislavremen.universityapp.adapter;

import java.util.ArrayList;

import sk.branislavremen.universityapp.R;
import sk.branislavremen.universityapp.filter.PlaceFilter;
import sk.branislavremen.universityapp.vo.PlaceData;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;

public class PlaceItemAdapter extends BaseAdapter implements OnClickListener,
		Filterable {
	
	

	PlaceFilter placeFilter;

	protected Activity activity;
	protected ArrayList<PlaceData> items;

	public void setItems(ArrayList<PlaceData> items) {
		this.items = items;
	}

	private static LayoutInflater inflater = null;

	PlaceData pdd;

	public PlaceItemAdapter(Activity activity, ArrayList<PlaceData> items) {
		this.activity = activity;
		this.items = items;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
	
	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {
		public TextView titleTextView;
		public TextView addressTextView;
		public ImageView iconImageView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// Generamos una convertView por motivos de eficiencia
		View v = convertView;
		ViewHolder holder;

		// Asociamos el layout de la lista que hemos creado
		if (convertView == null) {
			// LayoutInflater inf = (LayoutInflater)
			// activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/

			v = inflater.inflate(R.layout.list_item_places, parent, false);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.titleTextView = (TextView) v
					.findViewById(R.id.placeTitleLabel);
			holder.addressTextView = (TextView) v
					.findViewById(R.id.placeAddressLabel);
			holder.iconImageView = (ImageView) v.findViewById(R.id.placeIcon);

			/************ Set holder with LayoutInflater ************/
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		// Creamos un objeto directivo
		PlaceData dir = items.get(position);

		// foto = (ImageView) v.findViewById(R.id.imageView1);

		// Rellenamos el nombre
		holder.titleTextView.setText(dir.getTitle());
		holder.addressTextView.setText(dir.getAdresa());
		holder.iconImageView.setImageResource(R.drawable.ic_school);
		if(dir.getTyp().equals("Vedenie")){
			holder.iconImageView.setImageResource(R.drawable.ic_institution);
		}
		if(dir.getTyp().equals("Internát")){
			holder.iconImageView.setImageResource(R.drawable.ic_home);
		}
		
		
		v.setOnClickListener(new OnItemClickListener(position));

		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	
	/********* Called when Item click in ListView ************/
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;

		}

		@Override
		public void onClick(View arg0) {
			// NewsActivity sct = (NewsActivity) activity;
			//
			PlaceData dir = items.get(mPosition);
			Log.i("adapter", "KLIK: " + dir.getTitle());
			showDetailDialog(dir);
			/*
			 * Intent intent = new Intent(activity, WebViewActivity.class);
			 * Bundle b = new Bundle(); b.putString("url",
			 * dir.getPostThumbUrl()); intent.putExtras(b); //Put your id to
			 * your next Intent activity.startActivity(intent);
			 */
			// activity.finish();
		}
	}

	public void showDetailDialog(final PlaceData ed) {
		final Dialog dialog = new Dialog(activity);
		// dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_place);
		// dialog.setTitle("Pozvánka - POUŽITÁ");

		WindowManager.LayoutParams lp4 = new WindowManager.LayoutParams();
		lp4.copyFrom(dialog.getWindow().getAttributes());
		lp4.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp4.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp4.dimAmount = 0.8f;

		//if(ed.getPicture() != null & ed.getPicture().isDataAvailable()){
			ParseImageView imageViewPicture = (ParseImageView) dialog.findViewById(R.id.dialog_place_image);
			imageViewPicture.setPlaceholder(activity.getResources().getDrawable(R.drawable.logo));
			imageViewPicture.setParseFile(ed.getPicture());
			imageViewPicture.loadInBackground(new GetDataCallback() {
				
				@Override
				public void done(byte[] data, ParseException e) {
					// TODO Auto-generated method stub
					Log.d("obr", "e:" + e);
				}
			});
		//}
		
		((TextView) dialog.findViewById(R.id.dialog_place_title)).setText(ed
				.getTitle());
		((TextView) dialog.findViewById(R.id.dialog_place_address)).setText(ed
				.getAdresa());
		((TextView) dialog.findViewById(R.id.dialog_place_detail)).setText(ed
				.getDetail());
		
		((Button) dialog.findViewById(R.id.dialog_place_navigate_button)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(ed.getGps() == null){
					navigateToAddress(ed.getAdresa());
				} else {
					navigateToGeoPoint(ed.getGps());
				}
			}
		});
		
				//dialog.dismiss();
		Button btnCancel = (Button) dialog
				.findViewById(R.id.dialog_place_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
		dialog.getWindow().setAttributes(lp4);
	}
	
	public void navigateToGeoPoint(ParseGeoPoint gp){
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://maps.google.com/maps?" + "&daddr="
							+ gp.getLatitude() + "," + gp.getLongitude()));
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			activity.startActivity(intent);	
		} catch (ActivityNotFoundException e) {
			// TODO: handle exception
			Toast.makeText(activity, "google maps nie su v zariadeni", 500).show();
		}
			
	}
	
	public void navigateToAddress(String address){
		try {
			Uri gmmIntentUri = Uri.parse("geo:0,0?q="+address);
			Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
			mapIntent.setPackage("com.google.android.apps.maps");
			activity.startActivity(mapIntent);
		} catch (ActivityNotFoundException e) {
			// TODO: handle exception
			Toast.makeText(activity, "google maps nie su v zariadeni", 500).show();
		}
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		if (placeFilter == null) {
			placeFilter = new PlaceFilter();

			// nastavim arraylist<eventdata>
			placeFilter.setMyPlaceData(items);
			placeFilter.setAdapter(this);

		}
		return placeFilter;
	}

}
