package org.openhab.io.caldav;


public interface EventNotifier {
	void eventLoaded(CalDavEvent event);
	void eventRemoved(CalDavEvent event);
	void eventBegins(CalDavEvent event);
	void eventEnds(CalDavEvent event);
}
