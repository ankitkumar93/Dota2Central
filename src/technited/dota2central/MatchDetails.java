package technited.dota2central;

import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MatchDetails extends FragmentActivity implements NotesAlertDialog.NotesDialogListener{
	
	private static final String TAG = "MatchDetails";
	private String match_ID;

	//Nav Drawer
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SlidingMenuAdapter slidingMenuAdapter;
	
	//Fragments
	private MatchDetail matchDetailFragment;
	private MapStatus mapStatusFragment;
	private Statistics statisticsFragment;
	
	//View Pager
	private PageAdapter_2 mPageAdapter;
	private ViewPager mPager;
	
	//Libraries
	private URLConstructor urlConstructor;
	private MatchDetailsHandler matchDetailsHandler;
	private HeroListHandler heroListHandler;
	private ItemListHandler itemListHandler;
	private ImageFileHandler imageFileHandler;
	private NotesAlertDialog notesAlertDialog;
	private NotesHandler notesHandler;
	private LocalPreferences localPreferences;
	
	//Progress Dialog
	private ProgressDialog mProgressDialog;
	
	//Layouts
	private RelativeLayout mainContainer;
	
	
	//Variables

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_details);
		match_ID = getIntent().getStringExtra("match_ID");
		initialiseNavigationDrawer();
        initialiseFragments();
        initialisePaging();
        startLoading();
	}
	
	private void initialiseNavigationDrawer()
    {
    	 mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_2);
    	 mDrawerList = (ListView) findViewById(R.id.left_drawer_2);
    	 slidingMenuAdapter = new SlidingMenuAdapter(MatchDetails.this, R.layout.sliding_menu_row);
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
 							Intent dashboard = new Intent(MatchDetails.this,MainScreen.class);
 							dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
 							startActivity(dashboard);
 							finish();
 						}
 						break;
 					case 3:{
						mDrawerLayout.closeDrawer(mDrawerList);
						Intent settingsIntent = new Intent(MatchDetails.this,Settings.class);
						startActivity(settingsIntent);
					}
					break;
 					case 4:
 						{
 							mDrawerLayout.closeDrawer(mDrawerList);
 							localPreferences.clearPreferences();
 							MatchDetails.this.deleteDatabase("dota2Central");
 							disableNotificationReceiver();
 							Intent signOut = new Intent(MatchDetails.this,SignUp.class);
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
	
	private void initialiseFragments()
	{
		//Fragments
		matchDetailFragment = new MatchDetail();
		mapStatusFragment = new MapStatus();
		statisticsFragment = new Statistics();
		
		//Libraries
		localPreferences = new LocalPreferences(MatchDetails.this);
		urlConstructor = new URLConstructor(MatchDetails.this);
		matchDetailsHandler = new MatchDetailsHandler(MatchDetails.this);
		heroListHandler = new HeroListHandler(MatchDetails.this);
		itemListHandler = new ItemListHandler(MatchDetails.this);
		imageFileHandler = new ImageFileHandler(MatchDetails.this);
		notesHandler = new NotesHandler(MatchDetails.this);
		
		//Elements
		
		//Assign Data to Fragments
		matchDetailFragment.putMatchID(match_ID);
		matchDetailFragment.putMatchDetailsHandler(matchDetailsHandler);
		matchDetailFragment.putHeroListHandler(heroListHandler);
		matchDetailFragment.putItemListHandler(itemListHandler);
		matchDetailFragment.putImageFileHandler(imageFileHandler);
		
		mapStatusFragment.putMatchID(match_ID);
		mapStatusFragment.putMatchDetailHandler(matchDetailsHandler);
		
		statisticsFragment.putMatchID(match_ID);
		statisticsFragment.putMatchDetailsHandler(matchDetailsHandler);
		statisticsFragment.putHeroListHandler(heroListHandler);
		statisticsFragment.putImageFileHandler(imageFileHandler);
		
		
		//Layouts
		mainContainer = (RelativeLayout) findViewById(R.id.main_contatiner_2);
	}
	
	private void initialisePaging() {
        List<Fragment> fragments = new Vector<Fragment>();
        fragments.add(matchDetailFragment);
        fragments.add(mapStatusFragment);
        fragments.add(statisticsFragment);
        this.mPageAdapter  = new PageAdapter_2(super.getSupportFragmentManager(), fragments);
        mPager = (ViewPager)super.findViewById(R.id.pager_2);
        mPager.setAdapter(this.mPageAdapter); 
        mPager.setOffscreenPageLimit(2);
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
	 
	 private void startLoading()
	 {
		 Cursor matchDetailCursor = matchDetailsHandler.getMatchDetails(match_ID);
		 if(matchDetailCursor.getCount() > 0)
		 {
			 Log.d(TAG,"Already Have In DB");
			 matchDetailFragment.loadMatchDetails();
			 mapStatusFragment.loadMapStatus();
			 statisticsFragment.loadStatistics();
		 }
		 else
		 {
			 mainContainer.setVisibility(View.INVISIBLE);
			 mProgressDialog = ProgressDialog.show(MatchDetails.this, "Loading", "Please wait while We Load the Match Detail");
			 GetAPIResult getAPIResult = new GetAPIResult();
			 getAPIResult.execute(urlConstructor.getMatchDetailsURL(match_ID));
		 }
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
				JSONObject response = null;
				try {
					response = jsonObject.getJSONObject(TAG_RESPONSE);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(response.getString(TAG_RESULT).equals(TAG_SUCCESS))
					{
						Log.d(TAG,""+response.getJSONObject(TAG_DATA));
						putMatchDetails(response.getJSONObject(TAG_DATA));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.d(TAG,"JSON Exception",e);
				}
			}
		}
	 
	 private void putMatchDetails(JSONObject jsonObject)
	 {
		 matchDetailsHandler.assignJSON(jsonObject);
		 if(matchDetailsHandler.putMatchDetails(match_ID))
		 {
			 Log.d(TAG,"Fetched Successfully");
			 mProgressDialog.dismiss();
			 mainContainer.setVisibility(View.VISIBLE);
			 matchDetailFragment.loadMatchDetails();
			 mapStatusFragment.loadMapStatus();
			 statisticsFragment.loadStatistics();
		 }
		 else
		 {
			 Log.d(TAG,"Failed to Insert In DB");
		 }
	 }
	 
	 //Map Status Button Clicks
	 
	 public void enableDoodle(View view)
	 {
		 mapStatusFragment.enableDoodle(view);
	 }
	 public void clearDoodle(View view)
	 {
		 mapStatusFragment.clearDoodle(view);
	 }
	 public void exitDoodle(View view)
	 {
		 mapStatusFragment.exitDoodle(view);
	 }
	 public void shareImage(View view)
	 {
		 mapStatusFragment.shareImage(view);
	 }
	 
	 //Notes Button Click
	 public void addNotes(View view)
	 {
		notesAlertDialog = new NotesAlertDialog();
		Cursor notesCursor = notesHandler.getNote(match_ID);
		String noteContentString = "";
		if(notesCursor.getCount() > 0)
		{
			notesCursor.moveToFirst();
			noteContentString = notesCursor.getString(1);
		}
		Bundle args = new Bundle();
		args.putString("noteContent",noteContentString);
		notesAlertDialog.setArguments(args);
		notesAlertDialog.show(getFragmentManager(), "NotesAlertDialog");
	 }

	@Override
	public void onNoteAccepted(String newNoteContent) {
		
		Log.d(TAG,"Note Accepted: " + newNoteContent);
		notesAlertDialog.dismiss();
		
		if(notesHandler.putNote(match_ID, newNoteContent))
		{
			Toast success = Toast.makeText(MatchDetails.this, "Successfully added note", Toast.LENGTH_SHORT);
			success.show();
		}
		else
		{
			Toast failure = Toast.makeText(MatchDetails.this, "Failed to add note", Toast.LENGTH_SHORT);
			failure.show();
		}
	}

	@Override
	public void onNoteCancelled() {
		Log.d(TAG,"Note Cancelled");
		notesAlertDialog.dismiss();
		
	}
}
