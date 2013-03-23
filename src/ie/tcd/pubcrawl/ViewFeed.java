package ie.tcd.pubcrawl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ViewFeed extends Activity {
	
	int CrawlID = 11;


	public static String[][] _array;
	HttpEntity resEntity;
	
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	/** Single instance of our HttpClient */
	private static HttpClient mHttpClient;
	
	private static ArrayList<Map<String, String>> commentsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_feed);
		
		ListView commentView = (ListView) findViewById(R.id.commentsView);
		
		/*
		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		*/
		
		try {
			_array = Return_Comments(CrawlID);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			Log.w("ERRORRRRR", "Exception Caught");
			e1.printStackTrace();
		}
		/** the following code was causing a null pointer exception.
		* 	I solved it with a simple check if the array is never filled by return comments
		* 	if so i just make a textview reporting an error and adding it to the listview.
		* 	this only prevents it crashing if nothing is received from the server
		* 	- Graeme Power
		*/
		
		if(_array!=null) {
			commentsList = buildData(_array);
			String[] from = { "username", "commentbody", "datetime", "hasimage" };
            int[] to = { R.id.commentUserName, R.id.commentBody, R.id.commentDateTime, R.id.commentHasImage }; //identifies row layout to use
            SimpleAdapter adapter = new SimpleAdapter(this, commentsList, R.layout.list_item_comment, from, to);
            commentView.setAdapter(adapter);
            
            commentView.setOnItemClickListener(new OnItemClickListener()
            {


                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {
                                // TODO Auto-generated method stub
        //                      Intent myIntent = new Intent(CrawlsListPage.this, com.example.);
        //                      myIntent.putExtra("ID", idstring);
        //                      startActivity(myIntent);
        //                      Intent myIntent = new Intent("MAINACTIVITY");
                                //Intent myIntent = new Intent(this,com.example.crawllist.MainActivity.class);
        //                      startActivity(myIntent);
                        	
                        	String imagePath = commentsList.get(position).get("image");
                        	
                        	if (imagePath!="null")
                        		Display_Photo("http://164.138.29.169/" + imagePath);

                        }
                });
		}
		
		
		/*
		if (_array!=null) {
			int num_rows = _array.length;
			int j = 0;
			
			
			setContentView(R.layout.activity_view_feed);
			
			for(int i = 0; i < num_rows; i++) {
				Log.w("FOR", "ON CLICK");
				
				if(_array[i][1] != "null" || !_array[i][2].equals("")){
					
					TextView userId = new TextView(this);
					userId.setText("Name: " + _array[i][0]);
					userId.setTextSize(10);
					ll.addView(userId);
					
					if(_array[i][1] != "null"){ 
						TextView comment = new TextView(this);        
						comment.setText(_array[i][1]);
						comment.setTextSize(15);
						comment.setTextColor(0xFF000088);	//changed to dark blue as i couldn't read it before
						ll.addView(comment);            
					}
					
					final String x = "http://164.138.29.169/" + _array[i][2];
					
					
					if(!_array[i][2].equals("")){            
						Button btn = new Button(this);
						btn.setId(j+1);
						btn.setText("View Photo "+(j+1));
						final int index = j;
						btn.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Display_Photo(x);
								Log.i("TAG", "The index is " + index);
							}
						});
						ll.addView(btn);
						
						j++;
						
					}
					
					TextView time = new TextView(this);
					TextView border = new TextView(this);
					
					time.setText("Time: " + _array[i][3]);
					if (_array[i][3] == "null"){
						time.setText("Time not Available");
					}
					border.setText("---------------------------------------");
					time.setTextSize(10);
					time.setGravity(Gravity.RIGHT);
					ll.addView(time);
					border.setGravity(Gravity.CENTER);
					ll.addView(border);
					
				}
			}		
		}
		else {
			TextView error = new TextView(this);
			error.setText("Error unable to connect to server");
			ll.addView(error);
		}
		
		
		this.setContentView(sv);
		*/
		
		
	}

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dynamic_comments, menu);
		return true;
	}*/
	public void Display_Photo(String _url){

		//		 Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(_url));
		//	     startActivity(browserIntent);
		
		Intent imageIntent = new Intent(this, WebImage.class);
		imageIntent.putExtra("url", _url);
		startActivity(imageIntent);
		
		
	}
	
	public static String[][] Return_Comments(int _crawlID) throws Exception{
		
		// declare parameters that are passed to PHP script 
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		// define the parameter
		postParameters.add(new BasicNameValuePair("CrawlID",Integer.toString(_crawlID)));
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

	//Adds each pub crawl in the array to the map
	private ArrayList<Map<String, String>> buildData(String commentInfo[][])
	{
		int num_rows = _array.length;
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		
		for (int i=0; i<num_rows; i++)
		{
			list.add(putData(commentInfo[i][0], commentInfo[i][1], commentInfo[i][2], commentInfo[i][3]));
		}
		return list;
	}

	//hashmap created
	private HashMap<String, String> putData(String userName, String commentBody, String image, String dateTime)
	{
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("username", userName);
		item.put("commentbody", commentBody);
		
		if(image!="null")
			item.put("hasimage", "Touch for attached image");			
		
		item.put("datetime", dateTime);
		item.put("image", image);
		return item;
	}


}
