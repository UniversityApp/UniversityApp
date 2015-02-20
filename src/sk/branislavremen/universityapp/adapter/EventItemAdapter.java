package sk.branislavremen.universityapp.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sk.branislavremen.universityapp.R;
import sk.branislavremen.universityapp.vo.EventData;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class EventItemAdapter extends BaseAdapter implements OnClickListener {

	protected Activity activity;
	protected List<EventData> items;

	private static LayoutInflater inflater = null;

	public EventItemAdapter(Activity activity, List<EventData> items) {
		this.activity = activity;
		this.items = items;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {
		public TextView titleTextView;
		public TextView dateTextView;
		public TextView thumbTextView;
		public TextView descrTextView;
		public TextView placeTextView;
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

			v = inflater.inflate(R.layout.list_item_events, parent, false);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.titleTextView = (TextView) v
					.findViewById(R.id.postTitleLabel);
			holder.dateTextView = (TextView) v.findViewById(R.id.postDateLabel);
			holder.descrTextView = (TextView) v
					.findViewById(R.id.postDescrLabel);
			holder.thumbTextView = (TextView) v
					.findViewById(R.id.postThumbTextView);
			holder.placeTextView = (TextView) v
					.findViewById(R.id.postPlaceLabel);
			// holder.image = (ImageView) v.findViewById(R.id.imageView1);

			/************ Set holder with LayoutInflater ************/
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		// Creamos un objeto directivo
		EventData dir = items.get(position);

		// foto = (ImageView) v.findViewById(R.id.imageView1);

		// Rellenamos el nombre
		holder.titleTextView.setText(dir.getEventTitle());
		holder.thumbTextView.setText(dir.getEventTitle().substring(0, 1));
		String pomDescr = dir.getEventDescription();
		if (pomDescr != null) {
			holder.descrTextView.setVisibility(View.VISIBLE);
			holder.descrTextView.setText(pomDescr);
		} else {
			holder.descrTextView.setVisibility(View.GONE);
		}

		String pd = dir.getEventPlaceData();
		if (pd != null) {

			holder.placeTextView.setVisibility(View.VISIBLE);
			holder.placeTextView.setText(pd);
		} else {
			holder.placeTextView.setVisibility(View.GONE);
		}

		// String pomPlaceTitle = dir.getEventPlaceData().getTitle();

		/*
		 * if(pomPlaceTitle != null ){
		 * holder.placeTextView.setVisibility(View.VISIBLE);
		 * holder.placeTextView.setText(pomPlaceTitle); } else {
		 * holder.placeTextView.setVisibility(View.GONE); }
		 */

		// font title:
		// holder.textView1.setTypeface(mv.getFontBold(am));

		// TextView nazov = (TextView) v.findViewById(R.id.textView1);
		// nazov.setText();
		// nazov.setTypeface(mv.getFontBold(am));
		// Rellenamos el cargo
		// TextView popis = (TextView) v.findViewById(R.id.textView2);

		DateFormat format = new SimpleDateFormat("dd.MM.yyyy 'o' HH:mm");
		DateFormat formatBezHodin = new SimpleDateFormat("dd.MM.yyyy");
		String datum1 = "";
		String datum2 = "";
		Date d = null;

		Calendar calendar = Calendar.getInstance();

		if (dir.getEventEndDate() != null) {

			d = dir.getEventStartDate();

			calendar.setTime(d);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);

			if (hours == 0 && minutes == 0) {
				datum1 = formatBezHodin.format(d);
			} else {
				datum1 = format.format(d);
			}

			d = dir.getEventEndDate();

			calendar.setTime(d);
			hours = calendar.get(Calendar.HOUR_OF_DAY);
			minutes = calendar.get(Calendar.MINUTE);

			if (hours == 0 && minutes == 0) {
				datum2 = formatBezHodin.format(d);
			} else {
				datum2 = format.format(d);
			}

			holder.dateTextView.setText(datum1 + " - " + datum2);
		} else {
			d = dir.getEventStartDate();

			calendar.setTime(d);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);

			if (hours == 0 && minutes == 0) {
				datum1 = formatBezHodin.format(d);
			} else {
				datum1 = format.format(d);
			}

			holder.dateTextView.setText(datum1);
		}

		// holder.textView2.setTypeface(mv.getFontRegular(am));

		// ImageView image = holder.image;
		// imageLoader.DisplayImage(dir.getFoto(), image);
		// foto.setImageDrawable(v.getResources().getDrawable(R.drawable.ic_av_replay));
		// new DownloadImageTask(foto).execute(dir.getFoto());

		// Retornamos la vista
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
			EventData dir = items.get(mPosition);
			Log.i("adapter", "KLIK: " + dir.getEventTitle());
			showDetailDialog();
			/*
			 * Intent intent = new Intent(activity, WebViewActivity.class);
			 * Bundle b = new Bundle(); b.putString("url",
			 * dir.getPostThumbUrl()); intent.putExtras(b); //Put your id to
			 * your next Intent activity.startActivity(intent);
			 */
			// activity.finish();
		}
	}
	
	public void showDetailDialog() {
		final Dialog dialog = new Dialog(activity);
		// dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
		dialog.setContentView(R.layout.dialog_exist);
		// dialog.setTitle("Pozvánka - POUŽITÁ");

		WindowManager.LayoutParams lp4 = new WindowManager.LayoutParams();
		lp4.copyFrom(dialog.getWindow().getAttributes());
		lp4.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp4.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp4.dimAmount = 0.8f;

		
		((TextView) dialog.findViewById(R.id.dialog2_title1))
				.setText("POZVÁNKA");

		
		((TextView) dialog.findViewById(R.id.dialog2_title2))
				.setText("UPLATNENÁ");


		dialog.show();
		dialog.getWindow().setAttributes(lp4);
	}

}
