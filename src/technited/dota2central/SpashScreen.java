package technited.dota2central;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class SpashScreen extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spash_screen);
		
		new Timer().schedule(new TimerTask() {          
		    @Override
		    public void run() {
		    	checkLaunchStatus();       
		    }
		}, 2000);

	}
	
	private void checkLaunchStatus()
	{
		
		Intent intent;
		LocalPreferences localPreferences = new LocalPreferences(SpashScreen.this);
		localPreferences.readFromPreferences();
		if(!localPreferences.getLoginStatus())
		{
			intent = new Intent(this,MainScreen.class);
		}
		else
		{
			intent = new Intent(this,SignUp.class);
		}
		startActivity(intent);
		finish();
	}

}
