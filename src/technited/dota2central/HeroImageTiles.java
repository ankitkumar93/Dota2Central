package technited.dota2central;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class HeroImageTiles extends RelativeLayout{

	public HeroImageTiles(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public HeroImageTiles(Context context, AttributeSet attrs)
	{
		super(context,attrs);
	}
	
	public HeroImageTiles(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }
	
	@Override
	protected void onMeasure(int width, int height)
	{
		super.onMeasure(width, width);
		
	}
}
