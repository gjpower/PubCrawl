/*
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class CrawlMapOverview extends android.support.v4.app.FragmentActivity implements LocationListener{

	private int numpub = 2;
	private Marker[] marker = new Marker[numpub]; 
    private GoogleMap mMap;
    private LocationManager locationManager;
    private String provider;
    private UiSettings mUiSettings;
    public LatLng myloc;
    public LatLng[] allpubs = new LatLng[numpub];
    public CustomInfoAdapter cia = new CustomInfoAdapter(this);
    private BitmapDescriptor icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     	setContentView(R.layout.third);
        setUpMapIfNeeded();
        icon = BitmapDescriptorFactory.fromResource(R.drawable.icon);
        getpublocations();
        makeMarkers();
        init();
//        infoWindow=getLayoutInflater().inflate(R.layout.info_window_layout, null);
        mMap.setOnInfoWindowClickListener(null);
        //doSomething();  
          
        } 

    private void makeMarkers() {
    	for(int i=0; i < numpub; i++){
    		Marker tempmarker = mMap.addMarker(new MarkerOptions()
    	      .position(allpubs[i])
    	      .title("Messrs Maguire")
    	      .snippet("Touch for Directions")
    		  .icon(icon));
    		marker[i] = tempmarker;
    		marker[i].showInfoWindow();
    	}
		
	}

	private void getpublocations() {
		//String strlat, strlng;
		String[] testlat = new String[numpub];
		String[] testlng = new String[numpub];
		testlat[0] = "53.3439208";
		testlng[0] = "-6.2607184";
		testlat[1] = "53.347001626 ";
		testlng[1] = "-6.258100569";
		double templat, templng;
		for(int i=0; i < numpub; i++){
			//get strings from pub managment
			//strlat = "53.3439208";
			//strlng = "-6.2607184";
			templat = Double.parseDouble(testlat[i]);
			templng = Double.parseDouble(testlng[i]);
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
            location = locationManager.getLastKnownLocation(provider);
            onLocationChanged(location);
            
            mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

				public void onInfoWindowClick(Marker arg0) {
					getDirections(arg0);
				}
            });
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
    }

 
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
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
    	Log.i("user", "location changed");
    	if(allpubs[0]!=null)
        	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(allpubs[0], 14));
        else
        	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myloc, 14));
    	Log.i("user", "move camera");
    	Toast.makeText(this, "latitude " + lat + " longditude " + lng, Toast.LENGTH_SHORT).show(); 
    }
    
    
    private void setUpMap(double lat, double lng) {
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMyLocationEnabled(true);
        mUiSettings = mMap.getUiSettings();
        mMap.setInfoWindowAdapter(cia);
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
*/