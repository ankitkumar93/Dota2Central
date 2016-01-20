package technited.dota2central;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HeroFilterSelection extends Fragment{
	
	private View heroFilterSelectionFragment;
	private boolean flagViewIsLoaded;
	private List<HeroListItem> selectionList;
	private HeroFilterSelectionAdapter heroFilterSelectionAdapter;
	private ListView heroFilterSelectionListView;
	
	public HeroFilterSelection() {
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
        heroFilterSelectionFragment =inflater.inflate(R.layout.hero_selection_fragment, container, false);
        initializeViews();
    	return heroFilterSelectionFragment;
    	
    }
	
	private void initializeViews()
	{
		heroFilterSelectionListView = (ListView) heroFilterSelectionFragment.findViewById(R.id.heroFilters_HeroList);
		heroFilterSelectionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				HeroListItem heroListItem = selectionList.get(position);
				selectionList.remove(position);
				heroFilterSelectionAdapter.notifyDataSetChanged();
				((HeroFilter)getActivity()).updateSelectionList(heroListItem, selectionList);
			}
		});
		flagViewIsLoaded = true;
	}
	
	public List<HeroListItem> addItem(HeroListItem heroListItem)
	{
		this.selectionList.add(heroListItem);
		heroFilterSelectionAdapter.notifyDataSetChanged();
		return this.selectionList;
	}
	
	public void putSelectionList(List<HeroListItem> selectionList)
	{
		this.selectionList = selectionList;
	}
	
	public void refreshHeroList()
	{
		if(flagViewIsLoaded)
		{
			heroFilterSelectionAdapter = new HeroFilterSelectionAdapter(getActivity(), R.layout.hero_filters_selection_row, selectionList);
			heroFilterSelectionListView.setAdapter(heroFilterSelectionAdapter);
		}
	}
	
	public void initLoad()
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
			refreshHeroList();
			
		}
			
	 }

}