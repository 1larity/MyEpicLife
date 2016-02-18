package com.digitale.database;

import java.util.Comparator;
import java.util.Date;

import com.digitale.myepiclife.MELCalendar;


public class EventListComparator implements Comparator<MELEvent> {
    @Override
    public int compare(MELEvent o1, MELEvent o2) {
    	long i=o1.getTimeTillNextNotification();
    	long j=o2.getTimeTillNextNotification();
        return compare(j,i) ;
    }
    
    public int compare(Long o1, Long o2) {
        return o2.compareTo(o1);
    }
}


