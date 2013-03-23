package ie.tcd.pubcrawl;

import java.io.File;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebImage extends Activity {
	// implemented this so the user isn't leaving the program to load up a browser to simple view the image
	// could add ability to save the image to your gallery from the menu item

	private WebView webArea;
	private String urlString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_image);

		
		Intent prevIntent = getIntent();
		urlString = prevIntent.getStringExtra("url");
		
		webArea = (WebView) findViewById(R.id.webArea);
		
		
		try { 
			webArea.loadUrl(urlString);	//don't even know if this returns an exception on failure
		} catch (Exception e) {		//just making sure it won't crash
            e.printStackTrace();
        }
		
		
		WebSettings webSettings = webArea.getSettings();
		webSettings.setBuiltInZoomControls(true);
		//webSettings.setDisplayZoomControls(false);	//requires api 11
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_web_image, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection		
	    switch (item.getItemId()) {
	        case R.id.save:
	            saveImage();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	private void saveImage() {
		
		File root = android.os.Environment.getExternalStorageDirectory();       	//get our sd card directory        

        File savedir = new File ( root.getAbsolutePath() + "/Pictures/PubCrawl Images"); //create our directory if it doesn't exist
        if(savedir.exists()==false) {
             savedir.mkdirs();
        }
		
		Uri uri = Uri.parse(urlString);
        DownloadManager downloader = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, 
        					"PubCrawl Images/" + urlString.substring(urlString.lastIndexOf('/') +1));
        //request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //request.allowScanningByMediaScanner();
        downloader.enqueue(request);
        
		//below is manual method for downloading
       /* 
		try {
	           URL url = new URL(urlString);
	           String fileName = url.getFile();
	           fileName = fileName.substring(fileName.lastIndexOf('/') + 1);	//remove all but the filename
	           File file = new File(savedir, fileName);
	           
	           

	           // Open a connection to that URL.
	           URLConnection ucon = url.openConnection();

	          
	           //	Define InputStreams to read from the URLConnection.
	           InputStream is = ucon.getInputStream();
	           BufferedInputStream bis = new BufferedInputStream(is);


	           // Read bytes to the Buffer until there is nothing more to read(-1).

	           ByteArrayBuffer baf = new ByteArrayBuffer(5000);
	           int current = 0;
	           while ((current = bis.read()) != -1) {
	              baf.append((byte) current);
	           }


	           // Write our buffer to the file
	           FileOutputStream fos = new FileOutputStream(file);
	           fos.write(baf.toByteArray());
	           fos.flush();
	           fos.close();
	           
	           String[] filePath = {file.getAbsolutePath()}; //doing this as below method requires a string array
	           
	           // below required to make images appear in the users gallery program
	           MediaScannerConnection.scanFile(this, filePath, null, new MediaScannerConnection.OnScanCompletedListener()  
	           	{ public void onScanCompleted(String path, Uri uri) {	//open the image in the gallery once it has been scanned into it
//	        		Intent intent = new Intent();					//alternatively could just post a toast saying the image has been saved
//	              intent.setAction(Intent.ACTION_VIEW);
//	              intent.setDataAndType(uri,"image/*");
//	              startActivity(intent);
	      	} }  );
	           
	           Toast.makeText(this, "Image saved and added to Gallery" , Toast.LENGTH_SHORT).show(); //just giving a message instead of opening in gallery

	   } catch (IOException e) {
	       Log.e("DownloadManager", "Error: " + e);
	   }
	   */
	   
		
	}
	
	

}
