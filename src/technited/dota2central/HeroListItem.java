package technited.dota2central;

import android.graphics.Bitmap;

public class HeroListItem {
	
	private String heroName;
	private Bitmap heroPic;
	
	public HeroListItem(String heroName, Bitmap heroPic) {
		this.heroName = heroName;
		this.heroPic = heroPic;
	}
	
	public String getHeroName()
	{
		return this.heroName;
	}
	
	public Bitmap getHeroPic()
	{
		return this.heroPic;
	}

}
