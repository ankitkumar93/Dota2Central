package technited.dota2central;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter_2 extends FragmentPagerAdapter {
		 
	    private List<Fragment> fragments;
	    /**
	     * @param fm
	     * @param fragments
	     */
	    public PageAdapter_2(FragmentManager fm, List<Fragment> fragments) {
	        super(fm);
	        this.fragments = fragments;
	    }
	    /* (non-Javadoc)
	     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	     */
	    @Override
	    public Fragment getItem(int position) {
	        return this.fragments.get(position);
	    }
	 
	    /* (non-Javadoc)
	     * @see android.support.v4.view.PagerAdapter#getCount()
	     */
	    @Override
	    public int getCount() {
	        return this.fragments.size();
	    }
	    
	    @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
            case 0:
                return "Match Details";
            case 1:
                return "Map Status";
            case 2:
                return "Statistics";
            }
            return null;
        }
	    
	}
