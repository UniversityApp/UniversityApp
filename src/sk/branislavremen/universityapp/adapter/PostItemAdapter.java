package sk.branislavremen.universityapp.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;




import java.util.Locale;

import sk.branislavremen.universityapp.PostsActivity;
import sk.branislavremen.universityapp.R;
import sk.branislavremen.universityapp.WebViewActivity;
import sk.branislavremen.universityapp.vo.PostData;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostItemAdapter extends BaseAdapter implements OnClickListener{

	protected Activity activity;
	protected List<PostData> items;
	
	private static LayoutInflater inflater = null;

	public PostItemAdapter(Activity activity, List<PostData> items) {
		this.activity = activity;
		this.items = items;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


	}

	/********* Create a holder Class to contain inflated xml file elements *********/
	public static class ViewHolder {
		public TextView textViewTitle;
		public TextView textViewDate;
		public TextView textViewThumb;
		//public ImageView image;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return items.get(arg0);
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// Generamos una convertView por motivos de eficiencia
		View v = convertView;
		ViewHolder holder;

		// Asociamos el layout de la lista que hemos creado
		if (convertView == null) {
			// LayoutInflater inf = (LayoutInflater)
			// activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			/****** Inflate tabitem.xml file for each row ( Defined below ) *******/
			v = inflater.inflate(R.layout.list_item_posts, null);

			/****** View Holder Object to contain tabitem.xml file elements ******/

			holder = new ViewHolder();
			holder.textViewTitle = (TextView) v.findViewById(R.id.postTitleLabel);
			holder.textViewDate = (TextView) v.findViewById(R.id.postDateLabel);
			holder.textViewThumb = (TextView) v.findViewById(R.id.postThumbTextView);
			//holder.image = (ImageView) v.findViewById(R.id.imageView1);

			/************ Set holder with LayoutInflater ************/
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		// Creamos un objeto directivo
		PostData dir = items.get(position);

		// foto = (ImageView) v.findViewById(R.id.imageView1);

		// Rellenamos el nombre
		holder.textViewTitle.setText(dir.getPostTitle());
		holder.textViewThumb.setText(dir.getPostTitle().substring(0, 1));
		// font title:
		// holder.textView1.setTypeface(mv.getFontBold(am));

		// TextView nazov = (TextView) v.findViewById(R.id.textView1);
		// nazov.setText();
		// nazov.setTypeface(mv.getFontBold(am));
		// Rellenamos el cargo
		// TextView popis = (TextView) v.findViewById(R.id.textView2);
		Date d = dir.getPostDate();
		DateFormat format = new SimpleDateFormat("dd.MMM.yyyy 'o' HH:mm");
		String datum = format.format(d);
		
		holder.textViewDate.setText(datum);
		//holder.textView2.setTypeface(mv.getFontRegular(am));

		//ImageView image = holder.image;
		//imageLoader.DisplayImage(dir.getFoto(), image);
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
			PostsActivity sct = (PostsActivity) activity;
			PostData dir = items.get(mPosition);
			Log.i("adapter", "KLIK: " + dir.getPostThumbUrl());
			Intent intent = new Intent(activity, WebViewActivity.class);
			Bundle b = new Bundle();
			b.putString("url", dir.getPostThumbUrl());
			intent.putExtras(b); //Put your id to your next Intent
			activity.startActivity(intent);
			//activity.finish();
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	 * ImageView bmImage;
	 * 
	 * public DownloadImageTask(ImageView bmImage) { this.bmImage = null;
	 * this.bmImage = bmImage; }
	 * 
	 * protected Bitmap doInBackground(String... urls) { String urldisplay = "";
	 * urldisplay = urls[0]; Bitmap mIcon11 = null; try { InputStream in = new
	 * java.net.URL(urldisplay).openStream(); mIcon11 =
	 * BitmapFactory.decodeStream(in); } catch (Exception e) { Log.e("Error",
	 * e.getMessage()); e.printStackTrace(); } return mIcon11; }
	 * 
	 * protected void onPostExecute(Bitmap result) {
	 * bmImage.setImageBitmap(result); } }
	 */
}
