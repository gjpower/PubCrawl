package ie.tcd.pubcrawl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;

public class PermStorage {
	public static  void Store_User_Name(String name, Context c){
    	FileOutputStream fos;
    	String USERNAME = "userName";
    	try {
			fos = c.openFileOutput(USERNAME, Context.MODE_PRIVATE);
			fos.write(name.getBytes());
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static String Get_User_Name(Context c){
    	String name = null;  
    	FileInputStream fis = null;
    	String USERNAME = "userName";
        try {
			fis = c.openFileInput(USERNAME);
			byte[] dataArray = new byte[fis.available()];
			//when the file has been read then read() returns -1
			while (fis.read(dataArray) != -1) {
				name = new String(dataArray);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        return name;
    }
}
