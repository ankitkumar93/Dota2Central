package technited.dota2central;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.ListView;

public class Notification extends Fragment{
	private View notificationFragment;
	boolean flagViewIsLoaded;
	private NotificationHandler notificationHandler;
	private DateTimeHandler dateTimeHandler;
	private ImageFileHandler imageFileHandler;
	
	private List<NotificationListItem> notificationList;
	private NotificationAdapter notificationAdapter;
	private ListView notificationListView;
	
	private static final String TAG="NotificationFragment";
	
	public Notification(){
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
	        notificationFragment =inflater.inflate(R.layout.notification_fragment, container, false);
	        initializeViews();
	    	return notificationFragment;
	       
	    }
	 
	 private void initializeViews()
	 {
		 notificationListView = (ListView) notificationFragment.findViewById(R.id.notificationList);
		 notificationList = new ArrayList<NotificationListItem>();
		 flagViewIsLoaded = true;
	 }
	 
	 
	 public void putNotificationHandler(NotificationHandler notificationHandler)
	 {
		 this.notificationHandler = notificationHandler;
	 }
	 
	 public void putImageFileHandler(ImageFileHandler imageFileHandler)
	 {
		 this.imageFileHandler = imageFileHandler;
	 }
	 
	 public void refreshContent()
	 {
		 if(flagViewIsLoaded)
		 {
			 if(notificationList.size() > 0)
			 {
				 notificationList = new ArrayList<NotificationListItem>();
			 }
			 int countNewNotifications = 0;
			 Cursor notificationCursor = notificationHandler.getNotifications();
			 Log.d(TAG,""+notificationCursor.getCount());
			 if(notificationCursor.getCount() > 0)
			 {
				 notificationCursor.moveToFirst();
				 while(!notificationCursor.isAfterLast())
				 {
					 dateTimeHandler = new DateTimeHandler(notificationCursor.getString(1));
					 String dateTime = dateTimeHandler.getAgoTime();
					 if(dateTime.equals("0 minutes ago"))
					 {
						 dateTime = "just now";
						 countNewNotifications++;
					 }
					 Bitmap userImage = null;
					 if(imageFileHandler.imageExists(notificationCursor.getString(0)))
					 {
						 userImage = imageFileHandler.getImageFromMemory(notificationCursor.getString(0));
					 }	
					 else
					 {
						 userImage = BitmapFactory.decodeResource(getResources(),R.drawable.profile_pic_default);
					 }
					 NotificationListItem notificationListItem = new NotificationListItem(notificationCursor.getString(2), dateTime, userImage);
					 notificationList.add(notificationListItem);						 
					 notificationCursor.moveToNext();
				 }	
			 }
			 notificationCursor.close();
			 notificationAdapter = new NotificationAdapter(getActivity(), R.layout.notification_list_row, notificationList);
			 notificationListView.setAdapter(notificationAdapter);
			 Activity currentActivity = getActivity();
			 if(currentActivity instanceof MainScreen)
			 {
				 ((MainScreen)currentActivity).updateNotificationCount(countNewNotifications);
			 }
		 }
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
