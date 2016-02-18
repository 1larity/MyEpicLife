package com.digitale.myepiclife;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.digitale.database.AwardDef;
import com.digitale.database.AwardedDef;
import com.digitale.database.MELEvent;
import com.digitale.database.MELEventList;
import com.digitale.database.MELEventLoader;
import com.digitale.database.MELEventSaver;
import com.digitale.screens.CustomDialog;

public class DesktopTimer implements DesktopTimerInterface {
	private Timer timer = new Timer();
	private String TAG = "DESKTOPTIMER: ";
	private static Stage stage;
	private static boolean dialogShowing;
	MELEventList alarmList = new MELEventList();
	CustomDialog alarmDialog;
	CustomDialog awardDialog;
	public boolean timerStarted = false;
	public boolean dataInvalidated = false;
	public static boolean timerEnabled=true;
	public void start() {

		timerStarted = true;
		stage = new Stage();
		dialogShowing = false;

		timer.scheduleAtFixedRate(new TimerTask() {
			

			public void run() {
				if (timerEnabled==true){
				MELEventLoader MELLoader = new MELEventLoader();
				alarmList = (MELEventList) MELLoader.load("eventDb.json");
				if (MyEpicLife.DEBUG){
					System.out.println(TAG + "dialog showing " + dialogShowing);
				System.out.println("Tick-Tock");
				}
				if (Gdx.app.getType() == ApplicationType.Desktop) {
					// Do timer stuff for desktop
					checkAlarms();
				}
				// do timer stuff for all platforms
				checkAwards();
				}
			}

		}, 10000, 10000);

	}



	private void checkAwards() {
		if (MyEpicLife.DEBUG)
			System.out.println(TAG + "Checking awards");
		// have to local copy of global event list or concurrent
		// access errors will occur
		for (MELEvent currentEvent : alarmList) {
			// inner loop on awarddefs
			for (AwardDef currentAward : MyEpicLife.awardList)
				// check event to see if award ID is not in award list
				if (!isAlreadyAwarded(currentEvent, currentAward.getUid())) {
					if (MyEpicLife.DEBUG)System.out.println(TAG + "Award NOT completed " + currentAward.getText()
							+ " for event " + currentEvent.getEventName());

					// if so, decode award rule
					String[] rowData = currentAward.getRule().split("-");
					// check for streak award
					if (rowData[0].equals("streak")) {
						if (MyEpicLife.DEBUG)
							System.out.println(TAG + "Comparing for Streak "
									+ currentAward.getText() + " for event "
									+ currentEvent.getEventName());
						if (currentEvent.getStreakAward(Integer.valueOf(rowData[1]))) {
							if (MyEpicLife.DEBUG)
								System.out.println(TAG + "Streak Award Completed "
										+ currentAward.getText() + " for event "
										+ currentEvent.getEventName());
							// play alarm sound

							// prepare dialog
							if (dialogShowing == false) {
								showAwardDialog(currentAward, currentEvent);
								awardDialog.show(stage);
								dialogShowing = true;
							}
						}
						// check for streak award
					} else if (rowData[0].equals("count")) {
						if (MyEpicLife.DEBUG)System.out.println(TAG + "Comparing for Count " + currentAward.getText()
								+ " for event " + currentEvent.getEventName() + "required "
								+ currentAward.getRule() + "have "
								+ currentEvent.getCompletedEventCount());
						if (currentEvent.getTotalAward(Integer.valueOf(rowData[1]))) {
							if (MyEpicLife.DEBUG)
								System.out.println(TAG + "Competion Count AwardDue "
										+ currentEvent.getEventName() + dialogShowing);
							// play alarm sound

							// prepare dialog
							if (dialogShowing == false) {
								showAwardDialog(currentAward, currentEvent);
								awardDialog.show(stage);
								dialogShowing = true;
							}
						}
						// check award rule against event stats
					}

				}
		}
	}
	/**
	 * check to see if specified event has already received the specified award
	 * 
	 * @param currentEvent
	 * @param AwardID
	 * @return
	 */
	private boolean isAlreadyAwarded(MELEvent currentEvent, int AwardID) {
		boolean result = false;
		if(!currentEvent.getAwardsList().isEmpty()){
		for (AwardedDef currentAwarded : currentEvent.getAwardsList()) {
			if (MyEpicLife.DEBUG)System.out.println(TAG + "Award compare event: current award "
					+ currentAwarded.getAwardId() + " Awarddef ID " + AwardID);
			if(AwardID == currentAwarded.getAwardId()){
				if (MyEpicLife.DEBUG)System.out.println(TAG + "Award ids match");
				result=true;
			}
		}
		}else{
			if (MyEpicLife.DEBUG)System.out.println(TAG + "Award List is empty "
					+  " Awarddef ID " + AwardID);
		}
		return result;
	}
	private void showAwardDialog(final AwardDef currentAward, final MELEvent currentEvent) {
		awardDialog = new CustomDialog(currentAward.getTitle() + " award.") {

			protected void result(Object object) {
				if (MyEpicLife.DEBUG)System.out.println(TAG + "Chosen: " + object);
				String status = "unset";
				
				if (("" + object).equals("true")) {

				}
				//update master event list
				for (MELEvent eventCursor : MyEpicLife.eventList) {
					if (eventCursor.getUid() == currentEvent.getUid()) {
						eventCursor.addAward(currentAward);
					}	
					}
				try {
					saveEventDb();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialogShowing = false;

			}
		};
		if (MyEpicLife.DEBUG)
			awardDialog.debugAll();
		//set grammar of award screen
		if (MyEpicLife.eventName.equals("event")){
		awardDialog.text(currentAward.getText().replace("%E", MyEpicLife.eventName).replace("a ", "an ") + " For "
				+ currentEvent.getEventName() + " " + MyEpicLife.eventName+".");
		}
		awardDialog.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);

		awardDialog.button("OK", true).setSize(300, 300);
		awardDialog.key(Keys.ENTER, true);
		awardDialog.getContentTable().layout();
		awardDialog.getButtonTable().layout();
		awardDialog.pack();

	}

	/** checks if an alarm is pending **/
	private void checkAlarms() {
		// have to local copy of global event list or concurrent
		// access errors will occur
		for (MELEvent currentEvent : alarmList) {
			//if alarm is due and this is not a logging event
			if (currentEvent.isAlarmDue()&& !(currentEvent.getRepeat().getRepeatType().equals("log"))) {
				if (MyEpicLife.DEBUG)
					System.out.println(TAG + "AlarmDue " + currentEvent.getEventName()
							+ dialogShowing);
				// play alarm sound

				// prepare dialog
				if (dialogShowing == false) {

					showAlarmDialog(currentEvent);
					alarmDialog.show(stage);
					dialogShowing = true;
				}
			}
		}
	}

	private void showAlarmDialog(final MELEvent currentEvent) {

		alarmDialog = new CustomDialog("Alarm " + currentEvent.getEventName()) {

			protected void result(Object object) {
				if (MyEpicLife.DEBUG)System.out.println(TAG + "Chosen: " + object);
				String status = "unset";
				if (("" + object).equals("true")) {
					status = "completed";
					dataInvalidated = true;

				} else {
					status = "cancelled";
					dataInvalidated = true;
				}
				for (MELEvent eventCursor : MyEpicLife.eventList) {
					if (eventCursor.getUid() == currentEvent.getUid()) {
						eventCursor.completeEvent(status);
						try {
							saveEventDb();
							dialogShowing = false;

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			}
		};
		if (MyEpicLife.DEBUG)
			alarmDialog.debugAll();
		alarmDialog.text("Complete this " + MyEpicLife.eventName + "?");
		alarmDialog.setSize(Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 3);

		alarmDialog.button("Complete", true).setSize(300, 300);
		alarmDialog.button("Abort", false).setSize(300, 300);
		alarmDialog.key(Keys.ENTER, true);
		alarmDialog.key(Keys.ESCAPE, false);
		alarmDialog.getContentTable().layout();
		alarmDialog.getButtonTable().layout();
		alarmDialog.pack();

	}

	public void saveEventDb() throws IOException {
		MELEventSaver EventSaver = new MELEventSaver();

		for (MELEvent i : MyEpicLife.eventList) {
			if (MyEpicLife.DEBUG)System.out.println(TAG + "Event List prior to save:" + i.getEventName());
		}

		EventSaver.save("eventDb.json", MyEpicLife.eventList);

	}

	public static void setStage(Stage currentStage) {
		stage = currentStage;
	}

	public void dispose() {
		timer.cancel();

	}
}
