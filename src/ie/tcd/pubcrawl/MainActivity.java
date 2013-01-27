package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	String name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
        Button gameMenu = (Button) findViewById(R.id.bGames);
        
        gameMenu.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				EditText editText = (EditText) findViewById(R.id.eName);
		        
		        name = editText.getText().toString();
		        
		        PermStorage exampleVariable;
		        exampleVariable = new PermStorage(getApplicationContext());
		        
		        exampleVariable.Store_User_Name(name);
				
		        startActivity(new Intent("ie.tcd.pubcrawl.GAMEMENU"));
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
}
