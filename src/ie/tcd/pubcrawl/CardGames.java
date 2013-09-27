package ie.tcd.pubcrawl;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CardGames extends Activity implements OnTouchListener{
	private Spinner spinner1;
	EditText edittext;
	KingsView kingSurfaceView; 
	int bp, count;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bp=0;
    	kingSurfaceView = new KingsView(this);
    	kingSurfaceView.setOnTouchListener(this);
        setContentView(R.layout.activity_card_games);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //target api level can be set so things are not used like open gl dice
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_card_games, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void king(View v){
    	setContentView(kingSurfaceView);
    }
    public void set(View v){
    	setContentView(R.layout.kings_rules);
    }
    public void change(View v){
    	spinner1 = (Spinner) findViewById(R.id.spinner1);
    	edittext = (EditText) findViewById(R.id.editText2);
    	String t = String.valueOf(edittext.getText());
    	String c = String.valueOf(spinner1.getSelectedItem());
    	int i = 0;
    	if(c .equals("Jack")){i=10;}
    	if(c .equals("Queen")){i=11;}
    	if(c .equals("King")){i=12;}
    	if(c .equals("2"))i=1;
    	if(c .equals("3"))i=2;
    	if(c .equals("4"))i=3;
    	if(c .equals("5"))i=4;
    	if(c .equals("6"))i=5;
    	if(c .equals("7"))i=6;
    	if(c .equals("8"))i=7;
    	if(c .equals("9"))i=8;
    	if(c .equals("10"))i=9;    	
    	kingSurfaceView.Rules[i]=t;
    	Toast.makeText(getApplicationContext(),"\nCard :" + c+"\nRule : "+ t ,Toast.LENGTH_SHORT).show();
    	setContentView(R.layout.kings_rules);
    	InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
    }
    public void back(View v){
    	setContentView(R.layout.activity_card_games);
    }
	protected void onPause() {
		super.onPause();
		kingSurfaceView.pause();
	}
	protected void onResume() {
		super.onResume();
		kingSurfaceView.resume();
	}
	@Override
	public void onBackPressed()
	{
		bp = (bp + 1);
	    if (bp>1) {
	    	kingSurfaceView.bmp1.recycle();
	    	kingSurfaceView.bmp2.recycle();
	        this.finish();
	    }
	    else {Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();}
	}
	public boolean onTouch(View v, MotionEvent event) {
		bp=0;
		if(!kingSurfaceView.next){
			if(kingSurfaceView.kingrule==5){
				bp=1;
				onBackPressed();
			}
			else{kingSurfaceView.next=true;}
		}
		return false;
	}
}