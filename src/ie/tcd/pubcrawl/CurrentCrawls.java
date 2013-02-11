package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CurrentCrawls extends Activity{
	
	String[][] crawlData;
	int numCrawls=-1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.currentcrawls);
		
		//Crawl Data set to "No Crawls" on install of app
		TextView textView = (TextView) findViewById(R.id.Crawl_Data);
		/*String[][] test = new String[3][4];
		test[0][0] = "ZeroZero";
    	test[0][1] = "ZeroOne";
    	test[0][2] = "ZeroTwo";
    	test[0][3] = "ZeroThree";
    	test[1][0] = "OneZero";
    	test[1][1] = "OneOne";
    	test[1][2] = "OneTwo";
    	test[1][3] = "OneThree";
    	test[2][0] = "TwoZero";
    	test[2][1] = "TwoOne";
    	test[2][2] = "TwoTwo";
    	test[2][3] = "TwoThree";
    	PermStorage.Store_Crawl_Data(test, this);*/
    	//numCrawls = PermStorage.Get_Crawl_Data(this);
		crawlData = PermStorage.Get_Crawl_Data(this);
		//textView.setText(String.valueOf(numCrawls));
		textView.setText(crawlData[0][0]);
	}

}
