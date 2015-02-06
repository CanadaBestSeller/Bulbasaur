package com.sourcedave.bulbasaur;

import lifx.java.android.client.LFXClient;
import lifx.java.android.entities.LFXHSBKColor;
import lifx.java.android.entities.LFXTypes.LFXPowerState;
import lifx.java.android.light.LFXLightCollection;
import lifx.java.android.network_context.LFXNetworkContext;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class BulbasaurApplication extends Application {

	private static LFXNetworkContext localNetworkContext;
	private static Context context;
	
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

	public static void naturalLighting(int seconds) {
		checkLocalNetwork();
		if (C.DEBUG) Log.e("LIFX", String.format("Natural lighting starting: %d seconds", seconds));

		LFXLightCollection lightsCollection = localNetworkContext.getAllLightsCollection();
		LFXHSBKColor naturalSunColor = LFXHSBKColor.getColor(30, 1, 1, 5700);
		lightsCollection.setColorOverDuration(naturalSunColor, seconds*1000);
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
}