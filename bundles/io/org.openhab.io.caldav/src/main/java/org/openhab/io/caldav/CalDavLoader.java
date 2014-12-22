package org.openhab.io.caldav;


public interface CalDavLoader {
	public void addListener(EventNotifier notifier);
	
	public void removeListener(EventNotifier notifier);
}
