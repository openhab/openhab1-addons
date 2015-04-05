/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openhab.core.binding.BindingConfig;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxBindingConfig implements BindingConfig {

	public static final String TYPE_AMBIENT_LIGHT = "AmbientLight";

	public static final String TYPE_DISPLAY = "Display";

	public static final String TYPE_PAGE_INDEX = "PageIndex";

	public static final String TYPE_PROXIMITY = "Proximity";

	public static final String MESSAGE_AUDIO_PLAY_LOCAL = "AudioPlayLocal";

	public static final String MESSAGE_EDIT_VALUE = "EditValue";

	private short actorId;
	private short switchId;
	private String message;

	public short getActorId() {
		return this.actorId;
	}

	public short getSwitchId() {
		return this.switchId;
	}

	/**
	 * TODO UluxMessageId or a dedicated enum instead of String
	 */
	public String getMessage() {
		return this.message;
	}

	public void setActorId(short actorId) {
		this.actorId = actorId;
	}

	public void setSwitchId(short switchId) {
		this.switchId = switchId;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
		builder.append("switchId", this.switchId);
		builder.append("actorId", this.actorId);
		builder.append("message", this.message);

		return builder.toString();
	}
}