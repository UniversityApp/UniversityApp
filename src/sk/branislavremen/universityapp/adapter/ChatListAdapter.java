package sk.branislavremen.universityapp.adapter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import sk.branislavremen.universityapp.R;
import sk.branislavremen.universityapp.vo.MessageData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ChatListAdapter extends ArrayAdapter<MessageData> {
	private String mUserId;

	public ChatListAdapter(Context context, String userId,
			List<MessageData> messages) {
		super(context, 0, messages);
		this.mUserId = userId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.chat_item, parent, false);
			final ViewHolder holder = new ViewHolder();
			holder.imageLeft = (ImageView) convertView
					.findViewById(R.id.ivProfileLeft);
			holder.imageRight = (ImageView) convertView
					.findViewById(R.id.ivProfileRight);
			holder.body = (TextView) convertView.findViewById(R.id.tvBody);
			holder.date = (TextView) convertView.findViewById(R.id.tvDate);
			holder.name = (TextView) convertView.findViewById(R.id.tvName);
			convertView.setTag(holder);
		}
		final MessageData message = (MessageData) getItem(position);
		final ViewHolder holder = (ViewHolder) convertView.getTag();
		final boolean isMe = message.getUserId().equals(mUserId);
		// Show-hide image based on the logged-in user.
		// Display the profile image to the right for our user, left for other
		// users.
		if (isMe) {
			holder.imageRight.setVisibility(View.VISIBLE);
			holder.imageLeft.setVisibility(View.GONE);
			//holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
			//holder.name.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
		} else {
			holder.imageLeft.setVisibility(View.VISIBLE);
			holder.imageRight.setVisibility(View.GONE);
			//holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			//holder.name.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		}
		final ImageView profileView = isMe ? holder.imageRight
				: holder.imageLeft;

		// priradim obrazok
		//Picasso.with(getContext()).load(getProfileUrl(message.getUserId()))
			//	.into(profileView);
		Date ddd = message.getUpdatedAt();
		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm.ss");
		
		holder.body.setText(message.getBody());
		holder.date.setText(format.format(ddd));
		getUserName(message.getUserId(), holder.name, profileView);
		return convertView;
	}

	// Create a gravatar image based on the hash value obtained from userId
	private static String getProfileUrl(final String userId) {
		String hex = "";
		try {
			final MessageDigest digest = MessageDigest.getInstance("MD5");
			final byte[] hash = digest.digest(userId.getBytes());
			final BigInteger bigInt = new BigInteger(hash);
			hex = bigInt.abs().toString(16);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
	}

	// Create a gravatar image based on the hash value obtained from userId
	private static void getUserName(final String userId, final TextView tv, final ImageView iv) {

		ParseQuery<ParseUser> query = ParseUser.getQuery();
		// Configure limit and sort order
		//Log.d("msg2", "userid:" +userId);
		//query.whereEqualTo("objectId", userId);
		// Execute query to fetch all messages from Parse asynchronously
		// This is equivalent to a SELECT query with SQL
		query.getInBackground(userId, new GetCallback<ParseUser>() {

			@Override
			public void done(ParseUser object, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null) {
					tv.setText(object.getUsername().toString());
					Log.d("msg2", "msg:" +object.getUsername());
					
					ParseFile imageFile = (ParseFile) object.get("Picture");
					imageFile.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							
							if (e == null) {
								// data has the bytes for the image
								Log.d("img", "e je null");
								Bitmap pictureBitMap = BitmapFactory.decodeByteArray(data,
										0, data.length);
								iv.setImageBitmap(pictureBitMap);
							} else {
								// something went wrong
								Log.d("img", "e:" + e.getMessage());
							}
						}
					});
					
				} else {
					Log.d("message", "Error: " + e.getMessage());

				}
			}

		});
		
	}
	
	
	

	final class ViewHolder {
		public ImageView imageLeft;
		public ImageView imageRight;
		public TextView body;
		public TextView name;
		public TextView date;
	}

}
