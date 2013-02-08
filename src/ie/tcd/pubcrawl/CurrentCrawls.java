package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;

public class CurrentCrawls extends Activity{
	
	String crawlData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.currentcrawls);
		
		TextView textView = (TextView) findViewById(R.id.Crawl_Data);
		crawlData = PermStorage.Get_Crawl_Data(this);
		textView.setText(crawlData);
	}

}
