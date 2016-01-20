package technited.dota2central;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.text.format.DateUtils;

public class DateTimeHandler {
	
	private long timeStamp;
	private Calendar calendar;
	private TimeZone timeZone;
	private SimpleDateFormat dateFormat;
	private Date date;
	private long currentTime;
	
	DateTimeHandler(String timeStamp)
	{
		this.timeStamp = Long.parseLong(timeStamp)*1000;
		this.calendar = Calendar.getInstance();
		this.currentTime = this.calendar.getTimeInMillis();
		this.timeZone = TimeZone.getTimeZone("gmt");
		this.calendar.setTimeInMillis(this.timeStamp);
        this.calendar.add(Calendar.MILLISECOND, this.timeZone.getOffset(this.calendar.getTimeInMillis()));
	}
	
	public String getDate()
	{
		dateFormat = new SimpleDateFormat("dd MMMM");
		date = (Date)calendar.getTime();
		return dateFormat.format(date);
	}
	
	public String getTime()
	{
		dateFormat = new SimpleDateFormat("HH:mm aa");
		date = (Date)calendar.getTime();
		return dateFormat.format(date);
	}
	
	public String getAgoTime()
	{
		return DateUtils.getRelativeTimeSpanString(timeStamp, currentTime, DateUtils.MINUTE_IN_MILLIS).toString();
	}
	
	public String getDuration()
	{
		int mins = (int)((this.timeStamp/1000)/60);
		int secs = ((int)(this.timeStamp/1000) - (mins*60));
		return mins+":"+secs;
	}
}
