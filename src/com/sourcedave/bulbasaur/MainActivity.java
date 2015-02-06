package com.sourcedave.bulbasaur;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setupNotification();
		BulbasaurApplication.connect();
		initButtons();
	}
	
	public void setupNotification() {
	    // Setup buttons
		Intent connectI = new Intent(this, ControlActionBroadcastReceiver.class);
		connectI.putExtra(C.CONTROL_ACTION_KEY, C.CONTROL_ACTION_CONNECT);
		PendingIntent connectPI = PendingIntent.getBroadcast(this, C.CONTROL_ACTION_CONNECT, connectI, 0);

		Intent turnOnI = new Intent(this, ControlActionBroadcastReceiver.class);
		turnOnI.putExtra(C.CONTROL_ACTION_KEY, C.CONTROL_ACTION_TURN_ON);
		PendingIntent turnOnPI = PendingIntent.getBroadcast(this, C.CONTROL_ACTION_TURN_ON, turnOnI, 0);

		Intent turnOffI = new Intent(this, ControlActionBroadcastReceiver.class);
		turnOffI.putExtra(C.CONTROL_ACTION_KEY, C.CONTROL_ACTION_TURN_OFF);
		PendingIntent turnOffPI = PendingIntent.getBroadcast(this, C.CONTROL_ACTION_TURN_OFF, turnOffI, 0);

		// Setup notification
		NotificationManager bulbasaurNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	    Notification bulbasaurNotification  = new Notification.Builder(this)
                    // Dismiss notification screen after an action has been selected
                    .setAutoCancel(true)
                    // Make notification permanent 
	    			.setOngoing(true)
	    			// Place notification on top, where it displays big view by default
	    			.setPriority(Notification.PRIORITY_MAX)
	    			// Style
                    .setSmallIcon(R.drawable.bulbasaur_small)
                    .setContentTitle("Bulbasaur")
                    .setContentText("LIFX is On!")
                    // Add buttons
                    .addAction(R.drawable.perm_group_sync_settings, "SYNC", connectPI)
                    .addAction(R.drawable.radio_on, "ON", turnOnPI)
                    .addAction(R.drawable.radio_off, "OFF", turnOffPI)
                    .build();

	    bulbasaurNM.notify(C.BULBASAUR_NOTIFICATION_ID, bulbasaurNotification);
	}
	
	public void initButtons() {
		Button connect = (Button) findViewById(R.id.connect);
		connect.setOnClickListener(new OnClickListener() { public void onClick(View v) { BulbasaurApplication.connect(); } });

		Button on = (Button) findViewById(R.id.on);
		on.setOnClickListener(new OnClickListener() { public void onClick(View v) { BulbasaurApplication.turnOn(); } });

		Button off = (Button) findViewById(R.id.off);
		off.setOnClickListener(new OnClickListener() { public void onClick(View v) { BulbasaurApplication.turnOff(); } });

		Button nl10seconds = (Button) findViewById(R.id.natural_lighting_10_seconds);
		nl10seconds.setOnClickListener(new OnClickListener() { public void onClick(View v) { BulbasaurApplication.naturalLighting(10); } });

		Button nl30minutes = (Button) findViewById(R.id.natural_lighting_30_minutes);
		nl10seconds.setOnClickListener(new OnClickListener() { public void onClick(View v) { BulbasaurApplication.naturalLighting(60*30); } });
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
}