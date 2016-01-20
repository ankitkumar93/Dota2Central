package technited.dota2central;

import android.graphics.Bitmap;

public class NotificationListItem {
	
	private String name;
	private String date;
	private Bitmap pic;
	
	public NotificationListItem(String name, String date, Bitmap pic){
		
		this.name = name;
		this.pic = pic;
		this.date  = date;
		
	}
	
	public String getInfo(){
		return this.name + " was playing Dota 2";
	}
	
	public String getDate(){
		return this.date;
	}
	
	public Bitmap getPic(){
		return this.pic;
	}

}
