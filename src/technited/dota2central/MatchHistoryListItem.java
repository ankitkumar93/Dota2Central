package technited.dota2central;

import android.graphics.Bitmap;


public class MatchHistoryListItem {
	
	private String heroName;
	private String dateTime;
	private Bitmap heroPic;
	private String matchID;
	
	MatchHistoryListItem(String heroName, String dateTime, Bitmap heroPic,String matchID) {
		this.heroName = heroName;
		this.dateTime = dateTime;
		this.heroPic = heroPic;
		this.matchID = matchID;
	}
	
	public String getHeroName(){
		return this.heroName;
	}
	
	public String getDateTime(){
		return this.dateTime;
	}
	
	public Bitmap getHeroPic(){
		return this.heroPic;
	}
	
	public String getMatchID()
	{
		return this.matchID;
	}

}
