package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CurrentCrawls extends Activity{
	
	String[][] crawlData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.currentcrawls);
		
		//Crawl Data set to "No Crawls" on install of app
		TextView textView = (TextView) findViewById(R.id.Crawl_Data);
		crawlData = PermStorage.Get_Crawl_Data(this);
		textView.setText(crawlData[0][0]);
	}

}
