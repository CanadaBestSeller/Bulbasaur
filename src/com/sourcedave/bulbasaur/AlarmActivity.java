package com.sourcedave.bulbasaur;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

public class AlarmActivity extends Activity {

	private TextView clockText;
	private ToggleButton alarmToggle;
    private AlarmManager alarmManager;
    
    private int alarmHour = 8; 
    private int alarmMinute = 0;

    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        clockText = (TextView) findViewById(R.id.clock_text);
        getPreviousAlarmTime();
        syncClockText();

        alarmToggle = (ToggleButton) findViewById(R.id.alarm_toggle);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

	public void enterAlarm(View view) { 
		TimePickerDialog tpd = new TimePickerDialog(this, 
			new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    alarmHour = hourOfDay;
                    alarmMinute = minute;
                    syncClockText();
                    if (alarmToggle.isChecked()) {
                    	setAlarm();
                    }
                    if (C.DEBUG) Log.e("LIFX", String.format("Alarm set for %d:%d", alarmHour, alarmMinute));
                }
            }, 
			8, 0, false);

		tpd.show();
	}
	
	public void getPreviousAlarmTime() {
		// TODO read previous alarm time from hard disk
	}

	public void syncClockText() {
		clockText.setText(convert24to12(alarmHour, alarmMinute));
	}
	
	public void setAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmHour);
        calendar.set(Calendar.MINUTE, alarmMinute);

        String alarmText = String.format("Alarm Scheduled at %s.", convert24to12(alarmHour, alarmMinute));
        BulbasaurApplication.toast(alarmText);
        updateNotificationText(alarmText);

        Intent alarmIntent = new Intent(AlarmActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this, C.PI_ALARM, alarmIntent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	}
	
	public String convert24to12(int hour, int minute) {
		return hour % 12 + ":" + (minute == 0 ? "00" : minute) + " " + (hour < 12 ? "AM" : "PM");
	}

    public void onToggleClicked(View view) {
        if (((ToggleButton) view).isChecked()) {
            Log.e("LIFX", "Alarm On");
            setAlarm();
        } else {
            alarmManager.cancel(pendingIntent);
            Log.e("LIFX", "Alarm Off");
        }
    }
    
	public void updateNotificationText(String text) {
	    NotificationCompat.Builder bulbasaurNotificationBuilder = BulbasaurApplication.getNotificationBuilder().setContentText(text);

		NotificationManager bulbasaurNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		bulbasaurNM.notify(C.BULBASAUR_NOTIFICATION_ID, bulbasaurNotificationBuilder.build());
	}
}