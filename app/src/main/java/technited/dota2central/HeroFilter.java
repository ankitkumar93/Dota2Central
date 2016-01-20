package technited.dota2central;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
public class HeroFilter extends FragmentActivity {

	
	//TAG
	private static final String TAG = "HeroFilter";
	
	private TextView filterLabel;
	private HeroFilterSelection heroFilterSelectionFragment;
	private HeroFilterSelected heroFilterSelectedFragment;
	private ImageFileHandler imageFileHandler;
	private HeroListHandler heroListHandler;
	private ImageEditor imageEditor;
	private List<HeroListItem> selectedList;
	private List<HeroListItem> selectionList;
	
	private String filterString;
	
	static final String heroPicFolder = "heroimages";
	private static final String pathSeperator = "/";
	private static final String imageExtension = ".png";
	private int heroPicHeight = 100;
	private int heroPicWidth = 178;
	private int heroPicWidthCropped = 100;
	
	private LocalPreferences localPreferences;
	
	//View Pager
	private PageAdapter_3 mPagerAdapter;
	private ViewPager mPager;
	
	//Nav Drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SlidingMenuAdapter slidingMenuAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hero_filter);
		loadViews();
		initialiseNavigationDrawer();
		setFonts();
		loadContent();
	}
	
	private void loadViews()
	{
		//Fragments
		heroFilterSelectionFragment = new HeroFilterSelection();
		heroFilterSelectedFragment = new HeroFilterSelected();
		
		//Views
		filterLabel = (TextView) findViewById(R.id.heroFilterActivity_filterLabel);
		
		//Libraries
		imageFileHandler = new ImageFileHandler(HeroFilter.this);
		heroListHandler = new HeroListHandler(HeroFilter.this);
		imageEditor = new ImageEditor();
		localPreferences = new LocalPreferences(HeroFilter.this);
		
		//Variable
		selectedList = new ArrayList<HeroListItem>();
		selectionList = new ArrayList<HeroListItem>();
		
		filterString = getIntent().getStringExtra("filterListString");
	}
	
	private void setFonts()
	{
		Typeface tf;
		String fontPath1 = "fonts/OpenSans-Semibold.ttf";
		tf=Typeface.createFromAsset(getResources().getAssets(), fontPath1);
		filterLabel.setTypeface(tf);
	}
	
	private void initialiseNavigationDrawer()
    {
    	 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_3);
    	 mDrawerList = (ListView) findViewById(R.id.left_drawer_3);
    	 slidingMenuAdapter = new SlidingMenuAdapter(HeroFilter.this, R.layout.sliding_menu_row);
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
 							Intent dashboard = new Intent(HeroFilter.this,MainScreen.class);
 							dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
 							startActivity(dashboard);
 							finish();
 						}
 						break;
 					case 3:{
						mDrawerLayout.closeDrawer(mDrawerList);
						Intent settingsIntent = new Intent(HeroFilter.this,Settings.class);
						startActivity(settingsIntent);
					}
					break;
 					case 4:
 						{
 							mDrawerLayout.closeDrawer(mDrawerList);
 							localPreferences.clearPreferences();
 							HeroFilter.this.deleteDatabase("dota2Central");
 							disableNotificationReceiver();
 							Intent signOut = new Intent(HeroFilter.this,SignUp.class);
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
	
	private void loadContent()
	{
		Cursor heroListCursor = heroListHandler.getAllHeroes();
		heroListCursor.moveToFirst();
		while(!heroListCursor.isAfterLast())
		{
			Log.d(TAG,heroListCursor.getString(2));
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
			HeroListItem heroListItem = new HeroListItem(heroListCursor.getString(1), heroPicBitmapFinal);
			if(filterString != null)
			{
				if(filterString.length() > 0)
				{
					boolean selectedFlag = false;
					String[] selectedFilterList = filterString.split(",");
					for(String currentSelection : selectedFilterList)
					{
						if(heroListItem.getHeroName().equals(currentSelection))
						{
							selectedList.add(heroListItem);
							selectedFlag = true;
						}
					}
					if(!selectedFlag)
					{
						selectionList.add(heroListItem);
					}
				}
				else
				{
					selectionList.add(heroListItem);
				}
			}
			else
			{
				selectionList.add(heroListItem);
			}
			heroListCursor.moveToNext();
		}
		initialisePaging();
		heroFilterSelectionFragment.putSelectionList(selectionList);
		heroFilterSelectionFragment.initLoad();
		heroFilterSelectedFragment.putSelectedList(selectedList);
		heroFilterSelectedFragment.initLoad();
	}
	
	private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(heroFilterSelectionFragment);
        fragments.add(heroFilterSelectedFragment);
        this.mPagerAdapter  = new PageAdapter_3(super.getSupportFragmentManager(), fragments);
        mPager = (ViewPager)super.findViewById(R.id.pager_3);
        mPager.setAdapter(this.mPagerAdapter); 
        mPager.setOffscreenPageLimit(1);
    }
	
	public void updateSelectionList(HeroListItem heroListItem,List<HeroListItem>selectionList)
	{
		this.selectionList = selectionList;
		this.selectedList = heroFilterSelectedFragment.addItem(heroListItem);
	}
	
	public void updateSelectedList(HeroListItem heroListItem,List<HeroListItem>selectedList)
	{
		this.selectedList = selectedList;
		this.selectionList = heroFilterSelectionFragment.addItem(heroListItem);
	}
	
	public void finishSelection(View view)
	{
		List<String> filterList = new ArrayList<String>();
		for(int index=0;index<selectedList.size();index++)
		{
			filterList.add(selectedList.get(index).getHeroName());
		}
		String tempFilterString = filterList.toString();
		String filerListString = tempFilterString.substring(1, tempFilterString.length() - 1).replace(", ", ",");
		Intent resultIntent = new Intent();
		resultIntent.putExtra("filterListString", filerListString);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
