package ie.tcd.pubcrawl;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

public class PubDescription extends Activity {

//	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
//	private static HttpClient mHttpClient;
	private static Context context;
	TextView pub_name, pub_location, pub_description, up_rating, down_rating, latitude, longitude;
	static PermStorage entry;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		.penaltyLog().build());
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_pub_description);

		String[] infoArray;              			
		String pubID = this.getIntent().getStringExtra("pubID");
//		infoArray = getSchedule(pubID);
		entry = new PermStorage(PubDescription.this);
		entry.open();
		infoArray = entry.Get_Pub(pubID);
		
		if(infoArray!=null) {
			Log.e("ArrayBanter", infoArray[0]);
			Log.e("ArrayBanter", infoArray[1]);
			Log.e("ArrayBanter", infoArray[2]);
			Log.e("ArrayBanter", infoArray[3]);
			Log.e("ArrayBanter", infoArray[4]);
			Log.e("ArrayBanter", infoArray[5]);
			Log.e("ArrayBanter", infoArray[6]);
			Log.e("ArrayBanter", infoArray[7]);
			
			pub_name=new TextView(this); 
			pub_name=(TextView)findViewById(R.id.name); 
			pub_name.setText(infoArray[0]);
	
			pub_location=new TextView(this); 
			pub_location=(TextView)findViewById(R.id.location); 
			pub_location.setText(infoArray[1]);
	
			pub_description=new TextView(this); 
			pub_description=(TextView)findViewById(R.id.description); 
			pub_description.setText(infoArray[2]);
	
			up_rating=new TextView(this); 
			up_rating=(TextView)findViewById(R.id.up); 
			up_rating.setText(infoArray[3]);
	
			down_rating=new TextView(this); 
			down_rating=(TextView)findViewById(R.id.down); 
			down_rating.setText(infoArray[4]);
	
			latitude=new TextView(this); 
			latitude=(TextView)findViewById(R.id.latitude); 
			latitude.setText(infoArray[5]);
	
			longitude=new TextView(this); 
			longitude=(TextView)findViewById(R.id.longitude); 
			longitude.setText(infoArray[6]);
		}
		else {
			//say we don't have the info for some reason I guess
		}
	}
}