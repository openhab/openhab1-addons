package org.openhab.io.caldav;


public interface EventNotifier {
	void eventAdded(CalDavEvent event);
	void eventRemoved(CalDavEvent event);
}
