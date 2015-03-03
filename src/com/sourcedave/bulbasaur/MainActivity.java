package com.sourcedave.bulbasaur;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupNotification();
		BulbasaurApplication.connect();
	}
	
	public void setupNotification() {
	    // Setup buttons
		Intent connectI = new Intent(this, ControlActionBroadcastReceiver.class);
		connectI.putExtra(C.CONTROL_ACTION_KEY, C.CONTROL_ACTION_CONNECT);
		PendingIntent connectPI = PendingIntent.getBroadcast(this, C.PI_CONNECT, connectI, 0);

		Intent turnOnI = new Intent(this, ControlActionBroadcastReceiver.class);
		turnOnI.putExtra(C.CONTROL_ACTION_KEY, C.CONTROL_ACTION_TURN_ON);
		PendingIntent turnOnPI = PendingIntent.getBroadcast(this, C.PI_TURN_ON, turnOnI, 0);

		Intent turnOffI = new Intent(this, ControlActionBroadcastReceiver.class);
		turnOffI.putExtra(C.CONTROL_ACTION_KEY, C.CONTROL_ACTION_TURN_OFF);
		PendingIntent turnOffPI = PendingIntent.getBroadcast(this, C.PI_TURN_OFF, turnOffI, 0);

		// Setup notification
		NotificationManager bulbasaurNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    NotificationCompat.Builder bulbasaurNotificationBuilder  = new NotificationCompat.Builder(this)
                // Dismiss notification screen after an action has been selected
				.setAutoCancel(true)
				// Make notification permanent
				.setOngoing(true)
				// Place notification on top, where it displays big view by
				// default
				.setPriority(Notification.PRIORITY_MAX)
				// Style
				.setSmallIcon(R.drawable.bulbasaur_small)
				.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.bulbasaur))
				.setContentTitle("Bulbasaur")
				.setContentText("LIFX is On!")
				// Add buttons
				.addAction(R.drawable.perm_group_sync_settings, "SYNC", connectPI)
				.addAction(R.drawable.radio_on, "ON", turnOnPI)
				.addAction(R.drawable.radio_off, "OFF", turnOffPI);
	    
	    // Save notification builder for updating notification content text later
	    BulbasaurApplication.setNotificationBuilder(bulbasaurNotificationBuilder);

	    bulbasaurNM.notify(C.BULBASAUR_NOTIFICATION_ID, bulbasaurNotificationBuilder.build());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	//
	// Button Logic
	//
	public void connect(View view) { BulbasaurApplication.connect(); }
	public void turnOn(View view) { BulbasaurApplication.turnOn(); }
	public void turnOff(View view) { BulbasaurApplication.turnOff(); }
	public void naturalLightingWakeupDemo(View view) { BulbasaurApplication.naturalLightingWakeupDemo(); }
	public void naturalLightingWakeup(View view) { BulbasaurApplication.naturalLightingWakeup(); }
	
	public void setAlarm(View view) {
		  Intent setAlarmIntent = new Intent(this, AlarmActivity.class);
		  startActivity(setAlarmIntent);
	}
}