package technited.dota2central;

import android.content.Context;

public class URLConstructor {
	
	private Context context;
	private static final String baseAPIURL = "http://technited.com/api/v002/";
	private static final String d2cAPIKey = "450a786bc80a7126cae6e3957d7170f0";
	private static final String steamAPIKey = "481F6335EA0F949620BAE9D5EE7C6D47";
	private static final String baseHeroListURL = "http://api.steampowered.com/IEconDOTA2_570/GetHeroes/v1?";
	private static final String vanityURLApi = "http://api.steampowered.com/ISteamUser/ResolveVanityURL/v0001/?";
	public URLConstructor(Context context) {
		
		this.context = context;
		
	}

	public String getUserInfoURL(String steam_ID)
	{
		return baseAPIURL+"Player/"+steam_ID+"/"+d2cAPIKey;
	}
	
	public String getMatchHistoryURL(String steam_ID)
	{
		return baseAPIURL + "MatchHistory/"+steam_ID+"/"+d2cAPIKey;
	}
	
	public String getMatchDetailsURL(String match_ID)
	{
		return baseAPIURL + "Match/"+match_ID+"/"+d2cAPIKey;
	}
	
	public String getHeroListURL()
	{
		return baseHeroListURL+"key="+steamAPIKey;
	}
	
	public String getFriendListURL(String steam_ID)
	{
		return baseAPIURL + "Friend/"+steam_ID+"/"+d2cAPIKey;
	}
	
	public String getSteamIDURL(String vanityURL)
	{
		return vanityURLApi + "key=" + steamAPIKey + "&vanityurl=" + vanityURL;
	}

}
