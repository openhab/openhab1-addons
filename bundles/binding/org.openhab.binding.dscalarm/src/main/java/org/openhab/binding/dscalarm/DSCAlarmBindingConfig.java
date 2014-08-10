/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dscalarm;

import org.openhab.core.binding.BindingConfig;
import org.openhab.binding.dscalarm.internal.model.DSCAlarmDeviceType;
import org.openhab.binding.dscalarm.internal.DSCAlarmItemType;

/**
 * Binding Configuration class. Represents a binding configuration in
 * the items file to a DSC Alarm system.
 * @author Russell Stephens
 * @since 1.6.0
 */
public class DSCAlarmBindingConfig implements BindingConfig {
	/**
	 * Constructor. Creates a new instance of the DSCAlarmBindingConfig class.
	 * @param dscAlarmDeviceType the DSC Alarm Device Type.
	 * @param partitionId the PartionId of the item
	 * @param zoneId the ZoneId of the item
	 * @param itemType the DSC Alarm Item Type.
	 */
	public DSCAlarmBindingConfig(DSCAlarmDeviceType dscAlarmDeviceType, int partitionId, int zoneId, DSCAlarmItemType dscAlarmItemType) {
		this.dscAlarmDeviceType = dscAlarmDeviceType; 
		this.partitionId = partitionId;
		this.zoneId = zoneId;
		this.dscAlarmItemType = dscAlarmItemType;
	}

	private DSCAlarmDeviceType dscAlarmDeviceType;
	private int partitionId;
	private int zoneId;
	private DSCAlarmItemType dscAlarmItemType;

	/**
	 * Returns the DSC Alarm Device Type.
	 * @return deviceType
	 */
	public DSCAlarmDeviceType getDeviceType() {
		return dscAlarmDeviceType;
	}

	/**
	 * Returns Partition Id.
	 * @return partitionId
	 */
	public int getPartitionId() {
		return partitionId;
	}

	/**
	 * Returns Zone Id.
	 * @return zoneId
	 */
	public int getZoneId() {
		return zoneId;
	}

	/**
	 * Returns the DSC Alarm Item Type
	 * @return dscAlarmItemType
	 */
	public DSCAlarmItemType getDSCAlarmItemType() {
		return dscAlarmItemType;
	}
}
