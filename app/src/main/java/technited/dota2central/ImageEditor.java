package technited.dota2central;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.Base64;
import android.util.Log;

public class ImageEditor {

	private String TAG = "Image Editor";
	public ImageEditor() {
	}
	public Bitmap StringToBitMap(String image){
	    try{
	        byte [] encodeByte=Base64.decode(image,Base64.DEFAULT);
	        Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
	        return bitmap;
	      }catch(Exception e){
	        e.getMessage();
	       return null;
	      }
	}
	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
	            bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
	            bitmap.getWidth() / 2, paint);
	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);
	    return output;
	}
	public Bitmap setImageSize(int h,int w,Bitmap bitmap_image){
		Bitmap scaled = Bitmap.createScaledBitmap(bitmap_image, w, h, true);
		return scaled;
	}
	
	public Bitmap getCroppedImage(int ch, int cw, int h, int w, Bitmap bitmap_image)
	{
		int x = ((w-cw)/2);
		int y = ((h-ch)/2);
		Bitmap cropped = Bitmap.createBitmap(bitmap_image, x, y, cw, ch);
		return cropped;
	}
}
