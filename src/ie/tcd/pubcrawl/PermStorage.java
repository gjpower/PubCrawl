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
	final static String CURRCRAWL = "currcrawl";
	
	//Database Variables
	public static final String KEY_ROWID = "_id";
	public static final String KEY_PREVCRAWLS = "prevCrawls";
	public static final String KEY_ADMIN = "admin";
	public static final String KEY_CRAWLS = "crawls";
	public static final String KEY_RULES = "rules";
	
	private static final String DATABASE_NAME = "PermanentStorage";
	private static final String PREVCRAWLS_TABLE = "prevCrawlsTable";
	private static final String ADMIN_TABLE = "adminTable";
	private static final String CRAWLS_TABLE = "crawlsTable";
	private static final String RULES_TABLE = "rulesTable";
	private static final String COMMENTS_TABLE = "commentsTable";
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
			db.execSQL("CREATE TABLE " + CRAWLS_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_CRAWLS + " TEXT NOT NULL);"					
			);
			db.execSQL("CREATE TABLE " + RULES_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_RULES + " TEXT NOT NULL);"					
			);
			db.execSQL("CREATE TABLE " + COMMENTS_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"comment_body TEXT, " +
					"image TEXT, " +
					"crawlkey TEXT, " +
					"username TEXT, " +
					"time TEXT " +
					");"					
			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + PREVCRAWLS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ADMIN_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CRAWLS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + RULES_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + COMMENTS_TABLE);
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
	public void Store_Crawl_Data(String[][] crawlArray) {
    	ourDatabase.delete(CRAWLS_TABLE, null, null);
    	int numCrawls = crawlArray.length;
    	int numData = crawlArray[0].length;
		ContentValues cv = new ContentValues();
    	for (int i=0;i<numCrawls;i++) {
    		for (int j=0;j<numData;j++) {
    			cv.put(KEY_CRAWLS, crawlArray[i][j]);
    			ourDatabase.insert(CRAWLS_TABLE, null, cv);
    		}
    		cv.put(KEY_CRAWLS, "EndOfCrawlData");
			ourDatabase.insert(CRAWLS_TABLE, null, cv);
    	}
    }
    
    public String[][] Get_Crawl_Data() {
    	String [] columns = new String[] { KEY_ROWID, KEY_CRAWLS};
		Cursor c = ourDatabase.query(CRAWLS_TABLE, columns, null, null, null, null, null);
		int numCrawls=0;
		int curr=0, numData=0;
		int index = c.getColumnIndex(KEY_CRAWLS);
		//Analyse column to find the number of crawls and the amount of data for each crawl
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
			if (c.getString(index).equals("EndOfCrawlData")) {
				numCrawls++; 
				if (numData == 0) {
					numData = curr;
				}
			} else {
				curr++;
			}
		}
		String[][] result = new String[numCrawls][numData];	
		String temp = "";
		c.moveToFirst();
		for (int i=0;i<numCrawls;i++) {
        	int j;
        	for (j=0;j<numData;j++) {
        		temp = c.getString(index);
        		//Skip over "EndOfCrawlData"
        		if (temp.equals("EndOfCrawlData")) {
            		c.moveToNext();
        		}
    			result[i][j] = temp;
        		c.moveToNext();
        	}
        }
		return result;
    }
	
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
	
	public void Store_Kings_Rules(String[] rulesArray) {
		ourDatabase.delete(RULES_TABLE, null, null);
		ContentValues cv = new ContentValues();
    	for (int i=0;i<15;i++) {
			cv.put(KEY_RULES, rulesArray[i]);
			ourDatabase.insert(RULES_TABLE, null, cv);
		}
	}
	
	public String[] Get_Kings_Rules() {
		String [] columns = new String[] { KEY_ROWID, KEY_RULES};
		Cursor c = ourDatabase.query(RULES_TABLE, columns, null, null, null, null, null);
		String[] result = new String[15];
		int index = c.getColumnIndex(KEY_RULES);
		int i = 0;
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()) {
        	result[i] = c.getString(index);
        	i++;
        }
		return result;
	}
	
	public Cursor Get_Comment_Data(String crawlkey) {
		String [] columns = new String[] { KEY_ROWID,"username", "comment_body", "time", "image" };
		Cursor result = ourDatabase.query(COMMENTS_TABLE, columns, "crawlkey = ?", new String[] { crawlkey }, null, null, "time DESC");
		//ourDatabase.query(COMMENTS_TABLE, columns, "crawlkey = ?", new String[] { crawlkey }, null, null, "time DESC");
		return result;
	}
	
	public void Store_Comment_Data(String[][] commentData, String crawlkey) {
		int size = commentData.length;	// will just destroy all comments with a certain key and add new ones downloaded
										// not the cleverest way to do it but there's not a huge amount of time left for work on this
										//	Graeme Power
		ourDatabase.delete(COMMENTS_TABLE, "crawlkey = ?", new String[] { crawlkey });
		
		ContentValues cv = new ContentValues();
		
		for (int i=0; i<size; i++) {	// fill up our contentvalue with row of data and add to the database
			cv.put("username", commentData[i][0]);
			cv.put("comment_body", commentData[i][1]);
			cv.put("image", commentData[i][2]);
			cv.put("time", commentData[i][3]);
			cv.put("crawlkey", crawlkey);
			
			ourDatabase.insert(COMMENTS_TABLE, null, cv);
		}
		
	}

	public void close() {
		ourHelper.close();
	}
}
