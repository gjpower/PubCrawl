package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CurrentCrawls extends Activity{
	
	String[][] crawlData1, crawlData2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.currentcrawls);
		
		/*crawlData1 = new String[3][3];
		crawlData1[0][0] = "1";
		crawlData1[0][1] = "String";
		crawlData1[0][2] = "String";
		crawlData1[1][0] = "2";
		crawlData1[1][1] = "String";
		crawlData1[1][2] = "String";
		crawlData1[2][0] = "3";
		crawlData1[2][1] = "String";
		crawlData1[2][2] = "String";
		
		PermStorage.Store_Crawl_Data(crawlData1, this);
		PermStorage.Indicate_Current_Crawl(2, this);
		
		//Crawl Data set to "No Crawls" on install of app
		TextView textView = (TextView) findViewById(R.id.Crawl_Data);
		crawlData2 = new String[1][3];
		int numCrawls = PermStorage.Get_Current_Crawl(this);
		textView.setText(String.valueOf(numCrawls));
		crawlData2 = PermStorage.Get_Current_Crawl(this);
		textView.setText(crawlData2[0][0]);
		String testString = PermStorage.Get_Current_Crawl(this);
		textView.setText(testString);*/
	}

}
