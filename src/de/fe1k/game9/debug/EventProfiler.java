package de.fe1k.game9.debug;

import de.nerogar.noise.debug.Profiler;
import de.nerogar.noise.util.Color;

public class EventProfiler extends Profiler {

	private static final int LISTENER = 0;
	private static final int EVENT    = 1;
	private static final int TIME     = 2;

	public static final int LISTENER_COUNT        = 0;
	public static final int LISTENER_REGISTERED   = 1;
	public static final int LISTENER_UNREGISTERED = 2;
	public static final int LISTENER_EXECUTED     = 3;

	public static final int EVENT_TRIGGERED = 4;

	public static final int TIME_TRIGGER = 5;

	public EventProfiler() {
		super("event", true);

		registerProperty(LISTENER_COUNT, LISTENER, new Color(1.0f, 0.0f, 0.0f, 1.0f), "listener count");
		registerProperty(LISTENER_REGISTERED, LISTENER, new Color(1.0f, 0.4f, 0.0f, 1.0f), "listener registered");
		registerProperty(LISTENER_UNREGISTERED, LISTENER, new Color(1.0f, 0.6f, 0.2f, 1.0f), "listener unregistered");
		registerProperty(LISTENER_EXECUTED, LISTENER, new Color(1.0f, 0.6f, 0.4f, 1.0f), "listener executed");

		registerProperty(EVENT_TRIGGERED, EVENT, new Color(0.0f, 0.8f, 0.0f, 1.0f), "events triggered");

		registerProperty(TIME_TRIGGER, TIME, new Color(0.0f, 0.0f, 1.0f, 1.0f), "trigger time");

	}

	@Override
	public void reset() {
		super.reset();

		setValue(LISTENER_REGISTERED, 0);
		setValue(LISTENER_UNREGISTERED, 0);
		setValue(LISTENER_EXECUTED, 0);

		setValue(EVENT_TRIGGERED, 0);

		setValue(TIME_TRIGGER, 0);
	}
}
