
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	public String userName;
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	// Single instance of our HttpClient 
	private static HttpClient mHttpClient;
	HttpEntity resEntity;
	EditText guestCode;
	Button joinCrawl;
	
	
	//Global Varibles for Test
	int UserID = 1;

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        
        guestCode = (EditText) findViewById(R.id.loginCodeTV);
        joinCrawl = (Button) findViewById(R.id.bLogIn);           
        Button gameMenu = (Button) findViewById(R.id.bGames);
        Button currentCrawls = (Button) findViewById(R.id.bCurrentCrawls);
        Button changeName = (Button) findViewById(R.id.bChangeName);
        
        PermStorage request = new PermStorage(this);
        request.open();
        userName = request.Get_User_Name();
        request.close();
        
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(userName);
        
     
        
        
        joinCrawl.setOnClickListener(this);
        gameMenu.setOnClickListener(this);
        currentCrawls.setOnClickListener(this);
        changeName.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    protected void postLoginInfo (){
   	 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
       
       // define the parameter
       postParameters.add(new BasicNameValuePair("guest_code",guestCode.getText().toString()));


       String response = null;
       //Log.e("log_tag","Got to this part 1");
       // call executeHttpPost method passing necessary parameters 
       try {
    	   response = executeHttpPost("http://164.138.29.169/android_login.php",postParameters);
    	   
    	   List<String> list = new ArrayList<String>();
    	   // store the result returned by PHP script that runs MySQL query
    	   PermStorage entry = new PermStorage(MainActivity.this);
    	   list = entry.Get_Prev_Crawls();
    	   
    	   String result = response.toString();  
    	   list.add(0, result);
    	  
    	   
    	   if(result != "Login Fail")
    	   {
    		   	
   				entry.open();
   				entry.Store_Prev_Crawls(list);
    	   }
    	   
    	   //tv.setText(response);
    	   InputMethodManager imm = (InputMethodManager)getSystemService(
    	   Context.INPUT_METHOD_SERVICE);
   		   imm.hideSoftInputFromWindow(guestCode.getWindowToken(), 0);

   		   Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();



       }
       catch (Exception e) {
     	  Toast.makeText(getApplicationContext(),"Connection Error, Please try again", Toast.LENGTH_LONG).show();
     	  Log.e("log_tag","Error in http connection!!" + e.toString());     
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

    
    
    
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.bLogIn:
    		postLoginInfo();
    		break;
    	case R.id.bGames:
    		startActivity(new Intent("ie.tcd.pubcrawl.GAMEMENU"));
    		break;
    	case R.id.bCurrentCrawls:
    		startActivity(new Intent("ie.tcd.pubcrawl.tabview.CRAWLLISTPAGE"));
    		break;
    	case R.id.bChangeName:
    		startActivity(new Intent("ie.tcd.pubcrawl.CHANGEUSERNAME"));
    		break;
    	}
    }    
}
