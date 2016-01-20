package technited.dota2central;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class NotificationReciever extends BroadcastReceiver{
	
	private static final String TAG = "NotificationReciever";
	private URLConstructor urlConstructor;
	private LocalPreferences localPreferences;
	private NotificationHandler notificationHandler;

	@Override
	public void onReceive(Context context, Intent arg1) {
		Log.d(TAG,"Recived!");
		localPreferences = new LocalPreferences(context);
		localPreferences.readFromPreferences();
		urlConstructor = new URLConstructor(context);
		notificationHandler = new NotificationHandler(context);
		GetNotifications getNotifications = new GetNotifications();
		getNotifications.execute(urlConstructor.getFriendListURL(localPreferences.getSteamID()));
	}
	
	public void putNotifications(JSONArray jsonArray)
	{
		notificationHandler.putJSONContent(jsonArray);
		notificationHandler.putNotifications();
	}
	
	public boolean isAppForground(Context mContext) {

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }

        return true;
    }
	
	private class GetNotifications extends AsyncTask<String,Void,JSONObject>
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
			if(jsonObject != null)
			{
				JSONObject response = null;
				try {
					response = jsonObject.getJSONObject(TAG_RESPONSE);
					if(response == null)
					{
						return;
					}
					else if(response.getString(TAG_RESULT).equals(TAG_SUCCESS))
					{
						putNotifications(response.getJSONArray(TAG_DATA));
					}
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		
		}

	}
}
