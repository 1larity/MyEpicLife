/* * Copyright 2012 Richard Beech rp.beech@gmail.com * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language * governing permissions and limitations under the License. */package com.digitale.myepiclife;import com.digitale.database.Repeat;/** Provides templates for new events to be created from **/public class EventTemplate {	private String TAG = "EVENTTEMPLATE: ";	/** Unique ID number for this Event Template **/	private Integer uid;	/** The name of this Event Template **/	private String name;	/** Which category is this event in **/	private String category;	/** Question to ask user when filling out report **/	private String reportQuestion;	/** The default frequency of this event **/	private String defautFrequency;	/** Is this event template a good habit or bad habit **/	private int goodBad;	/**	 * What unit is the Event Template measuring f.ex. exercise=calories	 * burnt=Kj, stop smoking=cigarettes	 **/	private String unitType;	/** Description of this Event Template **/	private String description;	/** Default period the reminder should show before the event occurs **/	private String defaultReminderPeriod;	/** The time of day that the alarm should occur **/	private long defaultAlarmTime;	/** decoded repeat **/	private Repeat repeat;	public static final float GOOD_HABIT = 0;	public static final float BAD_HABIT = 1;	public static final float NEUTRAL_HABIT = 2;	/** Create blank Event Template **/	public EventTemplate() {	}	/** create populated Event Template **/	public EventTemplate(Integer uid, String name, int goodBad, String unitType,			String reportQuestion, String defautFrequency, String description,			String defaultReminderPeriod) {		setUid(uid);		setName(name);		setGoodBad(goodBad);		setUnitType(unitType);		setCategory(category);		setReportQuestion(reportQuestion);		setDefaultFrequency(defautFrequency);		setDescription(description);		setDefaultReminderPeriod(defaultReminderPeriod);		setRepeat(defautFrequency);	}	/** make pretty English string to display in repeat info label **/	public String concatinateRequiredDays() {		if (MyEpicLife.DEBUG)			System.out.println(TAG + "Writing repeat string for "+name+repeat.getRepeatType());		String requiredDays = "";		if (repeat.getRepeatType().equals("Weekly")) {			boolean firstDay = true;			if (defautFrequency.contains("mo")) {				requiredDays = requiredDays + "Monday";				firstDay = false;			}			if (defautFrequency.contains("tu")) {				if (!firstDay)					requiredDays = requiredDays + ", ";				requiredDays = requiredDays + "Tuesday";				firstDay = false;			}			if (defautFrequency.contains("we")) {				if (!firstDay)					requiredDays = requiredDays + ", ";				requiredDays = requiredDays + "Wednesday";				firstDay = false;			}			if (defautFrequency.contains("th")) {				if (!firstDay)					requiredDays = requiredDays + ", ";				requiredDays = requiredDays + "Thursday";				firstDay = false;			}			if (defautFrequency.contains("fr")) {				if (!firstDay)					requiredDays = requiredDays + ", ";				requiredDays = requiredDays + "Friday";				firstDay = false;			}			if (defautFrequency.contains("sa")) {				if (!firstDay)					requiredDays = requiredDays + ", ";				requiredDays = requiredDays + "Saturday";				firstDay = false;			}			if (defautFrequency.contains("su")) {				if (!firstDay)					requiredDays = requiredDays + ", ";				requiredDays = requiredDays + "Sunday.";			}		} else if (repeat.getRepeatType().equals("arbitary")) {			requiredDays = repeat.getRepeatUnit();		}		return requiredDays;	}	public void setUid(Integer integer) {		this.uid = integer;	}	public int getUid() {		return uid;	}	public void setCategory(String category) {		this.category = category;	}	public String getCategory() {		return category;	}	public void setName(String name) {		this.name = name;	}	public String getName() {		return name;	}	public void setGoodBad(int goodBad) {		this.goodBad = goodBad;	}	public int getGoodBad() {		return goodBad;	}	public void setUnitType(String unitType) {		this.unitType = unitType;	}	public String getUnitType() {		return unitType;	}	public void setReportQuestion(String reportQuestion) {		this.reportQuestion = reportQuestion;	}	public String getReportQuestion() {		return reportQuestion;	}	public void setDescription(String description) {		this.description = description;	}	public String getDescription() {		return description;	}	public String getDefaultFrequency() {		return defautFrequency;	}	public void setDefaultFrequency(String frequency) {		this.defautFrequency = frequency;		this.repeat = new Repeat(this.defautFrequency);	}	public String getDefaultReminderPeriod() {		// TODO Auto-generated method stub		return defaultReminderPeriod;	}	public void setDefaultReminderPeriod(String defaultReminderPeriod) {		this.defaultReminderPeriod = defaultReminderPeriod;	}	public long getDefaultAlarmTime() {		return defaultAlarmTime;	}	public void setDefaultAlarmTime(long defaultAlarmTime) {		this.defaultAlarmTime = defaultAlarmTime;	}	public Repeat getRepeat() {		return repeat;	}	public void setRepeat(Repeat repeat) {		this.repeat = repeat;	}	public void setRepeat(String repeatData) {		this.repeat = repeat(repeatData);	}	private Repeat repeat(String repeatData) {		// TODO Auto-generated method stub		return null;	}}