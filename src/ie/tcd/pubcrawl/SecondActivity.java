package ie.tcd.pubcrawl;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends Activity {
	Button Button; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
	
		 Button = (Button) findViewById(R.id.button1);
	
		 Button.setOnClickListener(new View.OnClickListener(){        
	        	public void onClick(View v) {
	        			Intent intent2 = new Intent(SecondActivity.this, ViewFeed.class);
		        	//intent2.putExtras(mBundle);
		        	startActivity(intent2);
		   	  
		        	}
	        	
	        });
	
	
	
	
	
	
	
	}
	
	
	
	
	
	

}
