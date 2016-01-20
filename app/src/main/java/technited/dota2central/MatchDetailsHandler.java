package technited.dota2central;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

public class MatchDetailsHandler {
	
	private Context context;
	private LocalDB localDB;
	private JSONObject matchDetailData;
	private JSONObject data;
	private JSONArray playerMatchDetailData;
	private String TAG = "MatchDetailsHandler";
	
	private static final String MATCH = "match";
	private static final String PLAYERS = "players";
	
	public MatchDetailsHandler(Context context) {
		
		this.context = context;
		localDB = new LocalDB(this.context);
		
	}
	
	public void assignJSON(JSONObject data)
	{
		this.data = data;
		try {
			this.matchDetailData = this.data.getJSONObject(MATCH);
			this.playerMatchDetailData = this.data.getJSONArray(PLAYERS);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean putMatchDetails(String match_ID)
	{
		return (localDB.insertMatchDetails(this.matchDetailData)&&localDB.insertPlayerMatchDetails(this.playerMatchDetailData,match_ID));
	}
	
	public Cursor getMatchDetails(String match_ID)
	{
		return localDB.getMatchDetail(match_ID);
	}
	
	public Cursor getPlayerMatchDetail(String match_ID)
	{
		return localDB.getPlayerMatchDetail(match_ID);
	}

}
