package technited.dota2central;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;


public class JsonParser {
static InputStream input_stream;
static JSONObject json_object;
static String json ="";
	
	public JsonParser() {
		//constructor
	}
	
	public JSONObject getJSONFromURL(String URL)
	{
		DefaultHttpClient http_client= new DefaultHttpClient();
		HttpGet http_get= new HttpGet(URL);
		try
		{
			HttpResponse http_response=http_client.execute(http_get);
			HttpEntity http_entity=http_response.getEntity();
			input_stream=http_entity.getContent();
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		} catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		try{
			BufferedReader buffered_reader= new BufferedReader(new InputStreamReader(input_stream,"iso-8859-1"),8);
			StringBuilder string_builder= new StringBuilder();
			String string_line=null;
			while((string_line=buffered_reader.readLine()) != null){
				string_builder.append(string_line+"\n");
			}
			input_stream.close();
			json=string_builder.toString();
		} catch(Exception e)
		{
			Log.e("buffer Error","Error converting result" + e.toString());
		}
		
		try {
            json_object = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
            json_object = null;
            return json_object;
        }
		
		return json_object;
	}

}
