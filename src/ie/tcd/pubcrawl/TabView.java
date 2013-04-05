package ie.tcd.pubcrawl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

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

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Display;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class TabView extends TabActivity {
	TabHost mTabHost;
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	private static HttpClient mHttpClient;
	private static Context context;
	static PermStorage entry;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
		context = this;
    	Display display = getWindowManager().getDefaultDisplay(); //Design
    	int tabWidth = (display.getWidth())/4; //Design
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);
        mTabHost=getTabHost();
        String[][] Pubs;
        
        entry = new PermStorage(TabView.this);
		entry.open();
		String current = entry.Get_Current_Crawl(context);
        getPubList(current);
        Pubs = entry.Get_Crawl_Pubs(current);
        
        for(int i=0; i<Pubs.length; i++){
        	getPubInfo(Pubs[i][2]);
        }
        
        
        TabSpec firstSpec = mTabHost.newTabSpec("Info");
        firstSpec.setIndicator("Info", null);
        Intent firstIntent = new Intent(this, Info.class);
        firstSpec.setContent(firstIntent);
        
        TabSpec secondSpec = mTabHost.newTabSpec("second");
        secondSpec.setIndicator("Stream", null);
        Intent secondIntent = new Intent(this, ViewFeed.class);
        secondSpec.setContent(secondIntent);
        
        TabSpec thirdSpec = mTabHost.newTabSpec("third");
        thirdSpec.setIndicator("Maps", null);
        Intent thirdIntent = new Intent(this, CrawlMapOverview.class);
        thirdSpec.setContent(thirdIntent);
        
        TabSpec fourthSpec = mTabHost.newTabSpec("fourth");
        fourthSpec.setIndicator("Upload", null);
        Intent fourthIntent = new Intent(this, Photo_Cap.class);
        fourthSpec.setContent(fourthIntent);
        
        mTabHost.addTab(firstSpec);
        mTabHost.addTab(secondSpec);
        mTabHost.addTab(thirdSpec);
        mTabHost.addTab(fourthSpec);
        setTabColor(mTabHost);  //Design
        setTabWidth(mTabHost, tabWidth); //Design
    }
    //Design Function
    public static void setTabWidth(TabHost tabhost, int width){
    	for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        	tabhost.getTabWidget().getChildAt(i).setMinimumWidth(width);
    }
  //Design Function
    public static void setTabColor(TabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        	tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tabunselected); //unselected
        	
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.tabselected); // selected
    }
  //Design Function
	public void onTabChanged(String tabId) {
		setTabColor(mTabHost);		
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

	public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {
		BufferedReader in = null;
		try {
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(url);
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
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
	public static void getPubList(String id){
		String[][] info;
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("CrawlID", id));
		String response = null;
		// call executeHttpPost method passing necessary parameters
		try {
			response = executeHttpPost("http://164.138.29.169/android/getschedule.php",postParameters);
			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();

			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);
				info = new String [jArray.length()][3];
				for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				info[i][0] = json_data.getString("pubname");
				info[i][1] = json_data.getString("time");
				info[i][2] = json_data.getString("pubid");
				}
				entry.Store_Crawl_Pubs(info, id);
			}

			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
		}
		catch (Exception e) {
			Log.e("log_tag","Error in http connection!!" + e.toString());
			Toast.makeText(context,"Error in http connection!!", Toast.LENGTH_LONG).show();
		}
		return; 
	}

	public static void getPubInfo(String id){
		String[] info = new String[7];

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("PubID",id));
		String response = null;

		// call executeHttpPost method passing necessary parameters
		try {
			response = executeHttpPost("http://164.138.29.169/getpubinfo.php",postParameters);

			// store the result returned by PHP script that runs MySQL query
			String result = response.toString();

			//parse json data
			try{
				JSONArray jArray = new JSONArray(result);

				JSONObject json_data = jArray.getJSONObject(0);
				info[0] = json_data.getString("name");
				info[1] = json_data.getString("address");
				info[2] = json_data.getString("description");
				info[3] = json_data.getString("ups");
				info[4] = json_data.getString("downs");
				info[5] = json_data.getString("lat");
				info[6] = json_data.getString("long");
				int ups = Integer.parseInt(info[3]);
				int downs = Integer.parseInt(info[4]);
				entry.Store_Pub(info[0], info[1], info[2], ups, downs, info[5], info[6], id);
			}
			catch(JSONException e){
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
		}
		catch (Exception e) {
			Log.e("log_tag","Error in http connection!!" + e.toString());
			Toast.makeText(context,"Error in http connection!!", Toast.LENGTH_LONG).show();
		}
		return; 
	}
	
	
	
}
