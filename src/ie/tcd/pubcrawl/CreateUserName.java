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

public final static String PREFS_NAME = "UserInfo";
public static String userName;
EditText getName;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createusername);
        
        getName = (EditText) findViewById(R.id.etUserName);
        Button saveName = (Button) findViewById(R.id.bSaveUserName);
        
        SharedPreferences info = getSharedPreferences(PREFS_NAME, 0);
        userName = info.getString("UserName", "NoName");

        if(userName!="NoName"){
        startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        finish();
        }
        
        saveName.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = getName.getText().toString();
        		SharedPreferences info = getSharedPreferences(PREFS_NAME, 0);
        		SharedPreferences.Editor editor = info.edit();
        		editor.putString("UserName", userName);
        		editor.commit();
        		
        		startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        		finish();
			}
		});

    }
}
