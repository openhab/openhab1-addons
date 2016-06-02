package org.openhab.binding.sonance.internal;

/**
 * IP codes to send to the amplifier. List of all possible commands:
 * https://danainnovationsupport.zendesk.com/attachments/token/
 * PHk0adMkeZX6bJN1PEBnU59lM/?name=Sonance_IP_Codes_-_Feb_2016_-_V1_3_100449.
 * xlsx
 * 
 * @author Laurens Van Acker
 * 
 */
final class SonanceConsts {
	protected static final String VOLUME_UP_3 = "FF55020E";
	protected static final String VOLUME_DOWN_3 = "FF55020F";
	protected static final String VOLUME_UP = "FF550204";
	protected static final String VOLUME_DOWN = "FF550205";
	protected static final String VOLUME_QUERY = "FF550210";

	protected static final String MUTE_ON = "FF550207";
	protected static final String MUTE_OFF = "FF550208";
	protected static final String MUTE_QUERY = "FF550212";

	protected static final String POWER_ON = "FF550101";
	protected static final String POWER_OFF = "FF550102";
	protected static final String POWER_QUERY = "FF550170";

	protected static final String DIRECT_VOLUME_QUERY = "FF5502";
}
