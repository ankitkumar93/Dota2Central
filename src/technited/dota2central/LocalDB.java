package technited.dota2central;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDB extends SQLiteOpenHelper{

	
	//Steam ID Converted String
	private static BigDecimal steamIDConverter = new BigDecimal("76561197960265728");
	
	//Error handlers
	long insertErrorVal;
	//Log Tag
	private static final String TAG = "LocalDB";
	//Content Values
	private  ContentValues dbValues ;
	//DB Objects
	private SQLiteDatabase dbWritable;
	private SQLiteDatabase dbReadable;
	//Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "dota2Central";

	// Table Names
	private static final String USER_INFO_TABLE_NAME = "userInfo";
	private static final String MATCH_DETAIL_TABLE_NAME = "matchDetail";
	private static final String MATCH_HISTORY_TABLE_NAME = "matchHistory";
	private static final String HERO_LIST_TABLE_NAME = "heroList";
	private static final String PLAYER_MATCH_DETAIL_TABLE_NAME = "playerMatchDetail";
	private static final String ITEM_LIST_TABLE_NAME = "itemList";
	private static final String MATCH_NOTES_TABLE_NAME = "matchNotes";
	private static final String NOTIFICATIONS_TABLE_NAME = "notifications";
	
	
	// Player Table Columns names
	private static final String STEAM_ID = "steam_id";
	private static final String PERSONA_NAME = "persona_name";
	private static final String LAST_LOGOUT = "last_logoff";
	private static final String COMMUNITY_VISIBILITY_STATE = "community_visibility_state";
	private static final String PROFILE_STATE = "profile_state";
	private static final String AVATAR = "avatar";
	private static final String AVATAR_MEDIUM = "avatar_medium";
	private static final String AVATAR_FULL = "avatar_full";
	private static final String PERSONA_STATE = "persona_state";
	private static final String PROFILE_URL = "profile_url";
	private static final String REAL_NAME = "real_name";
	private static final String PRIMARY_CLAN_ID = "primary_clan_id";
	private static final String TIME_CREATED = "time_created";
	private static final String PERSONA_STATE_FLAGS = "persona_state_flags";
	private static final String GAME_EXTRA_INFO = "game_extra_info";
	private static final String GAME_ID = "game_id";
	private static final String FRIEND = "friend";
	private static final String WON = "won";
	private static final String LOST = "lost";
			
	
	private static final String[] USER_INFO_COLUMS = {STEAM_ID,PERSONA_NAME,LAST_LOGOUT,COMMUNITY_VISIBILITY_STATE,PROFILE_STATE,AVATAR,AVATAR_MEDIUM,AVATAR_FULL,PERSONA_STATE,PROFILE_URL,REAL_NAME,PRIMARY_CLAN_ID,TIME_CREATED,PERSONA_STATE_FLAGS,GAME_EXTRA_INFO,GAME_ID,FRIEND,WON,LOST};
	
	//Match Detail Table Column Names
	private static final String MATCH_ID = "match_id";
	private static final String RADIANT_WIN = "radiant_win";
	private static final String DURATION = "duration";
	private static final String START_TIME = "start_time";
	private static final String MATCH_SEQ_NUM = "match_seq_num";
	private static final String TOWER_STATUS_RADIANT = "tower_status_radiant";
	private static final String TOWER_STATUS_DIRE = "tower_status_dire";
	private static final String BARRACKS_STATUS_RADIANT = "barracks_status_radiant";
	private static final String BARRACKS_STATUS_DIRE = "barracks_status_dire";
	private static final String CLUSTER = "cluster";
	private static final String FIRST_BLOOD_TIME = "first_blood_time";
	private static final String LOBBY_TYPE = "lobby_type";
	private static final String HUMAN_PLAYERS = "human_players";
	private static final String LEAGUE_ID = "leagueid";
	private static final String POSITIVE_VOTES = "positive_votes";
	private static final String NEGATIVE_VOTES = "negative_votes";
	private static final String GAME_MODE = "game_mode";
	
	private static final String[] MATCH_DETAIL_COLUMNS = {MATCH_ID,RADIANT_WIN,DURATION,START_TIME,MATCH_SEQ_NUM,TOWER_STATUS_RADIANT,TOWER_STATUS_DIRE,BARRACKS_STATUS_RADIANT,BARRACKS_STATUS_DIRE,CLUSTER,FIRST_BLOOD_TIME,LOBBY_TYPE,HUMAN_PLAYERS,LEAGUE_ID,POSITIVE_VOTES,NEGATIVE_VOTES,GAME_MODE};
	
	//Match History Column Names(Some are Already Defined in Match Detail Column Names)
	private static final String ACCOUNT_ID = "account_id";
	private static final String HERO_ID = "hero_id";
	
	private static final String[] MATCH_HISTORY_COLUMNS = {STEAM_ID,MATCH_ID,MATCH_SEQ_NUM,START_TIME,LOBBY_TYPE,ACCOUNT_ID,HERO_ID};
	
	//Hero List Column Names
	private static final String HERO_NAME = "name";
	private static final String HERO_LIST_ID = "id";
	private static final String HERO_IMAGE_PATH = "hero_image_path";
	
	private static final String[] HERO_LIST_COLUMNS = {HERO_LIST_ID,HERO_NAME,HERO_IMAGE_PATH};
	
	//Player Match Detail Column Names(Some Already Defined Before)
	private static final String PLAYER_SLOT = "player_slot";
	private static final String ITEM_0 = "item_0";
	private static final String ITEM_1 = "item_1";
	private static final String ITEM_2 = "item_2";
	private static final String ITEM_3 = "item_3";
	private static final String ITEM_4 = "item_4";
	private static final String ITEM_5 = "item_5";
	private static final String KILLS = "kills";
	private static final String DEATHS = "deaths";
	private static final String ASSISTS = "assists";
	private static final String LEAVER_STATUS = "leaver_status";
	private static final String GOLD = "gold";
	private static final String LAST_HITS = "last_hits";
	private static final String DENIES = "denies";
	private static final String GOLD_PER_MIN = "gold_per_min";
	private static final String XP_PER_MIN= "xp_per_min";
	private static final String GOLD_SPENT = "gold_spent";
	private static final String HERO_DAMAGE= "hero_damage";
	private static final String TOWER_DAMAGE = "tower_damage";
	private static final String HERO_HEALING = "hero_healing";
	private static final String LEVEL = "level";
	
	private static final String[] PLAYER_MATCH_DETAIL_COLUMNS = {STEAM_ID,MATCH_ID,HERO_ID,PERSONA_NAME,PLAYER_SLOT,ITEM_0,ITEM_1,ITEM_2,ITEM_3,ITEM_4,ITEM_5,KILLS,DEATHS,ASSISTS,LEAVER_STATUS,GOLD,LAST_HITS,DENIES,GOLD_PER_MIN,XP_PER_MIN,GOLD_SPENT,HERO_DAMAGE,TOWER_DAMAGE,HERO_HEALING,LEVEL};
	
	//Item List Column Names
	private static final String ITEM_ID = "item_id";
	private static final String ITEM_NAME = "item_name";
	private static final String ITEM_PIC = "item_pic";
	
	private static final String[] ITEM_LIST_COLUMNS = {ITEM_ID,ITEM_NAME,ITEM_PIC};
	
	//Match Notes Column(Some already defined)
	private static final String NOTES = "notes";
	
	private static final String[] MATCH_NOTES_COLUMNS = {MATCH_ID,NOTES};
	
	//Notifications Column(Most Already Defined)
	private static final String UPDATE_TIME = "update_time";
	
	private static final String[] NOTIFICATIONS_COLUMNS = {STEAM_ID,UPDATE_TIME,PERSONA_NAME};
			
	//Extra TAGs
	private static final String PLAYERS = "players";
	private static final String PLAYER_DATA = "player_data";
	
	//Table Creation Queries
	
	//Match Detail Table
	private static String CREATE_MATCH_DETAIL_TABLE = "CREATE TABLE "+MATCH_DETAIL_TABLE_NAME + "( " +
			MATCH_ID + " INT(11) PRIMARY KEY , " + 
            RADIANT_WIN + " VARCHAR(255), " +
            DURATION + " INT(11), " +
            START_TIME + " INT(11), " +
            MATCH_SEQ_NUM + " INT(11), " +
            TOWER_STATUS_RADIANT+ " INT(5), " +
            TOWER_STATUS_DIRE + " INT(5), " +
            BARRACKS_STATUS_RADIANT + " INT(3), " +
            BARRACKS_STATUS_DIRE + " INT(3), " +
            CLUSTER + " INT(11), " +
            FIRST_BLOOD_TIME + " INT(11), " +
            LOBBY_TYPE + " INT(11), " +
            HUMAN_PLAYERS + " INT(11), " +
            LEAGUE_ID + " INT(11), " +
            POSITIVE_VOTES + " INT(11), " +
            NEGATIVE_VOTES + " INT(11), " + 
            GAME_MODE + " INT(11) " + " )";
	
	//Match History Table
	private static String CREATE_MATCH_HISTORY_TABLE = "CREATE TABLE " + MATCH_HISTORY_TABLE_NAME + "( " +
			STEAM_ID + " BIGINT(20) , " +
			MATCH_ID + " INT(11) , " +
			MATCH_SEQ_NUM + " INT(11), " +
            START_TIME + " INT(11), " +
            LOBBY_TYPE + " INT(11), " +
            ACCOUNT_ID + " INT(11), " +
            HERO_ID + " INT(3), " +
            "PRIMARY KEY (" + STEAM_ID + ", "+MATCH_ID + ") " +  ")";
	
	//User Info Table
	private static String CREATE_USER_INFO_TABLE = "CREATE TABLE " + USER_INFO_TABLE_NAME + "( " +
            STEAM_ID + " BIGINT(20) PRIMARY KEY , " + 
            PERSONA_NAME + " VARCHAR(255), " +
            LAST_LOGOUT + " INT(11), " +
            COMMUNITY_VISIBILITY_STATE + " INT(1), " +
            PROFILE_STATE + " INT(1), " +
            PROFILE_URL + " TEXT, " +
            AVATAR + " TEXT, " +
            AVATAR_MEDIUM + " TEXT, " +
            AVATAR_FULL + " TEXT, " +
            PERSONA_STATE + " INT(1), " +
            REAL_NAME + " VARCHAR(255), " +
            PRIMARY_CLAN_ID + " BIGINT(20), " +
            TIME_CREATED + " INT(11), " +
            PERSONA_STATE_FLAGS + " INT(11), " +
            GAME_EXTRA_INFO + " TEXT, " +
            GAME_ID + " INT(11), " +
            FRIEND + " BIT, " +
            WON + " INT(5), " +
            LOST + " INT(5) " + " )";
	
	//Hero List Table
	private static String CREATE_HERO_LIST_TABLE = "CREATE TABLE " + HERO_LIST_TABLE_NAME + "( " +
			HERO_LIST_ID + " INT(3) PRIMARY KEY , " +
			HERO_NAME + " VARCHAR(255), " +
			HERO_IMAGE_PATH + " TEXT " + " )";
	
	//Player Match Detail Table
	private static String CREATE_PLAYER_MATCH_DETAIL_TABLE = "CREATE TABLE " + PLAYER_MATCH_DETAIL_TABLE_NAME + "( " +
			STEAM_ID + " BIGINT(20) , " +
			MATCH_ID + " INT(11) , " +
			HERO_ID + " INT(3) , " +
			PERSONA_NAME + " VARCHAR(255) , " +
			PLAYER_SLOT + " INT(1) , " +
			ITEM_0 + " INT(3) , " +
			ITEM_1 + " INT(3) , " +
			ITEM_2 + " INT(3) , " +
			ITEM_3 + " INT(3) , " +
			ITEM_4 + " INT(3) , " +
			ITEM_5 + " INT(3) , " +
			KILLS + " INT(5) , " +
			DEATHS + " INT(5) , " +
			ASSISTS + " INT(5) , " +
			LEAVER_STATUS + " INT(1) , " +
			GOLD + " INT(6) , " +
			LAST_HITS + " INT(5) , " +
			DENIES + " INT(5) , " +
			GOLD_PER_MIN + " INT(5) , " +
			XP_PER_MIN + " INT(5) , " +
			GOLD_SPENT + " INT(6) , " +
			HERO_DAMAGE + " INT(6) , " +
			TOWER_DAMAGE + " INT(6) , " +
			HERO_HEALING  + " INT(6) , " +
			LEVEL + " INT(2) ," +
			"PRIMARY KEY (" + HERO_ID + ", "+MATCH_ID + ") " +  ")";
	
	//Item List Table
	private static String CREATE_ITEM_LIST_TABLE = "CREATE TABLE " + ITEM_LIST_TABLE_NAME + "( " +
			ITEM_ID + " INT(3) PRIMARY KEY , " +
			ITEM_NAME + " VARCHAR(255) , " +
			ITEM_PIC + " VARCHAR(255) " + ")";
	
	//Match Notes Table
	private static String CREATE_MATCH_NOTES_TABLE = "CREATE TABLE " + MATCH_NOTES_TABLE_NAME + "( " +
			MATCH_ID + " INT(11) PRIMARY KEY, " +
			NOTES + " VARCHAR(255) " + ")";
			 
	
	//Notifications Tablee
	private static String CREATE_NOTIFICATIONS_TABLE = "CREATE TABLE " + NOTIFICATIONS_TABLE_NAME + "( " +
			STEAM_ID + " BIGINT(20) PRIMARY KEY, " +
			UPDATE_TIME + " INT(11), " +
			PERSONA_NAME + " VARCHAR(255) " + ")";
	
	
	LocalDB(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//Create Tables
        db.execSQL(CREATE_USER_INFO_TABLE);
        db.execSQL(CREATE_MATCH_DETAIL_TABLE);
        db.execSQL(CREATE_MATCH_HISTORY_TABLE);
        db.execSQL(CREATE_HERO_LIST_TABLE);
        db.execSQL(CREATE_PLAYER_MATCH_DETAIL_TABLE);
        db.execSQL(CREATE_ITEM_LIST_TABLE);
        db.execSQL(CREATE_MATCH_NOTES_TABLE);
		db.execSQL(CREATE_NOTIFICATIONS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// Drop Older tables on version upgrade
        db.execSQL("DROP TABLE IF EXISTS "+USER_INFO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MATCH_DETAIL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MATCH_HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+HERO_LIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+PLAYER_MATCH_DETAIL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_LIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MATCH_NOTES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+NOTIFICATIONS_TABLE_NAME);
 
        // Create fresh tables
        this.onCreate(db);
    }
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		// Drop Older tables on version downgrade
        db.execSQL("DROP TABLE IF EXISTS "+USER_INFO_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MATCH_DETAIL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MATCH_HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+HERO_LIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+PLAYER_MATCH_DETAIL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ITEM_LIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+MATCH_NOTES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+NOTIFICATIONS_TABLE_NAME);
 
        // Create fresh tables
        this.onCreate(db);
    }
	
	
	//User Info Methods
	
	public void updateUserInfo(JSONObject playerData, int friendFlag)
	{
		dbWritable = this.getWritableDatabase();
		Log.d(TAG, "Updating User Info");
		dbValues = new ContentValues();
		try {
			dbValues.put(PERSONA_NAME, playerData.getString(PERSONA_NAME));
			dbValues.put(LAST_LOGOUT, playerData.getString(LAST_LOGOUT));
			dbValues.put(COMMUNITY_VISIBILITY_STATE, playerData.getString(COMMUNITY_VISIBILITY_STATE));
			dbValues.put(PROFILE_STATE, playerData.getString(PROFILE_STATE));
			dbValues.put(PROFILE_URL, playerData.getString(PROFILE_URL));
			dbValues.put(AVATAR, playerData.getString(AVATAR));
			dbValues.put(AVATAR_MEDIUM, playerData.getString(AVATAR_MEDIUM));
			dbValues.put(AVATAR_FULL, playerData.getString(AVATAR_FULL));
			dbValues.put(PERSONA_STATE, playerData.getString(PERSONA_STATE));
			if(playerData.has(REAL_NAME))
			{
				dbValues.put(REAL_NAME, playerData.getString(REAL_NAME));
			}
			else
			{
				dbValues.put(REAL_NAME, "");
			}
			dbValues.put(PRIMARY_CLAN_ID, playerData.getString(PRIMARY_CLAN_ID));
			dbValues.put(TIME_CREATED, playerData.getString(TIME_CREATED));
			dbValues.put(PERSONA_STATE_FLAGS, playerData.getString(PERSONA_STATE_FLAGS));
			dbValues.put(GAME_EXTRA_INFO, playerData.getString(GAME_EXTRA_INFO));
			dbValues.put(GAME_ID, playerData.getString(GAME_ID));
			dbValues.put(FRIEND, friendFlag);
			if(playerData.has(WON) && playerData.has(LOST))
			{
				if(playerData.getInt(LOST) != 0 || playerData.getInt(WON) != 0)
				{
					dbValues.put(WON, playerData.getString(WON));
					dbValues.put(LOST, playerData.getString(LOST));
				}
			}
			dbWritable.update(USER_INFO_TABLE_NAME, dbValues, STEAM_ID +" = ?", new String[]{playerData.getString(STEAM_ID)});
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean insertUserInfo(JSONObject playerData, int friendFlag)
	{
		dbWritable = this.getWritableDatabase();
		Log.d(TAG, "Inserting User Info");
		dbValues = new ContentValues();
		try {
			dbValues.put(STEAM_ID, playerData.getString(STEAM_ID));
			dbValues.put(PERSONA_NAME, playerData.getString(PERSONA_NAME));
			dbValues.put(LAST_LOGOUT, playerData.getString(LAST_LOGOUT));
			dbValues.put(COMMUNITY_VISIBILITY_STATE, playerData.getString(COMMUNITY_VISIBILITY_STATE));
			dbValues.put(PROFILE_STATE, playerData.getString(PROFILE_STATE));
			dbValues.put(PROFILE_URL, playerData.getString(PROFILE_URL));
			dbValues.put(AVATAR, playerData.getString(AVATAR));
			dbValues.put(AVATAR_MEDIUM, playerData.getString(AVATAR_MEDIUM));
			dbValues.put(AVATAR_FULL, playerData.getString(AVATAR_FULL));
			dbValues.put(PERSONA_STATE, playerData.getString(PERSONA_STATE));
			if(playerData.has(REAL_NAME))
			{
				dbValues.put(REAL_NAME, playerData.getString(REAL_NAME));
			}
			else
			{
				dbValues.put(REAL_NAME, "");
			}
			dbValues.put(PRIMARY_CLAN_ID, playerData.getString(PRIMARY_CLAN_ID));
			dbValues.put(TIME_CREATED, playerData.getString(TIME_CREATED));
			dbValues.put(PERSONA_STATE_FLAGS, playerData.getString(PERSONA_STATE_FLAGS));
			dbValues.put(GAME_EXTRA_INFO, playerData.getString(GAME_EXTRA_INFO));
			dbValues.put(GAME_ID, playerData.getString(GAME_ID));
			dbValues.put(FRIEND, friendFlag);
			if(playerData.has(WON))
			{
				dbValues.put(WON, playerData.getString(WON));
			}
			else
			{
				dbValues.put(WON, "0");
			}
			if(playerData.has(LOST))
			{
				dbValues.put(LOST, playerData.getString(LOST));
			}
			else
			{
				dbValues.put(LOST, "0");
			}
			try{
			insertErrorVal = dbWritable.insertOrThrow(USER_INFO_TABLE_NAME, null, dbValues);
			}catch(Exception e)
			{
				Log.d(TAG, "Insert Exception",e);
			}
			dbWritable.close();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			Log.d(TAG, ""+e);
		}
		if(insertErrorVal != -1)
		{
			Log.d(TAG,"Successfull Insertion");
			return true;
		}
		return false;
	}
	
	public Cursor getUserInfo(String playerSteamID)
	{
		dbReadable = this.getReadableDatabase();
		Cursor playerDataCursor = null;
		Log.d(TAG, "Fetching User Info");
		playerDataCursor  = dbReadable.query(USER_INFO_TABLE_NAME, USER_INFO_COLUMS, STEAM_ID +" = ?", new String[]{playerSteamID}, null, null, null);
		return playerDataCursor;
	}
	
	public Cursor getFriendList()
	{
		int friendFlag = 1;
		dbReadable = this.getReadableDatabase();
		Cursor friendDataCursor = null;
		Log.d(TAG, "Fetching Friend List");
		friendDataCursor  = dbReadable.query(USER_INFO_TABLE_NAME, USER_INFO_COLUMS, FRIEND + " = ?", new String[]{Integer.toString(friendFlag)}, null, null, null);
		return friendDataCursor;
	}
	
	//Match History Methods
	
	public boolean insertMatchHistory(JSONArray matchHistoryData, String playerSteamID)
	{
		Log.d(TAG,"Inserting Match History");
		boolean insertFlag = false;
		JSONObject currentIndexMatchHistory = null;
		JSONObject currentPlayer = null;
		JSONArray currentIndexPlayers = null;
		String currentIndexMatchID = null;
		String hero_id = null;
		String user_account_id = null;
		String lastMatchID = null;
		user_account_id = (new BigDecimal(playerSteamID).subtract(steamIDConverter)).toString();
		Cursor lastMatchHistory = getMatchHistory(playerSteamID,"1");
		lastMatchHistory.moveToFirst();
		dbValues = new ContentValues();
		dbWritable = this.getWritableDatabase();
		user_account_id = (new BigDecimal(playerSteamID).subtract(steamIDConverter)).toString();
		if(lastMatchHistory.getCount()>0)
		{
			Log.d(TAG,"Inserting Few");
			for(int i=0;i<matchHistoryData.length();i++)
			{
				try {
					currentIndexMatchHistory = matchHistoryData.getJSONObject(i);
					currentIndexMatchID = currentIndexMatchHistory.getString(MATCH_ID);
					currentIndexPlayers = currentIndexMatchHistory.getJSONArray(PLAYERS);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG,"JSON Exception",e);
				}
				
				lastMatchID = lastMatchHistory.getString(1);
				Log.d(TAG,lastMatchID);
				if(currentIndexMatchID.equals(lastMatchID))
				{
					return insertFlag;
				}
				else
				{
					for(int j=0;j<currentIndexPlayers.length();j++)
					{
						try {
							currentPlayer = currentIndexPlayers.getJSONObject(j);
							if(currentPlayer.getString(ACCOUNT_ID).equals(user_account_id))
							{
								hero_id = currentPlayer.getString(HERO_ID);
								break;
							}
						}catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.d(TAG,"JSON Exception",e);
						}
					}
				
					try {
						dbValues.put(STEAM_ID, playerSteamID);
						dbValues.put(MATCH_ID, currentIndexMatchHistory.getString(MATCH_ID));
						dbValues.put(MATCH_SEQ_NUM, currentIndexMatchHistory.getString(MATCH_SEQ_NUM));
						dbValues.put(START_TIME, currentIndexMatchHistory.getString(START_TIME));
						dbValues.put(LOBBY_TYPE, currentIndexMatchHistory.getString(LOBBY_TYPE));
						dbValues.put(ACCOUNT_ID, user_account_id);
						dbValues.put(HERO_ID, hero_id);
						insertErrorVal = dbWritable.insert(MATCH_HISTORY_TABLE_NAME, null, dbValues);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d(TAG,"JSON Exception",e);
					}
				}
			}
		}
		else
		{
			Log.d(TAG,"Inserting All");
			for(int i=0;i<matchHistoryData.length();i++)
			{
				try {
					currentIndexMatchHistory = matchHistoryData.getJSONObject(i);
					currentIndexMatchID = currentIndexMatchHistory.getString(MATCH_ID);
					currentIndexPlayers = currentIndexMatchHistory.getJSONArray(PLAYERS);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG,"JSON Exception",e);
				}
				for(int j=0;j<currentIndexPlayers.length();j++)
				{
					try {
						currentPlayer = currentIndexPlayers.getJSONObject(j);
						if(currentPlayer.getString(ACCOUNT_ID).equals(user_account_id))
						{
							hero_id = currentPlayer.getString(HERO_ID);
							break;
						}
					}catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.d(TAG,"JSON Exception",e);
					}
				}
			
				try {
					dbValues.put(STEAM_ID, playerSteamID);
					dbValues.put(MATCH_ID, currentIndexMatchHistory.getString(MATCH_ID));
					dbValues.put(MATCH_SEQ_NUM, currentIndexMatchHistory.getString(MATCH_SEQ_NUM));
					dbValues.put(START_TIME, currentIndexMatchHistory.getString(START_TIME));
					dbValues.put(LOBBY_TYPE, currentIndexMatchHistory.getString(LOBBY_TYPE));
					dbValues.put(ACCOUNT_ID, user_account_id);
					dbValues.put(HERO_ID, hero_id);
					insertErrorVal = dbWritable.insertOrThrow(MATCH_HISTORY_TABLE_NAME, null, dbValues);
				} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d(TAG,"JSON Exception",e);
				}
			}
		}
		if(insertErrorVal != -1)
		{
			insertFlag = true;
			Log.d(TAG,"Success!");
		}
		return insertFlag;
	}
		
	
	public Cursor getMatchHistory(String playerSteamID, String numberOfRows)
	{
		dbReadable = this.getReadableDatabase();
		Cursor matchHistoryCursor = null;
		Log.d(TAG,"Fetching Match History");
		matchHistoryCursor = dbReadable.query(MATCH_HISTORY_TABLE_NAME, MATCH_HISTORY_COLUMNS, STEAM_ID +" = ?", new String[]{playerSteamID}, null, null, MATCH_ID+" DESC", numberOfRows);
		return matchHistoryCursor;
	}
	
	
	//Match Detail Methods
	
	public boolean insertMatchDetails(JSONObject matchDetailData)
	{
		String matchID = null;
		dbWritable = this.getWritableDatabase();
		Log.d(TAG, "Inserting Match Detail");
		dbValues = new ContentValues();
		try {
			matchID = matchDetailData.getString(MATCH_ID);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Cursor lastMatchDetail = getMatchDetail(matchID);
		if(lastMatchDetail.getCount() == 0)
		{
			lastMatchDetail.moveToFirst();
			
			try {
				dbValues.put(MATCH_ID, matchDetailData.getString(MATCH_ID));
				dbValues.put(RADIANT_WIN, matchDetailData.getString(RADIANT_WIN));
				dbValues.put(DURATION, matchDetailData.getString(DURATION));
				dbValues.put(START_TIME, matchDetailData.getString(START_TIME));
				dbValues.put(MATCH_SEQ_NUM, matchDetailData.getString(MATCH_SEQ_NUM));
				dbValues.put(TOWER_STATUS_RADIANT, matchDetailData.getString(TOWER_STATUS_RADIANT));
				dbValues.put(TOWER_STATUS_DIRE, matchDetailData.getString(TOWER_STATUS_DIRE));
				dbValues.put(BARRACKS_STATUS_RADIANT, matchDetailData.getString(BARRACKS_STATUS_RADIANT));
				dbValues.put(BARRACKS_STATUS_DIRE, matchDetailData.getString(BARRACKS_STATUS_DIRE));
				dbValues.put(CLUSTER, matchDetailData.getString(CLUSTER));
				dbValues.put(FIRST_BLOOD_TIME, matchDetailData.getString(FIRST_BLOOD_TIME));
				dbValues.put(LOBBY_TYPE, matchDetailData.getString(LOBBY_TYPE));
				dbValues.put(HUMAN_PLAYERS, matchDetailData.getString(HUMAN_PLAYERS));
				dbValues.put(LEAGUE_ID, matchDetailData.getString(LEAGUE_ID));
				dbValues.put(POSITIVE_VOTES, matchDetailData.getString(POSITIVE_VOTES));
				dbValues.put(NEGATIVE_VOTES, matchDetailData.getString(NEGATIVE_VOTES));
				dbValues.put(GAME_MODE, matchDetailData.getString(GAME_MODE));
				insertErrorVal = dbWritable.insert(MATCH_DETAIL_TABLE_NAME, null, dbValues);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG,""+e);
			}
			
			if(insertErrorVal != -1)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Cursor getMatchDetail(String matchID)
	{
		dbReadable = this.getReadableDatabase();
		Cursor matchHistoryCursor = null;
		matchHistoryCursor  = dbReadable.query(MATCH_DETAIL_TABLE_NAME, MATCH_DETAIL_COLUMNS, MATCH_ID +" = ?", new String[]{matchID}, null, null, null);
		Log.d(TAG, "Fetched Match Detail");
		return matchHistoryCursor;
	}
	
	//Hero Details
	
	public boolean insertHeroList(JSONArray heroList)
	{
		JSONObject currentIndexHero = null;
		String heroName;
		String heroID;
		String heroImagePath;
		dbWritable = this.getWritableDatabase();
		dbValues = new ContentValues();
		for(int i=0;i<heroList.length();i++)
		{
			try {
				currentIndexHero = heroList.getJSONObject(i);
				heroName = currentIndexHero.getString(HERO_NAME);
				heroID = currentIndexHero.getString(HERO_LIST_ID);
				heroName = heroName.substring(14);
				heroImagePath = heroName;
				String[] heroNameSplit = heroName.split("_");
				heroName = "";
				int heroPartIndex = 0;
				for(String heroNamePart: heroNameSplit)
				{
					heroName += (Character.toUpperCase(heroNamePart.charAt(0)))+heroNamePart.substring(1);
					heroPartIndex ++;
					if(heroPartIndex<heroNameSplit.length)
					{
						heroName += " ";
					}
				}
				dbValues.put(HERO_LIST_ID, heroID);
				dbValues.put(HERO_NAME, heroName);
				dbValues.put(HERO_IMAGE_PATH, heroImagePath);
				insertErrorVal = dbWritable.insert(HERO_LIST_TABLE_NAME, null, dbValues);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(insertErrorVal != -1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Cursor getHeroDetails(String heroID)
	{
		dbReadable = this.getReadableDatabase();
		Cursor heroListCursor = null;
		heroListCursor = dbReadable.query(HERO_LIST_TABLE_NAME, HERO_LIST_COLUMNS, HERO_LIST_ID +" = ?", new String [] {heroID}, null, null, null);
		Log.d(TAG,"Fetched Hero List");
		return heroListCursor;
	}
	
	public Cursor getAllHeroes()
	{
		dbReadable = this.getReadableDatabase();
		Cursor heroListCursor = null;
		heroListCursor = dbReadable.query(HERO_LIST_TABLE_NAME, HERO_LIST_COLUMNS, null, null, null, null, null);
		Log.d(TAG,"Fetched Hero List");
		return heroListCursor;
	}
	
	public boolean insertPlayerMatchDetails(JSONArray playerDetailArray,String match_ID)
	{
		Log.d(TAG,"Inserting Player Match Data");
		 boolean insertFlag = false;
		 
		 for(int index=0;index<playerDetailArray.length();index++)
		 {
			 dbWritable = this.getWritableDatabase();
			 dbValues = new ContentValues();
			 try {
				 JSONObject currentPlayerDetail = playerDetailArray.getJSONObject(index);
				 dbValues.put(STEAM_ID, currentPlayerDetail.getJSONObject(PLAYER_DATA).getString(STEAM_ID));
				 dbValues.put(PERSONA_NAME,currentPlayerDetail.getJSONObject(PLAYER_DATA).getString(PERSONA_NAME));
				 dbValues.put(MATCH_ID, match_ID);
				 dbValues.put(PLAYER_SLOT, currentPlayerDetail.getString(PLAYER_SLOT));
				 dbValues.put(HERO_ID, currentPlayerDetail.getString(HERO_ID));
				 dbValues.put(ITEM_0, currentPlayerDetail.getString(ITEM_0));
				 dbValues.put(ITEM_1, currentPlayerDetail.getString(ITEM_1));
				 dbValues.put(ITEM_2, currentPlayerDetail.getString(ITEM_2));
				 dbValues.put(ITEM_3, currentPlayerDetail.getString(ITEM_3));
				 dbValues.put(ITEM_4, currentPlayerDetail.getString(ITEM_4));
				 dbValues.put(ITEM_5, currentPlayerDetail.getString(ITEM_5));
				 dbValues.put(KILLS, currentPlayerDetail.getString(KILLS));
				 dbValues.put(DEATHS, currentPlayerDetail.getString(DEATHS));
				 dbValues.put(ASSISTS, currentPlayerDetail.getString(ASSISTS));
				 dbValues.put(LEAVER_STATUS, currentPlayerDetail.getString(LEAVER_STATUS));
				 dbValues.put(GOLD, currentPlayerDetail.getString(GOLD));
				 dbValues.put(LAST_HITS, currentPlayerDetail.getString(LAST_HITS));
				 dbValues.put(DENIES, currentPlayerDetail.getString(DENIES));
				 dbValues.put(GOLD_PER_MIN, currentPlayerDetail.getString(GOLD_PER_MIN));
				 dbValues.put(XP_PER_MIN, currentPlayerDetail.getString(XP_PER_MIN));
				 dbValues.put(GOLD_SPENT, currentPlayerDetail.getString(GOLD_SPENT));
				 dbValues.put(HERO_DAMAGE, currentPlayerDetail.getString(HERO_DAMAGE));
				 dbValues.put(TOWER_DAMAGE, currentPlayerDetail.getString(TOWER_DAMAGE));
				 dbValues.put(HERO_HEALING, currentPlayerDetail.getString(HERO_HEALING));
				 dbValues.put(LEVEL, currentPlayerDetail.getString(LEVEL));
				 insertErrorVal = dbWritable.insert(PLAYER_MATCH_DETAIL_TABLE_NAME, null, dbValues);
				 
				 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d(TAG,"JSONException",e);
			}
			 
		 }
		 
		 if(insertErrorVal != -1)
		 {
			 insertFlag = true;
		 }
		 
		 return insertFlag;
	}
	
	public Cursor getPlayerMatchDetail(String match_ID)
	{
		dbReadable = this.getReadableDatabase();
		Cursor playerMatchDetail = null;
		playerMatchDetail = dbReadable.query(PLAYER_MATCH_DETAIL_TABLE_NAME, PLAYER_MATCH_DETAIL_COLUMNS, MATCH_ID + " = ?", new String[]{match_ID}, null, null, null, null);
		Log.d(TAG, "Fetching Player Match Detail");
		return playerMatchDetail;
	}
	
	
	public boolean insertItemList(String[] key, String[] value)
	{
		boolean insertFlag = false;
		Log.d(TAG, "Inserting Item List");
		dbWritable = this.getWritableDatabase();
		for(int index = 0;index < key.length; index++)
		{
			dbValues = new ContentValues();
			dbValues.put(ITEM_ID, key[index]);
			dbValues.put(ITEM_PIC, value[index]);
			
			String[] tempName = value[index].split("_");
			String itemName = "";
			int parsingIndex = 0;
			for (String tempNameInd : tempName)
			{
				itemName += Character.toUpperCase(tempNameInd.charAt(0)) + tempNameInd.substring(1);
				parsingIndex++;
				if(parsingIndex < tempName.length)
				{
					itemName += " ";
				}
			}
			dbValues.put(ITEM_NAME, itemName);
			insertErrorVal = dbWritable.insert(ITEM_LIST_TABLE_NAME, null, dbValues);
		}
		
		dbValues = new ContentValues();
		dbValues.put(ITEM_ID, "0");
		dbValues.put(ITEM_PIC, "no_pic");
		dbValues.put(ITEM_NAME, "no_item");
		insertErrorVal = dbWritable.insert(ITEM_LIST_TABLE_NAME, null, dbValues);
		
		if(insertErrorVal != -1)
		{
			insertFlag = true;
		}
		
		return insertFlag;
	}
	
	public Cursor getItemList(String itemID)
	{
//		String QueryArgs = "";
//		for(int itemIndex = 0; itemIndex < itemID.length; itemIndex++)
//		{
//			QueryArgs += ",?";
//		}
//		
//		QueryArgs = "(" + QueryArgs.substring(1) + ")";
		
		dbReadable = this.getReadableDatabase();
		Cursor itemListCursor = null;
		itemListCursor = dbReadable.query(ITEM_LIST_TABLE_NAME, ITEM_LIST_COLUMNS, ITEM_ID + " = ?", new String[]{itemID}, null, null, null, null);
		Log.d(TAG,"Fetched Item List");
		return itemListCursor;
	}
	
	public boolean insertMatchNote(String match_ID,String note)
	{
		boolean insertFlag = false;
		dbWritable = this.getWritableDatabase();
		dbValues = new ContentValues();
		dbValues.put(MATCH_ID,match_ID);
		dbValues.put(NOTES,note);
		insertErrorVal = dbWritable.insert(MATCH_NOTES_TABLE_NAME, null, dbValues);
		if(insertErrorVal != -1)
		{
			insertFlag = true;
		}
		return insertFlag;
	}
	
	public boolean updateMatchNote(String match_ID,String note)
	{
		boolean updateFlag = false;
		dbWritable = this.getWritableDatabase();
		dbValues = new ContentValues();
		dbValues.put(MATCH_ID,match_ID);
		dbValues.put(NOTES,note);
		insertErrorVal = dbWritable.update(MATCH_NOTES_TABLE_NAME, dbValues, MATCH_ID +" = ?", new String[]{match_ID});
		if(insertErrorVal != -1)
		{
			updateFlag = true;
		}
		return updateFlag;
	}
	
	public Cursor getMatchNote(String match_ID)
	{
		dbReadable = this.getReadableDatabase();
		Cursor matchNoteCursor = null;
		matchNoteCursor = dbReadable.query(MATCH_NOTES_TABLE_NAME, MATCH_NOTES_COLUMNS, MATCH_ID + " = ?", new String[]{match_ID}, null, null, null, null);
		Log.d(TAG,"Fetched Match Note");
		return matchNoteCursor;
	}
	
	public boolean insertNotifications(JSONArray jsonArray,String updateTime)
	{
		Log.d(TAG,"Inserting Notifications");
		boolean insertFlag = false;
		dbValues = new ContentValues();
		dbWritable = this.getWritableDatabase();
		for(int index = 0;index<jsonArray.length();index++)
		{
			try {
				JSONObject notificationObject = jsonArray.getJSONObject(index).getJSONObject("player_data");
				Log.d(TAG,"Noti:"+notificationObject.getString(GAME_EXTRA_INFO));
				if(notificationObject.getString(GAME_EXTRA_INFO).equals("Dota 2"))
				{
					Log.d(TAG,"Noti:"+notificationObject.getString(PERSONA_NAME));
					dbValues.put(STEAM_ID, notificationObject.getString(STEAM_ID));
					dbValues.put(PERSONA_NAME, notificationObject.getString(PERSONA_NAME));
					dbValues.put(UPDATE_TIME, updateTime);
					if(!checkNotificationInfo(notificationObject.getString(STEAM_ID), updateTime))
					{
						insertErrorVal = dbWritable.insert(NOTIFICATIONS_TABLE_NAME, null, dbValues);
					}
					else
					{
						insertErrorVal = dbWritable.update(NOTIFICATIONS_TABLE_NAME, dbValues, STEAM_ID +" = ?", new String[]{notificationObject.getString(STEAM_ID)});
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(insertErrorVal != -1)
		{
			insertFlag = true;
		}
		
		return insertFlag;
	}
	
	public Cursor getNotifications()
	{
		dbReadable = this.getReadableDatabase();
		Cursor notificationsCursor = null;
		notificationsCursor = dbReadable.query(NOTIFICATIONS_TABLE_NAME, NOTIFICATIONS_COLUMNS, null, null, null, null, UPDATE_TIME + " DESC",Integer.toString(20));
		Log.d(TAG,"Fetched Notifications");
		return notificationsCursor;
	}
	
	public boolean checkNotificationInfo(String steam_ID,String updateTime)
	{
		dbReadable = this.getReadableDatabase();
		Cursor notificationsCursor = null;
		notificationsCursor = dbReadable.query(NOTIFICATIONS_TABLE_NAME, NOTIFICATIONS_COLUMNS, STEAM_ID + " = ?", new String[]{steam_ID}, null, null, null);
		if(notificationsCursor.getCount() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}