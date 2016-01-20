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

public class HeroFilterSelectedAdapter extends ArrayAdapter<HeroListItem> {

	private Context context;
	private static String TAG = "HeroFilterSelectionAdapter";
	private List<HeroListItem> heroListItem;
	
	
	
	
	private Typeface tf;
	
	public HeroFilterSelectedAdapter(Context context, int resource,List<HeroListItem> items) {
		super(context, resource);
		
		this.context = context;
		
		this.heroListItem = items;
		
		Log.d(TAG,"Match History Adapter Initialised");
		
	}

	private class HeroFilterSelectionViewHolder{
		
		RoundedImageView heroPic;
		TextView heroName;
	}
	
	@Override
	public int getCount() {
	      return heroListItem.size();
	}
	@Override
	public HeroListItem getItem(int position) {
	    return heroListItem.get(position);
	}
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HeroFilterSelectionViewHolder holder = null;
		HeroListItem heroListItem = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.hero_filters_selected_row, null);
			convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
			holder = new HeroFilterSelectionViewHolder();
			holder.heroName = (TextView) convertView.findViewById(R.id.heroFiltersSelectedRow_HeroName);
			holder.heroPic = (RoundedImageView) convertView.findViewById(R.id.heroFiltersSelectedRow_HeroPic);
			String fontPath1 = "fonts/segoeui.ttf";
			tf = Typeface.createFromAsset(this.context.getAssets(), fontPath1);
			holder.heroName.setTypeface(tf);
			convertView.setTag(holder);
		} else 
			holder = (HeroFilterSelectionViewHolder) convertView.getTag();
				
		holder.heroName.setText(heroListItem.getHeroName());
		holder.heroPic.setImageBitmap(heroListItem.getHeroPic());
		return convertView;
	}

}
