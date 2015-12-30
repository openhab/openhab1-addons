package org.openhab.binding.dscalarm;

public interface DSCAlarmActionProvider {

	/**
	 * Sends a DSC Alarm command
	 * @param command
	 * @param data TODO
	 * 
	 * @return
	 */
	public boolean sendDSCAlarmCommand(String command, String data);
	
}
