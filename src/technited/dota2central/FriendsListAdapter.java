package technited.dota2central;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FriendsListAdapter extends ArrayAdapter<FriendsListItem>{

	private Context context;
	private static String TAG = "FriendsListAdapter";
	private List<FriendsListItem> friendsListItems;
		
	private Typeface tf;
	
	public FriendsListAdapter(Context context, int resource,List<FriendsListItem> items) {
		super(context, resource);
		
		this.context = context;
		
		this.friendsListItems = items;
		
		Log.d(TAG,"Friends List Adapter Initialised");
		
	}

	private class FriendsViewHolder{
		
		CircularImageView friendPic;
		TextView friendName;
		TextView friendStatus;
	}
	
	@Override
	public int getCount() {
	      return friendsListItems.size();
	}
	@Override
	public FriendsListItem getItem(int position) {
	    return friendsListItems.get(position);
	}
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		FriendsViewHolder holder = null;
		FriendsListItem friendsListItem = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.friends_list_row, null);
			convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
			holder = new FriendsViewHolder();
			holder.friendName = (TextView) convertView.findViewById(R.id.friendsRowName);
			holder.friendPic = (CircularImageView) convertView.findViewById(R.id.friendsRowImage);
			holder.friendStatus = (TextView) convertView.findViewById(R.id.friendsRowState);
			String fontPath1 = "fonts/segoeui.ttf";
			String fontPath2 = "fonts/segoeuil.ttf";
			tf = Typeface.createFromAsset(this.context.getAssets(), fontPath1);
			holder.friendName.setTypeface(tf);
			tf = Typeface.createFromAsset(this.context.getAssets(), fontPath2);
			holder.friendStatus.setTypeface(tf);
			convertView.setTag(holder);
		} else 
			holder = (FriendsViewHolder) convertView.getTag();
				
		holder.friendName.setText(friendsListItem.getFriendName());
		holder.friendStatus.setText(friendsListItem.getFriendStatusText());
		int color = context.getResources().getIdentifier(friendsListItem.getFriendStatusColor(), "color", context.getPackageName());
		holder.friendStatus.setTextColor(Color.parseColor(context.getString(color)));
		holder.friendPic.setImageBitmap(friendsListItem.getFriendPic());
		
		return convertView;
	}
}
