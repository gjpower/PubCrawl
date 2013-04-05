package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;

public class DiceGames extends Activity {
	
	DiceView diceView;
	public static int Width, Height;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dice_games);
        Display display = getWindowManager().getDefaultDisplay();
        final int width = (display.getWidth());
        final int height = (display.getHeight());
        Width = width;
        Height = height;
        
        diceView = new DiceView(this);
        setContentView(diceView);
    }

 
    @Override
    public void onBackPressed() 
    {
    	super.onBackPressed();
    }

}
