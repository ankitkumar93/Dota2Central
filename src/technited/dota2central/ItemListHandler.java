package technited.dota2central;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class ItemListHandler {
	
	private Context context;
	private String[] key, value;
	private static final String TAG = "ItemListHandler";
	private LocalDB localDB;
	
	ItemListHandler(Context context)
	{
		this.context = context;
		localDB = new LocalDB(this.context);
	}
	
	public boolean putItemList()
	{
		loadItemList();
		return localDB.insertItemList(key, value);
	}
	
	public Cursor getItemDetail(String itemID)
	{
		return localDB.getItemList(itemID);
	}
	
private void loadItemList(){
	ArrayList<String> keyList = new ArrayList<String>();
	ArrayList<String> valueList = new ArrayList<String>();
    	BufferedReader reader = null;
    	try {
    	    reader = new BufferedReader(
    	        new InputStreamReader(context.getAssets().open("items/item.txt")));

    	    // do reading, usually loop until end of file reading  
    	    String mLine = reader.readLine();
    	    while (mLine != null) {
    	       
    	    	keyList.add(mLine.split(" => ")[0]);
    	    	valueList.add(mLine.split(" => ")[1]);
    	    	
    	       mLine = reader.readLine(); 
    	    }
    	    
    	    key = keyList.toArray(new String[keyList.size()]);
    	    value = valueList.toArray(new String[valueList.size()]);
    	    
    	} catch (Exception e) {
    	    //log the exception
    	} finally {
    	    if (reader != null) {
    	         try {
    	             reader.close();
    	         } catch (Exception e) {
    	             //log the exception
    	         }
    	    }
    	}
    	
    }

}
