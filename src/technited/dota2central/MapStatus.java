package technited.dota2central;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class MapStatus extends Fragment{
	
	private View mapStatusFragment;
	private static final String TAG = "MapStatusFragment";
	
	private boolean flagViewIsLoaded;
	
	private RelativeLayout mapStatusLayout;
	private TextView enableDoodle, clearDoodle, exitDoodle, shareImage;
	private ImageView rAncient, rBarTopM, rBarMidM, rBarBotM, rBarTopR, rBarMidR, rBarBotR, rTowAT, rTowAB, rTowTop, rTowBot, rTowMid, rTowTop_2, rTowBot_2, rTowMid_2, rTowTop_3, rTowBot_3, rTowMid_3, dAncient, dBarTopM, dBarMidM, dBarBotM, dBarTopR, dBarMidR, dBarBotR, dTowTop, dTowBot, dTowMid, dTowTop_2, dTowBot_2, dTowMid_2, dTowTop_3, dTowBot_3, dTowMid_3, dTowAT, dTowAB;
	private MatchDetailsHandler matchDetailsHandler;
	private DrawingView drawingView;
	
	private String match_ID;
	private String towerStatusRadiant;
	private String towerStatusDire;
	private String barrackStatusRadiant;
	private String barrackStatusDire;
	private String ancientStatus;
	
	public MapStatus() {
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
        mapStatusFragment=inflater.inflate(R.layout.map_status_fragment, container, false);
        initializeViews();
        setFonts();
    	return mapStatusFragment;
    	
    }
	
	private void initializeViews()
	{
		
		//Container
		mapStatusLayout = (RelativeLayout) mapStatusFragment.findViewById(R.id.mapStatus);
				
		//Doodle
		enableDoodle = (TextView) mapStatusFragment.findViewById(R.id.mapStatus_enableDoodle);
		clearDoodle = (TextView) mapStatusFragment.findViewById(R.id.mapStatus_clearDoodle);
		exitDoodle = (TextView) mapStatusFragment.findViewById(R.id.mapStatus_exitDoodle);
		
		//Share Image
		shareImage = (TextView) mapStatusFragment.findViewById(R.id.mapStatus_shareImage);
		
		//Radiant
		rAncient = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantAncient);
		rBarTopM = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantBaracksTopMeele);
		rBarTopR = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantBaracksTopRanged);
		rBarMidM = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantBaracksMidMeele);
		rBarMidR = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantBaracksMidRanged);
		rBarBotM = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantBaracksBotMeele);
		rBarBotR = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantBaracksBotRanged);
		rTowAT = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerAncientTop);
		rTowAB = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerAncientBot);
		rTowTop = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerTop);
		rTowTop_2 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerTop_2);
		rTowTop_3 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerTop_3);
		rTowMid = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerMid);
		rTowMid_2 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerMid_2);
		rTowMid_3 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerMid_3);
		rTowBot = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerBot);
		rTowBot_2 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerBot_2);
		rTowBot_3 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_radiantTowerBot_3);
		
		//Dire
		dAncient = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direAncient);
		dBarTopM = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direBaracksTopMeele);
		dBarTopR = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direBaracksTopRanged);
		dBarMidM = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direBaracksMidMeele);
		dBarMidR = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direBaracksMidRanged);
		dBarBotM = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direBaracksBotMeele);
		dBarBotR = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direBaracksBotRanged);
		dTowAT = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerAncientTop);
		dTowAB = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerAncientBot);
		dTowTop = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerTop);
		dTowTop_2 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerTop_2);
		dTowTop_3 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerTop_3);
		dTowMid = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerMid);
		dTowMid_2 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerMid_2);
		dTowMid_3 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerMid_3);
		dTowBot = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerBot);
		dTowBot_2 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerBot_2);
		dTowBot_3 = (ImageView) mapStatusFragment.findViewById(R.id.mapStatus_direTowerBot_3);
	}
	
	private void setFonts()
	{
		Typeface tf;
		String fontPath = "fonts/segoeuil.ttf";
		tf = Typeface.createFromAsset(this.getActivity().getAssets(), fontPath);
		enableDoodle.setTypeface(tf);
		clearDoodle.setTypeface(tf);
		exitDoodle.setTypeface(tf);
		shareImage.setTypeface(tf);
		
		flagViewIsLoaded = true;
	}
	
	public void enableDoodle(View view)
	{
		
		drawingView = new DrawingView(getActivity());
		LayoutParams drawingParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mapStatusLayout.addView(drawingView);
		drawingView.setLayoutParams(drawingParams);
		enableDoodle.setVisibility(View.INVISIBLE);
		clearDoodle.setVisibility(View.VISIBLE);
		exitDoodle.setVisibility(View.VISIBLE);
	}
	
	public void clearDoodle(View view)
	{
		mapStatusLayout.removeView(drawingView);
		drawingView = new DrawingView(getActivity());
		LayoutParams drawingParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		mapStatusLayout.addView(drawingView);
		drawingView.setLayoutParams(drawingParams);
	}
	
	public void exitDoodle(View view)
	{
		mapStatusLayout.removeView(drawingView);
		enableDoodle.setVisibility(View.VISIBLE);
		clearDoodle.setVisibility(View.INVISIBLE);
		exitDoodle.setVisibility(View.INVISIBLE);
	}
	
	public void shareImage(View view)
	{
		 Intent shareIntent = new Intent(Intent.ACTION_SEND);
		 shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		 shareIntent.setType("image/png");
		 shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Map Status");
		 shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Shared Via D2C App For Android");
		 mapStatusLayout.setDrawingCacheEnabled(true);
		 Bitmap bitmapToShare = mapStatusLayout.getDrawingCache();
		 String root = Environment.getExternalStorageDirectory().toString();
         File newDir = new File(root + "/Dota2Central");    
         newDir.mkdirs();
         String fotoname = "mapStatus-"+match_ID+".png";
         File file = new File (newDir, fotoname);
         if (file.exists ()) file.delete (); 
         	try {
                FileOutputStream out = new FileOutputStream(file);
                bitmapToShare.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
             } catch (Exception e) {
            	 Log.d(TAG,"Error",e);
            	 Toast.makeText(getActivity().getApplicationContext(), "Failed To Share", Toast.LENGTH_SHORT ).show();
            	 return;
             }
         shareIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
         startActivity(Intent.createChooser(shareIntent, "Share Map Status"));
		 mapStatusLayout.setDrawingCacheEnabled(false);
	}
	
	public void putMatchDetailHandler(MatchDetailsHandler matchDetailsHandler)
	{
		this.matchDetailsHandler = matchDetailsHandler;
	}
	
	public void putMatchID(String matchID)
	{
		this.match_ID = matchID;
	}
	
	public void assignMapStatus()
	{
		if(flagViewIsLoaded)
		{
			Cursor matchDetailCursor = matchDetailsHandler.getMatchDetails(match_ID);
			matchDetailCursor.moveToFirst();
			towerStatusRadiant = matchDetailCursor.getString(5);
			towerStatusDire = matchDetailCursor.getString(6);
			barrackStatusRadiant = matchDetailCursor.getString(7);
			barrackStatusDire = matchDetailCursor.getString(8);
			ancientStatus = matchDetailCursor.getString(1);
			Log.d(TAG,ancientStatus);
			assignDataOnMap();
		}
	}
	
	public void assignDataOnMap()
	{
		if(ancientStatus.equals("true"))
		{
			dAncient.setImageResource(R.drawable.tower_rubble);
		}
		else
		{
			rAncient.setImageResource(R.drawable.tower_rubble);
		}
		
		towerStatusRadiant = Integer.toBinaryString(0x10000 | Integer.parseInt(towerStatusRadiant)).substring(1);
		towerStatusDire = Integer.toBinaryString(0x10000 | Integer.parseInt(towerStatusDire)).substring(1);
		barrackStatusRadiant = Integer.toBinaryString(0x100 | Integer.parseInt(barrackStatusRadiant)).substring(1);
		barrackStatusDire = Integer.toBinaryString(0x100 | Integer.parseInt(barrackStatusDire)).substring(1);
		
		//Towers
		for(int towerIndex = 5; towerIndex < 16; towerIndex++ )
		{
			//Dire
			if(towerStatusDire.charAt(towerIndex) == '0')
			{
				switch(towerIndex)
				{
					case 5:
						dTowAT.setImageResource(R.drawable.tower_rubble);
						break;
					case 6:
						dTowAB.setImageResource(R.drawable.tower_rubble);
						break;
					case 7:
						dTowBot_3.setImageResource(R.drawable.tower_rubble);
						break;
					case 8:
						dTowBot_2.setImageResource(R.drawable.tower_rubble);
						break;
					case 9:
						dTowBot.setImageResource(R.drawable.tower_rubble);
						break;
					case 10:
						dTowMid_3.setImageResource(R.drawable.tower_rubble);
						break;
					case 11:
						dTowMid_2.setImageResource(R.drawable.tower_rubble);
						break;
					case 12:
						dTowMid.setImageResource(R.drawable.tower_rubble);
						break;
					case 13:
						dTowTop_3.setImageResource(R.drawable.tower_rubble);
						break;
					case 14:
						dTowTop_2.setImageResource(R.drawable.tower_rubble);
						break;
					case 15:
						dTowTop.setImageResource(R.drawable.tower_rubble);
						break;
					default:
						break;
				}
			}
			//Radiant
			if(towerStatusRadiant.charAt(towerIndex) == '0')
			{
				switch(towerIndex)
				{
					case 5:
						rTowAT.setImageResource(R.drawable.tower_rubble);
						break;
					case 6:
						rTowAB.setImageResource(R.drawable.tower_rubble);
						break;
					case 7:
						rTowBot_3.setImageResource(R.drawable.tower_rubble);
						break;
					case 8:
						rTowBot_2.setImageResource(R.drawable.tower_rubble);
						break;
					case 9:
						rTowBot.setImageResource(R.drawable.tower_rubble);
						break;
					case 10:
						rTowMid_3.setImageResource(R.drawable.tower_rubble);
						break;
					case 11:
						rTowMid_2.setImageResource(R.drawable.tower_rubble);
						break;
					case 12:
						rTowMid.setImageResource(R.drawable.tower_rubble);
						break;
					case 13:
						rTowTop_3.setImageResource(R.drawable.tower_rubble);
						break;
					case 14:
						rTowTop_2.setImageResource(R.drawable.tower_rubble);
						break;
					case 15:
						rTowTop.setImageResource(R.drawable.tower_rubble);
						break;
					default:
						break;
				}
			}
		}
		
		//Barracks
		for(int barrackIndex = 2; barrackIndex < 8; barrackIndex++ )
		{
			//Dire
			if(barrackStatusDire.charAt(barrackIndex) == '0')
			{
				switch(barrackIndex)
				{
					case 2:
						dBarBotR.setImageResource(R.drawable.tower_rubble);
						break;
					case 3:
						dBarBotM.setImageResource(R.drawable.tower_rubble);
						break;
					case 4:
						dBarMidR.setImageResource(R.drawable.tower_rubble);
						break;
					case 5:
						dBarMidM.setImageResource(R.drawable.tower_rubble);
						break;
					case 6:
						dBarTopR.setImageResource(R.drawable.tower_rubble);
						break;
					case 7:
						dBarTopM.setImageResource(R.drawable.tower_rubble);
						break;
					default:
						break;
				}
			}
			//Radiant
			if(barrackStatusRadiant.charAt(barrackIndex) == '0')
			{
				switch(barrackIndex)
				{
					case 2:
						rBarBotR.setImageResource(R.drawable.tower_rubble);
						break;
					case 3:
						rBarBotM.setImageResource(R.drawable.tower_rubble);
						break;
					case 4:
						rBarMidR.setImageResource(R.drawable.tower_rubble);
						break;
					case 5:
						rBarMidM.setImageResource(R.drawable.tower_rubble);
						break;
					case 6:
						rBarTopR.setImageResource(R.drawable.tower_rubble);
						break;
					case 7:
						rBarTopM.setImageResource(R.drawable.tower_rubble);
						break;
					default:
						break;
				}
			}
		}
	}
	
	public void loadMapStatus()
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
			assignMapStatus();
			
		}
			
	 }
}
