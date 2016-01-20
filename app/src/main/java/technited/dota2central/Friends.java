package technited.dota2central;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Friends extends Fragment{
	
	private static String TAG="FriendsFragment";
	private boolean flagViewIsLoaded;
	private FriendsListAdapter friendsListAdapter;
	private List<FriendsListItem> friendsListItems;
	private ListView friendsListView; 
	private FriendListHandler friendListHandler;
	private View friendsFragment;
	private DateTimeHandler dateTimeHandler;
	private ImageFileHandler imageFileHandler;
	
	public Friends(){
		flagViewIsLoaded = false;
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
	        friendsFragment =inflater.inflate(R.layout.friends_fragment, container, false);
	        initializeViews();
	    	return friendsFragment;
	       
	    }
	 
	 private void initializeViews()
	 {
		 friendsListView = (ListView) friendsFragment.findViewById(R.id.friendsList);
		 friendsListItems = new ArrayList<FriendsListItem>();
		 flagViewIsLoaded = true;
	 }
	 
	 public void putFriendListHandler(FriendListHandler friendListHandler)
	 {
		 this.friendListHandler = friendListHandler;
	 }
	 
	 public void putImageFileHandler(ImageFileHandler imageFileHandler)
	 {
		 this.imageFileHandler = imageFileHandler;
	 }
	 
	 public void refreshContent()
	 {
		 if(flagViewIsLoaded)
		 {
			 if(friendsListItems.size() > 0)
			 {
				 friendsListItems = new ArrayList<FriendsListItem>();
			 }
			 List<FriendsListItem> inGame = new ArrayList<FriendsListItem>();
			 List<FriendsListItem> online = new ArrayList<FriendsListItem>();
			 List<FriendsListItem> offline = new ArrayList<FriendsListItem>();
			 Cursor friendsDataCursor = friendListHandler.getFriendList();
			 if(friendsDataCursor.getCount() > 0)
			 {
				 friendsDataCursor.moveToFirst();
				 while(!friendsDataCursor.isAfterLast())
				 {
					 Bitmap friendPic = null;
					 dateTimeHandler = new DateTimeHandler(friendsDataCursor.getString(2));
					 if(imageFileHandler.imageExists(friendsDataCursor.getString(0)))
					 {
						 friendPic = imageFileHandler.getImageFromMemory(friendsDataCursor.getString(0));
					 }
					 else
					 {
						 friendPic = BitmapFactory.decodeResource(getResources(),R.drawable.profile_pic_default);
					 }
					 FriendsListItem friendsListItem = new FriendsListItem(friendsDataCursor.getString(1), getLastLogoutDetails(dateTimeHandler.getAgoTime(),Integer.parseInt(friendsDataCursor.getString(8)), friendsDataCursor.getString(14)), friendsDataCursor.getString(0),friendPic);
					 if(friendsListItem.getFriendStatusText().charAt(0) == 'I')
					 {
						 inGame.add(friendsListItem);
					 }
					 else if(friendsListItem.getFriendStatusText().charAt(0) == 'l')
					 {
						 offline.add(friendsListItem);
					 }
					 else
					 {
						 online.add(friendsListItem);
					 }
					 friendsDataCursor.moveToNext();
				 }
				 friendsDataCursor.close();
				 friendsListItems.addAll(inGame);
				 friendsListItems.addAll(online);
				 friendsListItems.addAll(offline);
				 
				 friendsListAdapter = new FriendsListAdapter(getActivity(), R.layout.friends_list_row, friendsListItems);
				 friendsListView.setAdapter(friendsListAdapter);
				 friendsListView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View arg1, int position,
								long arg3) {
							FriendsListItem friendsListEntry = (FriendsListItem) parent.getAdapter().getItem(position);
							Intent friendProfileIntent = new Intent(getActivity(),FriendProfile.class);
							friendProfileIntent.putExtra("steam_ID", friendsListEntry.getFriendID());
							startActivity(friendProfileIntent);
							
						}
					});
				 Log.d(TAG,"Friends Fragment Loaded");
			 }
		 }
	 }
	 
	 private String[] getLastLogoutDetails(String lastLogoutTime, int personaState, String gameInfo)
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
		 			logoutColor = "logoutBlue";
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
		 return (new String[]{logoutTextToSet,logoutColor});
	 }
	 
	 private class AssignDataToViews extends AsyncTask<Integer,Void,Void>
	 {

		@Override
		protected Void doInBackground(Integer... arg0) {
			while(!flagViewIsLoaded);
			return null;
		}

		@Override
		protected void onPostExecute(Void params) {
			
			//After View is Loaded Call Assignment function
			refreshContent();
			
		}
			
	 }
	 
	 public void initialLoadData()
	 {
		AssignDataToViews assignDataToViews = new AssignDataToViews();
		assignDataToViews.execute();
	 }

}
