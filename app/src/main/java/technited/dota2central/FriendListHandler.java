package technited.dota2central;

import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class FriendListHandler {

	
	private static final String TAG = "FriendListHandler";
	private static final int friendFlag = 1;
	private JSONArray data;
	private JSONObject playerData;
	private Context context;
	private LocalDB localDB;
	private ImageFileHandler imageFileHandler;
	
	FriendListHandler(Context context) {
		
		this.context = context;
		localDB = new LocalDB(this.context);
		
	}
	
	public void assignJSON(JSONArray data)
	{
		this.data = data;
	}
	
	public void putImageFileHandler(ImageFileHandler imageFileHandler)
	{
		this.imageFileHandler = imageFileHandler;
	}
	
	public Cursor getFriendList()
	{
		return localDB.getFriendList();
	}
	
	public void putFriendData(JSONObject data)
	{
		try {
			this.playerData = data.getJSONObject("player_data");
			this.localDB.updateUserInfo(this.playerData, friendFlag);
			String steam_ID = this.playerData.getString("steam_id");
			Cursor playerDataCursor = localDB.getUserInfo(steam_ID);
			playerDataCursor.moveToFirst();
			if((!this.playerData.getString("avatar_full").equals(playerDataCursor.getString(7))) || (!checkImageInFile(steam_ID)))
			{
				DownloadImageTask downloadImageTask = new DownloadImageTask();
				downloadImageTask.execute(new String[]{this.playerData.getString("avatar_full"),steam_ID});
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public Cursor getFriendData(String steam_ID)
	{
		return this.localDB.getUserInfo(steam_ID);
	}
	
	public boolean putFriendList()
	{
		boolean returnFlag = false;
		for(int i=0;i<data.length();i++)
		{
			String currentImageURL = null;
			String steam_ID = null;
			try {
				this.playerData = this.data.getJSONObject(i).getJSONObject("player_data");
				steam_ID = this.playerData.getString("steam_id");
				Cursor playerDataCursor = localDB.getUserInfo(steam_ID);
				playerDataCursor.moveToFirst();
				if(playerDataCursor.getCount() == 0)
				{
					if(localDB.insertUserInfo(this.playerData, friendFlag))
					{
						currentImageURL = this.playerData.getString("avatar_full");
						returnFlag = true;
					}
				}
				else
				{
					Log.d(TAG,"ELSE");
					localDB.updateUserInfo(this.playerData, friendFlag);
					if((!this.playerData.getString("avatar_full").equals(playerDataCursor.getString(7))) || (!checkImageInFile(steam_ID)))
					{
						currentImageURL = this.playerData.getString("avatar_full");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(currentImageURL != null)
			{
				DownloadImageTask downloadImageTask = new DownloadImageTask();
				downloadImageTask.execute(new String[]{currentImageURL,steam_ID});
			}
		}
		
		return returnFlag;
	}
	
	
private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		private String steam_ID;
	    @Override
		protected Bitmap doInBackground(String... imageURL) {
	    	steam_ID = imageURL[1];
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
			putImageInFile(downloadedImage, steam_ID);
		 }
	}

private boolean checkImageInFile(String steam_ID)
{
	return imageFileHandler.imageExists(steam_ID);
}

private void putImageInFile(Bitmap image, String steam_ID)
{
	if(image!=null)
	{
		imageFileHandler.storeImageInMemory(image, steam_ID);
		if(context instanceof FriendProfile)
		{
			FriendProfile currentFriendProfile = (FriendProfile)context;
			currentFriendProfile.setProfilePicture(image);
		}
		
	}
}
	
}
