package technited.dota2central;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageFileHandler {
	
	private Context context;
	private static final String TAG  = "ExternalStorage";
	
	ImageFileHandler(Context context){
		
		this.context = context;
	}
	
	public void storeImageInMemory(Bitmap storeImage, String steamID) {
	    // Create a path where we will place our private file on external
	    // storage.
	    File file = new File(context.getExternalFilesDir(null), steamID+".png");

	    try {
	        OutputStream os = new FileOutputStream(file);
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        storeImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        byte[] imageData = stream.toByteArray();
	        os.write(imageData);
	        os.close();
	    } catch (IOException e) {
	        // Unable to create file, likely because external storage is
	        // not currently mounted.
	        Log.d(TAG, "Error Storing Image in File: " + file, e);
	    }
	}

	public void deleteImage(String steamID){
	    File file = new File(context.getExternalFilesDir(null), steamID+".png");
	    if (file != null) {
	        file.delete();
	    }
	}

	boolean imageExists(String steamID) {
	    // Get path for the file on external storage.  If external
	    // storage is not currently mounted this will fail.
	    File file = new File(context.getExternalFilesDir(null), steamID+".png");
	    return file.exists();
	}
	
	public Bitmap getImageFromMemory(String steamID)
	{
		File file = new File(context.getExternalFilesDir(null), steamID+".png");
		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.d(TAG,"Error Reading File: "+file,e);
		}
		Bitmap imageFromFile = BitmapFactory.decodeStream(is);
		return imageFromFile;
	}
	
	public Bitmap getBitmapFromAsset(String imagePath)
    {
        AssetManager assetManager = context.getAssets();
        InputStream is = null;
        try {
            is = assetManager.open(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }
}
