package technited.dota2central;

import android.graphics.Bitmap;

public class PlayerMatchDetailItem {
	
	private String heroName;
	private Bitmap heroPic;
	private String playerName;
	private String kills;
	private String deaths;
	private String assists;
	private String lastHits;
	private String denies;
	private String gpm;
	private String xpm;
	private Bitmap[] itemPic;
	private int viewPage;
	
	PlayerMatchDetailItem(String heroName, Bitmap heroPic, String playerName, String kills, String deaths, String assists, String lastHits, String denies, String gpm, String xpm, Bitmap[] itemPic) {
		this.heroName = heroName;
		this.heroPic = heroPic;
		this.playerName = playerName;
		this.kills = kills;
		this.deaths = deaths;
		this.assists = assists;
		this.lastHits = lastHits;
		this.denies = denies;
		this.gpm = gpm;
		this.xpm = xpm;
		this.itemPic = itemPic;
		this.viewPage = 0;
	}
	
	public String getHeroName(){
		return this.heroName;
	}
	
	
	public Bitmap getHeroPic(){
		return this.heroPic;
	}
	
	public String getPlayerName(){
		return this.playerName;
	}
	
	public String getKills(){
		return this.kills;
	}
	
	public String getDeaths(){
		return this.deaths;
	}
	
	public String getAssists(){
		return this.assists;
	}
	
	public String getHits(){
		return this.lastHits+"/"+denies;
	}
	
	public String getGPM(){
		return this.gpm;
	}
	
	public String getXPM(){
		return this.xpm;
	}
	
	public Bitmap getItemPic(int index){
		return itemPic[index];
	}
	
	public void setViewPage(int arg)
	{
		this.viewPage = arg;
	}
	
	public int getViewPage()
	{
		return this.viewPage;
	}

}
