package technited.dota2central;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

public class MatchHistoryAdapter extends ArrayAdapter<MatchHistoryListItem> implements Filterable{

	private Context context;
	private static String TAG = "MatchHistoryAdapter";
	private List<MatchHistoryListItem> matchHistoryListItem;
	
	
	
	
	private Typeface tf;
	
	public MatchHistoryAdapter(Context context, int resource,List<MatchHistoryListItem> items) {
		super(context, resource);
		
		this.context = context;
		
		this.matchHistoryListItem = items;
		
		Log.d(TAG,"Match History Adapter Initialised");
		
	}

	private class MatchHistoryViewHolder{
		
		RoundedImageView heroPic;
		TextView heroName;
		TextView dateTime;
	}
	
	@Override
	public int getCount() {
	      return matchHistoryListItem.size();
	}
	@Override
	public MatchHistoryListItem getItem(int position) {
	    return matchHistoryListItem.get(position);
	}
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MatchHistoryViewHolder holder = null;
		MatchHistoryListItem matchHistoryItem= getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.match_history_list_row, null);
			convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
			holder = new MatchHistoryViewHolder();
			holder.heroName = (TextView) convertView.findViewById(R.id.matchHistoryRow_HeroName);
			holder.heroPic = (RoundedImageView) convertView.findViewById(R.id.matchHistoryRow_HeroPic);
			holder.dateTime = (TextView) convertView.findViewById(R.id.matchHistoryRow_DateTime);
			String fontPath1 = "fonts/segoeui.ttf";
			String fontPath2 = "fonts/segoeuil.ttf";
			tf = Typeface.createFromAsset(this.context.getAssets(), fontPath1);
			holder.heroName.setTypeface(tf);
			tf = Typeface.createFromAsset(this.context.getAssets(), fontPath2);
			holder.dateTime.setTypeface(tf);
			convertView.setTag(holder);
		} else 
			holder = (MatchHistoryViewHolder) convertView.getTag();
				
		holder.heroName.setText(matchHistoryItem.getHeroName());
		holder.dateTime.setText(matchHistoryItem.getDateTime());
		holder.heroPic.setImageBitmap(matchHistoryItem.getHeroPic());
		return convertView;
	}
	
	//Filtering
	
	@Override
    public Filter getFilter() {
		
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                matchHistoryListItem = (List<MatchHistoryListItem>) results.values;
                notifyDataSetChanged();
                clear();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                List<MatchHistoryListItem> filteredHistory = new ArrayList<MatchHistoryListItem>();
                // perform your search here using the searchConstraint String.
                String[] filterList = constraint.toString().split(",");
                for (int i = 0; i < matchHistoryListItem.size(); i++) {
                	String heroNameToFilter = matchHistoryListItem.get(i).getHeroName();
                	for(int j = 0;j < filterList.length; j++)
                	{
                		if (heroNameToFilter.equals(filterList[j]))  {
                			filteredHistory.add(matchHistoryListItem.get(i));
                		}
                	}
                }

                results.count = filteredHistory.size();
                results.values = filteredHistory;
                Log.d(TAG, results.values.toString());
                return results;
            }
        };

        return filter;
    }
	
	public void clearFilters(List<MatchHistoryListItem> matchHistoryListItem)
	{
		this.matchHistoryListItem = matchHistoryListItem;
		notifyDataSetChanged();
	}
	
}
