package sk.branislavremen.universityapp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sk.branislavremen.universityapp.adapter.PostItemAdapter;
import sk.branislavremen.universityapp.vo.PostData;
import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import at.theengine.android.simple_rss2_android.SimpleRss2ParserCallback;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PostsActivity extends ListActivity {

	private PostData p;
	private List<String> rss_links_list;
	public List<PostData> postDataList;
	public PostItemAdapter itemAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_news);

		rss_links_list = new ArrayList<String>();
		postDataList = new ArrayList<PostData>();

		itemAdapter = new PostItemAdapter(this, postDataList);
		setListAdapter(itemAdapter);

		refreshList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
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

	public void refreshList() {
		getRssLinks();

	}

	public void getRssLinks() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("RSS");
		setProgressBarIndeterminateVisibility(true);

		query.findInBackground(new FindCallback<ParseObject>() {
			@Override
			public void done(List<ParseObject> rssLinks, ParseException e) {
				setProgressBarIndeterminateVisibility(false);
				if (e == null) {
					// If there are results, update the list of posts
					// and notify the adapter
					// rss_links_list.clear();
					setProgressBarIndeterminateVisibility(true);
					for (ParseObject object : rssLinks) {
						rss_links_list.add(object.getString("url"));
						Log.e("tag", object.getString("url"));
					}

					// stiahnut xml data a parsovat ich

					for (String link : rss_links_list) {
						// new RssDataController().execute(link);
						Log.d("Link", link);
						SimpleRss2Parser parser = new SimpleRss2Parser(link,
								new SimpleRss2ParserCallback() {

									@Override
									public void onFeedParsed(List<RSSItem> items) {
										// TODO Auto-generated method stub
										for (int i = 0; i < items.size(); i++) {
											Log.d("SimpleRss2ParserDemo", items
													.get(i).getTitle());
											Log.d("SimpleRss2ParserDemo", items
													.get(i).getDate());
											Log.d("SimpleRss2ParserDemo", items
													.get(i).getLink()
													.toString());
											String string = items.get(i).getDate();
											// Fri, 30 Jan 2015 13:02:54 +0100
											DateFormat format = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss ZZZZZ", Locale.ENGLISH);
											Date date = null;
											try {
												date = format.parse(string);
											} catch (java.text.ParseException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											
											p = new PostData(items.get(i)
													.getLink().toString(),
													items.get(i).getTitle(),
													date);
											postDataList.add(p);
										}
										Collections.sort(postDataList,
												new Comparator<PostData>() {
													public int compare(
															PostData o1,
															PostData o2) {
														if (o1.getPostDate() == null
																|| o2.getPostDate() == null)
															return 0;
														return o2
																.getPostDate()
																.compareTo(
																		o1.getPostDate());
													}
												});
										itemAdapter.notifyDataSetChanged();
									}

									@Override
									public void onError(Exception arg0) {
										// TODO Auto-generated method stub

									}

								});
						parser.parseAsync();
						setProgressBarIndeterminateVisibility(false);
					}

				} else {
					Log.d(getClass().getSimpleName(),
							"Error: " + e.getMessage());
					// nie je pripojenie
				}
			}
		});
	}
}
