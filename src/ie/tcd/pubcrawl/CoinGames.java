package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class CoinGames extends Activity {

	public TextView result;
	public static boolean flip = true;
	CoinView coinView;
	public static String outcome;
	
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
	
		coinView.thread.Set_Running(false);
		coinView.thread.Reset();
		super.onBackPressed();
	}

}
