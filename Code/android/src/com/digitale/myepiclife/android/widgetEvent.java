package com.digitale.myepiclife.android;

public class widgetEvent {
	private String eventName;
	private long eventTime;
	public widgetEvent() {
		// TODO Auto-generated constructor stub
	}
	public widgetEvent(String eventName, long firstOccuranceTime) {
		// TODO Auto-generated constructor stub
		this.eventName=eventName;
		this.eventTime=firstOccuranceTime;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public long getEventTime() {
		return eventTime;
	}
	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

}
