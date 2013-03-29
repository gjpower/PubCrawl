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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


//Activity shows a list of all pub crawls that a user has entered the code for
//Includes some basic details underneath: date, time, first pub
//List items can clicked on which goes to main 4 tab activity
public class CrawlsListPage extends Activity {
	//public static String[][] fromNetworkArray;
	//Network Connections
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	private static HttpClient mHttpClient;
	private static Context context;

                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    context = this;
                setContentView(R.layout.activity_crawls_list_page);
                ListView dListView = (ListView) findViewById(R.id.mylist);

                String[][] fromNetworkArray;
                String[][] infoArray;
                
                /*
                //dummy information for now
                infoArray[0][0] = "Society Pub Crawl";
                infoArray[0][1] = "13/3/4\t\t7pm\t\tDoyle\'s";
                infoArray[1][0] = "Other Pub Crawl";
                infoArray[1][1] = "22/3/4\t\t8pm\t\tO\'Neill\'s";
                infoArray[2][0] = "";
                infoArray[2][1] = "";
				*/
                
                List<String> dataList = new ArrayList<String>();
    		   	PermStorage entry = new PermStorage(CrawlsListPage.this);
   				entry.open();
   				dataList = entry.Get_Prev_Crawls();
   				String CrawlID = dataList.get(0);
   				
   				
   				
                
                int i = 0;	//i is which crawl on list it is

                //try
                //{
                                //cycle through crawlIDs

                       //  while(aCrawlID != 0)
                       //  {
                                infoArray = getSchedule(CrawlID);
                                //infoArray[i][0] = fromNetworkArray[0][0];
                                //infoArray[i][1] = fromNetworkArray[0][1] + "\t\t";
                                //infoArray[i][1] = infoArray[i][1] + fromNetworkArray[0][2];
                        //        aCrawlID = 0;
                      //  }

                //}
                         /*
                catch
                {

                        PermStorage request = new PermStorage(this);

                        request.open();

                                //cycle through crawlIDs

                                while(aCrawlID != 0)
                        		{
                                fromNetworkArray = request.Get_Crawl_Data(aCrawlID);
                                infoArray[i][0] = fromNetworkArray[0][0];
                                infoArray[i][1] = fromNetworkArray[0][1] + "\t\t";
                                infoArray[i][1] = infoArray[i][1] + fromNetworkArray[0][2];
                                }
                        request.close();

                }
                */
                
                 



                    //filling the array of items to go into the list
                    ArrayList<Map<String, String>> list = buildData(infoArray);
                    String[] from = { "name", "details" };
                    int[] to = { R.id.crawlName, R.id.crawlDetails }; //identifies row layout to use

                    //adapter puts items in hashmap into list
                    SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.list_item_crawl, from, to);
                    dListView.setAdapter(adapter);



                    dListView.setOnItemClickListener(new OnItemClickListener()
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
                        		   		PermStorage entry = new PermStorage(CrawlsListPage.this);
                        		   		entry.open();
                                        entry.Indicate_Current_Crawl(CrawlsListPage.this, "36");
                        		   		Intent myIntent = new Intent("com.example.tabview.TABVIEW");
                                        startActivity(myIntent);

                                }
                        });

                  }




                  //Adds each pub crawl in the array to the map
                  private ArrayList<Map<String, String>> buildData(String info[][])
                  {
                                int i=0;
                            ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
                            
                            while (i < 1)
                            {
                                list.add(putData(info[i][0], info[i][1]));
                                i++;
                            }
                            return list;
                  }

                  //hashmap created
                  private HashMap<String, String> putData(String name, String details)
                  {
                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put("name", name);
                            item.put("details", details);
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
                    String[][] info = new String[12][6];
                    
                    ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
                    postParameters.add(new BasicNameValuePair("CrawlID",id));
                    String response = null;
                    
                    // call executeHttpPost method passing necessary parameters
                    try {
                        response = executeHttpPost("http://164.138.29.169/getschedule.php",postParameters);
                        
                        // store the result returned by PHP script that runs MySQL query
                        String result = response.toString();
                        
                        //parse json data
                        try{
                            JSONArray jArray = new JSONArray(result);
                            for(int i=0;i<jArray.length();i++){
                                JSONObject json_data = jArray.getJSONObject(i);
                                info[i][0] = json_data.getString("crawlname");
                                info[i][1] = json_data.getString("time");
                                info[i][2] = json_data.getString("pubname");
                                info[i][3] = json_data.getString("publocation");
                                info[i][4] = json_data.getString("latitude");
                                info[i][5] = json_data.getString("longitude");
                            }
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