package ie.tcd.pubcrawl;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PermStorage {
	//FileOutputStream Variables
	//no just no

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
	private static final String CRAWLS_PUBTABLE = "crawlsPubTable";
	private static final String PUB_TABLE = "pubTable";
	private static final String RULES_TABLE = "rulesTable";
	private static final String USERID_TABLE = "userIdTable";
	private static final String COMMENTS_TABLE = "commentsTable";
	private static final int DATABASE_VERSION = 1;

	//below is static so only one connection to database ever exists
	// this avoids a lot of problems
	private static DbHelper ourHelper;
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
					KEY_PREVCRAWLS + " TEXT);"					
					);
			db.execSQL("CREATE TABLE " + ADMIN_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_ADMIN + " INTEGER);"
					);
			db.execSQL("CREATE TABLE " + CRAWLS_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT," +
					"date TEXT," +
					"code TEXT " +
					");"					
					);
			db.execSQL("CREATE TABLE " + CRAWLS_PUBTABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT, " +
					"time TEXT, " +
					"serverid TEXT, " +
					"crawlcode TEXT " +
					");"					
					);

			db.execSQL("CREATE TABLE " + PUB_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT," +
					"address TEXT," +
					"description TEXT, " +
					"likes INTEGER, " +
					"dislikes INTEGER, " +
					"latitude TEXT, " +
					"longitude TEXT, " +
					"serverid TEXT " +
					");"					
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

			db.execSQL("CREATE TABLE " + USERID_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"usercode TEXT ," +
					"username TEXT " +
					");"
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + PREVCRAWLS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + ADMIN_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CRAWLS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + CRAWLS_PUBTABLE);
			db.execSQL("DROP TABLE IF EXISTS " + PUB_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + RULES_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + COMMENTS_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + USERID_TABLE);
			onCreate(db);
		}	
	}

	public PermStorage(Context c) {
		ourContext = c;		
	}

	public PermStorage open() {
		if (ourHelper == null) {
			ourHelper = new DbHelper(ourContext);
		}
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}

	public void Store_User_Name(String name){

		String userName = Get_User_Name();		//get current name
		String userId = Get_User_Id();			//get current id


		ContentValues cv = new ContentValues();		
		cv.put("username", name);	//add name to insert

		//if database is empty
		if (userName==null && userId==null) {
			ourDatabase.insert(USERID_TABLE, null, cv);
		}
		else {	//update current values
			ourDatabase.update(USERID_TABLE, cv, null, null);
		}

	}

	public String Get_User_Name(){

		String[] columns = { "username" };
		Cursor result = ourDatabase.query(USERID_TABLE, columns, null, null, null, null, null);
		if (result.getCount()>0) {
			result.moveToFirst();
			return result.getString(result.getColumnIndex("username"));
		}
		else {
			return null;
		}

	}

	public void Store_User_Id(String id) {

		String userName = Get_User_Name();		//get current name
		String userId = Get_User_Id();			//get current id

		ContentValues cv = new ContentValues();		
		cv.put("usercode", id);	//add id to insert

		//if database is empty
		if (userName==null && userId==null) {
			ourDatabase.insert(USERID_TABLE, null, cv);
		}
		else {	//update existing value with new value
			ourDatabase.update(USERID_TABLE, cv, null, null);
		}
	}

	public String Get_User_Id() {
		String[] columns = { "usercode" };
		Cursor result = ourDatabase.query(USERID_TABLE, columns, null, null, null, null, null);

		if (result.getCount()>0) {
			result.moveToFirst();
			return result.getString(result.getColumnIndex("usercode"));
		}
		else {
			return null;
		}
	}

	public void Indicate_Current_Crawl (Context context, String id) {
		SharedPreferences currentCrawl = context.getSharedPreferences("crawlIndicator",0);
		SharedPreferences.Editor editor =currentCrawl.edit();

		editor.putString("currentCrawlID", id);
		editor.commit();
	}

	public String Get_Current_Crawl (Context context) {
		SharedPreferences currentCrawl = context.getSharedPreferences("crawlIndicator",0);
		return currentCrawl.getString("currentCrawlID", "ERROR");
	}

	public void Store_Crawl_Data(String name, String date, String crawlcode) {
		// just going to delete any data about this crawl if it exists and update with new data
		//delete any rows where code is our provided code
		ourDatabase.delete(CRAWLS_TABLE, "code = ?", new String[] { crawlcode });


		ContentValues cv = new ContentValues();

		cv.put("name", name);
		cv.put("date", date);
		cv.put("code", crawlcode);

		ourDatabase.insert(CRAWLS_TABLE, null, cv);

	}

	public String[] Get_Crawl_Data(String crawlcode) {
		String[] result = new String [2];
		String [] columns = new String[] { "name", "date" };
		Cursor c = ourDatabase.query(CRAWLS_TABLE, columns, "code = ?", new String[] { crawlcode }, null, null, null);

		if (c.getCount()>0) {
			c.moveToFirst();
			result[0]= c.getString(c.getColumnIndex("name"));
			result[1]= c.getString(c.getColumnIndex("date"));

			return result;
		}
		else {
			return null;
		}
	}

	public Cursor Get_Crawl_Data_Cursor(String crawlcode) {
		String [] columns = new String[] { "name", "date" };
		Cursor result = ourDatabase.query(CRAWLS_TABLE, columns, "code = ?", new String[] { crawlcode }, null, null, null);

		return result;
	}

	//used to store name, datetime, serverid and crawl key
	public void Store_Crawl_Pubs(String[][] pubData, String crawlkey) {

		//delete any thing listed with that crawl code
		ourDatabase.delete(CRAWLS_PUBTABLE, "code = ?", new String[] { crawlkey } );
		int size = pubData.length;

		ContentValues cv = new ContentValues();

		for (int i=0; i<size; i++) {
			cv.put("name", pubData[i][0]);
			cv.put("time", pubData[i][1]);
			cv.put("serverid", pubData[i][2]);
			cv.put("crawlcode", crawlkey);

			ourDatabase.insert(CRAWLS_PUBTABLE, null, cv);
		}

	}

	//returns info on all pubs listed with provided crawlkey
	public String[][] Get_Crawl_Pubs(String crawlkey) {
		Cursor c = Get_Crawl_Pubs_Cursor(crawlkey);

		int size = c.getCount();

		if (size>0) {
			String[][] result = new String[size][3];

			for (int i=0; i<size; i++) {
				c.moveToPosition(i);
				result[i][0] = c.getString(c.getColumnIndex("name"));
				result[i][1] = c.getString(c.getColumnIndex("time"));
				result[i][2] = c.getString(c.getColumnIndex("serverid"));
			}

			return result;
		}

		else {
			return null;
		}
	}

	//provides a cursor result which can be useful for listviews
	public Cursor Get_Crawl_Pubs_Cursor(String crawlkey) {
		String [] columns = new String[] { "name", "time", "serverid" };
		Cursor result = ourDatabase.query(COMMENTS_TABLE, columns, "code = ?", new String[] { crawlkey }, null, null, null);

		return result;
	}

	public void Store_Pub(String name, String address, String description, int likes, int dislikes, String latitude, String longitude, String serverid) {
		String[] pubdata = Get_Pub(serverid);

		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("address", address);
		cv.put("description", description);
		cv.put("likes", likes);
		cv.put("dislikes", dislikes);
		cv.put("latitude", latitude);
		cv.put("longitude", longitude);
		cv.put("serverid", serverid);

		//if pub with that id already exists update it
		if (pubdata!=null) {
			ourDatabase.update(PUB_TABLE, cv, "serverid = ?", new String[] {serverid});
		}
		else {	//otherwise create new entry
			ourDatabase.insert(PUB_TABLE, null, cv);
		}

	}

	public String[] Get_Pub(String serverid) {
		Cursor c = Get_Pub_Cursor(serverid);

		int size = c.getCount();

		if(size>0) {
			String[] result = new String[8];
			c.moveToFirst();

			result[0] = c.getString(c.getColumnIndex("name"));
			result[1] = c.getString(c.getColumnIndex("address"));
			result[2] = c.getString(c.getColumnIndex("description"));
			result[3] = c.getString(c.getColumnIndex("likes"));
			result[4] = c.getString(c.getColumnIndex("dislikes"));
			result[5] = c.getString(c.getColumnIndex("latitude"));
			result[6] = c.getString(c.getColumnIndex("longitude"));
			result[7] = c.getString(c.getColumnIndex("serverid"));

			return result;
		}
		else {
			return null;
		}

	}

	public Cursor Get_Pub_Cursor(String serverid) {
		String [] columns = new String[] { "name", "address", "description", "likes", "dislikes", "latitude", "longitude" };
		Cursor result = ourDatabase.query(PUB_TABLE, columns, "serverid = ?", new String[] {serverid}, null, null, null);

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

	//this method should never be used
	public void close() {
		//ourHelper.close();
	}
}
