package ie.tcd.pubcrawl;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CrawlMapOverview extends android.support.v4.app.FragmentActivity implements LocationListener{
	
	private int numpub = 2;
	private Marker[] marker = new Marker[numpub]; 
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private UiSettings mUiSettings;
    public LatLngBounds bounds;
    public final double latitude = 54;
    public final double longitude = -5.9;
    public LatLngBounds pubsnme;
    public LatLng pubs = new LatLng(latitude, longitude);
    public LatLng pubs1 = new LatLng(latitude, longitude);
    public LatLng myloc;
    public LatLng[] allpubs = new LatLng[numpub];
    private BitmapDescriptor icon, icon2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     	setContentView(R.layout.activity_crawl_map_overview);
     	Log.i("user", "on create");
        setUpMapIfNeeded();
        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon);
        icon2 = BitmapDescriptorFactory.fromResource(R.drawable.icon2);
        Log.i("user", "map set up");
        getpublocations();
        Log.i("user", "got locations");
        makeMarkers();
        Log.i("user", "made markers");
        init();
        Log.i("user", "init");
        //doSomething();
        
        
          
        }
    
    

    private void makeMarkers() {
    	for(int i=0; i < numpub; i++){
    		if(i==0){
    			Marker tempmarker = mMap.addMarker(new MarkerOptions()
      	      .position(allpubs[i])
      	      .title("pub.title")
      	      .snippet("pub.info and shiit")
      		  .icon(icon2));
      		marker[i] = tempmarker;
      		marker[i].showInfoWindow();
    		}
    		else{
    		Marker tempmarker = mMap.addMarker(new MarkerOptions()
    	      .position(allpubs[i])
    	      .title("pub.title")
    	      .snippet("pub.info and shiit")
    		  .icon(icon));
    		marker[i] = tempmarker;
    		marker[i].showInfoWindow();
    		}
    	}
		
	}

	private void getpublocations() {
		for(int i=0; i < numpub; i++){
			String strlat, strlng;
			double templat, templng;
			//get strings from pub management
			Log.i("user", "for loop");
			if(i==0){
			strlat = "53.347001626";
			strlng = "-6.258100569";
			}
			else{
				strlat = "53.343057711";
				strlng = "-6.258905232";
			}
			templat = Double.parseDouble(strlat);
			templng = Double.parseDouble(strlng);
			allpubs[i] = new LatLng(templat, templng);
		}
	}

	private void init(){
        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
          } 
          // Get the location manager
          locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
          Criteria criteria = new Criteria();
          provider = locationManager.getBestProvider(criteria, false);
          Location location = locationManager.getLastKnownLocation(provider);
          // Initialize the location fields
          onLocationChanged(location);
          if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);}
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(pubsnme, 500, 500, 10));
            location = locationManager.getLastKnownLocation(provider);
            onLocationChanged(location);
           /* 
            mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker arg0) {
					getDirections(arg0);
					return false;
				}
            });*/
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        onLocationChanged(location);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(pubsnme, 500, 500, 10));
    }

 
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            SupportMapFragment fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
            mMap = fragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(52,-6);
            }
        }
    }

    public void onLocationChanged(Location location) {
    	double lat = (double) (location.getLatitude());
    	double lng = (double) (location.getLongitude());
    	myloc = new LatLng(lat, lng);
    	setUpMap(lat, lng);
    	
    	LatLngBounds temp = new LatLngBounds(new LatLng(lat, lng), pubs);
    	pubsnme = temp;   
    	Log.i("user", "location changed");
    	mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(pubsnme, 500, 500, 10));
    	Log.i("user", "move camera");
    	Toast.makeText(this, " latitude " + lat + "longditude" + lng, Toast.LENGTH_SHORT).show(); 
    }
    
    
    private void setUpMap(double lat, double lng) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMyLocationEnabled(true);
        mUiSettings = mMap.getUiSettings();
    }
    
    private void getDirections(Marker mark){
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
        							Uri.parse("http://maps.google.com/maps?saddr=" 
        							+ myloc.latitude + "," + myloc.longitude + "&daddr=" 
        							+ mark.getPosition().latitude + "," + mark.getPosition().longitude));
        startActivity(intent);
        }
    
    public void setMyLocationButtonEnabled(View v) {
        mUiSettings.setMyLocationButtonEnabled(((CheckBox) v).isChecked());
    }

    
    public void onStatusChanged(String provider, int status, Bundle extras) {
      // TODO Auto-generated method stub

    }

    public void onProviderEnabled(String provider) {
      Toast.makeText(this, "Enabled new provider " + provider,
          Toast.LENGTH_SHORT).show();

    }

    public void onProviderDisabled(String provider) {
      Toast.makeText(this, "Disabled provider " + provider,
          Toast.LENGTH_SHORT).show();
    }
}
