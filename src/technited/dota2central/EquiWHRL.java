package technited.dota2central;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

public class EquiWHRL extends RelativeLayout{

	private static final String TAG="EquiWHRL";
	public EquiWHRL(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public EquiWHRL(Context context, AttributeSet attrs) {
		super(context,attrs);
		// TODO Auto-generated constructor stub
	}
	
	public EquiWHRL(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int width, int height)
	{
		Log.d(TAG,width+":"+height);
		if(width > height && height > 0)
		{
			super.onMeasure(width, width);
		}
		else
		{
			super.onMeasure(width, height);
		}
		
	}

}
