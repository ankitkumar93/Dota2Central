package technited.dota2central;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class UserInfoHandler {
	private static final String TAG = "UserInfoHandler";
	private static final int friendFlag = 0;
	private JSONObject data;
	private JSONObject playerData;
	private Context context;
	private LocalDB localDB;
	UserInfoHandler(Context context)
	{
		this.context = context;
		localDB = new LocalDB(this.context);
	}
	
	public void assignJSON(JSONObject data)
	{
		this.data = data;
		try {
			this.playerData = this.data.getJSONObject("player_data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public boolean putData(boolean loginStatus)
	{
		DownloadImageTask downloadImageTask = new DownloadImageTask();
		String currentImageURL = null;
		
		try {
			currentImageURL = this.playerData.getString("avatar_full");
		} catch (JSONException e) {
			Log.d(TAG,"Error Getting URL from JSON");
		}
		if(loginStatus)
		{
			if(localDB.insertUserInfo(this.playerData,friendFlag));
			{
				downloadImageTask.execute(currentImageURL);
				return true;
			}
		}
		else
		{
			localDB.updateUserInfo(this.playerData,friendFlag);
			downloadImageTask.execute(currentImageURL);
		}
		return false;
	}
	
	public Cursor getData(String userSteamID)
	{
		return localDB.getUserInfo(userSteamID);
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		
	    @Override
		protected Bitmap doInBackground(String... imageURL) {
	    	Log.d(TAG, imageURL[0]);
	    	Bitmap downloadedImage = null;
		    try {
		        InputStream in = new java.net.URL(imageURL[0]).openStream();
		        downloadedImage = BitmapFactory.decodeStream(in);
		    } catch (Exception e) {
		        Log.d(TAG,"Error Downloading Image",e);
		    }
		    return downloadedImage;
		}

		protected void onPostExecute(Bitmap downloadedImage) {
			MainScreen currentMainScreen = (MainScreen)context;
			currentMainScreen.refreshProfilePic(downloadedImage);
		 }
	}
}
