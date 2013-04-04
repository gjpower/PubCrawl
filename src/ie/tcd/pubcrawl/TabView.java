package ie.tcd.pubcrawl;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Display;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabView extends TabActivity {
	TabHost mTabHost;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	Display display = getWindowManager().getDefaultDisplay(); //Design
    	int tabWidth = (display.getWidth())/4; //Design
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
        
        TabSpec fourthSpec = mTabHost.newTabSpec("fourth");
        fourthSpec.setIndicator("Upload", null);
        Intent fourthIntent = new Intent(this, Photo_Cap.class);
        fourthSpec.setContent(fourthIntent);
        
        mTabHost.addTab(firstSpec);
        mTabHost.addTab(secondSpec);
        mTabHost.addTab(thirdSpec);
        mTabHost.addTab(fourthSpec);
        setTabColor(mTabHost);  //Design
        setTabWidth(mTabHost, tabWidth); //Design
    }
    //Design Function
    public static void setTabWidth(TabHost tabhost, int width){
    	for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        	tabhost.getTabWidget().getChildAt(i).setMinimumWidth(width);
    }
  //Design Function
    public static void setTabColor(TabHost tabhost) {
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        	tabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tabunselected); //unselected
        	
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).setBackgroundResource(R.drawable.tabselected); // selected
    }
  //Design Function
	public void onTabChanged(String tabId) {
		setTabColor(mTabHost);		
	}

}
