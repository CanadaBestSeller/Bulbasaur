package com.sourcedave.bulbasaur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
    	BulbasaurApplication.toast("ALARM RECEIVED!");
		Log.e("LIFX", "ALARM RECEVIED!");
		
		//BulbasaurApplication.naturalLightingWakeupDemo();
		BulbasaurApplication.naturalLightingWakeup();

        setResultCode(Activity.RESULT_OK);
    }
}
