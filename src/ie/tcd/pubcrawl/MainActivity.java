
package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	public String userName;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button gameMenu = (Button) findViewById(R.id.bGames);
        Button currentCrawls = (Button) findViewById(R.id.bCurrentCrawls);
        
       PermStorage info = new PermStorage(getApplicationContext());
       userName = info.Get_User_Name();
        
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(userName);
        

        gameMenu.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) 
			{
		
				startActivity(new Intent("ie.tcd.pubcrawl.GAMEMENU"));
			}
		});

        currentCrawls.setOnClickListener(new View.OnClickListener() 
        {
			
			public void onClick(View v) 
			{
				

				startActivity(new Intent("ie.tcd.pubcrawl.CURRENTCRAWLS"));
			}
		});
	

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
