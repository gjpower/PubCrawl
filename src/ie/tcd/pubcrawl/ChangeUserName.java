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
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangeUserName extends Activity {
	public static String userName;
	EditText getName;
	TextView currName;
	Button saveName;
	private static HttpClient mHttpClient;
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        
        currName = (TextView) findViewById(R.id.tvCurrentName);
        getName = (EditText) findViewById(R.id.etUserName);
        saveName = (Button) findViewById(R.id.bSaveUserName);
        
        //Get user name and display
        PermStorage request = new PermStorage(this);
        request.open();
        userName = request.Get_User_Name();
        request.close();
        currName.setText("Current User Name is " + userName);
        getName.setText(userName);
        
        saveName.setOnClickListener(new View.OnClickListener() {			
			//Save new user name and go back to the main activity
			public void onClick(View v) {
				userName = getName.getText().toString();
				
				if(ChangeName(userName)){
					startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
					finish();
				}
			}
		});
    }
    
    boolean ChangeName(String username){
    	PermStorage entry = new PermStorage(ChangeUserName.this);
		entry.open();
		String user_id = entry.Get_User_Id();
		entry.Store_User_Name(userName);
    	
 		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
 		postParameters.add(new BasicNameValuePair("UserName",username));
 		postParameters.add(new BasicNameValuePair("UserID",user_id));
 		
 		String response, result;
 		 try {
 			    response = executeHttpPost("http://164.138.29.169/new_member.php",postParameters);
 			    
 			    // store the result returned by PHP script that runs MySQL query
 			    result = response.toString();  
 			    
 			    if(result == "User Update Failed"){
 			    	Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
 			    	return false;
 			    }
 		 }
 		 catch (Exception e) {
 			       	  Toast.makeText(getApplicationContext(),"Connection Error, Please try again", Toast.LENGTH_LONG).show();
 			       	  Log.e("log_tag","Error in http connection!!" + e.toString()); 
 			       	return false;
 		 }
 		
 		 
  		entry.Store_User_Name(username);
      	String[][] noCrawls = new String[1][4];	//Needs to be 4 to be compatible with Store_Crawl_Data
      	entry.Store_Crawl_Data(noCrawls);
 		 
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


