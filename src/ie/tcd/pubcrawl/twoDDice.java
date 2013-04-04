package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class twoDDice extends Activity  {
	
	diceView2 newView;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		newView = new diceView2(this);
		setContentView(newView);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		newView.end = true;
		try {
			newView.animationThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
