/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides battery infos for all Homematic devices.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class HmBatteryTypeProvider {
	private static Map<String, HmBattery> batteries = new HashMap<String, HmBattery>();

	static {
		batteries.put("HM-RC-4", new HmBattery(HmBatteryType.CR2016, 1));
		batteries.put("HM-RC-4-B", new HmBattery(HmBatteryType.CR2016, 1));
		batteries.put("HM-RC-SEC3", new HmBattery(HmBatteryType.CR2016, 1));
		batteries.put("HM-RC-SEC3-B", new HmBattery(HmBatteryType.CR2016, 1));
		batteries.put("HM-RC-P1", new HmBattery(HmBatteryType.CR2016, 1));
		batteries.put("HM-RC-KEY3", new HmBattery(HmBatteryType.CR2016, 1));
		batteries.put("HM-RC-KEY3-B", new HmBattery(HmBatteryType.CR2016, 1));

		batteries.put("HM-PB-2-WM", new HmBattery(HmBatteryType.CR2032, 1));
		batteries.put("HM-SWI-3-FM", new HmBattery(HmBatteryType.CR2032, 1));
		batteries.put("HM-PBI-4-FM", new HmBattery(HmBatteryType.CR2032, 1));
		batteries.put("HM-SCI-3-FM", new HmBattery(HmBatteryType.CR2032, 1));
		batteries.put("HM-SEC-TIS", new HmBattery(HmBatteryType.CR2032, 1));

		batteries.put("HM-SEC-SC", new HmBattery(HmBatteryType.LR44, 2));
		batteries.put("HM-SEC-SC-2", new HmBattery(HmBatteryType.LR44, 2));
		batteries.put("ZEL STG RM FFK", new HmBattery(HmBatteryType.LR44, 2));
		batteries.put("HM-SEC-RHS", new HmBattery(HmBatteryType.LR44, 2));

		batteries.put("HM-SEC-MDIR", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-SEC-MDIR-2", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-CC-TC", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-CC-VD", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-WDS100-C6-O", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-WDS40-TH-I", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-WDS10-TH-O", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-WDS30-T-O", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-SEC-KEY", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-SEC-KEY-S", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-SEC-SD", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-SEN-MDIR", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-SEN-MDIR-SM", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-SEN-MDIR-O", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-SEN-MDIR-O-2M", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-CCU-1", new HmBattery(HmBatteryType.AA, 4));
		batteries.put("HM-CC-RT-DN", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-SEC-WDS-2", new HmBattery(HmBatteryType.AA, 2));
		batteries.put("HM-WDC7000", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-SEN-WA-OD", new HmBattery(HmBatteryType.AA, 3));
		batteries.put("HM-DIS-TD-T", new HmBattery(HmBatteryType.AA, 2));

		batteries.put("HM-RC-4-2", new HmBattery(HmBatteryType.AAA, 1));
		batteries.put("HM-RC-KEY4-2", new HmBattery(HmBatteryType.AAA, 1));
		batteries.put("HM-RC-SEC4-2", new HmBattery(HmBatteryType.AAA, 1));
		batteries.put("HM-RC-12", new HmBattery(HmBatteryType.AAA, 3));
		batteries.put("HM-RC-12-W", new HmBattery(HmBatteryType.AAA, 3));
		batteries.put("HM-RC-12-B", new HmBattery(HmBatteryType.AAA, 3));
		batteries.put("HM-RC-19", new HmBattery(HmBatteryType.AAA, 3));
		batteries.put("HM-RC-19-B", new HmBattery(HmBatteryType.AAA, 3));
		batteries.put("HM-RC-19-W", new HmBattery(HmBatteryType.AAA, 3));
		batteries.put("HM-PB-4DIS-WM", new HmBattery(HmBatteryType.AAA, 3));
		batteries.put("HM-SEN-EP", new HmBattery(HmBatteryType.AAA, 2));
		batteries.put("HM-PB-2-WM55-2", new HmBattery(HmBatteryType.AAA, 2));
		batteries.put("HM-PB-6-WM55", new HmBattery(HmBatteryType.AAA, 2));
		batteries.put("HM-SEC-SCO", new HmBattery(HmBatteryType.AAA, 1));
		batteries.put("HM-TC-IT-WM-W-EU", new HmBattery(HmBatteryType.AAA, 2));
		batteries.put("HM-WDS30-TO", new HmBattery(HmBatteryType.AAA, 2));

		batteries.put("HM-Sec-Win", new HmBattery(HmBatteryType.WINMATIC_ACCU, 1));
	}

	/**
	 * Returns the battery info of the specified device type.
	 */
	public static HmBattery getBatteryType(String deviceType) {
		return batteries.get(deviceType.toUpperCase());
	}
}
