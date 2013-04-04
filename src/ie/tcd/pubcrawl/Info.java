
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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Info extends Activity {

	String crawlCode;
	String[][] pubArray;
	String[][] scheduleInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);

		PermStorage entry = new PermStorage(Info.this);
		entry.open();
		crawlCode = entry.Get_Current_Crawl(Info.this);

		//Retrieving Display Information from PermStorage
		String [] crawlInfo; 
		scheduleInfo = entry.Get_Crawl_Pubs(crawlCode);
		crawlInfo = entry.Get_Crawl_Data(crawlCode);

		TextView eventNameTV = (TextView) findViewById(R.id.eventName);   
		eventNameTV.setText(crawlInfo[0]);

		TextView eventTimeDateTV = (TextView) findViewById(R.id.eventTimeDate);
		eventTimeDateTV.setText(crawlInfo[1]);

		TextView eventDescriptionTV = (TextView) findViewById(R.id.eventDescription);
		eventDescriptionTV.setText(crawlCode);

		ListView listView = (ListView) findViewById(R.id.listOfPubs);

			
			
			ArrayList<Map<String, String>> list = buildData(scheduleInfo);
			String[] from = { "name", "time"};
			int[] to = { R.id.pubName, R.id.time };

			SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.activity_row_layout, from, to);
			listView.setAdapter(adapter);
			
			listView.setOnItemClickListener(new OnItemClickListener()
			{
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					// TODO Auto-generated method stub
					Intent myIntent = new Intent("ie.tcd.pubcrawl.PUBDESCRIPTION");
					myIntent.putExtra("pubID", scheduleInfo[position][2]);
					startActivity(myIntent);

				}
			});
	}


	private ArrayList<Map<String, String>> buildData(String info[][])
	{
		ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i=0; i<info.length; i++)
		{
			list.add(putData(info[i][0], info[i][1]));
		}
		return list;
	}

	private HashMap<String, String> putData(String name, String time)
	{
		HashMap<String, String> item = new HashMap<String, String>();
		item.put("name", name);
		item.put("time", time);
		return item;
	}
}