package technited.dota2central;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PageAdapter_3 extends FragmentPagerAdapter {
		 
	    private List<Fragment> fragments;
	    /**
	     * @param fm
	     * @param fragments
	     */
	    public PageAdapter_3(FragmentManager fm, List<Fragment> fragments) {
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
                return "Search List";
            case 1:
                return "Selected List";
            }
            return null;
        }
	    
	}
