package com.digitale.database;

import com.digitale.myepiclife.MyEpicLife;

public class Repeat {
	private String TAG = "REPEAT: ";
	private String repeatType;
	private int repeatDuration;
	private String repeatUnit;
	public Repeat() {
		// TODO Auto-generated constructor stub
	}
	public Repeat(String repeatData) {
		// retrieve repeat setting for this template
					// first letter A=arbitary period, W=weekly, ie every monday
					// repeat for arbitaries is:-
					// N-day (or month, year where n is the number of days
					// repeat for Weeklies is 
					//2 letter codes representing the days the occurance is on  (mo,tu,we,th,fr,sa,su)
					
					String[] row = repeatData.split("@");
					this.setRepeatType(row[0]);
					if (MyEpicLife.DEBUG)
						System.out.println(TAG+"Decoded "+this.getRepeatType()+" repeat.");
					if (row.length>=2){
					String repeatSpec = row[1];
					if (MyEpicLife.DEBUG)
						System.out.println(TAG+"Repeat detail data "+repeatSpec);
					if (repeatSpec.contains("-")) {
						String[] abitaryRepeat = repeatSpec.split("-");
						this.setRepeatDuration(Integer.valueOf(abitaryRepeat[0]));
						this.setRepeatUnit(abitaryRepeat[1]);
						if (MyEpicLife.DEBUG)
							System.out.println(TAG+this.getRepeatType()+ "Repeat every "+this.getRepeatDuration()+":"+this.getRepeatUnit());
					}else{
						this.setRepeatUnit(repeatSpec);
						if (MyEpicLife.DEBUG)
							System.out.println(TAG+this.getRepeatType()+ "Repeat every "+this.getRepeatUnit());
					}
					}
	}
	public String getRepeatType() {
		return repeatType;
	}
	public void setRepeatType(String repeatType) {
		this.repeatType = repeatType;
	}
	/**Return the number of repeats**/
	public int getRepeatDuration() {
		return repeatDuration;
	}
	public void setRepeatDuration(int repeatDuration) {
		this.repeatDuration = repeatDuration;
	}
	/**Return the unit this repeat is measured in (day/month/year)**/
	public String getRepeatUnit() {
		return repeatUnit;
	}
	public void setRepeatUnit(String repeatUnit) {
		this.repeatUnit = repeatUnit;
	}

}
