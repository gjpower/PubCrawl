
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
import android.widget.TextView;
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

		//pubArray = getSchedule(crawlCode);

		//Event event = new Event();
		String [] crawlInfo = new String[2]; 
		crawlInfo = entry.Get_Crawl_Data(crawlCode);


		TextView eventNameTV = (TextView) findViewById(R.id.eventName);   
		eventNameTV.setText(crawlInfo[0]);

		TextView eventTimeDateTV = (TextView) findViewById(R.id.eventTimeDate);
		eventTimeDateTV.setText(crawlInfo[1]);

		TextView eventDescriptionTV = (TextView) findViewById(R.id.eventDescription);
		eventDescriptionTV.setText(crawlCode);





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
}