package technited.dota2central;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class PagerAdapter_MatchDetail extends PagerAdapter {
	
	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}
	
	 @Override
	 public Object instantiateItem(ViewGroup collection, int position) {
	        int resId = 0;
	        switch (position) {
	        case 0:
	            resId = R.id.matchDetailStats;
	            break;
	        case 1:
	            resId = R.id.matchDetailItems;
	            break;
	        }
	        
	        return (collection).findViewById(resId);
	    }
	 
	 @Override
	    public void destroyItem(ViewGroup collection, int position, Object view) {
	    }
}
