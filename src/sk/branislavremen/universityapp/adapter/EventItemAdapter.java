package sk.branislavremen.universityapp.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import sk.branislavremen.universityapp.EventsActivity;
import sk.branislavremen.universityapp.R;
import sk.branislavremen.universityapp.filter.EventFilter;
import sk.branislavremen.universityapp.vo.EventData;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

public class EventItemAdapter extends BaseAdapter implements OnClickListener, Filterable {

	final private int DEFAULT_EVENT_DURATION = 60;
	
	EventFilter eventFilter;

	protected Activity activity;
	protected ArrayList<EventData> items;

	public void setItems(ArrayList<EventData> items) {
		this.items = items;
	}

	private static LayoutInflater inflater = null;

	EventData edd;

	public EventItemAdapter(Activity activity, ArrayList<EventData> items) {
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

		holder.dateTextView.setText(getFormatedDateString(
				dir.getEventStartDate(), dir.getEventEndDate()));

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

	public String getFormatedDateString(Date date1 /* start date */, Date date2 /*
																				 * end
																				 * date
																				 */) {

		DateFormat format = new SimpleDateFormat("dd.MMM yyyy 'o' HH:mm");
		DateFormat formatBezHodin = new SimpleDateFormat("dd.MMM yyyy");
		String datum1 = "";
		String datum2 = "";
		String finalString = "";

		Calendar calendar = Calendar.getInstance();

		if (date2 != null) {
			calendar.setTime(date1);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);

			if ((hours == 1 && minutes == 0) || (hours == 2 && minutes == 0)) {
				datum1 = formatBezHodin.format(date1);
			} else {
				datum1 = format.format(date1);
			}

			calendar.setTime(date2);
			hours = calendar.get(Calendar.HOUR_OF_DAY);
			minutes = calendar.get(Calendar.MINUTE);

			if ((hours == 1 && minutes == 0) || (hours == 2 && minutes == 0)) {
				datum2 = formatBezHodin.format(date2);
			} else {
				datum2 = format.format(date2);
			}

			finalString = datum1 + " - " + datum2;
		} else {

			calendar.setTime(date1);
			int hours = calendar.get(Calendar.HOUR_OF_DAY);
			int minutes = calendar.get(Calendar.MINUTE);

			if ((hours == 1 && minutes == 0) || (hours == 2 && minutes == 0)) {
				datum1 = formatBezHodin.format(date1);
			} else {
				datum1 = format.format(date1);
			}

			finalString = datum1;
		}

		return finalString;
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

	public void showDetailDialog(EventData ed) {
		final Dialog dialog = new Dialog(activity);
		this.edd = ed;
		// dialog.setCancelable(false);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		dialog.setContentView(R.layout.dialog_event);
		// dialog.setTitle("Pozvánka - POUŽITÁ");

		WindowManager.LayoutParams lp4 = new WindowManager.LayoutParams();
		lp4.copyFrom(dialog.getWindow().getAttributes());
		lp4.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp4.height = WindowManager.LayoutParams.WRAP_CONTENT;
		lp4.dimAmount = 0.8f;

		((TextView) dialog.findViewById(R.id.dialog_event_title)).setText(ed
				.getEventTitle());

		if (ed.getEventDescription() != null
				&& ed.getEventDescription().length() > 0) {
			((TextView) dialog.findViewById(R.id.dialog_event_descr))
					.setVisibility(View.VISIBLE);
			((TextView) dialog.findViewById(R.id.dialog_event_descr))
					.setText(ed.getEventDescription());
		} else {
			((TextView) dialog.findViewById(R.id.dialog_event_descr))
					.setVisibility(View.GONE);
		}

		((TextView) dialog.findViewById(R.id.dialog_event_date))
				.setText(getFormatedDateString(ed.getEventStartDate(),
						ed.getEventEndDate()));

		if (ed.getEventPlaceData() != null
				&& ed.getEventPlaceData().length() > 0) {
			((TextView) dialog.findViewById(R.id.dialog_event_place))
					.setVisibility(View.VISIBLE);
			((TextView) dialog.findViewById(R.id.dialog_event_place))
					.setText(ed.getEventPlaceData());
		} else {
			((TextView) dialog.findViewById(R.id.dialog_event_place))
					.setVisibility(View.GONE);
		}

		Button btnCancel = (Button) dialog
				.findViewById(R.id.dialog_event_navigate_cancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		Button btnAddToCalendar = (Button) dialog
				.findViewById(R.id.dialog_event_add_calendar);
		btnAddToCalendar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_EDIT);
				intent.setType("vnd.android.cursor.item/event");
				// nadpis
				intent.putExtra(Events.TITLE, edd.getEventTitle());
				// zaciatok
				intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, edd
						.getEventStartDate().getTime());
				// koniec
				if (edd.getEventEndDate() != null) {
					intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, edd
							.getEventEndDate().getTime());
				} else {
					intent.putExtra(
							CalendarContract.EXTRA_EVENT_END_TIME,
							edd.getEventStartDate().getTime()
									+ TimeUnit.MINUTES
											.toMillis(DEFAULT_EVENT_DURATION));

				}

				// miesto
				if (edd.getEventPlaceData() != null) {
					intent.putExtra(Events.EVENT_LOCATION,
							edd.getEventPlaceData());
				}

				// popis
				if (edd.getEventDescription() != null) {
					intent.putExtra(Events.DESCRIPTION,
							edd.getEventDescription());
				}
				activity.startActivity(intent);
			}
		});

		dialog.show();
		dialog.getWindow().setAttributes(lp4);
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		 if (eventFilter == null){
             eventFilter = new EventFilter();
             
             //nastavim arraylist<eventdata>
             eventFilter.setMyEventData(items);
             eventFilter.setAdapter(this);
             
		 }
         return eventFilter;
	}
	
	
	
	
}
