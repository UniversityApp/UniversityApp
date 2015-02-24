package sk.branislavremen.universityapp;

import java.util.ArrayList;
import java.util.List;

import sk.branislavremen.universityapp.adapter.ChatListAdapter;
import sk.branislavremen.universityapp.vo.MessageData;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class ChatActivity extends Activity {


	private static final String TAG = ChatActivity.class.getName();
	private static String sUserId;
	public static final String USER_ID_KEY = "userId";
	String room = "aim09--";

	private EditText etMessage;
	private Button btSend;

	private ListView lvChat;
	private ArrayList<MessageData> mMessages;
	private ChatListAdapter mAdapter;

	private static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
	
	// Create a handler which can run code periodically
	private Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		// User login
		//if (ParseUser.getCurrentUser() != null) { // start with existing user
			startWithCurrentUser();
		//} else { // If not logged in, login as a new anonymous user
		//	login();
		//}
		
		handler.postDelayed(runnable, 500);
	}
	
	// Defines a runnable which is run every 100ms
	private Runnable runnable = new Runnable() {
	    @Override
	    public void run() {
	       refreshMessages();
	       handler.postDelayed(this, 500);
	    }
	};
	
	private void refreshMessages() {
	    receiveMessage();       
	}

	// Get the userId from the cached currentUser object
	private void startWithCurrentUser() {
		sUserId = ParseUser.getCurrentUser().getObjectId();
		setupMessagePosting();
	}

	// Setup message field and posting
	private void setupMessagePosting() {
		etMessage = (EditText) findViewById(R.id.etMessage);
		btSend = (Button) findViewById(R.id.btSend);
		lvChat = (ListView) findViewById(R.id.lvChat);
		mMessages = new ArrayList<MessageData>();
		mAdapter = new ChatListAdapter(ChatActivity.this, sUserId, mMessages);
		lvChat.setAdapter(mAdapter);
		btSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String body = etMessage.getText().toString();
				
				// Use Message model to create new messages now
				MessageData message = new MessageData();
				message.setUserId(sUserId);
				message.setBody(body);
				message.setRoom(room);
				message.saveInBackground(new SaveCallback() {
					@Override
					public void done(ParseException e) {
						receiveMessage();
					}
				});
				etMessage.setText("");
			}
		});
	}
	
	// Query messages from Parse so we can load them into the chat adapter
	private void receiveMessage() {
	                // Construct query to execute
	        ParseQuery<MessageData> query = ParseQuery.getQuery(MessageData.class);
	                // Configure limit and sort order
	        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
	        query.orderByAscending("createdAt");
	        query.whereEqualTo("room", room);
	        // Execute query to fetch all messages from Parse asynchronously
	                // This is equivalent to a SELECT query with SQL
	        query.findInBackground(new FindCallback<MessageData>() {
	            public void done(List<MessageData> messages, ParseException e) {
	                if (e == null) {                    
	                    mMessages.clear();
	                    mMessages.addAll(messages);
	                    mAdapter.notifyDataSetChanged(); // update adapter
	                    lvChat.invalidate(); // redraw listview
	                } else {
	                   // Log.d("message", "Error: " + e.getMessage());
	                }
	            }
	        });
	}

	// Create an anonymous user using ParseAnonymousUtils and set sUserId
	private void login() {
		ParseAnonymousUtils.logIn(new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (e != null) {
					Log.d(TAG, "Anonymous login failed: " + e.toString());
				} else {
					startWithCurrentUser();
				}
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
