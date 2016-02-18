/*
 * Copyright 2012 Richard Beech rp.beech@gmail.com
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.digitale.myepiclife.android;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.digitale.database.AssetDef;
import com.digitale.myepiclife.EventTemplate;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

public class WidgetMELEvent implements WidgetMELEventInterface {

	private String TAG = "MELEvent Class: ";
	/** Unique ID number for this Event **/
	private long uid;
	/** Unique ID number for the Event Template this event is based on **/
	private Integer templateUid;
	/** The frequency of this event **/
	private String frequency;
	/** The first occurrence of this event **/
	private Long firstOccurance;
	/** array of completed occurrences UIDs **/
	private ArrayList<CompletionDef> completionList = new ArrayList<CompletionDef>();
	/** Duration between reminder and event **/
	private String reminderPeriod;
	/** Unit of measure **/
	private String unitType;
	/** Event Name, required if user edits default. **/
	private String eventName;
	/** Category Name, required if user edits default. **/
	private String catagoryName;
	/** Category Name, required if user edits default. **/
	private String description;
	/** the time of day the notification should occur **/
	private long alarmTime;
	/** the decoded form of the repeat */
	private Repeat repeat;
	/** next occurrence of notification **/
	private long nextNotification;
	/** last occurrence of notification **/
	private long lastNotification = 0;

	/** Create default Event **/
	public WidgetMELEvent(long uid) {
		setUid(uid);
		setTemplateUid(0);
		setFrequency("a@1-day");
		setUnitType(" ");
		setFirstOccurance(MELCalendar.MELGetDate());
		setCompletionList(new ArrayList<CompletionDef>());
		setReminderPeriod("1-day");
		setEventName("unset ");
		setCatagoryName("unset");
		setDescription("unset");
	}

	/** Create blank Event **/
	public WidgetMELEvent() {

	}

	/**create a completion and add it to this event **/
	public void completeEvent(String status){
		CompletionDef currentCompletion=new CompletionDef(completionList.size()+1, MELCalendar.MELGetDate(), status);
		completionList.add(currentCompletion);
		calculateNextNotification();
	}
	
	/**calculate when next notification is based on event date/alarm time**/
	public long calculateNextNotification() {

		// set last notification to first occurrence date+alarm time
		String calculatedFirstOccurance = MELCalendar
				.MELshortTimeFromLong(alarmTime)
				+ " "
				+ MELCalendar.MELshortDateFromLong(firstOccurance);

		this.lastNotification = MELCalendar
				.MELlongTimeAndDateFromString(calculatedFirstOccurance);
		if (MyEpicLife.DEBUG)
			System.out
					.println(TAG
							+ "Last Notification time is "
							+ MELCalendar
									.MELshortTimeAndDateFromLong(this.lastNotification));

		// add repeat period to get next notification for arbitrary alarms
		// if last notification has not happened yet (ie user set it in the future),
		// set it as next notification
		if (MELCalendar.MELGetDate() < this.lastNotification) {
			this.nextNotification=this.lastNotification;
			// else if last notification has happened, calculate next
			// notification is required
		} else {
			// if event is arbitrary repeating
			if (repeat.getRepeatType().equals("arbitary")) {
				addArbitraryRepeatPeriod();
			} else if (repeat.getRepeatType().equals("Weekly")) {
				// TODO addWeeklyRepeatPeriod();
			}
			if (MyEpicLife.DEBUG)
				System.out.println(TAG
						+ "Next alarm time is "
						+ MELCalendar
								.MELshortTimeAndDateFromLong(nextNotification));
		}
		return nextNotification;

	}

	/** Add days for arbitrary repeats **/
	private void addArbitraryRepeatPeriod() {

		Calendar l_calendar = new GregorianCalendar();
		l_calendar.setTime(new Date(this.lastNotification));

		// for day repeats add N days
		if (repeat.getRepeatUnit().equals("day")) {
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + "Days adding "
						+ repeat.getRepeatDuration() + " "
						+ repeat.getRepeatUnit());
			l_calendar.add(MELCalendar.DAY_OF_YEAR, repeat.getRepeatDuration());
			nextNotification = MELCalendar.MELDateToLong(l_calendar.getTime());
		}
		// for day repeats add N weeks
		if (repeat.getRepeatUnit().equals("week")) {
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + "Weeks adding "
						+ repeat.getRepeatDuration() + " "
						+ repeat.getRepeatUnit());
			l_calendar.add(MELCalendar.DAY_OF_YEAR,
					repeat.getRepeatDuration() * 7);
			nextNotification = MELCalendar.MELDateToLong(l_calendar.getTime());
		}
		// for monthlies add N months
		if (repeat.getRepeatUnit().equals("month")) {
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + "month adding "
						+ repeat.getRepeatDuration() + " "
						+ repeat.getRepeatUnit());
			l_calendar.add(MELCalendar.MONTH, repeat.getRepeatDuration());
			nextNotification = MELCalendar.MELDateToLong(l_calendar.getTime());

		}
		// for anniversaries add N years
		if (repeat.getRepeatUnit().equals("year")) {
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + "Years adding "
						+ repeat.getRepeatDuration() + " "
						+ repeat.getRepeatUnit());
			l_calendar.add(MELCalendar.YEAR, repeat.getRepeatDuration());
			nextNotification = MELCalendar.MELDateToLong(l_calendar.getTime());
		}
		// Recursively add repeats until notification is in the future
		if (MELCalendar.MELGetDate() > this.nextNotification) {
			System.out.println(TAG + "next notification is in the past "
					+ MELCalendar.MELshortTimeAndDateFromLong(nextNotification)
					+ "adding another repeat");
			this.lastNotification = this.nextNotification;
			 addArbitraryRepeatPeriod();
		}
	}

	/** Returns true if alarm time has been passed **/
	public boolean isAlarmDue() {
		if (MELCalendar.MELGetDate() > this.nextNotification) {
			return true;
		} else {
			return false;
		}
	}

	/** create populated Event Template **/
	public WidgetMELEvent(Integer uid, Integer templateUid, String frequency,
			String unitType, long firstOccurance, ArrayList<CompletionDef> completionList,
			String reminderPeriod, String eventName, String catagoryName,
			String description) {

		setUid(uid);
		setTemplateUid(templateUid);
		setFrequency(frequency);
		setUnitType(unitType);
		setFirstOccurance(firstOccurance);
		setCompletionList(completionList);
		setReminderPeriod(reminderPeriod);
		setEventName(eventName);
		setCatagoryName(catagoryName);
		setDescription(description);

	}

	/** create new event from specified EventTemplate **/
	public WidgetMELEvent(String selectedEventName) {
		EventTemplate currentTemplate = new EventTemplate();
		this.eventName = selectedEventName;
		for (int i = 0; i < MyEpicLife.eventTemplateList.size(); i++) {
			selectedEventName = MyEpicLife.eventTemplateList.get(i).getName();
			if (selectedEventName.equals("" + eventName)) {
				System.out.println(TAG + "Event matched : " + selectedEventName
						+ ":" + eventName);
				currentTemplate = MyEpicLife.eventTemplateList.get(i);
				this.repeat = new Repeat(MyEpicLife.eventTemplateList.get(i)
						.getDefaultFrequency());
			}
		}
		// ** copy over default template to event **//*
		this.setEventName(eventName);
		this.setCatagoryName(currentTemplate.getCategory());
		this.setUnitType(currentTemplate.getUnitType());
		this.setDescription(currentTemplate.getDescription());
		this.setFrequency(currentTemplate.getDefaultFrequency());
		this.setReminderPeriod(currentTemplate.getDefaultReminderPeriod());
		this.setFirstOccurance(MELCalendar.MELGetDate());
		this.setAlarmTime(currentTemplate.getDefaultAlarmTime());
		// ** update any user edited fields TODO **//*

	}

	/** make pretty English string to display in repeat info label **/
	public String concatinateRequiredDays() {
		String requiredDays = "";
		boolean firstDay = true;
		if (frequency.contains("mo")) {
			requiredDays = requiredDays + "Monday";
			firstDay = false;
		}
		if (frequency.contains("tu")) {
			if (!firstDay)
				requiredDays = requiredDays + ", ";
			requiredDays = requiredDays + "Tuesday";
			firstDay = false;
		}
		if (frequency.contains("we")) {
			if (!firstDay)
				requiredDays = requiredDays + ", ";
			requiredDays = requiredDays + "Wednesday";
			firstDay = false;
		}
		if (frequency.contains("th")) {
			if (!firstDay)
				requiredDays = requiredDays + ", ";
			requiredDays = requiredDays + "Thursday";
			firstDay = false;
		}
		if (frequency.contains("fr")) {
			if (!firstDay)
				requiredDays = requiredDays + ", ";
			requiredDays = requiredDays + "Friday";
			firstDay = false;
		}
		if (frequency.contains("sa")) {
			if (!firstDay)
				requiredDays = requiredDays + ", ";
			requiredDays = requiredDays + "Saturday";
			firstDay = false;
		}
		if (frequency.contains("su")) {
			if (!firstDay)
				requiredDays = requiredDays + ", ";
			requiredDays = requiredDays + "Sunday.";

		}
		return requiredDays;
	}

	/** decode repeat data **/
	public Repeat getRepeat() {
		return repeat;
	}

	public void setAlarmTime(long time) {
		this.alarmTime = time;

	}

	public int getTemplateUid() {
		return templateUid;
	}

	public void setTemplateUid(Integer templateUid) {
		this.templateUid = templateUid;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String string) {
		this.frequency = string;
		// update Repeat type because something changed the frequency
		this.repeat = new Repeat(this.frequency);
	}

	public long getFirstOccurance() {
		return firstOccurance;
	}

	public String getFirstOccuranceDate() {
		String firstOccuranceString = MELCalendar.MELshortDate(new Date(
				this.firstOccurance));
		return firstOccuranceString;
	}

	public void setFirstOccurance(long firstOccurance) {
		this.firstOccurance = firstOccurance;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUnitType() {
		return unitType;
	}

	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getReminderPeriod() {

		return reminderPeriod;
	}

	public void setReminderPeriod(String string) {
		this.reminderPeriod = string;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getCatagoryName() {
		return catagoryName;
	}

	public void setCatagoryName(String catagoryName) {
		this.catagoryName = catagoryName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFirstOccuranceTime() {
		String firstOccuranceString = MELCalendar.MELshortTime(new Date(
				this.firstOccurance));
		return firstOccuranceString;

	}

	public long getAlarmTime() {
		return alarmTime;
	}

	public long getNextNotification() {
		return this.nextNotification;
	}

	public ArrayList<CompletionDef> getCompletionList() {
		return completionList;
	}

	public void setCompletionList(ArrayList<CompletionDef> completionList) {
		this.completionList = completionList;
	}



}