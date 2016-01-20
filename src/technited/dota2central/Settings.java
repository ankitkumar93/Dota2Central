package technited.dota2central;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.AdapterView.OnItemClickListener;
public class Settings extends Activity {
	
	private LocalPreferences localPreferences;
	private Switch notificationToggle;
	
	//Nav Bar
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private SlidingMenuAdapter slidingMenuAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		initializeView();
		initializeNavigationBar();
		
	}
	
	private void initializeView()
	{
		localPreferences = new LocalPreferences(Settings.this);
		notificationToggle = (Switch) findViewById(R.id.settingsNotification_Toggle);
		localPreferences.readFromPreferences();
		
		//Init Toggle Switch
		notificationToggle.setChecked(localPreferences.getNotificationStatus());
	}
	
	private void initializeNavigationBar()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_4);
   	 	mDrawerList = (ListView) findViewById(R.id.left_drawer_4);
   	 	slidingMenuAdapter = new SlidingMenuAdapter(Settings.this, R.layout.sliding_menu_row);
   	 	mDrawerList.setAdapter(slidingMenuAdapter);
   	 	mDrawerList.setItemChecked(3, true);
   	 	mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch(position)
				{
					case 1:{
						mDrawerLayout.closeDrawer(mDrawerList);
						Intent dashboard = new Intent(Settings.this,MainScreen.class);
						dashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
						startActivity(dashboard);
						finish();
					}
						break;
						
					case 3:{
						mDrawerLayout.closeDrawer(mDrawerList);
					}
					break;
					case 4:
						{
							mDrawerLayout.closeDrawer(mDrawerList);
							localPreferences.clearPreferences();
							Settings.this.deleteDatabase("dota2Central");
							disableNotificationReceiver();
							Intent signOut = new Intent(Settings.this,SignUp.class);
							signOut.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
							startActivity(signOut);
							finish();
						}
						break;
				}
			}
		});
	}
	
	public void notificationSettingsToggle(View view)
	{
		boolean on = ((Switch) view).isChecked();
		localPreferences.putNotificationStatus(on);
		if(on){
			enableNotificationReceiver();
		}
		else{
			disableNotificationReceiver();
		}
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

}
