package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateUserName extends Activity {

	public static String userName;
	EditText getName;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createusername);
        
        getName = (EditText) findViewById(R.id.etUserName);
        Button saveName = (Button) findViewById(R.id.bSaveUserName);
        
        PermStorage info = new PermStorage(getApplicationContext());
        userName = info.Get_User_Name();

        if(userName!="NoName"){
        startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        finish();
        }
        
        saveName.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				 userName = getName.getText().toString();
				 PermStorage info = new PermStorage(getApplicationContext());
			     info.Store_User_Name(userName);
			        
        		
        		startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        		finish();
			}
		});

    }
}
