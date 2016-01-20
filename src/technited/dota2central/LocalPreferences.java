package technited.dota2central;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalPreferences {
	
	private SharedPreferences prefs;
	private  SharedPreferences.Editor editPrefs;
	private Context context;
	private String steamID;
	private boolean loginStatus;
	private boolean notificationEnabled;
	
	LocalPreferences(Context context)
	{
		this.context = context;
		this.prefs = this.context.getSharedPreferences("D2CLocale", 0);
	}
	
	public String getSteamID()
	{
		return steamID;
	}
	
	public boolean getLoginStatus()
	{
		return loginStatus;
	}
	
	public boolean getNotificationStatus(){
		return notificationEnabled;
	}
	
	public void putLoginStatus(boolean loginStatus)
	{
		this.loginStatus = loginStatus;
	}
	
	public void putSteamID(String steamID)
	{
		this.steamID = steamID;
	}
	
	public void putNotificationStatus(boolean notificationStatus)
	{
		this.notificationEnabled = notificationStatus;
	}
	
	public void readFromPreferences()
	{
		this.steamID = prefs.getString("steamID", null);
		this.loginStatus = prefs.getBoolean("loginStatus", true);
		this.notificationEnabled  = prefs.getBoolean("notificationStatus", true);
	}
	
	public void writeToPreferences()
	{
		this.editPrefs = prefs.edit();
		this.editPrefs.putString("steamID", this.steamID);
		this.editPrefs.putBoolean("loginStatus", this.loginStatus);
		this.editPrefs.putBoolean("notificationStatus", this.notificationEnabled);
		this.editPrefs.commit();
	}
	
	public void clearPreferences()
	{
		this.steamID = null;
		this.loginStatus = true;
		this.notificationEnabled = true;
		writeToPreferences();
	}

}
