package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class GameMenu extends Activity 
{
	String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamemenu);
	}
	
	public void Cards_Menu(View v)
	{
		startActivity(new Intent("ie.tcd.pubcrawl.CARDGAMES"));
	}
	public void Dice_Menu(View v2)
	{
		startActivity(new Intent("ie.tcd.pubcrawl.DICEGAMES"));
	}
	public void Coin_Menu(View v3)
	{
		startActivity(new Intent("ie.tcd.pubcrawl.COINGAMES"));
	}
	public void Spinner_Menu(View v4)
	{
		startActivity(new Intent("ie.tcd.pubcrawl.SPINNERGAMES"));
	}
	
	
}
