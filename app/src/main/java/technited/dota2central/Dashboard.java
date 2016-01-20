package technited.dota2central;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Dashboard extends Fragment{
	
	private String lastLogout, userName, won, lost, rate;
	private View dashboardFragment;
	private String TAG = "DashboardFragment";
	private UserInfoHandler userInfoHandler;
	private HeroListHandler heroListHandler;
	private DateTimeHandler dateTimeHandler;
	private ImageFileHandler imageFileHandler;
	private MatchHistoryHandler matchHistoryHandler;
	private String userSteamID;
	private boolean flagViewIsLoaded;
	private static String historyNumber = "5";
	private static int bigHeroWidth = 245;
	private static int bigHeroCroppedWidth = 138;
	private static int bigHeroHeight = 138;
	private static int roundHeroWidth = 140;
	private static int roundHeroCroppedWidth = 79;
	private static int roundHeroHeight = 79;
	private static final String heroPicFolder = "heroimages";
	private static final String pathSeperator = "/";
	private static final String imageExtension = ".png";
	
	//Layout Views
	TextView dashboardName, dashboardLogout, heroName, heroDate, heroTime, wins, winRate, newNotifications, winsText, winRateText, newNotificationsText;
	ImageView heroPic;
	CircularImageView dashboardPic;
	HeroImageTiles heroImageTile;
	RelativeLayout bottomHeroes;
	//Required Tools
	 ImageEditor imageEditor;
	
	public Dashboard(){
		
		flagViewIsLoaded = true;
		
	}
	
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		 
	        if (container == null) {
	            // We have different layouts, and in one of them this
	            // fragment's containing frame doesn't exist.  The fragment
	            // may still be created from its saved state, but there is
	            // no reason to try to create its view hierarchy because it
	            // won't be displayed.  Note this is not needed -- we could
	            // just run the code below, where we would create and return
	            // the view hierarchy; it would just never be used.
	            return null;
	        }
	        dashboardFragment =inflater.inflate(R.layout.dashboard_fragment, container, false);
	        loadDashboard();
	    	return dashboardFragment;
	    	
	    }
	 
	 private void loadDashboard(){
		 
		 initializeViews();
		 setFonts();
		 initializeProfilePic();
		 flagViewIsLoaded = false;
	 }
	 
	 private void initializeViews()
	 {
		//Initialize Views
		 dashboardName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutTop_userName);
		 dashboardLogout = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutTop_userLastLogout);
		 dashboardPic = (CircularImageView)dashboardFragment.findViewById(R.id.dashboardLayoutTop_userImage);
		 wins = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutMid_WinSpecifier_WinsValue);
		 winsText = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutMid_WinSpecifier_WinsText);
		 winRate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutMid_WinRateSpecifier_WinRateValue);
		 winRateText = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutMid_WinRateSpecifier_WinRateText);
		 newNotifications = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutMid_NotificationSpecifier_NotificationValue);
		 newNotificationsText = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutMid_NotificationSpecifier_NotificationText);
		 imageEditor = new ImageEditor();
		 Log.d(TAG,"Good");
	 }
	 
	 private void setFonts()
	 {
		 Typeface tf;
		 String fontPath1 = "fonts/segoeui.ttf";
		 String fontPath2 = "fonts/segoeuil.ttf";
		 
		 //User Info
		 tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath1);
	     dashboardName.setTypeface(tf);
	     wins.setTypeface(tf);
	     winsText.setTypeface(tf);
	     winRate.setTypeface(tf);
	     winRateText.setTypeface(tf);
	     newNotifications.setTypeface(tf);
	     newNotificationsText.setTypeface(tf);
	     tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath2);
	     dashboardLogout.setTypeface(tf);
	     
	     //Match History
	     tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath1);
	     TextView recentMatches = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_Heading);
	     recentMatches.setTypeface(tf);
	     tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath2);
	     TextView viewAll = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_ViewAll);
	     viewAll.setTypeface(tf);
	     
	     tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath1);
	     heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero_Info_HeroName);
	     heroName.setTypeface(tf);
	     heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero_Info_HeroName);
	     heroName.setTypeface(tf);
	     heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero_Info_HeroName);
	     heroName.setTypeface(tf);
	     heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero_Name);
	     heroName.setTypeface(tf);
	     heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero_Name);
	     heroName.setTypeface(tf);
	     tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath2);
		 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero_Info_HeroDate);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero_Info_HeroDate);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero_Info_HeroDate);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero_Date);
		 heroDate.setTypeface(tf);
		 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero_Date);
		 heroDate.setTypeface(tf);
		 //heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero_Info_HeroTime);
		 //heroTime.setTypeface(tf);
		 //heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero_Info_HeroTime);
		 //heroTime.setTypeface(tf);
		 //heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero_Info_HeroTime);
		 //heroTime.setTypeface(tf);
		 heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero_Time);
		 heroTime.setTypeface(tf);
		 heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero_Time);
		 heroTime.setTypeface(tf);

	 }
	 

	 public void refreshContent()
	 {
		 //Fetch Data and Store Locally
		 Cursor userInfo = userInfoHandler.getData(userSteamID);
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
		 Cursor matchHistory = matchHistoryHandler.getMatchHistory(userSteamID, historyNumber);
		//Assign Data
		 dashboardName.setText(userName);
		 if(!rate.equals("undefined"))
		 {
			 wins.setText(won);
			 winRate.setText(rate);
		 }
		 dateTimeHandler = new DateTimeHandler(lastLogout);
		 setLastLogoutDetails(dateTimeHandler.getAgoTime(),Integer.parseInt(userInfo.getString(8)),userInfo.getString(14));
		 userInfo.close();
		 assignMatchHistoryContent(matchHistory);
		 
	 }
	 
	 public void updateNotificationCount(int count)
	 {
		 if(count == 0)
		 {
			 newNotifications.setText("no");
		 }
		 else
		 {
			 newNotifications.setText(Integer.toString(count));
		 }
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
		 dashboardLogout.setText(logoutTextToSet);
		 int color = getActivity().getResources().getIdentifier(logoutColor, "color", getActivity().getPackageName());
		 dashboardLogout.setTextColor(Color.parseColor(getString(color)));
		 Log.d(TAG,""+color);
	 }
	 private void initializeProfilePic()
	 {
		 Bitmap currentPic = ((BitmapDrawable)dashboardPic.getDrawable()).getBitmap();
		 dashboardPic.setImageBitmap(currentPic);
	 }
	 
	 public void setProfilePicture(Bitmap userProfilePicture)
	 {
		 if(!flagViewIsLoaded)
		 {
			 Bitmap newScaledPic = imageEditor.setImageSize(79, 79, userProfilePicture);
			 Bitmap newUserProfilePic = imageEditor.getCroppedBitmap(newScaledPic);
			 dashboardPic.setImageBitmap(newUserProfilePic);
		 }
	 }
	 
	 private void assignMatchHistoryContent(final Cursor matchHistoryData)
	 {
		 Cursor heroDetails = null;
		 Bitmap heroImage,heroImageScaled,heroImageFinal;
		 int cursorCount = matchHistoryData.getCount();
		 
		 //Left Hero
		 if(cursorCount > 0)
		 {
			 matchHistoryData.moveToFirst();
			 heroImageTile = (HeroImageTiles) dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero);
			 heroImageTile.setTag(matchHistoryData.getString(1));
			 heroImageTile.setOnClickListener(new OnClickListener() {
			
				 @Override
				 public void onClick(View v) {
					 showMatchDetails(v);
				
				 }
			 });
			 heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero_Info_HeroName);
			 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero_Info_HeroDate);
			 heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero_Info_HeroTime);
			 heroPic = (ImageView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_LeftHero_Image);
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
		 } 
		 //Mid Hero
		 if(cursorCount > 1)
		 {
			 matchHistoryData.moveToNext();
			 heroImageTile = (HeroImageTiles) dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero);
			 heroImageTile.setTag(matchHistoryData.getString(1));
			 heroImageTile.setOnClickListener(new OnClickListener() {
				 
				 @Override
				 public void onClick(View v) {
					 showMatchDetails(v);
					 
				 }
			 });
			 heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero_Info_HeroName);
			 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero_Info_HeroDate);
			 heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero_Info_HeroTime);
			 heroPic = (ImageView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_MidHero_Image);
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
		 }			 
		 //Right Hero
		 if(cursorCount > 2)
		 {
			 matchHistoryData.moveToNext();
			 heroImageTile = (HeroImageTiles) dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero);
			 heroImageTile.setTag(matchHistoryData.getString(1));
			 heroImageTile.setOnClickListener(new OnClickListener() {
				 
				 @Override
				 public void onClick(View v) {
					 showMatchDetails(v);
					 
				 }
			 });
			 heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero_Info_HeroName);
			 heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero_Info_HeroDate);
			 heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero_Info_HeroTime);
			 heroPic = (ImageView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesTop_RightHero_Image);
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
		 } 
		 //Circular Hero Top
		 if(cursorCount > 3)
		 {
			 matchHistoryData.moveToNext();
			 bottomHeroes = (RelativeLayout) dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero);
			 bottomHeroes.setTag(matchHistoryData.getString(1));
			 bottomHeroes.setOnClickListener(new OnClickListener() {
				 
				 @Override
				 public void onClick(View v) {
					 showMatchDetails(v);
					 
				 }
			 });
			heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero_Name);
			heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero_Date);
		 	heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero_Time);
		 	heroPic = (ImageView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_TopHero_Image);
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
		 	heroPic.setImageBitmap(heroImageFinal);
		 	dateTimeHandler = new DateTimeHandler(matchHistoryData.getString(3));
		 	heroDate.setText(dateTimeHandler.getDate());
		 	heroTime.setText(dateTimeHandler.getTime());
		 	heroName.setText(heroDetails.getString(1));
		 }
		 //Circular Hero Bot
		 if(cursorCount > 4)
		 {	
			 matchHistoryData.moveToNext();
			 bottomHeroes = (RelativeLayout) dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero);
			 bottomHeroes.setTag(matchHistoryData.getString(1));
			 bottomHeroes.setOnClickListener(new OnClickListener() {
				 
				 @Override
				 public void onClick(View v) {
					 showMatchDetails(v);
					 
				 }
			 });	
			heroName = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero_Name);
			heroDate = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero_Date);
			heroTime = (TextView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero_Time);
			heroPic = (ImageView)dashboardFragment.findViewById(R.id.dashboardLayoutBot_HeroesBot_BotHero_Image);
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
		 	heroPic.setImageBitmap(heroImageFinal);
		 	dateTimeHandler = new DateTimeHandler(matchHistoryData.getString(3));
		 	heroDate.setText(dateTimeHandler.getDate());
		 	heroTime.setText(dateTimeHandler.getTime());
		 	heroName.setText(heroDetails.getString(1));
		 } 
		 matchHistoryData.close();
		 
	 }
	 
	 public void putSteamID(String steamID)
	 {
		 userSteamID = steamID;
	 }
	 
	 public void putUserInfoHandlerInstance(UserInfoHandler userInfoHandler)
	 {
		 this.userInfoHandler = userInfoHandler;
	 }
	 
	 public void putHeroListHandler(HeroListHandler heroListHandler)
	 {
		 this.heroListHandler = heroListHandler;
	 }
	 
	 public void putMatchHistoryHandler(MatchHistoryHandler matchHistoryHandler)
	 {
		 this.matchHistoryHandler = matchHistoryHandler;
	 }
	 
	 public void putImageFileHandler(ImageFileHandler imageFileHandler)
	 {
		 this.imageFileHandler = imageFileHandler;
	 }
	 
	 private class AssignDataToViews extends AsyncTask<Bitmap,Void,Bitmap>
	 {

		@Override
		protected Bitmap doInBackground(Bitmap... imageFromFile) {
			
			while(flagViewIsLoaded);
			
			return imageFromFile[0];
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			//After View is Loaded Call Assignment function
			refreshContent();
			if(result!=null)
			{setProfilePicture(result);}
			
		}
			
	 }
	 
	 public void initialLoadData(Bitmap imageFromFile)
	 {
		AssignDataToViews assignDataToViews = new AssignDataToViews();
		assignDataToViews.execute(imageFromFile);
	 }
	 
	 private void showMatchDetails(View v)
	 {
		 Intent matchDetailsIntent = new Intent(getActivity(),MatchDetails.class);
		 matchDetailsIntent.putExtra("match_ID",v.getTag().toString());
		 startActivity(matchDetailsIntent);
	 }
	
}
