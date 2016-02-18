package com.digitale.myepiclife.android;

import java.util.ArrayList;

import com.digitale.myepiclife.MELCalendar;

import android.app.PendingIntent;
import android.app.LauncherActivity.ListItem;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 * 
 */
public class ListProvider implements RemoteViewsFactory {
	private ArrayList<widgetEvent> listItemList = new ArrayList<widgetEvent>();
	private Context context = null;
	private int appWidgetId;
	private String TAG = "LISTREMOTEVIEWFACTORY: ";
	MELCalendar melCalendar= new MELCalendar();
	public ListProvider(Context context, Intent intent) {
		this.context = context;
		appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		
		populateListItem();
	}
	
	public void populateListItem() {
		int i=0;
		listItemList.clear();
		for (widgetEvent currentevent: MyService.wigetEvents) {
			System.out.println(TAG+"Populate listitem "+currentevent.getEventName());
			widgetEvent listItem = new widgetEvent();
			listItem.setEventName(i+" "+currentevent.getEventName());
			listItem.setEventTime(currentevent.getEventTime());	
			listItemList.add(listItem);
			i++;
		}

	}

	@Override
	public int getCount() {
		return listItemList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/*
	 *Similar to getView of Adapter where instead of View
	 *we return RemoteViews 
	 * 
	 */
	@Override
	public RemoteViews getViewAt(int position) {
		
		final RemoteViews remoteView = new RemoteViews(
				context.getPackageName(), R.layout.list_row);
		//prevent out of bounds array due to asynch update
		if (position<listItemList.size()){
		widgetEvent listItem = listItemList.get(position);
		System.out.println(TAG+"Getview at listitem "+listItem.getEventName());
		remoteView.setTextViewText(R.id.heading, listItem.getEventName());
		remoteView.setTextViewText(R.id.content, ""+melCalendar.MELshortTimeAndDateFromLong(listItem.getEventTime()));
		remoteView.setImageViewResource(R.id.imageButton, R.drawable.clockbuttonstates);
		}
		/*// prepare dialog intent NON working click listener
		Intent i = new Intent();
		i.setClass(context.getApplicationContext(), DialogActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// add event name data to intent
		i.putExtra("eventName", listItem.getEventName());
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, i,
		              PendingIntent.FLAG_UPDATE_CURRENT);
		
		remoteView.setOnClickFillInIntent(R.id.imageButton, i);*/
		// launch alarm dialog
		
		return remoteView; 
	}
	

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onDataSetChanged() {
		populateListItem();
	}

	@Override
	public void onDestroy() {
	}

}