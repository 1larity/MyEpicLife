package com.digitale.myepiclife.android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.digitale.database.EventListComparator;
import com.digitale.database.MELEvent;
import com.digitale.database.MELEventList;
import com.digitale.database.MELEventLoader;
import com.digitale.myepiclife.DesktopTimer;
import com.digitale.myepiclife.MELCalendar;
import com.digitale.myepiclife.MyEpicLife;

// alarm service
public class MyService extends Service {
	protected static final int RESULT_OK = 0;
	protected static final int RESULT_CANCELED = 1;
	protected static final int RESULT_POSTPONED = 2;
	private MediaPlayer mediaPlayer;
	public static boolean DEBUG = false;
	private String TAG = "WIDGETTIMEDSERVICE: ";
	public boolean timerEnabled=true;
	// widget friendly data structure of summary event information
	static ArrayList<widgetEvent> wigetEvents = new ArrayList<widgetEvent>();
	// event data as stored by main app
	public static MELEventList eventList = new MELEventList();

	// reciever for dialog result intent
	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			String status = "unset";
			if (bundle != null) {
				long eventId = bundle.getLong(DialogActivity.EVENTID);
				String eventName = (String) bundle.get(DialogActivity.EVENTNAME);
				int resultCode = bundle.getInt(DialogActivity.RESULT);
				System.out.println(TAG + "User pressed" + resultCode);
				if (resultCode == RESULT_OK) {
					Toast.makeText(MyService.this, eventName + " Completed ", Toast.LENGTH_LONG)
							.show();
					// TODO log event as completed and stop alarm
					shutdownMedia();
					status = "completed";

				} else if (resultCode == RESULT_CANCELED) {
					Toast.makeText(MyService.this, eventName + " Canceled", Toast.LENGTH_LONG)
							.show();
					// TODO log event as cancelled and stop alarm
					shutdownMedia();
					status = "cancelled";
				} else if (resultCode == RESULT_POSTPONED) {
					Toast.makeText(MyService.this, eventName + " Postponed", Toast.LENGTH_LONG)
							.show();
					// TODO log event as postponed and stop alarm for 5 mins
					shutdownMedia();
					status = "postpone";
				}
				// if result from user is not postponed, complete the event

				if (resultCode != RESULT_POSTPONED) {
					// update event list appropriately
					for (MELEvent currentEvent : eventList) {
						if (currentEvent.getUid() == eventId) {
							currentEvent.completeEvent(status);
							try {
								saveEventDb();
								MyEpicLife.desktopTimer.dataInvalidated = true;
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}
		}

		private void shutdownMedia() {
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying())
					mediaPlayer.stop();
				mediaPlayer.reset();
				mediaPlayer.release();
				mediaPlayer = null;
			}
		}
	};


	public void saveEventDb() throws IOException {
		WidgetMELEventSaver EventSaver = new WidgetMELEventSaver();

		for (MELEvent currentEvent : eventList) {
			if (DEBUG)System.out.println(TAG + "Event List prior to save:" + currentEvent.getEventName());
		}

		EventSaver.save("eventDb.json", eventList);

	}

	@Override
	public void onCreate() {
		DEBUG=MyEpicLife.DEBUG;
		timerEnabled=DesktopTimer.timerEnabled;
			MyEpicLife.callEventIntent("STARTSERVICE");
		if (DEBUG)
			System.out.println(TAG + "Start widget timer");
		
		
		super.onCreate();

		updateMELData();
		// register receiver for dialog messaging
		registerReceiver(receiver, new IntentFilter(DialogActivity.NOTIFICATION));
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		buildUpdate();

		return super.onStartCommand(intent, flags, startId);
	}

	private void buildUpdate() {
		String objectString = new MELCalendar().MELgetShortNowTimeWithSeconds();

		AppWidgetManager manager = AppWidgetManager.getInstance(this);
		ComponentName thisWidget = new ComponentName(this, MyWidgetProvider.class);
		int[] ids = manager.getAppWidgetIds(thisWidget);
		final int N = ids.length;
		for (int i = 0; i < N; i++) {
			int awID = ids[i];
			RemoteViews v = new RemoteViews(getPackageName(), R.layout.widget_layout);

			v.setTextViewText(R.id.update, objectString);
			manager.updateAppWidget(awID, v);

			// if seconds are 10 do update on wiget list
			if (objectString.substring(objectString.length() - 1).equals("0")) {
				if (DEBUG)
					System.out.println(TAG + "Updating widget list");
				// read in event data
				if (timerEnabled){
				updateMELData();
				checkAlarms();
				}
				updateRemoteList(manager, ids, i);

			}
		}
	}

	private void updateRemoteList(AppWidgetManager manager, int[] ids, int i) {
		RemoteViews remoteViews = updateWidgetListView(ids[i], getApplicationContext(),
				getPackageName());
		manager.updateAppWidget(ids[i], remoteViews);
		manager.notifyAppWidgetViewDataChanged(ids[i], R.id.listView1);
	}

	/** checks if an alarm is pending **/
	private void checkAlarms() {

		for (MELEvent currentEvent : eventList) {
			if (currentEvent.isAlarmDue() && !(currentEvent.getRepeat().getRepeatType().equals("log"))) {
				if (DEBUG)
					System.out.println(TAG + "AlarmDue " + currentEvent.getEventName());
				// play alarm sound

				if (mediaPlayer == null) {
					mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bouncyalarm);
					mediaPlayer.setLooping(true);
					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mediaPlayer.start();
				}
				// prepare dialog intent
				Intent i = new Intent();
				i.setClass(this, DialogActivity.class);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// add event name data to intent
				i.putExtra("eventName", currentEvent.getEventName());
				i.putExtra("eventId", currentEvent.getUid());
				// launch alarm dialog
				startActivity(i);
			}
		}
	}

	/**
	 * Refresh widget event list from file
	 **/
	private void updateMELData() {
        WidgetMELEventloader MELLoader = new WidgetMELEventloader();
		eventList.clear();
		eventList = (MELEventList) MELLoader.load("eventDb.json");
		if (eventList.isEmpty()) {
			if (DEBUG)
				System.out.println(TAG + "Eventlist empty, not loading ");
		} else {
			eventList.notificationSort();
			wigetEvents.clear();
			if (DEBUG)
				System.out.println(TAG + "Eventlist is populated, loading. ");
			for (MELEvent melEvent : eventList) {
				if (DEBUG)
					System.out.println(TAG + "Event loaded " + melEvent.getEventName());
				wigetEvents.add(new widgetEvent(melEvent.getEventName(), melEvent
						.getNextNotification()));
			}
		}

		Collections.sort(wigetEvents, new WidgetListComparator());

	}

	/** update remote list view **/
	public static RemoteViews updateWidgetListView(int appWidgetId, Context context,
			String packagename) {

		// which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(packagename, R.layout.widget_layout);

		// RemoteViews Service needed to provide adapter for ListView
		Intent svcIntent = new Intent(context, WidgetService.class);
		// passing app widget id to that RemoteViews Service
		svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
		// setting a unique Uri to the intent
		// don't know its purpose to me right now
		svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
		// setting adapter to listview of the widget
		remoteViews.setRemoteAdapter(appWidgetId, R.id.listView1, svcIntent);
		// setting an empty view in case of no data
		remoteViews.setEmptyView(R.id.listView1, R.id.empty_view);
		return remoteViews;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}