package technited.dota2central;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class StatisticsAdapter extends ArrayAdapter<StatisticsListItem>{
	
	private List<StatisticsListItem> items;
	
	private static final String TAG="StatisticsAdapter";
	
	private Context context;
	
	public StatisticsAdapter(Context context, int resource, List<StatisticsListItem> items) {
		super(context, resource, items);
		this.context = context;
		this.items = items;
		Log.d(TAG,"Statistics Adapter Initialized");
	}

	private static final int TYPE_DATA = 0;
	private static final int TYPE_HEAD = 1;
	
	@Override
	public int getItemViewType(int position) {
		if(position % 2 == 0)
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
	
	@Override
	public int getCount() {
	      return items.size();
	}
	@Override
	public StatisticsListItem getItem(int position) {
	    return items.get(position);
	}
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	private class ViewHolder_Head{
		TextView head;
		TextView minLabel;
		TextView maxLabel;
	}
	
	private class ViewHolder_Data{
		RoundedImageView maxHeroPic;
		TextView maxName;
		TextView maxValue;
		RoundedImageView minHeroPic;
		TextView minName;
		TextView minValue;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		
		StatisticsListItem statisticsListItem = getItem(position);
		
		ViewHolder_Data dataHolder;
		ViewHolder_Head headHolder;
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		dataHolder = new ViewHolder_Data();
		headHolder = new ViewHolder_Head();
		
		String fontPath1 = "fonts/OpenSans-Semibold.ttf";
		String fontPath2 = "fonts/segoeuil.ttf";
		Typeface tf;
		
		int type = getItemViewType(position);
		if (convertView == null) {
			switch(type)
			{
				case TYPE_HEAD:
					{
						convertView = mInflater.inflate(R.layout.statistics_row_head, null);
						convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
						headHolder.head = (TextView) convertView.findViewById(R.id.statisticsRowHead_Head);
						headHolder.maxLabel = (TextView) convertView.findViewById(R.id.statisticsRowHead_Max_Label);
						headHolder.minLabel = (TextView) convertView.findViewById(R.id.statisticsRowHead_Min_Label);
						tf = Typeface.createFromAsset(this.context.getAssets(), fontPath1);
						headHolder.head.setTypeface(tf);
						headHolder.maxLabel.setTypeface(tf);
						headHolder.minLabel.setTypeface(tf);
						convertView.setTag(R.layout.statistics_row_head,headHolder);
					}
					break;
				case TYPE_DATA:
					{
						convertView = mInflater.inflate(R.layout.statistics_row_data, null);
						convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
						dataHolder.maxName = (TextView) convertView.findViewById(R.id.statisticsRow_Max_Name);
						dataHolder.maxValue = (TextView) convertView.findViewById(R.id.statisticsRow_Max_Value);
						dataHolder.maxHeroPic = (RoundedImageView) convertView.findViewById(R.id.statisticsRow_Max_HeroPic);
						dataHolder.minName = (TextView) convertView.findViewById(R.id.statisticsRow_Min_Name);
						dataHolder.minValue = (TextView) convertView.findViewById(R.id.statisticsRow_Min_Value);
						dataHolder.minHeroPic = (RoundedImageView) convertView.findViewById(R.id.statisticsRow_Min_HeroPic);
						tf = Typeface.createFromAsset(this.context.getAssets(), fontPath2);
						dataHolder.maxName.setTypeface(tf);
						dataHolder.maxValue.setTypeface(tf);
						dataHolder.minName.setTypeface(tf);
						dataHolder.minValue.setTypeface(tf);
						convertView.setTag(R.layout.statistics_row_data,dataHolder);
					}
					break;
			}
		}
		else
		{
			if(type == TYPE_HEAD)
			{
				headHolder = (ViewHolder_Head) convertView.getTag(R.layout.statistics_row_head);
			}
			else
			{
				dataHolder = (ViewHolder_Data) convertView.getTag(R.layout.statistics_row_data);
			}
		}
		
		switch(type)
		{
			case TYPE_HEAD:
				{
					if(statisticsListItem.getHead() == "Deaths")
					{
						headHolder.minLabel.setTextColor(context.getResources().getColor(R.color.goldenColorStatistics));
						headHolder.maxLabel.setTextColor(context.getResources().getColor(R.color.silverColorStatistics));
					}
					else
					{
						headHolder.maxLabel.setTextColor(context.getResources().getColor(R.color.goldenColorStatistics));
						headHolder.minLabel.setTextColor(context.getResources().getColor(R.color.silverColorStatistics));
					}
					headHolder.head.setText(statisticsListItem.getHead());
				}
				break;
			case TYPE_DATA:
				{
					dataHolder.maxName.setText(statisticsListItem.getPlayerNameMax());
					dataHolder.maxValue.setText(statisticsListItem.getValueTextMax());
					dataHolder.maxHeroPic.setImageBitmap(statisticsListItem.getHeroPicMax());
					dataHolder.minName.setText(statisticsListItem.getPlayerNameMin());
					dataHolder.minValue.setText(statisticsListItem.getValueTextMin());
					dataHolder.minHeroPic.setImageBitmap(statisticsListItem.getHeroPicMin());
				}
				break;
		}
		
		return convertView;
		
	}

}
