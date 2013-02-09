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
	static FileInputStream fis = null;
	
	/*
	 *Using Data Output Streams lets you store specific data types
	 *rather than having to convert everything to and from strings
	 */	
	public static  void Store_User_Name(String name, Context context){
    	String USERNAME = "userName";
    	try {
			fos = context.openFileOutput(USERNAME, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeChars(name);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static String Get_User_Name(Context context){
    	String name = null;  
    	String USERNAME = "userName";
        try {
			fis = context.openFileInput(USERNAME);
			DataInputStream dis = new DataInputStream(fis);
			byte[] dataArray = new byte[dis.available()];
			//when the file has been read then read() returns -1
			while (dis.read(dataArray) != -1) {
				name = new String(dataArray);
			}
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        return name;
    }
    
    public static void Store_User_Id(int id, Context context) {
    	String USERID = "userId";
    	try {
			fos = context.openFileOutput(USERID, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(id);
	    	dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static int Get_User_Id(Context context) {
    	int id=-1;
    	String USERID = "userId";
        try {
			fis = context.openFileInput(USERID);
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
    
    public static void Store_Crawl_Data (String[][] crawlArray, Context context) {
    	int numCrawls = crawlArray.length;
    	int i;
    	StringBuffer strBuffer = new StringBuffer();
    	String CRAWLS = "crawls";
    	String crawlData;
    	for (i=0;i<numCrawls;i++) {
    		int j;
    		for (j=0;j<4;j++) {
        		strBuffer.append(crawlArray[i][j]); 
        		strBuffer.append("*");    	
    		}
    	}
    	crawlData = strBuffer.toString();
    	try {
			fos = context.openFileOutput(CRAWLS, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeChars(crawlData);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static String[][] Get_Crawl_Data (Context context) {
    	String USERNAME = "crawls";
    	String crawlData = null;
    	int numCrawls;
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
        String[] parts = crawlData.split("\\*");
        numCrawls = (parts.length)/4;
        String crawlArray[][] = new String[numCrawls][4];
        int i;
        int partNum;
        for (i=0;i<numCrawls;i++) {
        	int j;
        	for (j=0;j<4;j++) {
        		partNum = 4*i + j;
        		crawlArray[i][j] = parts[partNum];
        	}
        }
        return crawlArray;
        /*String[][] test = new String[3][4];
		test[0][0] = "ZeroZero";
    	test[0][1] = "ZeroOne";
    	test[0][2] = "ZeroTwo";
    	test[0][3] = "ZeroThree";
    	test[1][0] = "OneZero";
    	test[1][1] = "OneOne";
    	test[1][2] = "OneTwo";
    	test[1][3] = "OneThree";
    	test[2][0] = "TwoZero";
    	test[2][1] = "TwoOne";
    	test[2][2] = "TwoTwo";
    	test[2][3] = "TwoThree";
    	int i;
    	StringBuffer strBuffer = new StringBuffer();
    	for (i=0;i<3;i++) {
    		int j;
    		for (j=0;j<4;j++) {
        		strBuffer.append(test[i][j]); 
        		strBuffer.append("*");    	
    		}
    	}
    	String crawlData = strBuffer.toString();
        
        String[] tokens = crawlData.split("\\*");
        
        return tokens.length;*/
    }
}
