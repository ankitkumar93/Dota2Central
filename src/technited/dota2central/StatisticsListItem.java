package technited.dota2central;

import android.graphics.Bitmap;

public class StatisticsListItem {
	
	
	private Bitmap maxHeroPic,minHeroPic;
	private String maxPlayerName, maxValue, minPlayerName, minValue, head;
	
	public StatisticsListItem(String maxPlayerName, String maxValue, String head, Bitmap maxHeroPic, String minPlayerName, String minValue, Bitmap minHeroPic){
		
		this.head = head;
		this.maxPlayerName = maxPlayerName;
		this.maxValue = maxValue;
		this.maxHeroPic = maxHeroPic;
		this.minPlayerName = minPlayerName;
		this.minValue = minValue;
		this.minHeroPic = minHeroPic;
		
	}
	
	public String getPlayerNameMax(){
		return this.maxPlayerName;
	}
	
	public String getValueTextMax(){
		return this.maxValue;
	}
	
	public Bitmap getHeroPicMax(){
		return this.maxHeroPic;
	}
	
	public String getPlayerNameMin(){
		return this.minPlayerName;
	}
	
	public String getValueTextMin(){
		return this.minValue;
	}
	
	public Bitmap getHeroPicMin(){
		return this.minHeroPic;
	}
	
	public String getHead(){
		return this.head;
	}

}
