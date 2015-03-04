package com.sourcedave.bulbasaur;

import lifx.java.android.entities.LFXHSBKColor;

public class C {
	static final boolean DEBUG = true;

	static final int BULBASAUR_NOTIFICATION_ID = 0;
	
	// Control Actions
	static final String CONTROL_ACTION_KEY = "CONTROL_ACTION";

	static final int CONTROL_ACTION_UNKNOWN = 0;
	static final int CONTROL_ACTION_CONNECT = 1;
	static final int CONTROL_ACTION_TURN_ON = 2;
	static final int CONTROL_ACTION_TURN_OFF = 3;
	
	// Pending Intent Unique IDs
	static final int PI_UNKNOWN = 0;
	static final int PI_CONNECT = 1;
	static final int PI_TURN_ON = 2;
	static final int PI_TURN_OFF = 3;
	static final int PI_ALARM = 4;
	
	// Natural sunrise wake-up colors
	static final LFXHSBKColor NATURAL_SUNRISE_COLOR_START = LFXHSBKColor.getColor(30, 1, 0.05f, 5700);
	static final LFXHSBKColor NATURAL_SUNRISE_COLOR_INTERMEDIATE = LFXHSBKColor.getColor(30, 1, 1, 5700);
	static final LFXHSBKColor NATURAL_SUNRISE_COLOR_END = LFXHSBKColor.getColor(30, 0.25f, 1, 5700);
}