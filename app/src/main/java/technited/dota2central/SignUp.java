package technited.dota2central;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity {

	private boolean checkboxStatus = false;
	private String TAG = "Sign Up";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		setFonts();
	}

	private void setFonts()
	{
		    TextView txt;
		    Typeface tf;
		    EditText edit;
		    Button button;
		    String fontPath1 = "fonts/OpenSans-Semibold.ttf";
		    String fontPath2 = "fonts/OpenSans-Light.ttf";
	        tf = Typeface.createFromAsset(getAssets(), fontPath1);
	        button = (Button) findViewById(R.id.submitButton);
	        button.setTypeface(tf);
	        tf = Typeface.createFromAsset(getAssets(), fontPath2);
	        txt = (TextView) findViewById(R.id.customUrlMsg);
	        txt.setTypeface(tf);
	        txt = (TextView) findViewById(R.id.checkBoxText);
	        txt.setTypeface(tf);
	        edit = (EditText) findViewById(R.id.textBoxText);
	        edit.setTypeface(tf);
	}
	
	public void checkboxTicked(View view)
	{
		Log.d(TAG,"intoFunction");
		if(!checkboxStatus)
		{
			findViewById(R.id.checkbox).setBackgroundResource(R.drawable.signup_checkbox_on);
			Log.d(TAG,"checkBoxNotSet");
			checkboxStatus = true;
		}
		else
		{
			findViewById(R.id.checkbox).setBackgroundResource(R.drawable.signup_checkbox_off);
			Log.d(TAG,"checkBoxSet");
			checkboxStatus = false;
		}
	}
	
	public void vanityURLBoxTapped(View view)
	{
		EditText textBoxView = (EditText) findViewById(R.id.textBoxText);
		String vanityURL = textBoxView.getText().toString();
		String urlPart="";
		if(vanityURL.length() > 29 || vanityURL.length() == 29)
		{
			urlPart = vanityURL.substring(0, 29);
			if(urlPart.equals("http://steamcommunity.com/id/"))
			{
				if(!vanityURL.substring(29).matches("^[a-zA-Z0-9]*$"))
				{
					textBoxView.setText("http://steamcommunity.com/id/");
				}
			}
		}
		else
		{
			textBoxView.setText("http://steamcommunity.com/id/");
		}
		textBoxView.setSelection(textBoxView.getText().length());
	}
	
	public boolean vanityURLCorrect()
	{
		Log.v(TAG,"vanityURLTEST");
		EditText textBoxView = (EditText) findViewById(R.id.textBoxText);
		String vanityURL = textBoxView.getText().toString();
		String urlPart="";
		String userPart="";
		if(vanityURL.length() > 29 || vanityURL.length() == 29)
		{
			Log.d(TAG, "URL Base is good");
			urlPart = vanityURL.substring(0, 29);
			userPart = vanityURL.substring(29);
			if(urlPart.equals("http://steamcommunity.com/id/"))
			{
				if(userPart.length() > 0)
				{
					if(!userPart.matches("^[a-zA-Z0-9]*$"))
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		
		return true;
	}
	
	public void submitVanityUrl(View view)
	{
		if(checkboxStatus)
		{
			if(checkInternetConnection())
			{
				if(vanityURLCorrect())
				{
					EditText vanityId=(EditText) findViewById(R.id.textBoxText);
					Intent intent=new Intent(this,MainScreen.class);
					intent.putExtra("vanityURL", vanityId.getText().toString().substring(29));
					startActivity(intent);
					finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(),"Please Enter a valid vanity URL!", Toast.LENGTH_LONG).show();
				}
			}
			else
				Toast.makeText(getApplicationContext(),"Please Check Your Internet!", Toast.LENGTH_LONG).show();
		}
		else
			Toast.makeText(getApplicationContext(),"Pls Check the Button First!", Toast.LENGTH_LONG).show();
	}
	
	private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;

        } else {
            return false;
        }
    }

}
