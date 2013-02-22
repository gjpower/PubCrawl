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
	final static String USERNAME = "userName";
	final static String USERID = "userId";
	final static String CRAWLS = "crawls";
	final static String CURRCRAWL = "currcrawl";
	
	/*
	 *Using Data Output Streams lets you store specific data types
	 *rather than having to convert everything to and from strings
	 */	
	public static  void Store_User_Name(String name, Context context){
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
    	String crawlData;
    	for (i=0;i<numCrawls;i++) {
    		int j;
    		for (j=0;j<3;j++) {
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
    	String crawlData = null;
    	try {
			fis = context.openFileInput(CRAWLS);
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
        int numCrawls = (parts.length)/3;
        String crawlArray[][] = new String[numCrawls][4];
        int i;
        int partNum;
        for (i=0;i<numCrawls;i++) {
        	int j;
        	for (j=0;j<3;j++) {
        		partNum = 3*i + j;
        		crawlArray[i][j] = parts[partNum];
        	}
        }
        return crawlArray;
    }

    public static void Indicate_Current_Crawl (int id, Context context) {
    	try {
			fos = context.openFileOutput(CURRCRAWL, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(id);
	    	dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static int Num_Crawls(Context context) {
    	String crawlData = null;
    	try {
			fis = context.openFileInput(CRAWLS);
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
        return (parts.length)/3;
    }
    
    public static String[][] Get_Current_Crawl (Context context) {
    	String currCrawlData[][] = new String[1][3];
    	/*currCrawlData[0][0] = "2";
    	currCrawlData[0][1] = "String";
    	currCrawlData[0][2] = "String";*/
    	int id=-1;
        try {
			fis = context.openFileInput(CURRCRAWL);
			DataInputStream dis = new DataInputStream(fis);
			id= dis.readInt();
			dis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        int numCrawls = Num_Crawls(context);
        String allCrawlsData[][] = new String[numCrawls][3];
        allCrawlsData = Get_Crawl_Data(context);
        int i;
        for (i=0;i<numCrawls;i++) {
            String testString = allCrawlsData[i][0];
        	if (testString == String.valueOf(id)) {
        		break;
        	}
        }
        int j;
        for (j=0;j<3;j++) {
        	currCrawlData[0][j] = allCrawlsData[i][j];
        }
    	return currCrawlData;
    }
}
