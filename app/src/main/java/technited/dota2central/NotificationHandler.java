package technited.dota2central;

import org.json.JSONArray;

import android.content.Context;
import android.database.Cursor;

public class NotificationHandler {
	
	private Context context;
	private JSONArray data;
	private LocalDB localDB;
	
	public NotificationHandler(Context context){
		this.context = context;
		localDB = new LocalDB(this.context);
	}
	
	public void putJSONContent(JSONArray data)
	{
		this.data = data;
	}
	
	public boolean putNotifications()
	{
		return localDB.insertNotifications(data,Long.toString(System.currentTimeMillis()/1000));
	}
	
	public Cursor getNotifications()
	{
		return localDB.getNotifications();
	}

}
