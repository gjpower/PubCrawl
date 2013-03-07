package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangeUserName extends Activity {
	public static String userName;
	EditText getName;
	TextView currName;
	Button saveName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        
        currName = (TextView) findViewById(R.id.tvCurrentName);
        getName = (EditText) findViewById(R.id.etUserName);
        saveName = (Button) findViewById(R.id.bSaveUserName);
        
        //Get user name and display
        PermStorage request = new PermStorage(this);
        request.open();
        userName = request.Get_User_Name();
        request.close();
        currName.setText("Current User Name is " + userName);
        getName.setText(userName);
        
        saveName.setOnClickListener(new View.OnClickListener() {			
			//Save new user name and go back to the main activity
			public void onClick(View v) {
				userName = getName.getText().toString();
				PermStorage entry = new PermStorage(ChangeUserName.this);
				entry.open();
        		entry.Store_User_Name(userName);
        		entry.close();
        		startActivity(new Intent("ie.tcd.pubcrawl.MAINACTIVITY"));
        		finish();
			}
		});
    }
}
