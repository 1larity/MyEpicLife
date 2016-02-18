package com.digitale.myepiclife;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;


public class MELCalendar extends Calendar {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5264839666443793393L;
	public final List<DayEntry> m_dayList = new ArrayList<DayEntry>();;
	// Array holding long English names of week days
	private final String[] m_weekdays = new String[] { "Sun", "Mon", "Tue",
			"Wed", "Thu", "Fri", "Sat" };
	// Array holding long English names of months
	private final String[] m_months = { "January", "February", "March",
			"April", "May", "June", "July", "August", "September", "October",
			"November", "December" };
	// Array holding the number of days in each month
	private final int[] m_daysOfMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };

	int m_daysInMonth, m_prevMonthDays;
	Calendar m_calendar = Calendar.getInstance();
	private final int m_currentDayOfMonth = m_calendar
			.get(Calendar.DAY_OF_MONTH);

	private int m_month = m_calendar.get(Calendar.MONTH);
	private int m_year = m_calendar.get(Calendar.YEAR);
	
	/** Supplies a long with todays date**/
	 
	public static long MELGetDate(){
		Calendar m_calendar = Calendar.getInstance();
		return m_calendar.getTimeInMillis();
	}
	
	
	/** Supplies a string representing the specified date 
	 * in the format DD-MM-YY
	 */
	public static String MELshortDate(Date date){
		DateFormat df1 = new SimpleDateFormat("dd/MM/yy");
		String dateString = df1.format(date);
		return dateString;
	}
	
	/** Supplies a string representing the specified long 
	 * in the format DD-MM-YY
	 */
	public static String MELshortDateFromLong(long date){
		DateFormat df1 = new SimpleDateFormat("dd/MM/yy");
		String dateString = df1.format(date);
		return dateString;
	}
	/** Supplies a string representing the specified Time  
	 *  in the format HH:MM
	 */
	public static String MELshortTime(Date date){
		DateFormat df1 = new SimpleDateFormat("HH:mm");
		String dateString = df1.format(date);
		return dateString;
	}
	/** Supplies a string representing the specified Time  
	 *  in the format HH:MM:ss
	 */
	public String MELshortTimeWithSeconds(Date date){
		DateFormat df1 = new SimpleDateFormat("HH:mm:ss");
		String dateString = df1.format(date);
		return dateString;
	}
	
	/** Supplies a string representing the specified long 
	 * in the format HH:MM
	 */
	public static String MELshortTimeFromLong(long date){
		DateFormat df1 = new SimpleDateFormat("HH:mm");
		String dateString = df1.format(date);
		return dateString;
	}
	/** Supplies a string representing the specified long 
	 * in the format HH:MM DD/MM/YY 
	 */
	public static String MELshortTimeAndDateFromLong(long date){
		DateFormat df1 = new SimpleDateFormat("HH:mm dd/MM/yy");
		String dateString = df1.format(date);
		return dateString;
	}
	/** Supplies a long representing the specified string 
	 * in the format HH:MM DD/MM/YY 
	 */
	public static long MELlongTimeAndDateFromString(String inputDate){
		DateFormat df1 = new SimpleDateFormat("HH:mm dd/MM/yy");
		Date date;
		long dateLong = 0;
		try {
			date = df1.parse(inputDate);
			 dateLong = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateLong;
	}
	
	/** Supplies a long representing the specified string 
	 * in the format  DD/MM/YY 
	 */
	public static long MELlongDateFromString(String inputDate){
		DateFormat df1 = new SimpleDateFormat("dd/MM/yy");
		Date date;
		long dateLong = 0;
		try {
			date = df1.parse(inputDate);
			 dateLong = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateLong;
	}
	
	/** Supplies a long representing the specified DATE 
	 */
	public static long MELDateToLong(Date inputDate){
			long dateLong = inputDate.getTime();
		return dateLong;
	}
	
	/** Supplies a long representing the specified date 
	 * in the format HH:MM
	 */
	public static long MELTimeAsLong(String inputDate){
		DateFormat df1 = new SimpleDateFormat("HH:mm");
		Date date;
		long dateLong=0;
		try {
			date = df1.parse(inputDate);
			 dateLong = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dateLong;
	}
	/** Supplies  the time now with seconds.  **/
	public String MELgetShortNowTimeWithSeconds(){
		return MELshortTimeWithSeconds(new Date(MELGetDate()));
	}
	/** Supplies  the time now.  **/
	public String MELgetShortNowTime(){
		return MELshortTime(new Date(MELGetDate()));
	}
	
	/** Supplies a the date now.  **/
	public String MELgetShortNowDate(){
		return MELshortDate(new Date(MELGetDate()));
	}
	
	/** Supplies a string representing the specified date 
	 * in the format DD-Monthname-YY
	 */
	public String MELlongDate(Date date){
		DateFormat df1 = new SimpleDateFormat("dd/MM/yy");
		String dateString = df1.format(date);
		return dateString;
	}
	
	
	// creates array of daynumbers with padding for square grid
	// and different colours for active month and days
	public void monthList(int mm, int yy) {
		// The number of days
		// to leave blank at
		// the start of this month.
		int l_trailingSpaces = 0;
		int l_daysInPrevMonth = 0;
		int l_prevMonth = 0;
		int l_prevYear = 0;
		int l_nextMonth = 0;
		int l_nextYear = 0;

		GregorianCalendar l_calendar = new GregorianCalendar(yy, mm,
				m_currentDayOfMonth);

		// Days in Current Month
		m_daysInMonth = m_daysOfMonth[mm];
		int l_currentMonth = mm;
		if (l_currentMonth == 11) {
			l_prevMonth = 10;
			l_daysInPrevMonth = m_daysOfMonth[l_prevMonth];
			l_nextMonth = 0;
			l_prevYear = yy;
			l_nextYear = yy + 1;
		} else if (l_currentMonth == 0) {
			l_prevMonth = 11;
			l_prevYear = yy - 1;
			l_nextYear = yy;
			l_daysInPrevMonth = m_daysOfMonth[l_prevMonth];
			l_nextMonth = 1;
		} else {
			l_prevMonth = l_currentMonth - 1;
			l_nextMonth = l_currentMonth + 1;
			l_nextYear = yy;
			l_prevYear = yy;
			l_daysInPrevMonth = m_daysOfMonth[l_prevMonth];
		}

		l_trailingSpaces = l_calendar.get(Calendar.DAY_OF_WEEK) ;

		// add extraday if this month is February and this year is a leap year
		if (l_calendar.isLeapYear(l_calendar.get(Calendar.YEAR)) && mm == 1) {
			++m_daysInMonth;
		}
		int dayID = Integer.parseInt(m_year + "" + m_month + ""
				+ m_currentDayOfMonth);
		// Compute padding days after the last day of this month
		// set font colour for those cells to grey.
		for (int i = 0; i < l_trailingSpaces; i++) {
			/*
			 * m_dayList.add(String .valueOf((l_daysInPrevMonth -
			 * l_trailingSpaces + 1) + i) + "-GRAY" + "-" +
			 * m_months[l_prevMonth] + "-" + l_prevYear);
			 */
			m_dayList.add(new DayEntry(dayID, Color.GRAY, String
					.valueOf((l_daysInPrevMonth - l_trailingSpaces + 1) + i)
					+ "-" + m_months[l_prevMonth] + "-" + l_prevYear));
		}

		// Compute if this day is in the active month, if so set fontcolour
		// to white, or set to cyan if this day is IRL today
		for (int l_i = 1; l_i <= m_daysInMonth; l_i++) {
			if (l_i == m_currentDayOfMonth) {
				/*
				 * m_dayList.add(String.valueOf(l_i) + "-CYAN" + "-" +
				 * m_months[mm] + "-" + yy);
				 */

				m_dayList.add(new DayEntry(dayID, Color.CYAN, String.valueOf(l_i)
						+ "-" + m_months[mm] + "-" + yy));
			} else {
				/*
				 * m_dayList.add(String.valueOf(l_i) + "-WHITE" + "-" +
				 * m_months[mm] + "-" + yy);
				 */

				m_dayList.add(new DayEntry(dayID, Color.WHITE, String.valueOf(l_i)
						+ "-" + m_months[mm] + "-" + yy));
			}
		}

		// Compute padding days before the first day of this month
		// set fontcolour for those cells to grey
		for (int l_i = 0; l_i < m_dayList.size() % 7; l_i++) {
			/*
			 * m_dayList.add(String.valueOf(l_i + 1) + "-GRAY" + "-" +
			 * m_months[l_nextMonth] + "-" + l_nextYear);
			 */

			m_dayList.add(new DayEntry(dayID, Color.GRAY, String.valueOf(l_i + 1)
					+ "-" + m_months[l_nextMonth] + "-" + l_nextYear));
		}
	}


	@Override
	protected void computeTime() {
		// TODO Auto-generated method stub
		
	}


	@Override
	protected void computeFields() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void add(int field, int amount) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void roll(int field, boolean up) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getMinimum(int field) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getMaximum(int field) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getGreatestMinimum(int field) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int getLeastMaximum(int field) {
		// TODO Auto-generated method stub
		return 0;
	}

}