package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;


public class CoinGames extends Activity {
	public static boolean flip = true;
	CoinView coinView;
	public static String outcome;
	public static int Width, Height;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
		
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.activity_coin_games);
        Display display = getWindowManager().getDefaultDisplay();
        final int width = (display.getWidth());
        final int height = (display.getHeight());
        Width = width;
        Height = height;
        coinView = new CoinView(this);
        setContentView(coinView);
        outcome = "tails";

        
    }
@Override
public void onBackPressed() 
	{
	
		coinView.thread.Set_Running(false);
		coinView.thread.Reset();
		super.onBackPressed();
	}

}
