package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebImage extends Activity {
	// implemented this so the user isn't leaving the program to load up a browser to simple view the image
	// could add ability to save the image to your gallery from the menu item

	private WebView webArea;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_image);
		
		Intent prevIntent = getIntent();
		String url = prevIntent.getStringExtra("url");
		
		webArea = (WebView) findViewById(R.id.webArea);
		
		
		try { 
			webArea.loadUrl(url);	//don't even know if this returns an exception on failure
		} catch (Exception e) {		//just making sure it won't crash
            e.printStackTrace();
        }
		
		
		WebSettings webSettings = webArea.getSettings();
		webSettings.setBuiltInZoomControls(true);
		//webSettings.setDisplayZoomControls(false);	//requires api 11
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_web_image, menu);
		return true;
	}

}
