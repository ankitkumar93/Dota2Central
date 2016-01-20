package technited.dota2central;

import android.content.Context;
import android.database.Cursor;

public class NotesHandler {
	
	private Context context;
	private LocalDB localDB;
	
	public NotesHandler(Context context) {
		this.context = context;
		this.localDB = new LocalDB(this.context);
	}
	
	public boolean putNote(String match_ID, String note)
	{
		Cursor oldNoteCursor = localDB.getMatchNote(match_ID);
		if(oldNoteCursor.getCount() > 0)
		{
			return localDB.updateMatchNote(match_ID,note);
		}
		else
		{
			return localDB.insertMatchNote(match_ID, note);
		}
	}
	
	public Cursor getNote(String match_ID)
	{
		return localDB.getMatchNote(match_ID);
	}

}
