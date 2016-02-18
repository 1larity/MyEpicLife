package com.digitale.myepiclife.android;


import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogActivity extends Activity{
	protected static final int RESULT_OK = 0;
	protected static final int RESULT_CANCELED = 1;
	protected static final int RESULT_POSTPONED = 2;

	  private int result = RESULT_CANCELED;
	private String dialogTitle;
	private long eventId;
	  public static final String URL = "urlpath";
	  public static final String FILENAME = "eventname";
	  public static final String EVENTNAME = "filepath";
	  public static final String EVENTID = "eventid";
	  public static final String RESULT = "result";
	  public static final String NOTIFICATION = "com.digitale.myepiclife.android.MyService.receiver";

	
	
	
@Override
protected void onCreate(Bundle savedInstanceState) {
// TODO load notifications and update them on user input
	
super.onCreate(savedInstanceState);
requestWindowFeature(Window.FEATURE_NO_TITLE);
setContentView(R.layout.floatingactivity);
//retrieve eventname from calling intent
Intent incomingIntent= getIntent();
Bundle incomingBundle = incomingIntent.getExtras();
final TextView textViewTitle = (TextView) findViewById(R.id.TextView1);
KeyguardManager km = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("IN");
kl.disableKeyguard();

PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
PowerManager.WakeLock wl=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "My_App");
wl.acquire();

if(incomingBundle!=null)
{
     dialogTitle =(String) incomingBundle.get("eventName");
     eventId =incomingBundle.getLong("eventId");
    textViewTitle.setText(dialogTitle);
}


final Button buttonComplete = (Button) findViewById(R.id.buttonComplete);
buttonComplete.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
        // Perform action on click
    	result = RESULT_OK;
    	 publishResults(dialogTitle,eventId, result);
    	//close dialog
    	 kl.reenableKeyguard();
    	finish();
    }
});
final Button buttonPostpone = (Button) findViewById(R.id.buttonPostpone);
buttonPostpone.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
        // Perform action on click
    	result = RESULT_POSTPONED;
   	 publishResults(dialogTitle, eventId, result);
   	kl.reenableKeyguard();
    	//close dialog
    	finish();
    }
});
final Button buttonAbort = (Button) findViewById(R.id.buttonAbort);
buttonAbort.setOnClickListener(new View.OnClickListener() {
    public void onClick(View v) {
        // Perform action on click
    	result = RESULT_CANCELED;
    	 publishResults(dialogTitle, eventId, result);
    	 kl.reenableKeyguard();
    	//close dialog
    	finish();
    }
});
}
private void publishResults(String eventName, long eventId, int result) {
    Intent intent = new Intent(NOTIFICATION);
    intent.putExtra(EVENTNAME, eventName);
    intent.putExtra(EVENTID, eventId);
    intent.putExtra(RESULT, result);
    sendBroadcast(intent);
  }
}
