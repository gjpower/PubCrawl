package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;

public class twoDDice extends Activity  {
	
	DiceView2 newView;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		newView = new DiceView2(this);
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
