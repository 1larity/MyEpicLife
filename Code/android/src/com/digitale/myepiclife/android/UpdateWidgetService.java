package com.digitale.myepiclife.android;


import java.util.Random;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
  
  @Override
  public void onStart(Intent intent, int startId) {
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this
        .getApplicationContext());

    int[] allWidgetIds = intent
        .getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

    for (int widgetId : allWidgetIds) {
      // create some random data
      int number = (new Random().nextInt(100));

      RemoteViews remoteViews = new RemoteViews(this
          .getApplicationContext().getPackageName(),
          R.layout.widget_layout);
      Log.w("WidgetExample", String.valueOf(number));
      // Set the text
      remoteViews.setTextViewText(R.id.update,
          "Random: " + String.valueOf(number));

      // Register an onClickListener
      Intent clickIntent = new Intent(this.getApplicationContext(),
          MyWidgetProvider.class);

      clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
      clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
          allWidgetIds);

      PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent,
          PendingIntent.FLAG_UPDATE_CURRENT);
      remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
      appWidgetManager.updateAppWidget(widgetId, remoteViews);
   
      RemoteViews v = MyService.updateWidgetListView(widgetId,
				getApplicationContext(), getPackageName());
      appWidgetManager.updateAppWidget(widgetId, v);
    }
    stopSelf();

    super.onStart(intent, startId);
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
} 