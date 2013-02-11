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
        
        /*
         *Test if it is the first time running the app
         *If it is, ask to create user name
         *If not, go straight to the main activity
         */
        appSharedPrefs = getSharedPreferences(STATUS, 0);
        /*
         *The search for the boolean value labelled "install" should fail
         *on the first run and therefore "true" will be returned, the boolean
         *value is then created and given the value false for future runs
         */
        boolean isFirstRun = appSharedPrefs.getBoolean("install", true);
        if (isFirstRun) {
            Editor prefsEditor = appSharedPrefs.edit();
        	prefsEditor.putBoolean("install", false);
        	prefsEditor.commit();
        	userId = 5746677;//get user id from server
        	PermStorage.Store_User_Id(userId, this);
        	String[][] noCrawls = new String[1][1];
        	noCrawls[0][0] = "No Crawls";
        	noCrawls[0][1] = "";
        	//PermStorage.Store_Crawl_Data(noCrawls, this);
        }
        else {
        	startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        	finish();
        }
        //End of test for first run
        
        getName = (EditText) findViewById(R.id.etUserName);
        Button saveName = (Button) findViewById(R.id.bSaveUserName);
        
        saveName.setOnClickListener(new View.OnClickListener() {			
			//Save user name and go to the main activity
			public void onClick(View v) {
				userName = getName.getText().toString();
        		PermStorage.Store_User_Name(userName, CreateUserName.this);        		
        		startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        		finish();
			}
		});
    }
}
