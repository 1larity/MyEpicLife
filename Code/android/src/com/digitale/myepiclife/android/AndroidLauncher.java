package com.digitale.myepiclife.android;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.digitale.database.MELEventLoader;
import com.digitale.myepiclife.MyEpicLife;

public class AndroidLauncher extends AndroidApplication  implements MyEpicLife.MyGameCallback  {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		
		 // create an instance of MyGame, and set the callback
		MyEpicLife myGame = new MyEpicLife();
        // Since AndroidLauncher implements MyGame.MyGameCallback, we can just pass 'this' to the callback setter.
        myGame.setMyGameCallback(this);
		
		initialize(new MyEpicLife(), config);
	}
	/*
	 * add an event through intent, this doesn't require any permission just
	 * send intent to android calendar http://www.openintents.org/en/uris
	 */
	 @Override
	    public void onStartActivityA() {
	       // Intent intent = new Intent(this, ActivityA.class);
	        Intent l_intent = new Intent(Intent.ACTION_EDIT);
			l_intent.setType("vnd.android.cursor.item/event");
			// l_intent.putExtra("calendar_id", m_selectedCalendarId); //this
			// doesn't work
			l_intent.putExtra("title", "calendar intent test");
			l_intent.putExtra("description",
					"This is a simple test for calendar api");
			l_intent.putExtra("eventLocation", "@home");
			l_intent.putExtra("beginTime", System.currentTimeMillis());
			l_intent.putExtra("endTime", System.currentTimeMillis() + 1800 * 1000);
			l_intent.putExtra("allDay", 0);
			// status: 0~ tentative; 1~ confirmed; 2~ canceled
			l_intent.putExtra("eventStatus", 1);
			// 0~ default; 1~ confidential; 2~ private; 3~ public
			l_intent.putExtra("visibility", 0);
			// 0~ opaque, no timing conflict is allowed; 1~ transparency, allow
			// overlap of scheduling
			l_intent.putExtra("transparency", 0);
			// 0~ false; 1~ true
			l_intent.putExtra("hasAlarm", 1);
			try {
				startActivity(l_intent);
			} catch (Exception e) {
				Toast.makeText(this.getApplicationContext(),
						"Sorry, no compatible calendar is found!",
						Toast.LENGTH_LONG).show();
			}
	    }

	    @Override
	    public void onStartActivityB(){
	    	Context context =this.getApplicationContext();
	    	final AlarmManager m = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
	    	Intent i = new Intent(context, MyService.class);
	    	PendingIntent service = null;
	    	//start timed service
			if (service == null) {
				service = PendingIntent.getService(context, 0, i,
						PendingIntent.FLAG_CANCEL_CURRENT);
			}
			m.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), 1000,
					service);
			 
	    }

	    @Override
	    public void onStartSomeActivity(int someParameter, String someOtherParameter){
	       // Intent intent = new Intent(this, ActivityA.class);

	      //  // do whatever you want with the supplied parameters.
	        if (someParameter == 42) {
	       //     intent.putExtra(MY_EXTRA, someOtherParameter);
	        }
	     //   startActivity(intent);
	    }
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data){
	    if (resultCode == 2 && requestCode ==1){
	        System.out.println("bollox");
	    }else{
	        //do something else
	    	 System.out.println("knob");
	    }
	    }
}
