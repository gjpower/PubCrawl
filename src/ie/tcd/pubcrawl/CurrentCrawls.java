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
		
//		crawlData1 = new String[3][3];
//		crawlData1[0][0] = "1";
//		crawlData1[0][1] = "String";
//		crawlData1[0][2] = "";
//		crawlData1[1][0] = "2";
//		crawlData1[1][1] = "String";
//		crawlData1[1][2] = "Success";
//		crawlData1[2][0] = "3";
//		crawlData1[2][1] = "String";
//		crawlData1[2][2] = "String";
//		
//		PermStorage entry = new PermStorage(this);
//		entry.open();
//		entry.Store_Crawl_Data(crawlData1);
//		crawlData2 = entry.Get_Crawl_Data();
//		entry.close();
//		TextView tv = (TextView) findViewById(R.id.Crawl_Data);
//		tv.setText(crawlData2[1][2]);
	}

}
