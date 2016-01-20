package technited.dota2central;

import java.util.List;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class MainScreen extends FragmentActivity {

	private static final String TAG = "MainScreen";
	private String steam_ID;
	private boolean loginStatus;
	private String currentFetchContent;
	private boolean flagBackgroundLoad;
	
	private URLConstructor urlConstructor;
	private GetAPIResult getAPIResult;
	private GetOtherAPIResult getOtherAPIResult;
	
	private PageAdapter_1 mPagerAdapter;
	private ProgressDialog mProgressDialog;
	private ProgressBar mProgressBar;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	
	private ImageButton refreshContentButton;
	private RelativeLayout mainContainer;
	
	private ViewPager mPager;
	private Dashboard dashboardFragment;
	private Notification notificationFragment;
	private Friends friendsFragment;
	
	//Libraries
	private	UserInfoHandler userInfoHandler;
	private ImageFileHandler imageFileHandler;
	private HeroListHandler heroListHandler;
	private MatchHistoryHandler matchHistoryHandler;
	private FriendListHandler friendListHandler;
	private LocalPreferences localPreferences;
	private SlidingMenuAdapter slidingMenuAdapter;
	private ItemListHandler itemListHandler;
	private NotificationHandler notificationHandler;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        initialiseNavigationDrawer();
        initialiseFragments();
        startLoading();
    }
    
    private void initialiseNavigationDrawer()
    {
    	 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    	 mDrawerList = (ListView) findViewById(R.id.left_drawer);
    	 slidingMenuAdapter = new SlidingMenuAdapter(MainScreen.this, R.layout.sliding_menu_row);
    	 mDrawerList.setAdapter(slidingMenuAdapter);
    	 mDrawerList.setItemChecked(1, true);
    	 mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch(position)
				{
					case 1:{
						mDrawerLayout.closeDrawer(mDrawerList);
					}
						break;
						
					case 3:{
						mDrawerLayout.closeDrawer(mDrawerList);
						Intent settingsIntent = new Intent(MainScreen.this,Settings.class);
						startActivity(settingsIntent);
					}
					break;
					case 4:
						{
							mDrawerLayout.closeDrawer(mDrawerList);
							localPreferences.clearPreferences();
							MainScreen.this.deleteDatabase("dota2Central");
							disableNotificationReceiver();
							Intent signOut = new Intent(MainScreen.this,SignUp.class);
							signOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
							startActivity(signOut);
							finish();
						}
						break;
				}
			}
		});
    }
    
    private void enableNotificationReceiver() {
        ComponentName receiver = new ComponentName(this, NotificationReciever.class);
        PackageManager pm = this.getPackageManager();

        if( receiver != null && pm != null ) {
                pm.setComponentEnabledSetting(receiver, 
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
        }
    }
    
    private void disableNotificationReceiver() {
        ComponentName receiver = new ComponentName(this, NotificationReciever.class);
        PackageManager pm = this.getPackageManager();

        if( receiver != null && pm != null ) {
                pm.setComponentEnabledSetting(receiver, 
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
        }
    }
    
    public void toggleNavigationDrawer(View view)
    {
    	if(!mDrawerLayout.isDrawerOpen(mDrawerList))
    	{
    		mDrawerLayout.openDrawer(mDrawerList);
    	}
    	else
    	{
    		mDrawerLayout.closeDrawer(mDrawerList);
    	}
    }
    
    private void initialiseFragments()
    {
    	//Fragments
    	dashboardFragment = new Dashboard();
    	notificationFragment = new Notification();
    	friendsFragment = new Friends();
    	
    	//Libraries
    	matchHistoryHandler = new MatchHistoryHandler(MainScreen.this);
    	heroListHandler = new HeroListHandler(MainScreen.this);
    	imageFileHandler = new ImageFileHandler(MainScreen.this);
		userInfoHandler = new UserInfoHandler(MainScreen.this);
    	urlConstructor = new URLConstructor(MainScreen.this);
    	friendListHandler = new FriendListHandler(MainScreen.this);
    	itemListHandler = new ItemListHandler(MainScreen.this);
    	notificationHandler = new NotificationHandler(MainScreen.this);
    	
    	//Other Stuff
    	mainContainer = (RelativeLayout) findViewById(R.id.main_contatiner);
    	localPreferences = new LocalPreferences(MainScreen.this);
    	localPreferences.readFromPreferences();
    	mProgressBar = (ProgressBar) findViewById(R.id.loader_bar);
    	refreshContentButton = (ImageButton) findViewById(R.id.refreshContent);
    	
    	//Assign Libraries to Fragments
    	dashboardFragment.putUserInfoHandlerInstance(userInfoHandler);
		dashboardFragment.putMatchHistoryHandler(matchHistoryHandler);
		dashboardFragment.putHeroListHandler(heroListHandler);
		dashboardFragment.putImageFileHandler(imageFileHandler);
		
		friendsFragment.putFriendListHandler(friendListHandler);
		friendsFragment.putImageFileHandler(imageFileHandler);
		friendListHandler.putImageFileHandler(imageFileHandler);
		
		notificationFragment.putImageFileHandler(imageFileHandler);
		notificationFragment.putNotificationHandler(notificationHandler);
		
		//Flags
		flagBackgroundLoad = false;
    }
    
    public void refreshAllContent(View view)
    {
    	if(dashboardFragment.isVisible())
    	{
    		startBackgroundLoad();
    	}
    	else if(friendsFragment.isVisible())
    	{
    		loadFriendList();
    	}
    }
    
    private void startLoading()                                                      //Checks For Login Status i.e. first time or returning user
    {
    	loginStatus = localPreferences.getLoginStatus();
    	
    	if(loginStatus)
    	{
    		startFreshLoad();
    	}
    	else
    	{
    		steam_ID = localPreferences.getSteamID();
    		this.initialisePaging();
    		dashboardFragment.putSteamID(steam_ID);
    		dashboardFragment.initialLoadData(refreshProfilePicFromFile());
    		notificationFragment.initialLoadData();
    		friendsFragment.initialLoadData();
    		startBackgroundLoad();
    	}
    }
    
    private void startFreshLoad()													//For First time users start load
    {
    	mainContainer.setVisibility(View.INVISIBLE);
    	loadSteamID(getIntent().getStringExtra("vanityURL"));
    }
    
    private void startBackgroundLoad()
    {
    	flagBackgroundLoad = true;
    	refreshContentButton.setVisibility(View.INVISIBLE);
    	mProgressBar.setVisibility(View.VISIBLE);
    	loadUserInfo();
    }
    
    private void loadSteamID(String userVanityURL)
    {
    	mProgressDialog = ProgressDialog.show(MainScreen.this, "Loading", "Please wait while We Load your Dota 2 Profile");
    	currentFetchContent = "steamID";
    	getOtherAPIResult = new GetOtherAPIResult();
    	getOtherAPIResult.execute(urlConstructor.getSteamIDURL(userVanityURL));
    }
    
    private void loadUserInfo(){
    	
    	currentFetchContent = "userInfo";
    	getAPIResult = new GetAPIResult();
    	getAPIResult.execute(urlConstructor.getUserInfoURL(steam_ID));
    }
    
    private void loadMatchHistory(){
    	currentFetchContent = "matchHistory";
    	getAPIResult = new GetAPIResult();
    	getAPIResult.execute(urlConstructor.getMatchHistoryURL(steam_ID));
    	
    }
    
    private void loadItemList()
    {
    	if(itemListHandler.putItemList())
    	{
    		loadHeroList();
    	}
    	else
		{
			this.deleteDatabase("dota2Central");
			fetchFailError("Please make sure your app is updated & connected to the internet!", "Item List");
		}
    }
    
    private void loadHeroList(){
    	currentFetchContent = "heroList";
    	getOtherAPIResult = new GetOtherAPIResult();
    	getOtherAPIResult.execute(urlConstructor.getHeroListURL());
    }
    
    private void loadFriendList()
    {
    	currentFetchContent = "friendList";
    	getAPIResult = new GetAPIResult();
    	getAPIResult.execute(urlConstructor.getFriendListURL(steam_ID));
    }
    
    private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(dashboardFragment);
        fragments.add(notificationFragment);
        fragments.add(friendsFragment);
        this.mPagerAdapter  = new PageAdapter_1(super.getSupportFragmentManager(), fragments);
        mPager = (ViewPager)super.findViewById(R.id.pager);
        mPager.setAdapter(this.mPagerAdapter); 
        mPager.setOffscreenPageLimit(2);
    }
    
    private class GetAPIResult extends AsyncTask<String,Void,JSONObject>
	{
		private static final String TAG_RESULT = "result";
		private static final String TAG_DATA = "data";
		private static final String TAG_SUCCESS = "success";
		private static final String TAG_RESPONSE = "response";
		@Override
		protected JSONObject doInBackground(String... URLS) {
			Log.d(TAG, "Background Fetching Starts:"+URLS[0]);
			JSONObject json_object = null;
			JsonParser json_parser=new JsonParser();
			json_object = json_parser.getJSONFromURL(URLS[0]);
			return json_object;
		}
		
		@Override
		protected void onPostExecute(JSONObject jsonObject)
		{
			if(jsonObject != null && jsonObject.has(TAG_RESPONSE))
			{
				JSONObject response = null;
				try {
					response = jsonObject.getJSONObject(TAG_RESPONSE);
					if(response == null)
					{
						if(loginStatus)
						{
							mProgressDialog.dismiss();
							serverDownError();
						}
						else
						{
							mProgressBar.setVisibility(View.INVISIBLE);
							refreshContentButton.setVisibility(View.VISIBLE);
						}
						return;
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(response.getString(TAG_RESULT).equals(TAG_SUCCESS))
					{
						if(currentFetchContent.equals("userInfo"))
						{
							putUserInfo(response.getJSONObject(TAG_DATA));
						}
						else if(currentFetchContent.equals("matchHistory"))
						{
							putMatchHistory(response.getJSONObject(TAG_DATA));
						}
						else if(currentFetchContent.equals("friendList"))
						{
							putFriendList(response.getJSONArray(TAG_DATA));
							putNotifications(response.getJSONArray(TAG_DATA));
						}
					}
					else
					{
						if(loginStatus)
						{
							mProgressDialog.dismiss();
							serverDownError();
						}
						else
						{
							mProgressBar.setVisibility(View.INVISIBLE);
							refreshContentButton.setVisibility(View.VISIBLE);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG,"JSON Exception",e);
				}
			}
			else
			{
				if(loginStatus)
				{
					mProgressDialog.dismiss();
					serverDownError();
				}
				else
				{
					mProgressBar.setVisibility(View.INVISIBLE);
					refreshContentButton.setVisibility(View.VISIBLE);
				}
			}
		}
		
	}
    
    private class GetOtherAPIResult extends AsyncTask<String,Void,JSONObject>
	{
		private static final String TAG_RESULT = "result";
		private static final String TAG_RESPONSE = "response";
		private static final String TAG_SUCCESS = "success";
		private static final String TAG_STEAMID = "steamid";
		@Override
		protected JSONObject doInBackground(String... URLS) {
			Log.d(TAG, "Background Fetching Starts:"+URLS[0]);
			JSONObject json_object = null;
			JsonParser json_parser=new JsonParser();
			json_object = json_parser.getJSONFromURL(URLS[0]);
			return json_object;
		}
		
		@Override
		protected void onPostExecute(JSONObject jsonObject)
		{
			if(currentFetchContent.equals("heroList"))
			{
				JSONObject result = null;
				try {
					if(!jsonObject.has(TAG_RESULT))
					{
						if(loginStatus)
						{
							mProgressDialog.dismiss();
							serverDownError();
						}
						else
						{
							mProgressBar.setVisibility(View.INVISIBLE);
							refreshContentButton.setVisibility(View.VISIBLE);
						}
						return;
					}
					result = jsonObject.getJSONObject(TAG_RESULT);
					if(result != null)
					{
						putHeroList(result);
					}
					else
					{
						fetchFailError("Cannot connect to steam servers right Now. Please try again after some time!", "Hero List");
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			else if(currentFetchContent.equals("steamID"))
			{
				JSONObject response = null;
				try {
					response = jsonObject.getJSONObject(TAG_RESPONSE);
					String success = response.getString(TAG_SUCCESS);
					if(success.equals("1"))
					{
						String steamID = response.getString(TAG_STEAMID);
						steam_ID = steamID;
						loadUserInfo();
					}
					else
					{
						MainScreen.this.deleteDatabase("dota2Central");
						fetchFailError("Please Enter the Correct Vanity URL", "Steam Profile");
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
    
    private void putUserInfo(JSONObject jsonObject)
    {
		Log.d(TAG, "Data: "+jsonObject);
		userInfoHandler.assignJSON(jsonObject);
		boolean fetchStatus = userInfoHandler.putData(loginStatus);
		if(loginStatus)
		{
			if(fetchStatus)
			{
				loadMatchHistory();
			}
			else
			{
				this.deleteDatabase("dota2Central");
				serverDownError();
			}
			
		}
		else
		{
			loadMatchHistory();
		}
    }
    
    private void putMatchHistory(JSONObject jsonObject){
    	
    	matchHistoryHandler.assignJSON(jsonObject);
    	Log.d(TAG,jsonObject.toString());
    	if(!jsonObject.isNull("match_history_list"))
    	{
    		boolean fetchStatus = matchHistoryHandler.putMatchHistory(steam_ID);
    		if(loginStatus)
    		{
    			if(fetchStatus)
    			{
    				loadItemList();
    			}
    			else
    			{
    				this.deleteDatabase("dota2Central");
    				serverDownError();
    			}
    		}
    		else
    		{
    			dashboardFragment.putSteamID(steam_ID);
    			dashboardFragment.refreshContent();
    			mProgressBar.setVisibility(View.INVISIBLE);
    			refreshContentButton.setVisibility(View.VISIBLE);
    			flagBackgroundLoad = false;
    			loadFriendList();
    		}
    	}
    	else
    	{
    		if(loginStatus)
    		{
    			fetchFailError("Please Make Sure you have enabled exposed your match history in your dota profile!","Match History");
    		}
    		else
    		{
    			mProgressBar.setVisibility(View.INVISIBLE);
    			refreshContentButton.setVisibility(View.VISIBLE);
    		}
    	}
    	
    }
    
    private void putHeroList(JSONObject jsonObject)
    {
    	heroListHandler.assignJSON(jsonObject);
    	if(heroListHandler.putHeroList())
    	{
			localPreferences.putLoginStatus(false);
			localPreferences.putSteamID(steam_ID);
			localPreferences.writeToPreferences();
			this.initialisePaging();
    		dashboardFragment.putSteamID(steam_ID);
    		dashboardFragment.initialLoadData(refreshProfilePicFromFile());
    		mainContainer.setVisibility(View.VISIBLE);
    		mProgressDialog.dismiss();
    		AlarmManager alarmManager=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    		Intent intent = new Intent(this, NotificationReciever.class);
    		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
    		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1800000,pendingIntent);
    		enableNotificationReceiver();
    		loadFriendList();
    	}
    	else
		{
    		this.deleteDatabase("dota2Central");
    		serverDownError();
		}
    }
    
    private void putFriendList(JSONArray jsonArray)
    {
    	friendListHandler.assignJSON(jsonArray);
    	if(loginStatus)
    	{
    		if(friendListHandler.putFriendList())
    		{
    			friendsFragment.initialLoadData();
    		}
    		loginStatus = false;
    	}
    	else
    	{
    		if(friendListHandler.putFriendList())
    		{
    			friendsFragment.refreshContent();
    		}
    	}
    }
    
    public void refreshProfilePic(Bitmap userProfilePic)
    {
    	Log.d(TAG,"Refreshing Profile Pic of User:");
    	dashboardFragment.setProfilePicture(userProfilePic);
    	imageFileHandler.storeImageInMemory(userProfilePic, steam_ID);
    }
    
    private Bitmap refreshProfilePicFromFile()
    {
    	Bitmap userProfilePic = null;
    	if(imageFileHandler.imageExists(steam_ID))
    	{
    		Log.d(TAG,"Image Found in File");
    		 userProfilePic = imageFileHandler.getImageFromMemory(steam_ID);
    	}
    	return userProfilePic;
    }
    
    public void viewAllMatchHistory(View view)
    {
    	Intent matchHistoryIntent  = new Intent(this,MatchHistory.class);
    	matchHistoryIntent.putExtra("steam_ID", steam_ID);
    	startActivity(matchHistoryIntent);
    }
    
    private void serverDownError(){
		AlertDialog alertDialog = new AlertDialog.Builder(
                MainScreen.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Error:Connecting to Server");

		// Setting Dialog Message
		alertDialog.setMessage("Please Check Your Internet or Try After Some Time");

		// 	Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.ic_launcher);

		// 	Setting OK Button
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to execute after dialog closed
        finish();
        }
		});

		// 	Showing Alert Message
		alertDialog.show();
    }
    
    private void fetchFailError(String message,String type){
		AlertDialog alertDialog = new AlertDialog.Builder(
                MainScreen.this).create();

		// Setting Dialog Title
		alertDialog.setTitle("Error:Fetching "+type);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// 	Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.ic_launcher);

		// 	Setting OK Button
		alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL,"OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
        // Write your code here to execute after dialog closed
        finish();
        }
		});

		// 	Showing Alert Message
		alertDialog.show();
    }
    
    public void updateFriendsList(){
    	friendsFragment.refreshContent();
    }
    
    public void putNotifications(JSONArray jsonArray)
    {
    	notificationHandler.putJSONContent(jsonArray);
    	boolean insertNotifications = notificationHandler.putNotifications();
    	if(insertNotifications)
    	{
    		Log.d(TAG,"Insrted Noti");
    		notificationFragment.refreshContent();
    	}
    }
    
    public void updateNotificationCount(int count)
    {
    	dashboardFragment.updateNotificationCount(count);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	Log.d(TAG,"MainScreen Resumed");
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	if(getAPIResult != null)
    	{
    		getAPIResult.cancel(true);
    	}
    	if(getOtherAPIResult != null)
    	{
    		getOtherAPIResult.cancel(true);
    	}
    }
}
