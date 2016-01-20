package technited.dota2central;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;

public class HeroListHandler {
	
	private static final String TAG = "UserInfoHandler";
	private JSONObject data;
	private JSONArray heroData;
	private Context context;
	private LocalDB localDB;
	
	HeroListHandler(Context context) {
		this.context = context;
		localDB = new LocalDB(this.context);
	}
	
	public void assignJSON(JSONObject data)
	{
		this.data = data;
		try {
			this.heroData = this.data.getJSONArray("heroes");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean putHeroList()
	{
		return localDB.insertHeroList(heroData);
	}
	
	public Cursor getHero(String heroID)
	{
		return localDB.getHeroDetails(heroID);
	}
	
	public Cursor getAllHeroes()
	{
		return localDB.getAllHeroes();
	}
}
