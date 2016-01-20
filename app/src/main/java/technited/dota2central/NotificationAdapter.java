package technited.dota2central;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationAdapter extends ArrayAdapter<NotificationListItem>{

	private List<NotificationListItem> notificationListItem;
	private Context context;
	public NotificationAdapter(Context context, int resource, List<NotificationListItem> item) {
		super(context, resource, item);
		this.notificationListItem = item;
		this.context = context;
	}
	
	@Override
	public int getCount() {
	      return notificationListItem.size();
	}
	@Override
	public NotificationListItem getItem(int position) {
	    return super.getItem(position);
	}
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	private class ViewHolder{
		TextView info;
		TextView date;
		RoundedImageView pic;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		
		NotificationListItem currentItem = getItem(position);
		
		Typeface tf;
		
		String fontPath1 = "fonts/segoeui.ttf";
		String fontPath2 = "fonts/segoeuil.ttf";
		
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.notification_list_row, null);
			convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
			holder.info = (TextView) convertView.findViewById(R.id.notificationList_info);
			holder.date = (TextView) convertView.findViewById(R.id.notificationList_date);
			holder.pic = (RoundedImageView) convertView.findViewById(R.id.notificationList_Pic);
			//Fonts
			tf = Typeface.createFromAsset(this.context.getAssets(), fontPath1);
			holder.info.setTypeface(tf);
			tf = Typeface.createFromAsset(this.context.getAssets(), fontPath2);
			holder.date.setTypeface(tf);
			
			convertView.setTag(holder);
		}else
		{
			holder = (ViewHolder)convertView.getTag();
		}
		//Data
		holder.pic.setImageBitmap(currentItem.getPic());
		holder.date.setText(currentItem.getDate());
		holder.info.setText(currentItem.getInfo());
		
		
		return convertView;
	}

}
