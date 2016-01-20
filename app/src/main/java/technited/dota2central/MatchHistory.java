package technited.dota2central;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MatchHistory extends Activity {

	private TextView filterLabel, addFilterButton, editFilterButton, removeFilterButton;
	private String steam_ID;
	private MatchHistoryHandler matchHistoryHandler;
	private HeroListHandler heroListHandler;
	private MatchHistoryAdapter matchHistoryAdapter;
	private List<MatchHistoryListItem> matchHistoryList;
	private DateTimeHandler dateTimeHandler;
	private static final String heroPicFolder = "heroimages";
	private static final String pathSeperator = "/";
	private static final String imageExtension = ".png";
	private static final int heroPicWidth = 178;
	private static final int heroPicWidthCropped = 100;
	private static final int heroPicHeight = 100;
	private ImageEditor imageEditor;
	private ImageFileHandler imageFileHandler;
	private LocalPreferences localPreferences;
	
	private String filterListString;
	
	private ListView matchHistoryListView;
	
	private RelativeLayout heroFilterImages;
	
	private ProgressBar mProgressBar;
	private ImageButton refreshAllButton;
	
	private boolean firstFetch;
	
	private RelativeLayout matchHistoryContainer;
	
	//Debugging
	private static final String TAG = "MatchHistory";
	
	
	//Nav Drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SlidingMenuAdapter slidingMenuAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.match_history);
		initializeData();
		initialiseNavigationDrawer();
		initializeViews();
		setFonts();
		setDisplayList();

	}
	
	private void initializeData()
	{
		this.steam_ID = getIntent().getStringExtra("steam_ID");
	}
	
	public void refreshAllContent(View view)
	{
		setDisplayList();
	}
	
	private void initializeViews()
	{
		firstFetch = true;
		localPreferences = new LocalPreferences(MatchHistory.this);
		imageEditor = new ImageEditor();
		imageFileHandler = new ImageFileHandler(MatchHistory.this);
		filterLabel = (TextView) findViewById(R.id.matchHistory_FilterLabel);
		addFilterButton = (TextView) findViewById(R.id.matchHistory_addFilterButton);
		addFilterButton.setVisibility(View.VISIBLE);
		editFilterButton = (TextView) findViewById(R.id.matchHistory_editFilterButton);
		editFilterButton.setVisibility(View.INVISIBLE);
		removeFilterButton = (TextView) findViewById(R.id.matchHistory_removeFilterButton);
		removeFilterButton.setVisibility(View.INVISIBLE);
		mProgressBar = (ProgressBar) findViewById(R.id.loader_bar);
		mProgressBar.setVisibility(View.INVISIBLE);
		refreshAllButton = (ImageButton) findViewById(R.id.refreshContent);
		refreshAllButton.setVisibility(View.VISIBLE);
		matchHistoryContainer = (RelativeLayout) findViewById(R.id.matchHistoryContainer);
		heroFilterImages = (RelativeLayout) findViewById(R.id.matchHistory_FilterImages);
		matchHistoryHandler = new MatchHistoryHandler(MatchHistory.this);
		heroListHandler = new HeroListHandler(MatchHistory.this);
		matchHistoryList = new ArrayList<MatchHistoryListItem>();
		matchHistoryListView = (ListView) findViewById(R.id.matchHistory_matchHistoryList);
		matchHistoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View arg1, int position,
					long arg3) {
				MatchHistoryListItem matchHistoryEntry = (MatchHistoryListItem) parent.getAdapter().getItem(position);
				Intent matchDetailsIntent = new Intent(MatchHistory.this,MatchDetails.class);
				matchDetailsIntent.putExtra("match_ID", matchHistoryEntry.getMatchID());
				startActivity(matchDetailsIntent);
				
			}
		});
	}
	
	private void initialiseNavigationDrawer()
    {
    	 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_3);
    	 mDrawerList = (ListView) findViewById(R.id.left_drawer_3);
    	 slidingMenuAdapter = new SlidingMenuAdapter(MatchHistory.this, R.layout.sliding_menu_row);
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
 							Intent dashboard = new Intent(MatchHistory.this,MainScreen.class);
 							dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
 							startActivity(dashboard);
 							finish();
 						}
 						break;
 					case 3:{
						mDrawerLayout.closeDrawer(mDrawerList);
						Intent settingsIntent = new Intent(MatchHistory.this,Settings.class);
						startActivity(settingsIntent);
					}
					break;
 					case 4:
 						{
 							mDrawerLayout.closeDrawer(mDrawerList);
 							localPreferences.clearPreferences();
 							MatchHistory.this.deleteDatabase("dota2Central");
 							disableNotificationReceiver();
 							Intent signOut = new Intent(MatchHistory.this,SignUp.class);
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
	
	private void setFonts()
	{

		 Typeface tf;
		 String fontPath1 = "fonts/OpenSans-Semibold.ttf";
		 String fontPath2 = "fonts/segoeuil.ttf";
		 
		 //User Info
		 tf = Typeface.createFromAsset(getAssets(), fontPath1);
	     filterLabel.setTypeface(tf);
	     tf = Typeface.createFromAsset(getAssets(), fontPath2);
	     addFilterButton.setTypeface(tf);
	     editFilterButton.setTypeface(tf);
	     removeFilterButton.setTypeface(tf);
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
	
	private void setDisplayList()
	{
		if(firstFetch)
		{
			matchHistoryContainer.setVisibility(View.INVISIBLE);
		}
		else
		{
			matchHistoryList = new ArrayList<MatchHistoryListItem>();
		}
		refreshAllButton.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);
		buildHistoryList(fetchHistory());
		if(firstFetch)
		{
			setListAdapter();
			matchHistoryContainer.setVisibility(View.VISIBLE);
		}
		else
		{
			updateListAdapter();
		}
		refreshAllButton.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.INVISIBLE);
	}
	
	private Cursor fetchHistory()
	{
		Cursor matchHistoryCursor = matchHistoryHandler.getMatchHistory(steam_ID, null);
		return matchHistoryCursor;
	}
	
	private void buildHistoryList(Cursor matchHistoryCursor)
	{
		matchHistoryCursor.moveToFirst();
		while(!matchHistoryCursor.isAfterLast())
		{
			Cursor heroCursor = heroListHandler.getHero(matchHistoryCursor.getString(6));
			heroCursor.moveToFirst();
			dateTimeHandler = new DateTimeHandler(matchHistoryCursor.getString(3));
			Bitmap heroPicBitmapFinal = null;
			Bitmap heroPicBitmap = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroCursor.getString(2) + imageExtension);
	        if(heroPicBitmap != null)
	        {
	        	Bitmap heroPicBitmapScaled = imageEditor.setImageSize(heroPicHeight, heroPicWidth, heroPicBitmap);
	        	heroPicBitmapFinal = imageEditor.getCroppedImage(heroPicHeight, heroPicWidthCropped, heroPicHeight, heroPicWidth, heroPicBitmapScaled);
	        }
	        else
	        {
	        	heroPicBitmapFinal = BitmapFactory.decodeResource(getResources(),R.drawable.dark_grey_back);
	        }
			MatchHistoryListItem matchHistoryListItem = new MatchHistoryListItem(heroCursor.getString(1),dateTimeHandler.getDate()+","+dateTimeHandler.getTime(),heroPicBitmapFinal,matchHistoryCursor.getString(1));
			Log.d(TAG, matchHistoryListItem.getHeroName());
			matchHistoryList.add(matchHistoryListItem);
			heroCursor.close();
			matchHistoryCursor.moveToNext();
		}
		matchHistoryCursor.close();
	}
	
	private void setListAdapter()
	{
		matchHistoryAdapter = new MatchHistoryAdapter(MatchHistory.this, R.layout.match_history_list_row, matchHistoryList);
		matchHistoryListView.setAdapter(matchHistoryAdapter);
		firstFetch = false;
	}
	
	private void updateListAdapter()
	{
		matchHistoryAdapter.notifyDataSetChanged();
	}
	
	public void clearFilters()
	{
		matchHistoryAdapter.clearFilters(matchHistoryList);
		heroFilterImages.removeAllViews();
	}
	
	public void editFilters(View view)
	{
		Intent heroFilterIntent = new Intent(this,HeroFilter.class);
		heroFilterIntent.putExtra("filterListString", filterListString);
		startActivityForResult(heroFilterIntent,02);
	}
	
	public void removeFilters(View view)
	{
		clearFilters();
		editFilterButton.setVisibility(View.INVISIBLE);
		removeFilterButton.setVisibility(View.INVISIBLE);
		addFilterButton.setVisibility(View.VISIBLE);
	}
	
	public void addFilters(View view)
	{
		Intent heroFilterIntent = new Intent(this,HeroFilter.class);
		startActivityForResult(heroFilterIntent,02);
	}
	
	public void setFilterImages()
	{
		String[] filterList = filterListString.split(",");
		Cursor heroListCursor = heroListHandler.getAllHeroes();
		heroListCursor.moveToFirst();
		int index=0;
		while(!heroListCursor.isAfterLast())
		{
			boolean assignFlag = false;
			for(String filterName : filterList)
			{
				if(filterName.equals(heroListCursor.getString(1)))
				{
					assignFlag = true;
				}
			}
			if(assignFlag)
			{
				Log.d(TAG,heroListCursor.getString(1));
				RoundedImageView heroFilterImage = new RoundedImageView(MatchHistory.this);
				heroFilterImage.setVisibility(View.VISIBLE);
				Bitmap heroPicBitmapFinal = null;
				Bitmap heroPicBitmap = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroListCursor.getString(2) + imageExtension);
		        if(heroPicBitmap != null)
		        {
		        	Bitmap heroPicBitmapScaled = imageEditor.setImageSize(heroPicHeight, heroPicWidth, heroPicBitmap);
		        	heroPicBitmapFinal = imageEditor.getCroppedImage(heroPicHeight, heroPicWidthCropped, heroPicHeight, heroPicWidth, heroPicBitmapScaled);
		        }
		        else
		        {
		        	heroPicBitmapFinal = BitmapFactory.decodeResource(getResources(),R.drawable.dark_grey_back);
		        }
				RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(convertDpToPixel(50, MatchHistory.this), convertDpToPixel(50, MatchHistory.this));
				mParams.addRule(RelativeLayout.RIGHT_OF,index);
				Log.d(TAG,"Right");
				index++;
				heroFilterImage.setLayoutParams(mParams);
				heroFilterImage.setImageBitmap(heroPicBitmapFinal);
				heroFilterImage.setId(index);
				Log.d(TAG,"Index:"+index);
				heroFilterImages.addView(heroFilterImage);
			}
			heroListCursor.moveToNext();
		}
	}
	
	private int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = (int)(dp * (metrics.densityDpi / 160f));
        return px;
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 02)
		{
			if (resultCode == RESULT_OK) {
				Bundle res = data.getExtras();
				filterListString = res.getString("filterListString");
				Log.d(TAG, "Filter:"+filterListString);
				clearFilters();
				if(filterListString.length() > 0)
				{
					matchHistoryAdapter.getFilter().filter(filterListString);
					setFilterImages();
					editFilterButton.setVisibility(View.VISIBLE);
					removeFilterButton.setVisibility(View.VISIBLE);
					addFilterButton.setVisibility(View.INVISIBLE);
				}
				else
				{
					editFilterButton.setVisibility(View.INVISIBLE);
					removeFilterButton.setVisibility(View.INVISIBLE);
					addFilterButton.setVisibility(View.VISIBLE);
				}
				
			}
		}
	}
}
