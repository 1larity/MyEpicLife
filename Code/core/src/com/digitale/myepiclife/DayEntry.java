package com.digitale.myepiclife;

import com.badlogic.gdx.graphics.Color;

public class DayEntry {

	private int uid;
	private Color dayColour;
	private String dateString;


	public DayEntry(int uid, Color dayColour, String dateString) {
		this.setUid(uid);
		this.setDayColour(dayColour);
		this.setDateString(dateString);

	}

	public DayEntry() {
		// TODO Auto-generated constructor stub
	}

	/** get day unique ID **/
	public int getUid() {
		return uid;
	}

	/** set day unique ID **/
	public void setUid(int uid) {
		this.uid = uid;
	}

	/** get day colourflag **/
	public Color getDayColour() {
		return dayColour;
	}

	/** set day colourflag **/
	public void setDayColour(Color dayColour) {
		this.dayColour = dayColour;
	}

	/** get asset file extension **/
	public String getDateString() {
		return dateString;
	}

	/** set asset file extension **/
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}


}