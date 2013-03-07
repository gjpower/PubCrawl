package ie.tcd.pubcrawl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class FirstActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first);
		
		ListView dListView = (ListView) findViewById(R.id.mylist);

		String[][] infoArray = new String[3][3];
		infoArray[0][0] = "Society Pub Crawl";
		infoArray[0][1] = "13/3/4\t\t7pm\t\tDoyle\'s";
		infoArray[1][0] = "Other Pub Crawl";
		infoArray[1][1] = "22/3/4\t\t8pm\t\tO\'Neill\'s";
		infoArray[2][0] = "";
		infoArray[2][1] = "";

/*
		while (name != "")
		{
			infoArray[i][0] = name;
			infoArray[i][1] = details;
			i++;
			name = "";
		}
        */      

          ArrayList<Map<String, String>> list = buildData(infoArray);
          String[] from = { "name", "details" };
          int[] to = { R.id.crawlName, R.id.crawlDetails };

          SimpleAdapter adapter = new SimpleAdapter(this, list,
              R.layout.list_item_crawl, from, to);
          dListView.setAdapter(adapter);
        }

        private ArrayList<Map<String, String>>buildData(String info[][])
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

        private HashMap<String, String> putData(String name, String details)
        {
        	HashMap<String, String> item = new HashMap<String, String>();
        	item.put("name", name);
        	item.put("details", details);
        	return item;
        }


}


