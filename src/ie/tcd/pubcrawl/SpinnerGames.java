package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class SpinnerGames extends Activity {

	public static int Width, Height; 
	SpinnerView spinnerView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spinner_games);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        Display display = getWindowManager().getDefaultDisplay();
        final int width = (display.getWidth());
        final int height = (display.getHeight());
        Width = width;
        Height = height;
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        getMenuInflater().inflate(R.menu.activity_spinner_games, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void numPos(View view)
    {
    	switch(view.getId()) 
    	{
        case R.id.radioButton1:
        	spinnerView = new SpinnerView(this, 4);
        	break;
        case R.id.radioButton2:
        	spinnerView = new SpinnerView(this, 6);
            break;
        case R.id.radioButton3:
        	spinnerView = new SpinnerView(this, 8);
            break;
        case R.id.radioButton4:
        	spinnerView = new SpinnerView(this, 12);
            break;
    	}
    }
    
    
    public void start(View view)
    {
    	if(spinnerView == null)
    	{
    		spinnerView = new SpinnerView(this, 12);
    	}
    	setContentView(spinnerView);
    	return;
    }
    
    public void onBackPressed()
    {
    	spinnerView.thread.Set_Running(false);
    	this.finish();
    }

}
