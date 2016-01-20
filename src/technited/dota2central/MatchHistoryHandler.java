package technited.dota2central;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class MatchHistoryHandler {
	
	private Context context;
	private LocalDB localDB;
	private JSONArray matchHistoryArray;
	private JSONObject data;
	private String TAG = "MatchHistoryHandler";
	
	MatchHistoryHandler(Context context)
	{
		this.context = context;
		localDB = new LocalDB(this.context);
	}
	
	public void assignJSON(JSONObject data)
	{
		this.data = data;
		
		try {
			this.matchHistoryArray = this.data.getJSONArray("match_history_list");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.d(TAG, "Error in Assigning Match History JSON Array ",e);
		}
		
	}
	
	public boolean putMatchHistory(String historySteamID)
	{
		return localDB.insertMatchHistory(matchHistoryArray, historySteamID);
	}
	
	public Cursor getMatchHistory(String historySteamID, String numberOfRows)
	{
		return localDB.getMatchHistory(historySteamID, numberOfRows);
	}
}
