/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.types;

/**
 * Available zone states.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public enum ZoneState implements StateType {
	armed(0x09), really_armed(0x0a), armed_mode_2(0x0b), armed_mode_3(0x0c), first_code_entered(0x0d), entry_time(0x0e), exit_time_gt_10(
			0x0f), exit_time_lt_10(0x10), temporary_blocked(0x11), blocked_for_guard(0x12), alarm(0x13), fire_alarm(
			0x14), alarm_memory(0x15), fire_alarm_memory(0x16), violated_zones(0x25), verified_alarms(0x27), armed_mode_1(
			0x2a), warning_alarms(0x2b);

	private byte refreshCommand;

	ZoneState(int refreshCommand) {
		this.refreshCommand = (byte) refreshCommand;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte getRefreshCommand() {
		return refreshCommand;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ObjectType getObjectType() {
		return ObjectType.zone;
	}
}