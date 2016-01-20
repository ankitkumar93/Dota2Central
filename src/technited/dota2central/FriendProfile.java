package technited.dota2central;


import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FriendProfile extends Activity{
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SlidingMenuAdapter slidingMenuAdapter;
	
	private FriendListHandler friendListHandler;
	private MatchHistoryHandler matchHistoryHandler;
	private HeroListHandler heroListHandler;
	private DateTimeHandler dateTimeHandler;
	private ImageEditor imageEditor;
	private ImageFileHandler imageFileHandler;
	private GetAPIResult getAPIResult;
	private URLConstructor urlConstructor;
	
	private TextView friendName, friendLogout, heroName, heroDate, heroTime, wins, winRate, newNotifications, winsText, winRateText, newNotificationsText;
	private CircularImageView friendPic;
	private ImageView heroPic;
	private RoundedImageView roundHeroPic;
	private RelativeLayout friendContainer;
	private ImageButton refreshContentButton;
	private ProgressBar mProgressBar;
	private LocalPreferences localPreferences;
	
	//TAG
	private static final String TAG="FriendProfile";
	
	//SteamID
	private String friendSteamID;
	
	//Variables
	private String userName,lastLogout, won, lost, rate;
	private static int bigHeroWidth = 245;
	private static int bigHeroCroppedWidth = 138;
	private static int bigHeroHeight = 138;
	private static int roundHeroWidth = 140;
	private static int roundHeroCroppedWidth = 79;
	private static int roundHeroHeight = 79;
	private static final String heroPicFolder = "heroimages";
	private static final String pathSeperator = "/";
	private static final String imageExtension = ".png";
	private boolean flagBackgroundLoad;
	private String currentFetchContent;
	private boolean historyStatus;
	
	//History Number
	private static final String historyNumber = "5";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_profile);
        loadViews();
        initialiseNavigationDrawer();
        setFonts();
        startLoading();
	}
	
	private void initialiseNavigationDrawer()
    {
    	 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_friends);
    	 mDrawerList = (ListView) findViewById(R.id.left_drawer_friends);
    	 slidingMenuAdapter = new SlidingMenuAdapter(FriendProfile.this, R.layout.sliding_menu_row);
    	 mDrawerList.setAdapter(slidingMenuAdapter);
    	 mDrawerList.setOnItemClickListener(new OnItemClickListener() {

  			@Override
  			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
  					long arg3) {
  				switch(position)
  				{
  					case 1:
  						{
  							mDrawerLayout.closeDrawer(mDrawerList);
  							Intent dashboard = new Intent(FriendProfile.this,MainScreen.class);
  							dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
  							startActivity(dashboard);
  							finish();
  						}
  						break;
  					case 3:{
						mDrawerLayout.closeDrawer(mDrawerList);
						Intent settingsIntent = new Intent(FriendProfile.this,Settings.class);
						startActivity(settingsIntent);
					}
					break;
  					case 4:
  						{
  							mDrawerLayout.closeDrawer(mDrawerList);
  							localPreferences.clearPreferences();
  							FriendProfile.this.deleteDatabase("dota2Central");
  							disableNotificationReceiver();
  							Intent signOut = new Intent(FriendProfile.this,SignUp.class);
  							signOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
  							startActivity(signOut);
  							finish();
  						}
  						break;
  				}
  			}
  		});
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
	
	public void refreshAllContent(View view)
    {
		if(!flagBackgroundLoad)
		{
			startBackgroundLoad();
		}
    }
	
	private void loadViews()
	{
		//Load Steam ID
		friendSteamID = getIntent().getStringExtra("steam_ID");
		//Views
		friendName = (TextView) findViewById(R.id.friendsProfileLayoutTop_userName);
		friendLogout = (TextView) findViewById(R.id.friendsProfileLayoutTop_userLastLogout);
		friendPic = (CircularImageView) findViewById(R.id.friendsProfileLayoutTop_userImage);
		wins = (TextView) findViewById(R.id.friendsProfileLayoutMid_WinSpecifier_WinsValue);
		winsText = (TextView) findViewById(R.id.friendsProfileLayoutMid_WinSpecifier_WinsText);
		winRate = (TextView) findViewById(R.id.friendsProfileLayoutMid_WinRateSpecifier_WinRateValue);
		winRateText = (TextView) findViewById(R.id.friendsProfileLayoutMid_WinRateSpecifier_WinRateText);
		newNotifications = (TextView) findViewById(R.id.friendsProfileLayoutMid_NotificationSpecifier_NotificationValue);
		newNotificationsText = (TextView) findViewById(R.id.friendsProfileLayoutMid_NotificationSpecifier_NotificationText);
		friendContainer = (RelativeLayout) findViewById(R.id.friendProfileContainer);
		refreshContentButton = (ImageButton) findViewById(R.id.refreshContent);
		mProgressBar = (ProgressBar) findViewById(R.id.loader_bar);
		
		//Libararies
		friendListHandler = new FriendListHandler(FriendProfile.this);
		matchHistoryHandler = new MatchHistoryHandler(FriendProfile.this);
		heroListHandler = new HeroListHandler(FriendProfile.this);
		imageEditor = new ImageEditor();
		imageFileHandler = new ImageFileHandler(FriendProfile.this);
		urlConstructor  = new URLConstructor(FriendProfile.this);
		localPreferences = new LocalPreferences(FriendProfile.this);
		
		//Put Libraries
		friendListHandler.putImageFileHandler(imageFileHandler);
		
		//Flags
		flagBackgroundLoad = false;
	}
	
	private void setFonts()
	 {
		 Typeface tf;
		 String fontPath1 = "fonts/segoeui.ttf";
		 String fontPath2 = "fonts/segoeuil.ttf";
		 
		 //User Info
		 tf = Typeface.createFromAsset(FriendProfile.this.getAssets(), fontPath1);
	     friendName.setTypeface(tf);
	     wins.setTypeface(tf);
	     winsText.setTypeface(tf);
	     winRate.setTypeface(tf);
	     winRateText.setTypeface(tf);
	     newNotifications.setTypeface(tf);
	     newNotificationsText.setTypeface(tf);
	     tf = Typeface.createFromAsset(FriendProfile.this.getAssets(), fontPath2);
	     friendLogout.setTypeface(tf);
	     
	     //Match History
	     tf = Typeface.createFromAsset(FriendProfile.this.getAssets(), fontPath1);
	     TextView recentMatches = (TextView)findViewById(R.id.friendsProfileLayoutBot_Heading);
	     recentMatches.setTypeface(tf);
	     tf = Typeface.createFromAsset(FriendProfile.this.getAssets(), fontPath2);
	     
	     tf = Typeface.createFromAsset(FriendProfile.this.getAssets(), fontPath1);
	     heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_LeftHero_Info_HeroName);
	     heroName.setTypeface(tf);
	     heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_MidHero_Info_HeroName);
	     heroName.setTypeface(tf);
	     heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_RightHero_Info_HeroName);
	     heroName.setTypeface(tf);
	     heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_TopHero_Name);
	     heroName.setTypeface(tf);
	     heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_BotHero_Name);
	     heroName.setTypeface(tf);
	     tf = Typeface.createFromAsset(FriendProfile.this.getAssets(), fontPath2);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_LeftHero_Info_HeroDate);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_MidHero_Info_HeroDate);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_RightHero_Info_HeroDate);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_TopHero_Date);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_BotHero_Date);
		 heroDate.setTypeface(tf);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_LeftHero_Info_HeroTime);
		 heroTime.setTypeface(tf);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_MidHero_Info_HeroTime);
		 heroTime.setTypeface(tf);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_RightHero_Info_HeroTime);
		 heroTime.setTypeface(tf);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_TopHero_Time);
		 heroTime.setTypeface(tf);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_BotHero_Time);
		 heroTime.setTypeface(tf);

	 }
	
	private void startLoading()
	{
		Cursor matchHistoryCheck = matchHistoryHandler.getMatchHistory(friendSteamID, "1");
		if(matchHistoryCheck.getCount() > 0)
		{
			refreshContent();
			historyStatus = false;
		}
		else
		{
			friendContainer.setVisibility(View.INVISIBLE);
			historyStatus = true;
		}
		startBackgroundLoad();
	}
	
	private void startBackgroundLoad()
    {
    	flagBackgroundLoad = true;
    	refreshContentButton.setVisibility(View.INVISIBLE);
    	mProgressBar.setVisibility(View.VISIBLE);
    	loadUserInfo();
    }
    
    private void loadUserInfo(){
    	
    	currentFetchContent = "userInfo";
    	getAPIResult = new GetAPIResult();
    	getAPIResult.execute(urlConstructor.getUserInfoURL(friendSteamID));
    }
    
    private void loadMatchHistory(){
    	
    	currentFetchContent = "matchHistory";
    	getAPIResult = new GetAPIResult();
    	getAPIResult.execute(urlConstructor.getMatchHistoryURL(friendSteamID));
    	
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
			if(jsonObject != null &&  jsonObject.has(TAG_RESPONSE))
			{
				JSONObject response = null;
				try {
					response = jsonObject.getJSONObject(TAG_RESPONSE);
					if(response == null)
					{
						if(historyStatus)
						{
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
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG,"JSON Exception",e);
				}
			}
			if(historyStatus)
			{
				serverDownError();
			}
			else
			{
				mProgressBar.setVisibility(View.INVISIBLE);
				refreshContentButton.setVisibility(View.VISIBLE);
			}
		}
		
	}
    
    private void putUserInfo(JSONObject jsonObject)
    {
		Log.d(TAG, "Data: "+jsonObject);
		friendListHandler.putFriendData(jsonObject);
		loadMatchHistory();
    }
    
    private void putMatchHistory(JSONObject jsonObject){
    	
    	if(!jsonObject.isNull("match_history_list"))
		{
			matchHistoryHandler.assignJSON(jsonObject);
			boolean fetchStatus = matchHistoryHandler.putMatchHistory(friendSteamID);
			if(historyStatus)
			{
				if(fetchStatus)
				{
					refreshContent();
					friendContainer.setVisibility(View.VISIBLE);
					mProgressBar.setVisibility(View.INVISIBLE);
					refreshContentButton.setVisibility(View.VISIBLE);
					historyStatus = false;
				}
				else
				{
					AlertDialog alertDialog = new AlertDialog.Builder(
		                FriendProfile.this).create();
 
					// Setting Dialog Title
					alertDialog.setTitle("Error:Fetching Data");
 
					// 	Setting Dialog Message
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
			}
			else
			{
				mProgressBar.setVisibility(View.INVISIBLE);
				refreshContentButton.setVisibility(View.VISIBLE);
				refreshContent();
			}
		}
		else
		{
			AlertDialog alertDialog = new AlertDialog.Builder(
		        FriendProfile.this).create();

			// Setting Dialog Title
			alertDialog.setTitle("Error:Fetching Data");

			// 	Setting Dialog Message
			alertDialog.setMessage("Your Friend has not enable match History Sharing!");

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
    		flagBackgroundLoad = false;
    }
	
	private void refreshContent()
	{
		 //Fetch Data and Store Locally
		 Cursor userInfo = friendListHandler.getFriendData(friendSteamID);
		 userInfo.moveToFirst();
		 userName = userInfo.getString(1);
		 lastLogout = userInfo.getString(2);
		 won = userInfo.getString(17);
		 lost  = userInfo.getString(18);
		 if(Integer.parseInt(lost) != 0 || Integer.parseInt(won) != 0)
		 {
			 rate = " " + Integer.toString((Integer.parseInt(won)*100)/(Integer.parseInt(lost) + Integer.parseInt(won))) + "%";
		 }
		 else
		 {
			 rate = "undefined";
		 }
		 Cursor matchHistory = matchHistoryHandler.getMatchHistory(friendSteamID, historyNumber);
		//Assign Data
		 friendName.setText(userName);
		 if(!rate.equals("undefined"))
		 {
			 wins.setText(won);
			 winRate.setText(rate);
		 }
		 if(imageFileHandler.imageExists(friendSteamID))
		 {
			 setProfilePicture(imageFileHandler.getImageFromMemory(friendSteamID));
		 }
		 dateTimeHandler = new DateTimeHandler(lastLogout);
		 setLastLogoutDetails(dateTimeHandler.getAgoTime(),Integer.parseInt(userInfo.getString(8)),userInfo.getString(14));
		 assignMatchHistoryContent(matchHistory);
	}
	
	public void setProfilePicture(Bitmap userProfilePicture)
	 {
		Bitmap newScaledPic = imageEditor.setImageSize(79, 79, userProfilePicture);
		Bitmap newUserProfilePic = imageEditor.getCroppedBitmap(newScaledPic);
		friendPic.setImageBitmap(newUserProfilePic);
	 }
	
	private void setLastLogoutDetails(String lastLogoutTime, int personaState, String gameInfo)
	 {
		 String logoutTextToSet = null;
		 String logoutColor = "black";
		 if(!gameInfo.equals("null"))
		 {
			 logoutTextToSet = "In Game " + gameInfo;
			 logoutColor = "logoutGreen";
		 }
		 else
		 {
			 switch(personaState)
			 {
		 		case 0:
		 			logoutTextToSet = "last seen "+lastLogoutTime;
		 			logoutColor = "black";
		 			break;
		 		case 1:
		 			logoutTextToSet = "Online";
		 			logoutColor = "logoutBlue";;
		 			break;
		 		case 2:
		 			logoutTextToSet = "Busy";
		 			logoutColor = "logoutBlue";
		 			break;
		 		case 3:
		 			logoutTextToSet = "Away";
		 			logoutColor = "logoutBlue";
		 			break;
		 		case 4:
		 			logoutTextToSet = "Snooze";
		 			logoutColor = "logoutBlue";
		 			break;
		 		case 5:
		 			logoutTextToSet = "Looking To Trade";
		 			logoutColor = "logoutBlue";
		 			break;
		 		case 6:
		 			logoutTextToSet = "Looking To Play";
		 			logoutColor = "logoutBlue";
		 			break;
			 }
		 }
		 friendLogout.setText(logoutTextToSet);
		 int color = getResources().getIdentifier(logoutColor, "color", getPackageName());
		 friendLogout.setTextColor(Color.parseColor(getString(color)));
		 Log.d(TAG,""+color);
	 }
	
	private void assignMatchHistoryContent(Cursor matchHistoryData)
	 {
		 Cursor heroDetails = null;
		 Bitmap heroImage,heroImageScaled,heroImageFinal;
		 
		 //Left Hero
		 matchHistoryData.moveToFirst();
		 heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_LeftHero_Info_HeroName);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_LeftHero_Info_HeroDate);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_LeftHero_Info_HeroTime);
		 heroPic = (ImageView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_LeftHero_Image);
		 heroDetails = heroListHandler.getHero(matchHistoryData.getString(6));
		 heroDetails.moveToFirst();
		 heroImage = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroDetails.getString(2) + imageExtension);
		 if(heroImage != null)
		 {
			 heroImageScaled = imageEditor.setImageSize(bigHeroHeight, bigHeroWidth, heroImage);
			 heroImageFinal = imageEditor.getCroppedImage(bigHeroHeight, bigHeroCroppedWidth, bigHeroHeight, bigHeroWidth, heroImageScaled);
		 }
		 else
		 {
			 heroImageFinal = BitmapFactory.decodeResource(getResources(), R.drawable.dark_grey_back);
		 }
		 heroPic.setImageBitmap(heroImageFinal);
		 dateTimeHandler = new DateTimeHandler(matchHistoryData.getString(3));
		 heroDate.setText(dateTimeHandler.getDate());
		 heroTime.setText(dateTimeHandler.getTime());
		 heroName.setText(heroDetails.getString(1));
		 
		 //Mid Hero
		 matchHistoryData.moveToNext();
		 heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_MidHero_Info_HeroName);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_MidHero_Info_HeroDate);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_MidHero_Info_HeroTime);
		 heroPic = (ImageView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_MidHero_Image);
		 heroDetails = heroListHandler.getHero(matchHistoryData.getString(6));
		 heroDetails.moveToFirst();
		 heroImage = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroDetails.getString(2) + imageExtension);
		 if(heroImage != null)
		 {
			 heroImageScaled = imageEditor.setImageSize(bigHeroHeight, bigHeroWidth, heroImage);
			 heroImageFinal = imageEditor.getCroppedImage(bigHeroHeight, bigHeroCroppedWidth, bigHeroHeight, bigHeroWidth, heroImageScaled);
		 }
		 else
		 {
			 heroImageFinal = BitmapFactory.decodeResource(getResources(), R.drawable.dark_grey_back);
		 }
		 heroPic.setImageBitmap(heroImageFinal);
		 dateTimeHandler = new DateTimeHandler(matchHistoryData.getString(3));
		 heroDate.setText(dateTimeHandler.getDate());
		 heroTime.setText(dateTimeHandler.getTime());
		 heroName.setText(heroDetails.getString(1));
		 
		 //Right Hero
		 matchHistoryData.moveToNext();
		 heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_RightHero_Info_HeroName);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_RightHero_Info_HeroDate);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_RightHero_Info_HeroTime);
		 heroPic = (ImageView)findViewById(R.id.friendsProfileLayoutBot_HeroesTop_RightHero_Image);
		 heroDetails = heroListHandler.getHero(matchHistoryData.getString(6));
		 heroDetails.moveToFirst();
		 heroImage = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroDetails.getString(2) + imageExtension);
		 if(heroImage != null)
		 {
			 heroImageScaled = imageEditor.setImageSize(bigHeroHeight, bigHeroWidth, heroImage);
			 heroImageFinal = imageEditor.getCroppedImage(bigHeroHeight, bigHeroCroppedWidth, bigHeroHeight, bigHeroWidth, heroImageScaled);
		 }
		 else
		 {
			 heroImageFinal = BitmapFactory.decodeResource(getResources(), R.drawable.dark_grey_back);
		 }
		 heroPic.setImageBitmap(heroImageFinal);
		 dateTimeHandler = new DateTimeHandler(matchHistoryData.getString(3));
		 heroDate.setText(dateTimeHandler.getDate());
		 heroTime.setText(dateTimeHandler.getTime());
		 heroName.setText(heroDetails.getString(1));
		 
		 //Circular Hero Top
		 matchHistoryData.moveToNext();
		 heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_TopHero_Name);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_TopHero_Date);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_TopHero_Time);
		 roundHeroPic = (RoundedImageView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_TopHero_Image);
		 heroDetails = heroListHandler.getHero(matchHistoryData.getString(6));
		 heroDetails.moveToFirst();
		 heroImage = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroDetails.getString(2) + imageExtension);
		 if(heroImage != null)
		 {
			 heroImageScaled = imageEditor.setImageSize(roundHeroHeight, roundHeroWidth, heroImage);
			 heroImageFinal = imageEditor.getCroppedImage(roundHeroHeight, roundHeroCroppedWidth, roundHeroHeight, roundHeroWidth, heroImageScaled);
		 }
		 else
		 {
			 heroImageFinal = BitmapFactory.decodeResource(getResources(), R.drawable.dark_grey_back);
		 }
		 roundHeroPic.setImageBitmap(heroImageFinal);
		 dateTimeHandler = new DateTimeHandler(matchHistoryData.getString(3));
		 heroDate.setText(dateTimeHandler.getDate());
		 heroTime.setText(dateTimeHandler.getTime());
		 heroName.setText(heroDetails.getString(1));
		 
		 //Circular Hero Bot
		 matchHistoryData.moveToNext();
		 heroName = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_BotHero_Name);
		 heroDate = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_BotHero_Date);
		 heroTime = (TextView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_BotHero_Time);
		 roundHeroPic = (RoundedImageView)findViewById(R.id.friendsProfileLayoutBot_HeroesBot_BotHero_Image);
		 heroDetails = heroListHandler.getHero(matchHistoryData.getString(6));
		 heroDetails.moveToFirst();
		 heroImage = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroDetails.getString(2) + imageExtension);
		 if(heroImage != null)
		 {
			 heroImageScaled = imageEditor.setImageSize(roundHeroHeight, roundHeroWidth, heroImage);
			 heroImageFinal = imageEditor.getCroppedImage(roundHeroHeight, roundHeroCroppedWidth, roundHeroHeight, roundHeroWidth, heroImageScaled);
		 }
		 else
		 {
			 heroImageFinal = BitmapFactory.decodeResource(getResources(), R.drawable.dark_grey_back);
		 }
		 roundHeroPic.setImageBitmap(heroImageFinal);
		 dateTimeHandler = new DateTimeHandler(matchHistoryData.getString(3));
		 heroDate.setText(dateTimeHandler.getDate());
		 heroTime.setText(dateTimeHandler.getTime());
		 heroName.setText(heroDetails.getString(1));
		 
	 }
	
	private void serverDownError(){
		AlertDialog alertDialog = new AlertDialog.Builder(
                FriendProfile.this).create();

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
}
