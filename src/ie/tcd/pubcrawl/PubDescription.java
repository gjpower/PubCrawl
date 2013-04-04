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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class PubDescription extends Activity {

//	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
//	private static HttpClient mHttpClient;
	private static Context context;
	TextView pub_name, pub_location, pub_description, up_rating, down_rating, latitude, longitude;
	static PermStorage entry;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		.penaltyLog().build());
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_pub_description);

		String[] infoArray;              			
		String pubID = this.getIntent().getStringExtra("pubID");
//		infoArray = getSchedule(pubID);
		entry = new PermStorage(PubDescription.this);
		entry.open();
		infoArray = entry.Get_Pub(pubID);
		Log.e("ArrayBanter", infoArray[0]);
//		Log.e("ArrayBanter", infoArray[1]);
//		Log.e("ArrayBanter", infoArray[2]);
//		Log.e("ArrayBanter", infoArray[3]);
//		Log.e("ArrayBanter", infoArray[4]);
//		Log.e("ArrayBanter", infoArray[5]);
//		Log.e("ArrayBanter", infoArray[6]);
//		Log.e("ArrayBanter", infoArray[7]);
//		
//		pub_name=new TextView(this); 
//		pub_name=(TextView)findViewById(R.id.name); 
//		pub_name.setText(infoArray[0]);
//
//		pub_location=new TextView(this); 
//		pub_location=(TextView)findViewById(R.id.location); 
//		pub_location.setText(infoArray[1]);
//
//		pub_description=new TextView(this); 
//		pub_description=(TextView)findViewById(R.id.description); 
//		pub_description.setText(infoArray[2]);
//
//		up_rating=new TextView(this); 
//		up_rating=(TextView)findViewById(R.id.up); 
//		up_rating.setText(infoArray[3]);
//
//		down_rating=new TextView(this); 
//		down_rating=(TextView)findViewById(R.id.down); 
//		down_rating.setText(infoArray[4]);
//
//		latitude=new TextView(this); 
//		latitude=(TextView)findViewById(R.id.latitude); 
//		latitude.setText(infoArray[5]);
//
//		longitude=new TextView(this); 
//		longitude=(TextView)findViewById(R.id.longitude); 
//		longitude.setText(infoArray[6]);		
	}


//	//These 3 methods are required for Network Connection
//	public static String executeHttpGet(String url) throws Exception {
//		BufferedReader in = null;
//		try {
//			HttpClient client = getHttpClient();
//			HttpGet request = new HttpGet();
//			request.setURI(new URI(url));
//			HttpResponse response = client.execute(request);
//			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//			StringBuffer sb = new StringBuffer("");
//			String line = "";
//			String NL = System.getProperty("line.separator");
//			while ((line = in.readLine()) != null) {
//				sb.append(line + NL);
//			}
//			in.close();
//			String result = sb.toString();
//			return result;
//		} finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					Log.e("log_tag", "Error converting result " + e.toString());
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	public static String executeHttpPost(String url,
//			ArrayList<NameValuePair> postParameters) throws Exception {
//		BufferedReader in = null;
//		try {
//			HttpClient client = getHttpClient();
//			HttpPost request = new HttpPost(url);
//			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
//					postParameters);
//			request.setEntity(formEntity);
//			HttpResponse response = client.execute(request);
//			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//			StringBuffer sb = new StringBuffer("");
//			String line = "";
//			String NL = System.getProperty("line.separator");
//
//			while ((line = in.readLine()) != null) {
//				sb.append(line + NL);
//			}
//			in.close();
//			String result = sb.toString();
//			return result;
//		} finally {
//			if (in != null) {
//				try {
//					in.close();
//				} catch (IOException e) {
//					Log.e("log_tag", "Error converting result " + e.toString());
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//
//	private static HttpClient getHttpClient() {
//		if (mHttpClient == null) {
//			mHttpClient = new DefaultHttpClient();
//			final HttpParams params = mHttpClient.getParams();
//			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
//			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
//			ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
//		}
//		return mHttpClient;
//	}          
//
//	//This method takes down the needed information
//	public static String[]getSchedule(int id){
//		String[] info = new String[7];
//
//		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
//		postParameters.add(new BasicNameValuePair("PubID",Integer.toString(id)));
//		String response = null;
//
//		// call executeHttpPost method passing necessary parameters
//		try {
//			response = executeHttpPost("http://164.138.29.169/getpubinfo.php",postParameters);
//
//			// store the result returned by PHP script that runs MySQL query
//			String result = response.toString();
//
//			//parse json data
//			try{
//				JSONArray jArray = new JSONArray(result);
//
//				JSONObject json_data = jArray.getJSONObject(0);
//				info[0] = json_data.getString("name");
//				info[1] = json_data.getString("address");
//				info[2] = json_data.getString("description");
//				info[3] = json_data.getString("ups");
//				info[4] = json_data.getString("downs");
//				info[5] = json_data.getString("lat");
//				info[6] = json_data.getString("long");
//				
//				Toast.makeText(context,"PECTRA SMASH!", Toast.LENGTH_LONG).show();
//
//			}
//			catch(JSONException e){
//				Log.e("log_tag", "Error parsing data "+e.toString());
//			}
//		}
//		catch (Exception e) {
//			Log.e("log_tag","Error in http connection!!" + e.toString());
//			Toast.makeText(context,"Error in http connection!!", Toast.LENGTH_LONG).show();
//		}
//		return info; 
//	}
}