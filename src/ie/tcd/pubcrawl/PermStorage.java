package ie.tcd.pubcrawl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;

public class PermStorage {
	static FileOutputStream fos;
	static FileInputSteam fis = null;
	
	/*
	 *Using Data Output Streams lets you store specific data types
	 *rather than having to convert everything to and from strings
	 */	
	public static  void Store_User_Name(String name, Context c){
    	String USERNAME = "userName";
    	try {
			fos = c.openFileOutput(USERNAME, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeChars(name);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static String Get_User_Name(Context c){
    	String name = null;  
    	String USERNAME = "userName";
        try {
			fis = c.openFileInput(USERNAME);
			DataInputStream dis = new DataInputStream(fis);
			byte[] dataArray = new byte[dis.available()];
			//when the file has been read then read() returns -1
			while (dis.read(dataArray) != -1) {
				name = new String(dataArray);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return name;
    }
    
    public static void Store_User_Id(int id, Context c) {
    	String USERID = "userId";
    	try {
			fos = c.openFileOutput(USERID, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(id);
	    	dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static int Get_User_Id(Context c) {
    	int id=-1;
    	String USERID = "userId";
        try {
			fis = c.openFileInput(USERID);
			DataInputStream dis = new DataInputStream(fis);
			id= dis.readInt();
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return id;
    }
    
    public static void Store_Crawl_Data (String crawlData, Context context) {
    	String CRAWLS = "crawls";
    	try {
			fos = context.openFileOutput(CRAWLS, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeChars(crawlData);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static String Get_Crawl_Data (Context context) {
    	String crawlData = null;
    	String USERNAME = "crawls";
        try {
			fis = context.openFileInput(USERNAME);
			DataInputStream dis = new DataInputStream(fis);
			byte[] dataArray = new byte[dis.available()];
			//when the file has been read then read() returns -1
			while (dis.read(dataArray) != -1) {
				crawlData = new String(dataArray);
			}
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return crawlData;
    }
}
