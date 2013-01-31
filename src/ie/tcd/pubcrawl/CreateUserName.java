package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Intent;
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
        
        userName = PermStorage.Get_User_Name(this);
        if(userName!="NoName"){
        	startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        	finish();
        }
        
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
