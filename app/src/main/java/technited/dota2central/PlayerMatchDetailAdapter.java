package technited.dota2central;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PlayerMatchDetailAdapter  extends ArrayAdapter<PlayerMatchDetailItem>{
	
	private Context context;
	private static String TAG = "PlayerMatchDetailAdapter";
	private List<PlayerMatchDetailItem> playerMatchDetailItem;
	
	private int viewPagerItem[];
	
	
	private Typeface tf;
	
	public PlayerMatchDetailAdapter(Context context, int resource,List<PlayerMatchDetailItem> items) {
		super(context, resource);
		
		this.context = context;
		
		this.playerMatchDetailItem = items;
		
		
		this.viewPagerItem = new int[10];
		for(int index=0; index < 10;index++)
		{
			this.viewPagerItem[index] = 0;
		}
		
		Log.d(TAG,"Player Match Detail Adapter Initialised");
		
	}

	private class ViewHolder_1{
		
		ViewPager viewPager;
		PagerAdapter_MatchDetail pagerAdapter;
		
		//Stats
		RoundedImageView heroPic;
		TextView heroName;
		TextView playerName;
		TextView kills;
		TextView deaths;
		TextView assists;
		TextView hits;
		
		//Items
		TextView gpm;
		TextView gpmLabel;
		TextView xpm;
		TextView xpmLabel;
		RoundedImageView item_1;
		RoundedImageView item_2;
		RoundedImageView item_3;
		RoundedImageView item_4;
		RoundedImageView item_5;
		RoundedImageView item_6;
	}
	
private class ViewHolder_2{
	
		TextView teamName;
		TextView killsLabel;
		TextView deathsLabel;
		TextView assistsLabel;
		TextView hitsLabel;
		
	}

	private static final int TYPE_ITEM = 0;
	private static final int TYPE_SECTION = 1;

	@Override
	public int getItemViewType(int position) {
		if(position == 0 || position == 6)
		{
			return TYPE_SECTION;
		}
		else
		{
			return TYPE_ITEM;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getCount() {
		Log.d(TAG,""+playerMatchDetailItem.size());
	      return (playerMatchDetailItem.size()+2);
	}
	@Override
	public PlayerMatchDetailItem getItem(int position) {
	    return playerMatchDetailItem.get(position);
	}
	@Override
	public long getItemId(int position) {
	    return position;
	}
	
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder_1 holder_1;
		ViewHolder_2 holder_2;
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		holder_1 = new ViewHolder_1();
		holder_2 = new ViewHolder_2();
		String fontPath1 = "fonts/segoeui.ttf";
		String fontPath2 = "fonts/segoeuil.ttf";
		int type = getItemViewType(position);
		if (convertView == null) {
			switch(type)
			{
			case TYPE_SECTION:
			{
				convertView = mInflater.inflate(R.layout.match_detail_list_seperatorrow, null);
				convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
				holder_2.teamName = (TextView) convertView.findViewById(R.id.matchDetailSeperatorRow_TeamName);
				holder_2.killsLabel = (TextView) convertView.findViewById(R.id.matchDetailSeperatorRow_KillsLabel);
				holder_2.deathsLabel = (TextView) convertView.findViewById(R.id.matchDetailSeperatorRow_DeathsLabel);
				holder_2.assistsLabel = (TextView) convertView.findViewById(R.id.matchDetailSeperatorRow_AssistsLabel);
				holder_2.hitsLabel = (TextView) convertView.findViewById(R.id.matchDetailSeperatorRow_HitsLabel);
				tf = Typeface.createFromAsset(this.context.getAssets(), fontPath1);
				holder_2.teamName.setTypeface(tf);
				holder_2.killsLabel.setTypeface(tf);
				holder_2.deathsLabel.setTypeface(tf);
				holder_2.assistsLabel.setTypeface(tf);
				holder_2.hitsLabel.setTypeface(tf);
				convertView.setTag(R.layout.match_detail_list_seperatorrow,holder_2);
			}
			break;
			case TYPE_ITEM:
			{
				Log.d(TAG,"Position: "+position);
				convertView = mInflater.inflate(R.layout.match_detail_list_row, null);
				convertView.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT, ListView.LayoutParams.WRAP_CONTENT));
				
				holder_1.viewPager = (ViewPager) convertView.findViewById(R.id.matchDetailsPager);
				holder_1.pagerAdapter = new PagerAdapter_MatchDetail();
				holder_1.viewPager.setAdapter(holder_1.pagerAdapter);
				
				//Stats
				holder_1.playerName = (TextView) convertView.findViewById(R.id.matchDetailRow_PlayerName);
				holder_1.heroName = (TextView) convertView.findViewById(R.id.matchDetailRow_HeroName);
				holder_1.heroPic = (RoundedImageView) convertView.findViewById(R.id.matchDetailRow_HeroPic);
				holder_1.kills = (TextView) convertView.findViewById(R.id.matchDetailRow_Kills);
				holder_1.deaths = (TextView) convertView.findViewById(R.id.matchDetailRow_Deaths);
				holder_1.assists = (TextView) convertView.findViewById(R.id.matchDetailRow_Assists);
				holder_1.hits = (TextView) convertView.findViewById(R.id.matchDetailRow_Hits);
				
				//Items
				holder_1.gpmLabel = (TextView) convertView.findViewById(R.id.matchDetailRow_PerMinStats_GPM_Label);
				holder_1.xpmLabel = (TextView) convertView.findViewById(R.id.matchDetailRow_PerMinStats_XPM_Label);
				holder_1.gpm= (TextView) convertView.findViewById(R.id.matchDetailRow_PerMinStats_GPM_Text);
				holder_1.xpm = (TextView) convertView.findViewById(R.id.matchDetailRow_PerMinStats_XPM_Text);
				holder_1.item_1 = (RoundedImageView) convertView.findViewById(R.id.matchDetailRow_Item_1);
				holder_1.item_2 = (RoundedImageView) convertView.findViewById(R.id.matchDetailRow_Item_2);
				holder_1.item_3 = (RoundedImageView) convertView.findViewById(R.id.matchDetailRow_Item_3);
				holder_1.item_4 = (RoundedImageView) convertView.findViewById(R.id.matchDetailRow_Item_4);
				holder_1.item_5 = (RoundedImageView) convertView.findViewById(R.id.matchDetailRow_Item_5);
				holder_1.item_6 = (RoundedImageView) convertView.findViewById(R.id.matchDetailRow_Item_6);
				
				//Fonts
				tf = Typeface.createFromAsset(context.getAssets(), fontPath1);
				holder_1.gpm.setTypeface(tf);
				holder_1.gpmLabel.setTypeface(tf);
				holder_1.xpm.setTypeface(tf);
				holder_1.xpmLabel.setTypeface(tf);
				holder_1.playerName.setTypeface(tf);
				holder_1.kills.setTypeface(tf);
				holder_1.deaths.setTypeface(tf);
				holder_1.assists.setTypeface(tf);
				holder_1.hits.setTypeface(tf);
				tf = Typeface.createFromAsset(context.getAssets(), fontPath2);
				holder_1.heroName.setTypeface(tf);
				
				convertView.setTag(R.layout.match_detail_list_row,holder_1);
			}
			break;
			}
		} else
		{
			Log.d(TAG,"Position: "+position);
			if(type == TYPE_ITEM)
			{
				holder_1 = (ViewHolder_1) convertView.getTag(R.layout.match_detail_list_row);
			}
			else
			{
				holder_2 = (ViewHolder_2) convertView.getTag(R.layout.match_detail_list_seperatorrow);
			}
		}
		
		switch(type)
		{
		case TYPE_SECTION:
		{
			if(position == 0)
			{
				holder_2.teamName.setText("RADIANT TEAM");
			}
			else
			{
				holder_2.teamName.setText("DIRE TEAM");
			}
		}
		break;
		case TYPE_ITEM:
		{
			final PlayerMatchDetailItem playerMatchDetailItem;
			if(position < 6)
			{
				playerMatchDetailItem= getItem(position-1);
			}
			else
			{
				playerMatchDetailItem = getItem(position-2);
			}
			
			//Text
			holder_1.playerName.setText(playerMatchDetailItem.getPlayerName());
			holder_1.heroName.setText(playerMatchDetailItem.getHeroName());
			holder_1.kills.setText(playerMatchDetailItem.getKills());
			holder_1.deaths.setText(playerMatchDetailItem.getDeaths());
			holder_1.assists.setText(playerMatchDetailItem.getAssists());
			holder_1.hits.setText(playerMatchDetailItem.getHits());
			holder_1.gpm.setText(playerMatchDetailItem.getGPM());
			holder_1.xpm.setText(playerMatchDetailItem.getXPM());
			
			//Images
			
			holder_1.heroPic.setImageBitmap(playerMatchDetailItem.getHeroPic());
			
	        for(int itemIndex = 0; itemIndex < 6; itemIndex++)
	        {
	        	switch (itemIndex) {
				case 0:
					holder_1.item_1.setImageBitmap(playerMatchDetailItem.getItemPic(itemIndex));
					break;
				case 1:
					holder_1.item_2.setImageBitmap(playerMatchDetailItem.getItemPic(itemIndex));
					break;
				case 2:
					holder_1.item_3.setImageBitmap(playerMatchDetailItem.getItemPic(itemIndex));
					break;
				case 3:
					holder_1.item_4.setImageBitmap(playerMatchDetailItem.getItemPic(itemIndex));
					break;
				case 4:
					holder_1.item_5.setImageBitmap(playerMatchDetailItem.getItemPic(itemIndex));
					break;
				case 5:
					holder_1.item_6.setImageBitmap(playerMatchDetailItem.getItemPic(itemIndex));
					break;

				default:
					break;
				}
	        	
	        }
	        
	        holder_1.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					playerMatchDetailItem.setViewPage(arg0);
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        holder_1.viewPager.setCurrentItem(playerMatchDetailItem.getViewPage(), false);
	        
		}
		break;
		}
		return convertView;
	}
}
