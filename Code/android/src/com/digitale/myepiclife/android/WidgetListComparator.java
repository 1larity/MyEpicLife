package com.digitale.myepiclife.android;


	import java.util.Comparator;


	public class WidgetListComparator implements Comparator<widgetEvent> {
	    @Override
	    public int compare(widgetEvent o1, widgetEvent o2) {
	    	long i=o1.getEventTime();
	    	long j=o2.getEventTime();
	        return compare(j,i) ;
	    }
	    
	    public int compare(Long o1, Long o2) {
	        return o2.compareTo(o1);
	    }
	}



