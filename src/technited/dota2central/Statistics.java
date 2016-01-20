package technited.dota2central;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.RelativeLayout;

public class Statistics extends Fragment{
	
	private View statisticsFragment;
	private static final String TAG = "StatisticsFragment";
	private boolean flagViewIsLoaded;
	
	private ListView statisticsListView;
	
	private List<StatisticsListItem> statisticsList;
	
	private StatisticsAdapter statisticsAdapter;
	
	private RelativeLayout statisticsContainer;
	
	//ListView Tools
	private String match_ID;
	private MatchDetailsHandler matchDetailsHandler;
	private ImageFileHandler imageFileHandler;
	private ImageEditor imageEditor;
	private HeroListHandler heroListHandler;
	private static final String heroPicFolder = "heroimages";
	private static final String pathSeperator = "/";
	private static final String imageExtension = ".png";
	private int heroPicHeight = 100;
	private int heroPicWidth = 178;
	private int heroPicWidthCropped = 100;
	
	private int maxGpmVal = 0, maxXpmVal = 0, maxKillVal = 0, maxDeathVal = 0, maxAssistVal = 0, minGpmVal = 0, minXpmVal = 0, minKillVal = 0, minDeathVal = 0, minAssistVal = 0;
	private String maxGpmName, maxXpmName, maxKillName, maxDeathName, maxAssistName, minGpmName, minXpmName, minKillName, minDeathName, minAssistName;
	private String maxGpmPic, maxXpmPic, maxKillPic, maxDeathPic, maxAssistPic, minGpmPic, minXpmPic, minKillPic, minDeathPic, minAssistPic;
	
	public Statistics()
	{
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
        statisticsFragment=inflater.inflate(R.layout.statistics_fragment, container, false);
        initializeViews();
    	return statisticsFragment;
    	
    }
	
	public void initializeViews()
	{
		statisticsContainer = (RelativeLayout) statisticsFragment.findViewById(R.id.statisticsContainer);
		statisticsListView = (ListView) statisticsFragment.findViewById(R.id.statisticsList);
		statisticsList = new ArrayList<StatisticsListItem>();
		imageEditor = new ImageEditor();
		statisticsContainer.setVisibility(View.INVISIBLE);
		flagViewIsLoaded = true;
	}
	
	public void putMatchID(String match_ID)
	{
		this.match_ID = match_ID;
	}
	
	public void putMatchDetailsHandler(MatchDetailsHandler matchDetailsHandler)
	{
		this.matchDetailsHandler = matchDetailsHandler;
	}
	
	public void putHeroListHandler(HeroListHandler heroListHandler)
	{
		this.heroListHandler = heroListHandler;
	}
	
	public void putImageFileHandler(ImageFileHandler imageFileHandler)
	{
		this.imageFileHandler = imageFileHandler;
	}
	
	public void assignStatistics()
	{
		Cursor statisticsMatchDetailCursor = matchDetailsHandler.getPlayerMatchDetail(match_ID);
		statisticsMatchDetailCursor.moveToFirst();
		minGpmVal = 65535;
		minXpmVal = 65535;
		minKillVal = 65535;
		minDeathVal = 65535;
		minAssistVal = 65535;
		while(!statisticsMatchDetailCursor.isAfterLast())
		{
			int xpm = statisticsMatchDetailCursor.getInt(19);
			int gpm = statisticsMatchDetailCursor.getInt(18);
			int kill = statisticsMatchDetailCursor.getInt(11);
			int death = statisticsMatchDetailCursor.getInt(12);
			int assist = statisticsMatchDetailCursor.getInt(13);
			
			if(xpm > maxXpmVal)
			{
				maxXpmVal = xpm;
				maxXpmName = statisticsMatchDetailCursor.getString(3);
				maxXpmPic = statisticsMatchDetailCursor.getString(2);
			}
			else if(xpm < minXpmVal)
			{
				minXpmVal = xpm;
				minXpmName = statisticsMatchDetailCursor.getString(3);
				minXpmPic = statisticsMatchDetailCursor.getString(2);
			}
			
			if(gpm > maxGpmVal)
			{
				maxGpmVal = gpm;
				maxGpmName = statisticsMatchDetailCursor.getString(3);
				maxGpmPic = statisticsMatchDetailCursor.getString(2);
			}
			else if(gpm < minGpmVal)
			{
				minGpmVal = gpm;
				minGpmName = statisticsMatchDetailCursor.getString(3);
				minGpmPic = statisticsMatchDetailCursor.getString(2);
			}
			
			if(kill > maxKillVal)
			{
				maxKillVal = kill;
				maxKillName = statisticsMatchDetailCursor.getString(3);
				maxKillPic = statisticsMatchDetailCursor.getString(2);
			}
			else if(kill < minKillVal)
			{
				minKillVal = kill;
				minKillName = statisticsMatchDetailCursor.getString(3);
				minKillPic = statisticsMatchDetailCursor.getString(2);
			}
			
			if(death > maxDeathVal)
			{
				maxDeathVal = death;
				maxDeathName = statisticsMatchDetailCursor.getString(3);
				maxDeathPic = statisticsMatchDetailCursor.getString(2);
			}
			else if(death < minXpmVal)
			{
				minDeathVal = death;
				minDeathName = statisticsMatchDetailCursor.getString(3);
				minDeathPic = statisticsMatchDetailCursor.getString(2);
			}
			
			if(assist > maxAssistVal)
			{
				maxAssistVal = assist;
				maxAssistName = statisticsMatchDetailCursor.getString(3);
				maxAssistPic = statisticsMatchDetailCursor.getString(2);
			}
			else if(assist < minAssistVal)
			{
				minAssistVal = assist;
				minAssistName = statisticsMatchDetailCursor.getString(3);
				minAssistPic = statisticsMatchDetailCursor.getString(2);
			}
			statisticsMatchDetailCursor.moveToNext();
		}
		
		StatisticsListItem item = new StatisticsListItem(null, null, "Gold Per Minute(GPM)", null, null, null, null);
		statisticsList.add(item);
		item = new StatisticsListItem(maxGpmName, Integer.toString(maxGpmVal), "GPM", getHeroImage(maxGpmPic), minGpmName, Integer.toString(minGpmVal), getHeroImage(minGpmPic));
		statisticsList.add(item);
		item = new StatisticsListItem(null, null, "XP Per Minute(XPM)", null, null, null, null);
		statisticsList.add(item);
		item = new StatisticsListItem(maxXpmName, Integer.toString(maxXpmVal), "XPM", getHeroImage(maxXpmPic), minXpmName, Integer.toString(minXpmVal), getHeroImage(minXpmPic));
		statisticsList.add(item);
		item = new StatisticsListItem(null, null, "Kills", null, null, null, null);
		statisticsList.add(item);
		item = new StatisticsListItem(maxKillName, Integer.toString(maxKillVal), "Kill", getHeroImage(maxKillPic), minKillName, Integer.toString(minKillVal), getHeroImage(minKillPic));
		statisticsList.add(item);
		item = new StatisticsListItem(null, null, "Deaths", null, null, null, null);
		statisticsList.add(item);
		item = new StatisticsListItem(maxDeathName, Integer.toString(maxDeathVal), "Death", getHeroImage(maxDeathPic), minDeathName, Integer.toString(minDeathVal), getHeroImage(minDeathPic));
		statisticsList.add(item);
		item = new StatisticsListItem(null, null, "Assists", null, null, null, null);
		statisticsList.add(item);
		item = new StatisticsListItem(maxAssistName, Integer.toString(maxAssistVal), "Assist", getHeroImage(maxAssistPic), minAssistName, Integer.toString(minAssistVal), getHeroImage(minAssistPic));
		statisticsList.add(item);
		
		statisticsAdapter = new StatisticsAdapter(getActivity(), R.layout.statistics_row_data, statisticsList);
		statisticsListView.setAdapter(statisticsAdapter);
		statisticsContainer.setVisibility(View.VISIBLE);
		Log.d(TAG,"Max: Kill:" + maxKillVal + "Assist:" + maxAssistVal + "Death:" + maxDeathVal + "GPM:" + maxGpmVal + "XPM:"+maxXpmVal);
	}
	
	private Bitmap getHeroImage(String heroPic)
	{
		Cursor heroDetailCursor = heroListHandler.getHero(heroPic);
		heroDetailCursor.moveToFirst();
		Bitmap heroPicBitmapFinal = null;
		Bitmap heroPicBitmap = imageFileHandler.getBitmapFromAsset(heroPicFolder + pathSeperator + heroDetailCursor.getString(2) + imageExtension);
        if(heroPicBitmap != null)
        {
        	Bitmap heroPicBitmapScaled = imageEditor.setImageSize(heroPicHeight, heroPicWidth, heroPicBitmap);
        	 heroPicBitmapFinal = imageEditor.getCroppedImage(heroPicHeight, heroPicWidthCropped, heroPicHeight, heroPicWidth, heroPicBitmapScaled);
        }
        else
        {
        	heroPicBitmapFinal = BitmapFactory.decodeResource(getResources(),R.drawable.dark_grey_back);
        }
        
        return heroPicBitmapFinal;
	}
	
	public void loadStatistics()
	{
		AssignDataToViews assignDataToViews = new AssignDataToViews();
		assignDataToViews.execute();
	}
	
	private class AssignDataToViews extends AsyncTask<Void,Void,Void>
	 {

		@Override
		protected Void doInBackground(Void... args) {
			
			while(!flagViewIsLoaded);
			
			return null;
			
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			//After View is Loaded Call Assignment function
			assignStatistics();
			
		}
			
	 }

}
