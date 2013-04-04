package ie.tcd.pubcrawl;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class Photo_Cap extends Activity implements View.OnClickListener {
	
	ImageButton ib;
	//Button b;
	ImageView iv;
	Intent i;
	final static int cameraData = 0;
	Bitmap bmp;
	Uri captured_image_uri;
	
	//Global Varibles for Test
	String UserID;
 	String CrawlID = "11";
 	String gps1 = "00.123";
 	String gps2 = "00.456";
 	
 	
 	
 	private static final int SELECT_FILE1 = 1;
	String selectedPath1 = "NONE";
	ProgressDialog progressDialog;
	Button upload;
 	EditText commentEditText; 
 	Button submitComment;  
	HttpEntity resEntity;
 	
	public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
	// Single instance of our HttpClient 
	private static HttpClient mHttpClient;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		  .detectDiskReads().detectDiskWrites().detectNetwork() // StrictMode is most commonly used to catch accidental disk or network access on the application's main thread
		  .penaltyLog().build());
		  
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo__cap);
		
    	PermStorage stored = new PermStorage(Photo_Cap.this);						//getting ID from perm storage
 		stored.open();
    	UserID = stored.Get_User_Id();

		CrawlID = stored.Get_Current_Crawl(Photo_Cap.this);

		
		initialize();
		InputStream is = getResources().openRawResource(R.drawable.ic_launcher);
		bmp = BitmapFactory.decodeStream(is);
		
	}
	
	
	
private void initialize(){
	iv=(ImageView) findViewById(R.id.IVRP);
	ib=(ImageButton) findViewById(R.id.takepic);
	//b=(Button)findViewById(R.id.uploadButton);
	commentEditText = (EditText) findViewById(R.id.commentEditText);
    submitComment = (Button) findViewById(R.id.submitComment);
    
    
	//b.setOnClickListener(this);
	ib.setOnClickListener(this);
	submitComment.setOnClickListener(this);
	
}

public void onClick(View v) {
	// TODO Auto-generated method stub
	switch (v.getId()){
	case R.id.takepic:
		i=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		//System.out.println("_______________uri_________________");
		startActivityForResult(i, cameraData);
		
	break;
	case R.id.submitComment:
		Thread commentPosting = new Thread() {
			public void run() {
				postComment();
			}
		};
		commentPosting.start();
	break;
	

	
	}
}
//__________________________________________________________________________________________
/*
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode == RESULT_OK){
		Bundle extras = data.getExtras();
		bmp=(Bitmap)extras.get("data");
		iv.setImageBitmap(bmp);
		//captured_image_uri
    }
}
*/


protected void postComment (){
	 ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    
    // define the parameter
    postParameters.add(new BasicNameValuePair("comment",commentEditText.getText().toString()));
    postParameters.add(new BasicNameValuePair("userID", UserID));
    postParameters.add(new BasicNameValuePair("crawlID", CrawlID));
    postParameters.add(new BasicNameValuePair("gps1", gps1));
    postParameters.add(new BasicNameValuePair("gps2", gps2));
   
    
    String response = null;
    //Log.e("log_tag","Got to this part 1");
    // call executeHttpPost method passing necessary parameters 
    try {
    	doFileUpload();	
    }
    catch (Exception e) {
    	//wrapping toasts with run on ui thread for laziness
    	runOnUiThread(new Runnable(){ public void run() {
    	Toast.makeText(getApplicationContext(),"Connection Error, Please try again", Toast.LENGTH_LONG).show();
    	}});
    	Log.e("log_tag","Error in http connection!!" + e.toString());
    	
    }
    try {
response = executeHttpPost("http://164.138.29.169/post_comment_script.php",postParameters);

// store the result returned by PHP script that runs MySQL query
final String result = response.toString();  
//tv.setText(response);
InputMethodManager imm = (InputMethodManager)getSystemService(
		Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
		runOnUiThread(new Runnable(){ public void run() {
Toast.makeText(getApplicationContext(),result, Toast.LENGTH_LONG).show();
		}});



    }
    catch (Exception e) {
    	runOnUiThread(new Runnable(){ public void run() {
  	  Toast.makeText(getApplicationContext(),"Connection Error, Please try again", Toast.LENGTH_LONG).show();
    	}});
  	  Log.e("log_tag","Error in http connection!!" + e.toString());     
    }
    
    // put reset thing here
    commentEditText.setText("");
}         
  



public static String executeHttpPost(String url,
		ArrayList<NameValuePair> postParameters) throws Exception {
			BufferedReader in = null;
			try {
				HttpClient client = getHttpClient();
				HttpPost request = new HttpPost(url);
				UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
				postParameters);
				request.setEntity(formEntity);
				HttpResponse response = client.execute(request);
				in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String line = "";
				String NL = System.getProperty("line.separator");
				
				while ((line = in.readLine()) != null) {
					sb.append(line + NL);
				}
				in.close();
				String result = sb.toString();
				return result;
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						Log.e("log_tag", "Error converting result " + e.toString());
						e.printStackTrace();
					}
				}
			}
		}


private static HttpClient getHttpClient() {
	if (mHttpClient == null) {
		mHttpClient = new DefaultHttpClient();
		final HttpParams params = mHttpClient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
		ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
	}
	return mHttpClient;
}

/*
public void openGallery(){
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent,"Select file to upload "), 1);
}*/

public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (resultCode == RESULT_OK) {
    	
    	Bundle extras = data.getExtras();
		bmp=(Bitmap)extras.get("data");
		iv.setImageBitmap(bmp);
		captured_image_uri = data.getData();
		System.out.println(captured_image_uri);
        //Uri selectedImageUri = data.getData();
  //      if (requestCode == SELECT_FILE1)
  //      {
	
            selectedPath1 = getPath(captured_image_uri);
            System.out.println("selectedPath1 : " + selectedPath1);
   //     }
      if(!(selectedPath1.trim().equalsIgnoreCase("NONE"))) {
          progressDialog = ProgressDialog.show(Photo_Cap.this, "", "Selecting Photo.....", false);
           Thread thread=new Thread(new Runnable(){
                  public void run(){
                      doFileUpload();
                      runOnUiThread(new Runnable(){
                          public void run() {
                              if(progressDialog.isShowing())
                                progressDialog.dismiss();
                          }
                      });
                  }
          });
          thread.start();
      }else
      {
                  Toast.makeText(getApplicationContext(),"Please select a file to upload.", Toast.LENGTH_SHORT).show();
      } 
      
    }
}

public String getPath(Uri uri) {
    String[] projection = { MediaStore.Images.Media.DATA };
    Cursor cursor = managedQuery(uri, projection, null, null, null);
    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
    cursor.moveToFirst();
    return cursor.getString(column_index);
}

private void doFileUpload(){

    File file1 = new File(selectedPath1);
    
    Bitmap bigImage = BitmapFactory.decodeFile(selectedPath1);
    
    File file2;
    
	try {
		file2 = downSizeImage(bigImage);
	} catch (IOException e1) {
		e1.printStackTrace();
		Log.e("DownSizingImage", "IO error on image resizing");
		return;
	}
    
    String urlString = "http://164.138.29.169/upload_photo_script.php";
    try
    {
         HttpClient client = new DefaultHttpClient();
         HttpPost post = new HttpPost(urlString);
         FileBody bin1 = new FileBody(file2);
         MultipartEntity reqEntity = new MultipartEntity();
         reqEntity.addPart("uploadedfile1", bin1);
         reqEntity.addPart("userID", new StringBody(UserID));
         reqEntity.addPart("crawlID", new StringBody(CrawlID));
         reqEntity.addPart("type", new StringBody(Integer.toString(2)));
         reqEntity.addPart("gps1", new StringBody(gps1));
         reqEntity.addPart("gps2", new StringBody(gps2));
         post.setEntity(reqEntity);
         HttpResponse response = client.execute(post);
         resEntity = response.getEntity();
         final String response_str = EntityUtils.toString(resEntity);
         if (resEntity != null) {
             Log.i("RESPONSE",response_str);
             runOnUiThread(new Runnable(){
                    public void run() {
                         try {
                             Toast.makeText(getApplicationContext(),"Response: "+response_str, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       }
                });
         }
    }
    catch (Exception ex){
         Log.e("Debug", "error: " + ex.getMessage(), ex);
    }
    finally {
    	file2.delete();
    }
  }

private File downSizeImage(Bitmap image) throws IOException {
	final int maxSide = 1024;	//max side size in pixels
	
	int newWidth, newHeight;	//the new size of our image computed below
	
	int oldHeight = image.getHeight();
	int oldWidth = image.getWidth();
	
	if (oldWidth < maxSide && oldHeight < maxSide) {
		newWidth = oldWidth;
		newHeight = oldHeight;
	}
	else {
		if( oldHeight>oldWidth ) {
			newHeight = maxSide;
			newWidth = (int) (((float) maxSide/oldHeight)*oldWidth);
		}
		else {
			newWidth = maxSide;
			newHeight = (int) (((float) maxSide/oldWidth)*oldHeight);		
		}
	}
	
	Log.d("imageresize", "newHeight" + newHeight);
	Log.d("imageresize", "newWidth" + newWidth);
	
	// setting filtering to true as I assume it's something like lanczos 
	// and will improve image quality after scaling
	Bitmap outputImage = Bitmap.createScaledBitmap(image, newWidth, newHeight, true);
	
	File root = android.os.Environment.getExternalStorageDirectory();       	//get our sd card directory        

		//using the directory I used for downloading images temporarily
    File savedir = new File ( root.getAbsolutePath() + "/Pictures/PubCrawl Images"); //create our directory if it doesn't exist
    if(savedir.exists()==false) {
         savedir.mkdirs();
    }
	
	
	File returnFile = new File(savedir, "tempUpload.jpg");
	
	ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
	outputImage.compress(Bitmap.CompressFormat.JPEG, 80, byteStream);
	
	byte[] buffer = byteStream.toByteArray();
	
	FileOutputStream fileStream = new FileOutputStream(returnFile);
	
	fileStream.write(buffer);
	
	return returnFile;
}



}




