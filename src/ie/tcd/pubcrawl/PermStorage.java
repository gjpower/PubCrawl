package ie.tcd.pubcrawl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/*
 * How to use this class:
 * 
 * Create an instance of the class: 
 * 
 * PermStorage exampleVariable;
 * exampleVariable = new PermStorage(getApplicationContext());
 * 
 * Call the desired functions:
 * 
 * exampleVariable.Some_Function();
 * 
 * More functions will be added later
 */

public class PermStorage 
{
  private static final String APPDATA = "AppData"; // Name of the file -.xml
	
    private SharedPreferences appSharedPrefs;
    
    private Editor prefsEditor;

	//Constructor
    public PermStorage(Context context)
    {
        this.appSharedPrefs = context.getSharedPreferences(APPDATA, Activity.MODE_PRIVATE);
        this.prefsEditor = appSharedPrefs.edit();
    }

    //Return the user's ID number as int. Returns 0 if fails to find ID
    public int Get_User_Id() 
    {
        return appSharedPrefs.getInt("userId", 0);
    }
    
    //Return the user's chosen Name as String. Returns "error" if fails to find Name
    public String Get_User_Name()
    {
    	return appSharedPrefs.getString("userName", "error");
    }

    //Store the user's ID number.
    public void Store_User_Id(int id) 
    {
    	prefsEditor.putInt("userId", id);
        prefsEditor.commit();
    }
    
    //Store the user's chosen Name.
    public void Store_User_Name(String name)
    {
    	prefsEditor.putString("userName", name);
    	prefsEditor.commit();
    }
}
