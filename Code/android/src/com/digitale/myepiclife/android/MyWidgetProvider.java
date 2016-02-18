package com.digitale.myepiclife.android;

//m.setRepeating(AlarmManager.RTC,System.currentTimeMillis(), 10000, service);   
import java.io.File;
import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class MyWidgetProvider extends AppWidgetProvider {
	private String TAG = "WIDGETPROVIDER: ";
	
	private PendingIntent service = null;

	public static Context context;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		final AlarmManager m = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		final Intent i = new Intent(context, MyService.class);
		System.out.println(TAG+"onUpdate method called");
		this.context=context;
		// Get all ids
		ComponentName thisWidget = new ComponentName(context,
				MyWidgetProvider.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

		// Build the intent to call the service
		Intent intent = new Intent(context.getApplicationContext(),
				UpdateWidgetService.class);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
		context.startService(intent);

		//start timed service
		if (service == null) {
			service = PendingIntent.getService(context, 0, i,
					PendingIntent.FLAG_CANCEL_CURRENT);
		}
		m.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000,
				service);
		   
		   /*
		 * int[] appWidgetIds holds ids of multiple instance of your widget
		 * meaning you are placing more than one widget on your homescreen
		 */
		final int N = appWidgetIds.length;
		for (int j = 0; j < N; ++j) {
			RemoteViews remoteViews = updateWidgetListView(appWidgetIds[j]);
			appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
			appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[j], R.id.listView1);
		/*	
			//abortive attempt at onclick listener for list view
			Intent appsIntent = new Intent(context, ListProvider.class);
			 //  String packageName = appsIntent.getStringExtra(APP_ID);
			   appsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[j]);
			   PendingIntent appsPendingIntent = PendingIntent.getActivity(context, 0, appsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			   RemoteViews views =new RemoteViews(context.getPackageName(),	R.layout.widget_layout);
			   views.setPendingIntentTemplate(R.id.imageButton, appsPendingIntent); */
			
		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}

	public static RemoteViews updateWidgetListView( int appWidgetId) {

		// which layout to show on widget
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.widget_layout);

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
	
}
