package com.ibm.watson.apis.conversation_enhanced.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayHelper {
	public enum Days { yesterday,today, tomorrow, day_after_tomorrow, this_friday,this_thursday,this_wednesday,this_tuesday} 
//	public enum weekday { monday,}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DayHelper dh = new DayHelper() ;
		System.out.println("Requested day: " + dh.getDate("day after tomorrow")) ;
		
	}
	  public String getDate(String dayUtterence)
	  {
		  
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		Calendar c = Calendar.getInstance() ;
	
		String dayAsked = dayUtterence.replaceAll(" ", "_") ;
		Days theDay = Days.valueOf(dayAsked);
		switch (theDay) {
        case today: break; 
        case yesterday: c.add(Calendar.DATE, -1);
        break; 
        case tomorrow: c.add(Calendar.DATE, 1); 
        break; 
        case day_after_tomorrow: c.add(Calendar.DATE, 2);
        break; 
        default: ;
	  }
        Date dt = c.getTime();
        
        dayAsked = df.format(dt);
		String day = new SimpleDateFormat("EEEE").format(dt) ;		
 	
		return day+" "+dayAsked ;
	  }
}
	  
