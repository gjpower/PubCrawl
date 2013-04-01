package ie.tcd.pubcrawl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewFeed extends Activity {
	
	String CrawlID;
	
	public static PermStorage dbAccess;
	HttpEntity resEntity;
	
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;
	
	private static SimpleCursorAdapter adapter;
	private Handler databaseUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_feed);
		PermStorage entry = new PermStorage(ViewFeed.this);

		CrawlID = entry.Get_Current_Crawl(ViewFeed.this);
	
			
		databaseUpdate = new Handler(new Handler.Callback() {
			
			public boolean handleMessage(Message msg) {
				adapter.changeCursor((Cursor) msg.obj);
				Toast.makeText(getApplicationContext(), "Comments Updated", Toast.LENGTH_SHORT).show();
				return true;
			}
		});
		
		ListView commentView = (ListView) findViewById(R.id.commentsView);
		
		dbAccess = new PermStorage(this);
		dbAccess.open();
		Cursor c = dbAccess.Get_Comment_Data(CrawlID);
		
		String[] from = { "username", "comment_body", "time", "image" };
        int[] to = { R.id.commentUserName, R.id.commentBody, R.id.commentDateTime, R.id.commentHasImage }; //identifies row layout to use
		adapter = new SimpleCursorAdapter(this, R.layout.list_item_comment, c, from, to,  SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		
		// the following block handles display attached image link or not
		adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				if (view.getId()==R.id.commentHasImage) {	// if it's for the has image field
					final String hasImage = cursor.getString(4);
					
					if (!hasImage.equals("null")) {
						((TextView) view).setText("Click for attached Image");
					}
					else {
						((TextView) view).setText("");
					}
					
					return true;	//we handled it
					
				}
				
				return false;	//we didn't handle it get default to
			}
			
		});
		
		commentView.setAdapter((ListAdapter) adapter);
        
        commentView.setOnItemClickListener(new OnItemClickListener()
        {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                    	Cursor cursor = ((SimpleCursorAdapter) parent.getAdapter()).getCursor();
                    	cursor.moveToPosition(position);
                    	String imagePath = cursor.getString(4);
                    												
                    		//this is not silly code. the mysql server will send "null" string for any null columns
                    	if (!imagePath.equals("null")) {	//if not null image name show the image
                    		Display_Photo("http://164.138.29.169/" + imagePath);
                    	}

                    }
            });
        
        commentView.setOnItemLongClickListener(new OnItemLongClickListener() {	//reload content on long-click

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Thread loadContent = new Thread() {
					public void run() {
						getListViewComments();
					}
				};
				
				loadContent.start();				
				return true;
			}
        	
        });
		
		Thread loadContent = new Thread() {
			public void run() {
				getListViewComments();
			}
		};
		
		loadContent.start();
		
	}

	public void Display_Photo(String _url){
		
		Intent imageIntent = new Intent(this, WebImage.class);
		imageIntent.putExtra("url", _url);
		startActivity(imageIntent);
		
	}
	
	public void getListViewComments() {
		String[][] _array = null;
		
		try {
			_array = Return_Comments(CrawlID);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.w("ERRORRRRR", "Exception Caught");
			e1.printStackTrace();
		}
		
		if(_array!=null) {	//if the array isn't empty
			//commentsList = buildData(_array);
			dbAccess.Store_Comment_Data(_array, CrawlID);	//hardcode
			Cursor cursor = dbAccess.Get_Comment_Data(CrawlID);
			databaseUpdate.sendMessage(Message.obtain(databaseUpdate, 0, 0, 0, cursor));
		}
	}
	
	public static String[][] Return_Comments(String _crawlID) throws Exception{
		
		// declare parameters that are passed to PHP script 
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		// define the parameter
		postParameters.add(new BasicNameValuePair("CrawlID",_crawlID));
		//Log.w("PostParameters: ", postParameters.toString());
		String response = null;
		
		// call executeHttpPost method passing necessary parameters 
		
		response = executeHttpPost("http://164.138.29.169/display_comments_script.php",postParameters);

		// store the result returned by PHP script that runs MySQL query
		String result = response.toString();  
		
		//parse JSON data

		JSONArray jArray = new JSONArray(result);
		
		final int N = jArray.length(); // number of rows returned from the mysql_query....ie number of comments for the pub crawl
		String[][] comment = new String[N][4]; 
		
		for(int i=0;i<N;i++){
			JSONObject json_data = jArray.getJSONObject(i);		                   
			
			comment[i][0] = json_data.getString("user_name");	//changed from id_user as names make more sense
			comment[i][1] = json_data.getString("comment_body");
			if (comment[i][1] == "null")
				comment[i][1] = "";
			comment[i][2] = json_data.getString("image");
			comment[i][3] = json_data.getString("comment_time");
			
		}
		return comment;
	}
	
	public static String executeHttpPost(String url,
	ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
			postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			in.close();
			String result = sb.toString();
			return result;
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("log_tag", "Error converting result " + e.toString());
					e.printStackTrace();
				}
			}
		}
	}
	
	private static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		return mHttpClient;
	}
	
}
