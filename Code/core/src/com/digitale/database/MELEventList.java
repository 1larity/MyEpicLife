package com.digitale.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

/**
 * package MELEvents into an arrayList and add convinience methods for sorting
 * deleting &&
 * 
 * @author rich
 *
 */
public class MELEventList extends ArrayList<MELEvent> implements MELEventListInterface {

	private static final long serialVersionUID = 4566684635916007100L;
	private static final int BAD = 1;
	private static final int GOOD = 0;
	private String TAG = "MELEventLIST Class: ";

	/** Create blank Event **/
	public MELEventList() {

	}

	@Override
	/**Delete event specified by UID**/
	public void deleteEvent(long uid) {
		// delete this event
		System.out.println(TAG + "Delete event pressed, UID:" + uid);

		Iterator<MELEvent> listIterator = MyEpicLife.eventList.listIterator();
		boolean found = false;
		MELEvent cursor = null;
		while (listIterator.hasNext() && !found) {
			cursor = listIterator.next();
			System.out.println(TAG + "Searching event, UID:" + cursor.getUid());
			if (cursor.getUid() == uid) {
				found = true;
				listIterator.remove();
				System.out.println(TAG + "removing event " + cursor.getUid());
			}
		}
		if (!found)
			System.out.println(TAG + "no such event event found for deletion with id " + uid);

	}

	@Override
	/**Find event specified by UID**/
	public MELEvent getEvent(long uid) {
		Iterator<MELEvent> listIterator = MyEpicLife.eventList.listIterator();
		boolean found = false;
		MELEvent cursor = null;
		while (listIterator.hasNext() && !found) {
			cursor = listIterator.next();
			System.out.println(TAG + "Searching event, UID:" + cursor.getUid());
			if (cursor.getUid() == uid) {
				found = true;
				System.out.println(TAG + "returning event " + cursor.getUid());
			}
		}
		if (!found)
			System.out.println(TAG + "no such event event found with id " + uid);
		return cursor;
	}

	public int getXP() {
		return modCount;

	}

	public int getAwardPoints() {
		int pointsTotal = 0;
		// loop through events
		for (MELEvent currentEvent : MyEpicLife.eventList) {
			// loop through event awards
			for (AwardedDef currentAward : currentEvent.getAwardsList()) {
				// loop through awarddefs
				for (AwardDef currentAwardDef : MyEpicLife.awardList) {
					// get award def using ID
					if (currentAwardDef.getUid() == currentAward.getAwardId()) {
						// add award points for good stuff
						if (currentEvent.getGoodBad() == GOOD) {
							pointsTotal = pointsTotal + currentAwardDef.getPointValue();
						} else {
							// subtract award points for bad stuff
							pointsTotal = pointsTotal - currentAwardDef.getPointValue();
						}
					}
				}
			}
		}
		return pointsTotal;
	}

	public int getExpPoints() {
		int pointsTotal = 0;
		// loop through events
		for (MELEvent currentEvent : MyEpicLife.eventList) {

			// loop through completions
			for (CompletionDef currentCompletion : currentEvent.getCompletionList()) {
				// get award def using ID
				if (currentCompletion.getCompletionStatus().equals("completed")) {
					// add award points for good stuff
					if (currentEvent.getGoodBad() == GOOD) {
						pointsTotal = pointsTotal + 10;
					} else {
						// subtract award points for bad stuff
						pointsTotal = pointsTotal - 10;
					}

				}
			}
		}
		return pointsTotal;
	}

	public void notificationSort() {

		Collections.sort(MyEpicLife.eventList, new EventListComparator());

	}

	public void addCompletion(long uid, CompletionDef completionDef) {
		Iterator<MELEvent> listIterator = MyEpicLife.eventList.listIterator();
		boolean found = false;
		MELEvent cursor = null;
		while (listIterator.hasNext() && !found) {
			cursor = listIterator.next();
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + "Searching event, UID:" + cursor.getUid());
			if (cursor.getUid() == uid) {
				found = true;
				cursor.getCompletionList().add(completionDef);
				System.out.println(TAG + "Added completion to event " + cursor.getUid());
			}
		}
		if (!found) {
			System.out.println(TAG + "cant add completion,no event event found with id " + uid);
		} else {
			try {
				saveEventDb();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void saveEventDb() throws IOException {
		MELEventSaver EventSaver = new MELEventSaver();

		for (MELEvent i : MyEpicLife.eventList) {
			if (MyEpicLife.DEBUG)
				System.out.println(TAG + "Event List prior to save:" + i.getEventName());
		}

		EventSaver.save("eventDb.json", MyEpicLife.eventList);

	}
}
