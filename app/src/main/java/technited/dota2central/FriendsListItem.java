package technited.dota2central;

import android.graphics.Bitmap;

public class FriendsListItem {
	
	private String friendName;
	private String[] friendStatus;
	private String friendID;
	private Bitmap friendPic;
	
	FriendsListItem(String friendName, String[] friendStatus, String friendID, Bitmap friendPic){
		
		this.friendName = friendName;
		this.friendStatus = friendStatus;
		this.friendID = friendID;
		this.friendPic = friendPic;
		
	}
	
	public String getFriendName(){
		return this.friendName;
	}
	
	public String getFriendStatusText(){
		return this.friendStatus[0];
	}
	
	public String getFriendStatusColor(){
		return this.friendStatus[1];
	}
	
	public String getFriendID(){
		return this.friendID;
	}
	
	public Bitmap getFriendPic(){
		return this.friendPic;
	}

}
