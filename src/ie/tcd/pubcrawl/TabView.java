package ie.tcd.pubcrawl;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabView extends TabActivity {
	TabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_main);
        mTabHost=getTabHost();
        
        TabSpec firstSpec = mTabHost.newTabSpec("Info");
        firstSpec.setIndicator("Info", null);
        Intent firstIntent = new Intent(this, Info.class);
        firstSpec.setContent(firstIntent);
        
        TabSpec secondSpec = mTabHost.newTabSpec("second");
        secondSpec.setIndicator("Stream", null);
        Intent secondIntent = new Intent(this, ViewFeed.class);
        secondSpec.setContent(secondIntent);
        
        TabSpec thirdSpec = mTabHost.newTabSpec("third");
        thirdSpec.setIndicator("Maps", null);
        Intent thirdIntent = new Intent(this, ThirdActivity.class);
        thirdSpec.setContent(thirdIntent);
        
        mTabHost.addTab(firstSpec);
        mTabHost.addTab(secondSpec);
        mTabHost.addTab(thirdSpec);
    }

}
