package com.sourcedave.bulbasaur;

import lifx.java.android.client.LFXClient;
import lifx.java.android.entities.LFXTypes.LFXPowerState;
import lifx.java.android.light.LFXLightCollection;
import lifx.java.android.network_context.LFXNetworkContext;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	private LFXNetworkContext localNetworkContext;
	private LFXLightCollection lightsCollection;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button connect = (Button) findViewById(R.id.connect);
		connect.setOnClickListener(new OnClickListener() { public void onClick(View v) { connect(); } });

		Button on = (Button) findViewById(R.id.on);
		on.setOnClickListener(new OnClickListener() { public void onClick(View v) { turnOn(); } });

		Button off = (Button) findViewById(R.id.off);
		off.setOnClickListener(new OnClickListener() { public void onClick(View v) { turnOff(); } });
	}
	
	public void connect() {
		// Connection logic
		localNetworkContext = LFXClient.getSharedInstance(this).getLocalNetworkContext();
		localNetworkContext.connect();
		
		lightsCollection = localNetworkContext.getAllLightsCollection();
		Log.e("LIFX", String.format("Number of lights detected: %d", lightsCollection.getLights().size()));
		
	}
	
	public void turnOff() {
		Log.e("LIFX", "Turning lights off..");
		lightsCollection.setPowerState(LFXPowerState.OFF);
	}

	public void turnOn() {
		Log.e("LIFX", "Turning lights on..");
		lightsCollection.setPowerState(LFXPowerState.ON);
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
