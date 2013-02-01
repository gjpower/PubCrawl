package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateUserName extends Activity {

	private static String STATUS = "Status";
	SharedPreferences appSharedPrefs;
	public static String userName;
	EditText getName;
	int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createusername);
        
        appSharedPrefs = getSharedPreferences(STATUS, 0);
        
        boolean isFirstRun = appSharedPrefs.getBoolean("install", true);
        if (isFirstRun) {
            Editor prefsEditor = appSharedPrefs.edit();
        	prefsEditor.putBoolean("install", false);
        	prefsEditor.commit();
        	userId = 5746677;//get user id from server
        	PermStorage.Store_User_Id(userId, this);
        }
        else {
        	startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        	finish();
        }
        
        getName = (EditText) findViewById(R.id.etUserName);
        Button saveName = (Button) findViewById(R.id.bSaveUserName);
        
        saveName.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				userName = getName.getText().toString();
        		PermStorage.Store_User_Name(userName, CreateUserName.this);        		
        		startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        		finish();
			}
		});
    }
}
