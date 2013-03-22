package ie.tcd.pubcrawl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateUserName extends Activity {

	private final static String STATUS = "Status";
	SharedPreferences appSharedPrefs;
	public static String userName;
	EditText getName;
	int userId;
	private static HttpClient mHttpClient;
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createusername);
        
        Test_First_Use();
        
        getName = (EditText) findViewById(R.id.etUserName);
        Button saveName = (Button) findViewById(R.id.bSaveUserName);
        
        saveName.setOnClickListener(new View.OnClickListener() {			
			//Save user name and go to the main activity
			public void onClick(View v) {
				userName = getName.getText().toString();
				if(New_Member(userName)){	
					startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
					finish();
				}
			}
		});
    }
    
    /*
     *Test if it is the first time running the app
     *If it is, ask to create user name
     *If not, go straight to the main activity
     */
    public void Test_First_Use() {
    	appSharedPrefs = getSharedPreferences(STATUS, 0);
        /*
         *The search for the boolean value labelled "install" should fail
         *on the first run and therefore "true" will be returned, the boolean
         *value is then created and given the value false for future runs
         */
        boolean isFirstRun = appSharedPrefs.getBoolean("install", true);
        if (isFirstRun) {
            Editor prefsEditor = appSharedPrefs.edit();
        	prefsEditor.putBoolean("install", false);
        	prefsEditor.commit();
            
        }
        else {
        	startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        	finish();
        }
    }
    
    boolean New_Member(String username){

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("UserName",username));
		
		String response, result;
		 try {
			    response = executeHttpPost("http://164.138.29.169/new_member.php",postParameters);
			    
			    // store the result returned by PHP script that runs MySQL query
			    result = response;//.toString();  
			    
			    //Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
			  
		 }
		 catch (Exception e) {
			       	  Toast.makeText(getApplicationContext(),"Connection Error, Please try again", Toast.LENGTH_LONG).show();
			       	  Log.e("log_tag","Error in http connection!!" + e.toString()); 
			       	return false;
		 }
     	
     	//Log.e("result", result);
     	//Log.e("length", Integer.toString(result.length()));
     	result = result.substring(0, result.length() - 1);
     	userId = Integer.parseInt(result, 10);//get user id from server
     	Toast.makeText(getApplicationContext(), "User id received: " + userId, Toast.LENGTH_LONG).show();
     	PermStorage entry = new PermStorage(CreateUserName.this);
			entry.open();
 		entry.Store_User_Id(userId);
 		entry.Store_User_Name(username);
     	String[][] noCrawls = new String[1][4];	//Needs to be 4 to be compatible with Store_Crawl_Data
     	noCrawls[0][0] = "No Crawls";
     	noCrawls[0][1] = "";
     	noCrawls[0][2] = "";
     	noCrawls[0][3] = "";
     	entry.Store_Crawl_Data(noCrawls);
     	entry.close();
		 
		return true;
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
