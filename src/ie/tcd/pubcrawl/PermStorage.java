package ie.tcd.pubcrawl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PermStorage {
	//FileOutputStream Variables
	static FileOutputStream fos;
	static FileInputStream fis = null;
	final static String USERNAME = "userName";
	final static String USERID = "userId";
	final static String CRAWLS = "crawls";
	final static String CURRCRAWL = "currcrawl";
	
	//Database Variables
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PREVCRAWLS = "prevCrawls";
	public static final String KEY_ADMIN = "admin";
	
	private static final String DATABASE_NAME = "PermanentStorage";
	private static final String PREVCRAWLS_TABLE = "prevCrawlsTable";
	private static final String ADMIN_TABLE = "adminTable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper {
		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + PREVCRAWLS_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_PREVCRAWLS + " TEXT NOT NULL);"					
			);
			db.execSQL("CREATE TABLE " + ADMIN_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_ADMIN + " INTEGER);"
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + PREVCRAWLS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ADMIN_TABLE);
			onCreate(db);
		}	
	}
	
	public PermStorage(Context c) {
		ourContext = c;		
	}
	
	public PermStorage open() {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	//FileOutputStream/DataOutputStream Storage
	public void Store_User_Name(String name){
    	try {
			fos = ourContext.openFileOutput(USERNAME, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeChars(name);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public String Get_User_Name(){
    	String name = null;  
        try {
			fis = ourContext.openFileInput(USERNAME);
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
    
    public void Store_User_Id(int id) {
    	try {
			fos = ourContext.openFileOutput(USERID, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(id);
	    	dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public int Get_User_Id() {
    	int id=-1;
        try {
			fis = ourContext.openFileInput(USERID);
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
    
    public void Store_Crawl_Data(String[][] crawlArray) {
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
			fos = ourContext.openFileOutput(CRAWLS, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeChars(crawlData);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public String[][] Get_Crawl_Data() {
    	String crawlData = null;
    	try {
			fis = ourContext.openFileInput(CRAWLS);
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

    public void Indicate_Current_Crawl (int id) {
    	try {
			fos = ourContext.openFileOutput(CURRCRAWL, Context.MODE_PRIVATE);
			DataOutputStream dos = new DataOutputStream(fos);
			dos.writeInt(id);
	    	dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public int Get_Current_Crawl () {
    	int id=-1;
        try {
			fis = ourContext.openFileInput(CURRCRAWL);
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
    
    //Database Storage
    public void Store_Prev_Crawls(List<String> l) {
		ourDatabase.delete(PREVCRAWLS_TABLE, null, null);
		for (int i=0;i<l.size();i++) {
			ContentValues cv = new ContentValues();
			cv.put(KEY_PREVCRAWLS, l.get(i));
			ourDatabase.insert(PREVCRAWLS_TABLE, null, cv);
		}
	}

	public List<String> Get_Prev_Crawls() {
		String [] columns = new String[] { KEY_ROWID, KEY_PREVCRAWLS};
		Cursor c = ourDatabase.query(PREVCRAWLS_TABLE, columns, null, null, null, null, null);
		List<String> result = new ArrayList<String>();
		int index = c.getColumnIndex(KEY_PREVCRAWLS);		
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			result.add(c.getString(index));
		}
		return result;
	}
    
    public void Store_Admin_Info(List<Boolean> l) {
		ourDatabase.delete(ADMIN_TABLE, null, null);
		int bool;
		for (int i=0;i<l.size();i++) {
			if (l.get(i))
				bool = 1;
			else bool = 0;
			ContentValues cv = new ContentValues();
			cv.put(KEY_ADMIN, bool);
			ourDatabase.insert(ADMIN_TABLE, null, cv);
		}
	}
	
	public List<Boolean> Get_Admin_Info() {
		String [] columns = new String[] { KEY_ROWID, KEY_ADMIN};
		Cursor c = ourDatabase.query(ADMIN_TABLE, columns, null, null, null, null, null);
		List<Boolean> result = new ArrayList<Boolean>();
		int index = c.getColumnIndex(KEY_ADMIN);		
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			if (c.getInt(index) == 1)
				result.add(true);
			else result.add(false);
		}
		return result;
	}
	
	public void close() {
		ourHelper.close();
	}
}
