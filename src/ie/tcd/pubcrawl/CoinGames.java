package ie.tcd.pubcrawl;

import android.app.Activity;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;


public class CoinGames extends Activity implements android.view.GestureDetector.OnGestureListener {

	public TextView result;
	public Coin coin = new Coin();
	GestureOverlayView gOV;
	public GestureDetector gestureScanner;
	public static boolean flip =true;
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

		gestureScanner = new GestureDetector(this);
		outcome = "tails";

        
    }


		// reads the gesture and determines what type it is 
	public boolean onTouchEvent(MotionEvent me){
		return gestureScanner.onTouchEvent(me);
	}
	
	// function that has the swipe vector and calls the "Flip_Coin" function 
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
	{
/*		result.setText("   ");
		if(velocityY<0)
		{
		 velocityY = velocityY*(-1);
		}
		//Toast.makeText(this, coin.Flip_Coin(pos), 0).show();
		result.setText(coin.Flip_Coin(velocityY));
		*/
		return true;
	}
	
	
	// useless functions but they have to be included for the ones we want to work 
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
		return false;
	}

	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2, float arg3) {
		// TODO Auto-generated method stub
		
		//System.out.println(arg3);
		//Toast.makeText(this, coin.Flip_Coin(arg3), 0).show();
		return false;
	}

	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
		
	}

	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
		return false;
	}
	// added functionality to stop the threads from running
	@Override
	public void onBackPressed() {
		coinView.thread.Set_Running(false);
		coinView.thread.flipping = false;
		System.out.println("back pressed");
		super.onBackPressed();

	}

}
