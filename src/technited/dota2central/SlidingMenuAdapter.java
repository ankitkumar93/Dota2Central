package technited.dota2central;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SlidingMenuAdapter extends ArrayAdapter<SlidingMenuItems>{

	private Context context;
	private static String TAG = "SlidingMenuAdapter";
	
	public SlidingMenuAdapter(Context context, int resource) {
		super(context, resource);
		
		this.context = context;
		
		Log.d(TAG,"Sliding Adapter Initialised");
		
	}
	
	private static final int TYPE_DATA = 0;
	private static final int TYPE_HEAD = 1;
	
	@Override
	public int getItemViewType(int position) {
		if(position == 0 || position == 2)
		{
			return TYPE_HEAD;
		}
		else
		{
			return TYPE_DATA;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	private class ViewHolder{
		
		ImageView icon;
		TextView name;
	}
	
	private class HeadHolder{
		TextView head;
	}
	
	@Override
	public int getCount() {
	      return 5;
	}
	@Override
	public SlidingMenuItems getItem(int position) {
	    return super.getItem(position);
	}
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		HeadHolder headHolder = null;
		SlidingMenuItems slidingMenuItems = new SlidingMenuItems();
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		holder = new ViewHolder();
		headHolder = new HeadHolder();
		
		String fontPath1 = "fonts/OpenSans-Semibold.ttf";
		String fontPath2 = "fonts/segoeui.ttf";
		
		Typeface tf;
		int type = getItemViewType(position);
		
		if (convertView == null) {
			switch(type)
			{
				case TYPE_DATA:
				{
					convertView = mInflater.inflate(R.layout.sliding_menu_row, null);
					convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
					holder.name = (TextView) convertView.findViewById(R.id.slidingMenuRowName);
					holder.icon = (ImageView) convertView.findViewById(R.id.slidingMenuRowIcon);
					tf = Typeface.createFromAsset(this.context.getAssets(), fontPath2);
					holder.name.setTypeface(tf);
					convertView.setTag(R.layout.sliding_menu_row,holder);
				}
				break;
				case TYPE_HEAD:
				{
					convertView = mInflater.inflate(R.layout.sliding_menu_row_head, null);
					convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
					headHolder.head = (TextView) convertView.findViewById(R.id.slidingMenuRowHead);
					tf = Typeface.createFromAsset(this.context.getAssets(), fontPath1);
					headHolder.head.setTypeface(tf);
					convertView.setTag(R.layout.sliding_menu_row_head,headHolder);
					convertView.setOnClickListener(null);
				}
			}
		} else 
			{
				if(type == TYPE_DATA)
				{
					holder = (ViewHolder) convertView.getTag(R.layout.sliding_menu_row);
				}
				else
				{
					headHolder = (HeadHolder) convertView.getTag(R.layout.sliding_menu_row_head);
				}
			}
		
		
		switch(type)
		{
			case TYPE_DATA:
				{
					holder.name.setText(slidingMenuItems.getItemName(position));
					Resources res = context.getResources();
					int resourceId = res.getIdentifier(
					   slidingMenuItems.getImageName(position), "drawable", context.getPackageName() );
					holder.icon.setImageResource(resourceId);
				}
				break;
			case TYPE_HEAD:
				{
					headHolder.head.setText(slidingMenuItems.getItemName(position));
				}
				break;
		}
		
		return convertView;
	}
	
}
