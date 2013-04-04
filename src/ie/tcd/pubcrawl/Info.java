
package ie.tcd.pubcrawl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Info extends Activity {

	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	private static HttpClient mHttpClient;
	private static Context context;
	String crawlCode;
	String[][] pubArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		
		PermStorage entry = new PermStorage(Info.this);
		entry.open();
		crawlCode = entry.Get_Current_Crawl(Info.this);
		
		pubArray = getSchedule(crawlCode);
		
		//Event event = new Event();


		/*
       TextView eventNameTV = (TextView) findViewById(R.id.eventName);   
        eventNameTV.setText(event.name);

        TextView eventOrganiserTV = (TextView) findViewById(R.id.eventOrganiser);
        eventOrganiserTV.setText(event.organiser);

        TextView eventTimeDateTV = (TextView) findViewById(R.id.eventTimeDate);
        eventTimeDateTV.setText(event.timeDate);

        TextView eventDescriptionTV = (TextView) findViewById(R.id.eventDescription);
        eventDescriptionTV.setText(event.description);


		 */





		ListView listView = (ListView) findViewById(R.id.listOfPubs);

		String[][] infoArray = new String[10][2];


		Pub[] pubArray = new Pub[10];
		{
			for (int i=0; i<10; i++){
				pubArray[i] = new Pub("name".concat(String.valueOf(i+1)), i+1);

				System.out.println(i);
				System.out.println(pubArray[i].name);
				System.out.println(pubArray[i].ratingString);		
			}

			for(int i=0; i<10; i++)
			{
				infoArray[i][0] = pubArray[i].name;
				infoArray[i][1] = pubArray[i].ratingString;
			}

			ArrayList<Map<String, String>> list = buildData(infoArray);
			String[] from = { "name", "rating"};
			int[] to = { R.id.pubName, R.id.pubRating };

			SimpleAdapter adapter = new SimpleAdapter(this, list,
					R.layout.activity_row_layout, from, to);
			listView.setAdapter(adapter);}
	}

	private ArrayList<Map<String, String>> buildData(String info[][])
	{

		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i=0; i<10; i++)
		{
			list.add(putData(info[i][0], info[i][1]));

		}
		return list;
	}

	private HashMap<String, String> putData(String name, String rating)
	{
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("name", name);
		item.put("rating", rating);
		return item;
	}


	//These 3 methods are required for Network Connection
		public static String executeHttpGet(String url) throws Exception {
			BufferedReader in = null;
			try {
				HttpClient client = getHttpClient();
				HttpGet request = new HttpGet();
				request.setURI(new URI(url));
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

		//This method takes down the needed information
		public static String[][] getSchedule(String id){

				String[][] info = new String[1][2];
				ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				postParameters.add(new BasicNameValuePair("CrawlID",id));
				String response = null;

				// call executeHttpPost method passing necessary parameters
				try {
					response = executeHttpPost("http://164.138.29.169/getscheduleREAL.php",postParameters);

					//store the result returned by PHP script that runs MySQL query
					String result = response.toString();
					JSONArray jArray = new JSONArray(result);
	                
					//parse json data
					try{
						//for(int i=0;i<jArray.length();i++){
							JSONObject json_data = jArray.getJSONObject(0);
							info[0][0] = "";//json_data.getString("crawlname");
							info[0][1] = "";//json_data.getString("time");
							//info[i][2] = json_data.getString("pubname");
							//info[i][3] = json_data.getString("publocation");
	//						info[i][4] = json_data.getString("latitude");
	//						info[i][5] = json_data.getString("longitude");
						//}
					}

					catch(JSONException e){
						Log.e("log_tag", "Error parsing data "+e.toString());
					}
				}
				catch (Exception e) {
					Log.e("log_tag","Error in http connection!!" + e.toString());
					Toast.makeText(context,"Error in http connection!!", Toast.LENGTH_LONG).show();
				}
			
			return info; 
		
		}
}