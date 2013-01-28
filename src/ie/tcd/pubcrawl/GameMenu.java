package ie.tcd.pubcrawl;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class GameMenu extends Activity 
{
	String name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamemenu);
		
		TextView textView = (TextView) findViewById(R.id.textView1);
		
		PermStorage exampleVariable;
        exampleVariable = new PermStorage(getApplicationContext());
        
        name = exampleVariable.Get_User_Name();
        
        textView.setText(name);
	}
}
