package technited.dota2central;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MatchDetail extends Fragment{
	
	private View matchDetailFragment;
	private static final String TAG = "MatchDetailFragment";
	private String match_ID;
	private TextView radiantKillsText, direKillsText, matchDuration, victoryLabel;
	private ImageView radiantKillsBack, direKillsBack;
	private boolean flagViewIsLoaded;
	private MatchDetailsHandler matchDetailsHandler;
	private DateTimeHandler dateTimeHandler;
	private List<PlayerMatchDetailItem> playerMatchDetailItems;
	private HeroListHandler heroListHandler;
	private PlayerMatchDetailAdapter playerMatchDetailAdapter;
	private ListView playerMatchDetailListView;
	private ItemListHandler itemListHandler;
	private ImageFileHandler imageFileHandler;
	private ImageEditor imageEditor;
	private static final String heroPicFolder = "heroimages";
	private static final String itemPicFolder = "items";
	private static final String pathSeperator = "/";
	private static final String imageExtension = ".png";
	private int heroPicHeight = 100;
	private int heroPicWidth = 178;
	private int heroPicWidthCropped = 100;
	
	//Variables
	private int direScore;
	private int radiantScore;
	private int indexCounter;
	
	public MatchDetail()
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
        matchDetailFragment=inflater.inflate(R.layout.match_detail_fragment, container, false);
        initialiseViews();
        setFonts();
    	return matchDetailFragment;
    	
    }
	
	private void initialiseViews()
	{
		radiantKillsText = (TextView) matchDetailFragment.findViewById(R.id.matchDetail_Top_radiantKillsText);
		direKillsText = (TextView) matchDetailFragment.findViewById(R.id.matchDetail_Top_direKillsText);
		matchDuration = (TextView) matchDetailFragment.findViewById(R.id.matchDetail_Top_MatchStatus_MatchDuration);
		victoryLabel = (TextView) matchDetailFragment.findViewById(R.id.matchDetail_Top_MatchStatus_VictoryLabel);
		
		radiantKillsBack = (ImageView) matchDetailFragment.findViewById(R.id.matchDetail_Top_radiantKillsBack);
		direKillsBack = (ImageView) matchDetailFragment.findViewById(R.id.matchDetail_Top_direKillsBack);
		playerMatchDetailItems = new ArrayList<PlayerMatchDetailItem>();
		playerMatchDetailListView = (ListView) matchDetailFragment.findViewById(R.id.matchDetail_playerList);
		imageEditor = new ImageEditor();
		
		matchDetailFragment.setVisibility(View.INVISIBLE);
		
		flagViewIsLoaded = true;
	}

	private void setFonts()
	{
		Typeface tf;
		 String fontPath1 = "fonts/segoeui.ttf";
		 String fontPath2 = "fonts/segoeuil.ttf";
		 String fontPath3 = "fonts/OpenSans-Semibold.ttf";
		 
		 //User Info
		 tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath1);
	     victoryLabel.setTypeface(tf);
	     tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath2);
	     matchDuration.setTypeface(tf);
	     tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath3);
	     direKillsText.setTypeface(tf);
	     radiantKillsText.setTypeface(tf);
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
	
	public void putItemListHandler(ItemListHandler itemListHandler)
	{
		this.itemListHandler = itemListHandler;
	}
	
	public void putImageFileHandler(ImageFileHandler imageFileHandler)
	{
		this.imageFileHandler = imageFileHandler;
	}
	
	public void loadMatchDetails()
	{
		AssignDataToViews assignDataToViews = new AssignDataToViews();
		assignDataToViews.execute();
	}
	
	private void assignMatchDetail()
	{
		Log.d(TAG,"Assigning Match Details");
		Cursor matchDetailsCursor = matchDetailsHandler.getMatchDetails(match_ID);
		matchDetailsCursor.moveToFirst();
		if(matchDetailsCursor.getString(1).equals("true"))
		{
			victoryLabel.setText("Radiant Victory");
			radiantKillsBack.setImageResource(R.drawable.win);
			direKillsBack.setImageResource(R.drawable.loss);
		}
		else
		{
			victoryLabel.setText("Dire Victory");
			radiantKillsBack.setImageResource(R.drawable.loss);
			direKillsBack.setImageResource(R.drawable.win);	
		}
		dateTimeHandler = new DateTimeHandler(matchDetailsCursor.getString(2));
		matchDuration.setText(dateTimeHandler.getDuration());
		
		assignPlayerDetails();
	}
	
	public void assignPlayerDetails()
	{
		Cursor playerMatchDetailCursor = matchDetailsHandler.getPlayerMatchDetail(match_ID);
		playerMatchDetailCursor.moveToFirst();
		indexCounter = 0;
		radiantScore = 0;
		direScore = 0;
		while(!playerMatchDetailCursor.isAfterLast())
		{
			String itemID = null;
			Bitmap itemPic[] = new Bitmap[6];
			for(int itemIndex = 0;itemIndex < 6; itemIndex ++)
			{
				itemID =  playerMatchDetailCursor.getString(5+itemIndex);
				Cursor itemDetailCursor = itemListHandler.getItemDetail(itemID);
				itemDetailCursor.moveToFirst();
				Bitmap itemPicBitmapFinal = null;
	        	Bitmap itemPicBitmap = imageFileHandler.getBitmapFromAsset(itemPicFolder + pathSeperator + itemDetailCursor.getString(2) + imageExtension);
	        	if(itemPicBitmap != null)
	        	{
	        		Log.d(TAG,itemDetailCursor.getString(2));
	        		itemPicBitmapFinal = itemPicBitmap;
	        		
	        	}
	        	else
	        	{
	        		itemPicBitmapFinal = imageFileHandler.getBitmapFromAsset(itemPicFolder + pathSeperator + "emptyitembg" + imageExtension);
	        	}
				itemPic[itemIndex] = itemPicBitmapFinal;
			}
			
			Cursor heroDetailCursor = heroListHandler.getHero(playerMatchDetailCursor.getString(2));
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
			
			PlayerMatchDetailItem playerMatchDetailItem = new PlayerMatchDetailItem(heroDetailCursor.getString(1), heroPicBitmapFinal, playerMatchDetailCursor.getString(3), playerMatchDetailCursor.getString(11), playerMatchDetailCursor.getString(12), playerMatchDetailCursor.getString(13), playerMatchDetailCursor.getString(16), playerMatchDetailCursor.getString(17), playerMatchDetailCursor.getString(18), playerMatchDetailCursor.getString(19),itemPic);
			playerMatchDetailItems.add(playerMatchDetailItem);
			if(indexCounter<5)
			{
				direScore += Integer.parseInt(playerMatchDetailCursor.getString(12));
			}
			else
			{
				radiantScore +=Integer.parseInt(playerMatchDetailCursor.getString(12));
			}
			indexCounter++;
			playerMatchDetailCursor.moveToNext();
		}
			
		direKillsText.setText(Integer.toString(direScore));
		radiantKillsText.setText(Integer.toString(radiantScore));
		
		playerMatchDetailAdapter = new PlayerMatchDetailAdapter(getActivity(), R.layout.match_detail_list_row, playerMatchDetailItems);
		playerMatchDetailListView.setAdapter(playerMatchDetailAdapter);
		matchDetailFragment.setVisibility(View.VISIBLE);
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
			assignMatchDetail();
			
		}
			
	 }

}
