package ie.tcd.pubcrawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


//Activity shows a list of all pub crawls that a user has entered the code for
//Includes some basic details underneath: date, time, first pub
//List items can be made into buttons which go to main 3 tab activity
public class CrawlsListPage extends Activity {

   
		  protected void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_crawls_list_page);
	        ListView dListView = (ListView) findViewById(R.id.mylist);
	        
	        String[][] infoArray = new String[6][6];
	        
	        //dummy information for now
	        infoArray[0][0] = "Society Pub Crawl";
	        infoArray[0][1] = "13/3/4\t\t7pm\t\tDoyle\'s";
	        infoArray[1][0] = "Other Pub Crawl";
	        infoArray[1][1] = "22/3/4\t\t8pm\t\tO\'Neill\'s";
	        infoArray[2][0] = "";
	        infoArray[2][1] = "";
	        
	        
		    ArrayList<Map<String, String>> list = buildData(infoArray);
		    String[] from = { "name", "details" };
		    int[] to = { R.id.crawlName, R.id.crawlDetails }; //identifies row layout to use
		    
		    //adapter puts items in hashmap into list
		    SimpleAdapter adapter = new SimpleAdapter(this, list,
		        R.layout.list_item_crawl, from, to);
		    dListView.setAdapter(adapter);
		  
		  
		  
		    dListView.setOnItemClickListener(new OnItemClickListener() 
		    {
		    				
				
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
				{
					// TODO Auto-generated method stub
		//			Intent myIntent = new Intent(CrawlsListPage.this, com.example.);
		//			myIntent.putExtra("ID", idstring);
		//			startActivity(myIntent);
	//				Intent myIntent = new Intent("MAINACTIVITY");
					//Intent myIntent = new Intent(this, com.example.crawllist.MainActivity.class);
		//			startActivity(myIntent);
					
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
			    while (info[i][0] != "")
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
		  
	}



