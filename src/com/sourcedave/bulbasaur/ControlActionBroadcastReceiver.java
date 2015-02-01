package com.sourcedave.bulbasaur;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ControlActionBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		int controlAction = intent.getIntExtra(C.CONTROL_ACTION_KEY, C.CONTROL_ACTION_UNKNOWN);
		if (C.DEBUG) Log.e("LIFX", String.format("Control Action: %d", controlAction));

		switch(controlAction) {
			case C.CONTROL_ACTION_CONNECT:
				BulbasaurApplication.connect();
				break;
			case C.CONTROL_ACTION_TURN_ON:
				BulbasaurApplication.turnOn();
				break;
			case C.CONTROL_ACTION_TURN_OFF:
				BulbasaurApplication.turnOff();
				break;
			default:
				throw new IllegalArgumentException("Control Action is invalid");
		}
	}
}