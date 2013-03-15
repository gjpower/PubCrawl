package ie.tcd.pubcrawl;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	public String userName;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button gameMenu = (Button) findViewById(R.id.bGames);
        Button currentCrawls = (Button) findViewById(R.id.bCurrentCrawls);
        Button changeName = (Button) findViewById(R.id.bChangeName);
        
        PermStorage request = new PermStorage(this);
        request.open();
        userName = request.Get_User_Name();
        request.close();
        
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvUserName.setText(userName);
        
        gameMenu.setOnClickListener(this);
        currentCrawls.setOnClickListener(this);
        changeName.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void onClick(View v)
    {
    	switch (v.getId())
    	{
    	case R.id.bGames:
    		startActivity(new Intent("ie.tcd.pubcrawl.GAMEMENU"));
    		break;
    	case R.id.bCurrentCrawls:
    		startActivity(new Intent("ie.tcd.pubcrawl.tabview.CRAWLLISTPAGE"));
    		break;
    	case R.id.bChangeName:
    		startActivity(new Intent("ie.tcd.pubcrawl.CHANGEUSERNAME"));
    		break;
    	}
    }    
}
