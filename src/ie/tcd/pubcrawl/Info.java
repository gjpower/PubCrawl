
package ie.tcd.pubcrawl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Info extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    
        Event event = new Event();
        
        
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





}
	
        
  













    


