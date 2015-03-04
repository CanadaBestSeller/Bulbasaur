package com.sourcedave.bulbasaur;

import lifx.java.android.client.LFXClient;
import lifx.java.android.entities.LFXTypes.LFXPowerState;
import lifx.java.android.light.LFXLightCollection;
import lifx.java.android.network_context.LFXNetworkContext;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

public class BulbasaurApplication extends Application {

	private static LFXNetworkContext localNetworkContext;
	private static Context context;
	
	private static NotificationCompat.Builder bulbasaurNotificationBuilder;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
	}
	
	public static void connect() {
		localNetworkContext = LFXClient.getSharedInstance(context).getLocalNetworkContext();
		localNetworkContext.connect();
		
		if (C.DEBUG) Log.e("LIFX", "Connecting...");

		LFXLightCollection lightsCollection = localNetworkContext.getAllLightsCollection();
		int lightNumber = lightsCollection.getLights().size();
		toast(String.format("Connected to %d light(s)", lightNumber));
		
		// add callback handler to listen for connect on
	}

	public static void turnOn() {
		checkLocalNetwork();
        if (C.DEBUG) Log.e("LIFX", "Turning lights ON..");

		LFXLightCollection lightsCollection = localNetworkContext.getAllLightsCollection();
		lightsCollection.setPowerState(LFXPowerState.ON);
	}
	
	public static void turnOff() {
		checkLocalNetwork();
		if (C.DEBUG) Log.e("LIFX", "Turning lights OFF..");

        LFXLightCollection lightsCollection = localNetworkContext.getAllLightsCollection();
        lightsCollection.setPowerState(LFXPowerState.OFF);
	}

	public static void naturalLightingWakeupDemo() {
		new AsyncNaturalLightingTask().execute(10);
	}
	
	public static void naturalLightingWakeup() {
		new AsyncNaturalLightingTask().execute(60*30);
	}
	
	//
	// UTILS
	//
	public static void toast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();  
	}
	
	public static void checkLocalNetwork() {
		// Turn on Wifi if it is not already on
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);

			// Sleep for half a minute to let OS connect to Wifi
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (localNetworkContext == null) {
            if (C.DEBUG) Log.e("LIFX", "Local network context is NULL, attempting to connect and populate network context...");
            connect();

			// Sleep for 10 seconds to let OS connect to LIFX
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static class AsyncNaturalLightingTask extends AsyncTask<Integer, Void, Void> {
		protected Void doInBackground(Integer... args) {
			Integer halfTime = args[0]/2;
			
			checkLocalNetwork();

			LFXLightCollection lightsCollection = localNetworkContext .getAllLightsCollection();

			if (C.DEBUG) Log.e("LIFX", "[Natural lighting] Pre-setting light to DARK RED. Waiting 3 seconds...");
			lightsCollection.setColor(C.NATURAL_SUNRISE_COLOR_START);

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// Make handler for turnOn callback to change color ASAP & bypass hard coded sleep
			if (C.DEBUG) Log.e("LIFX", "[Natural lighting] Turning ON. Waiting 3 seconds...");
			turnOn();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (C.DEBUG) Log.e("LIFX", String.format( "[Natural lighting] Changing to RED: %d seconds", halfTime));
			lightsCollection.setColorOverDuration(C.NATURAL_SUNRISE_COLOR_INTERMEDIATE, halfTime * 1000);

			try {
				Thread.sleep(halfTime * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (C.DEBUG) Log.e("LIFX", String.format( "[Natural lighting] Changing to WHITE: %d seconds", halfTime));
			lightsCollection.setColorOverDuration(C.NATURAL_SUNRISE_COLOR_END, halfTime * 1000);

			return null;
		}
	 }

	public static NotificationCompat.Builder getNotificationBuilder() {
		return bulbasaurNotificationBuilder;
	}
	
	public static void setNotificationBuilder(NotificationCompat.Builder builder) {
		bulbasaurNotificationBuilder = builder;
	}
}