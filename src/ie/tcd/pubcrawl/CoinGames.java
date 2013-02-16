package ie.tcd.pubcrawl;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.TextView;

 


public class CoinGames extends Activity {

	public TextView result;
	GestureOverlayView gOV;
	public GestureDetector gestureScanner;
	public static boolean flip = true;
	CoinView coinView;
	public static String outcome;
	
	@SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        coinView = new CoinView(this);
        setContentView(coinView);
        result = (TextView) findViewById(R.id.resultOfCoin);
		outcome = "tails";

        
    }
	
	@Override
	public void onBackPressed() 
	{
		this.coinView.thread.Set_Running(false);
		System.out.println("Back pressed");
		super.onBackPressed();
	}

}