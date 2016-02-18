package com.digitale.myepiclife.android;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;

public class WidgetService extends RemoteViewsService {
/*
* Define the Adapter of the listview
* here Adapter is ListProvider
* */

@Override
public RemoteViewsFactory onGetViewFactory(Intent intent) {
int appWidgetId = intent.getIntExtra(
AppWidgetManager.EXTRA_APPWIDGET_ID,
AppWidgetManager.INVALID_APPWIDGET_ID);

return (new ListProvider(this.getApplicationContext(), intent));
}

}