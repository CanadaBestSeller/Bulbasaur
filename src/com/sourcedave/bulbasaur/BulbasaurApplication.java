package com.sourcedave.bulbasaur;

import lifx.java.android.client.LFXClient;
import lifx.java.android.entities.LFXHSBKColor;
import lifx.java.android.entities.LFXTypes.LFXPowerState;
import lifx.java.android.light.LFXLightCollection;
import lifx.java.android.network_context.LFXNetworkContext;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
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
		
		// TODO add handler for async connection
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
		naturalLighting(60*30);
	}
	
	//
	// UTILS
	//
	public static void toast(String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();  
	}
	
	public static void checkLocalNetwork() {
		if (localNetworkContext == null) {
            if (C.DEBUG) Log.e("LIFX", "Tried to turn lights OFF, but localNetworkContext is null. Attempting reconnection");
			// TODO attempt re-connect, make sure it is NOT null, because other logic is based on this
		}
	}

	public static void naturalLighting(int seconds) {
		checkLocalNetwork();
		if (C.DEBUG) Log.e("LIFX", String.format("Natural lighting starting: %d seconds", seconds));

		LFXLightCollection lightsCollection = localNetworkContext.getAllLightsCollection();
		LFXHSBKColor naturalSunColorStart = LFXHSBKColor.getColor(30, 1, 0.05f, 5700);
		LFXHSBKColor naturalSunColorFinal = LFXHSBKColor.getColor(30, 1, 1, 5700);

		turnOn();

		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lightsCollection.setColor(naturalSunColorStart);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		lightsCollection.setColorOverDuration(naturalSunColorFinal, seconds*1000);
	}
	
	public static class AsyncNaturalLightingTask extends AsyncTask<Integer, Void, Void> {
		protected Void doInBackground(Integer... args) {
			Integer halfTime = args[0]/2;
			
			checkLocalNetwork();

			LFXLightCollection lightsCollection = localNetworkContext .getAllLightsCollection();
			LFXHSBKColor naturalSunColorStart = LFXHSBKColor.getColor(30, 1, 0.05f, 5700);
			LFXHSBKColor naturalSunColorIntermediate = LFXHSBKColor.getColor(30, 1, 1, 5700);
			LFXHSBKColor naturalSunColorFinal = LFXHSBKColor.getColor(30, 0, 1, 5700);

			// Make handler for turnOn callback to change color ASAP & bypass hard coded sleep
			if (C.DEBUG) Log.e("LIFX", "Natural lighting TURNING ON");
			turnOn();

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (C.DEBUG) Log.e("LIFX", String.format( "Natural lighting STARTING: %d seconds", halfTime));
			lightsCollection.setColor(naturalSunColorStart);

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (C.DEBUG) Log.e("LIFX", String.format( "Natural lighting CHANGING TO COLOR: %d seconds", halfTime));
			lightsCollection.setColorOverDuration(naturalSunColorIntermediate, halfTime * 1000);

			try {
				Thread.sleep(halfTime * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (C.DEBUG) Log.e("LIFX", String.format( "Natural lighting CHANGING TO WHITE: %d seconds", halfTime));
			lightsCollection.setColorOverDuration(naturalSunColorFinal, halfTime * 1000);

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