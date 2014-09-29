/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.mqtt;

import org.apache.commons.lang.StringUtils;

/**
 * Class encapsulating the last will and testament that is published after the
 * client has gone offline.
 * 
 * @author Markus Mann
 * 
 */
public class MqttWillAndTestament {
	private String topic;
	private byte[] payload;
	private int qos = 0;
	private boolean retain = false;

	/**
	 * Create an instance of the last will using a string with the following
	 * format:<br/>
	 * topic:message:qos:retained <br/>
	 * Where
	 * <ul>
	 * <li>topic is a normal topic string (no placeholders are allowed)</li>
	 * <li>message the message to send</li>
	 * <li>qos Valid values are 0 (Deliver at most once),1 (Deliver at least
	 * once) or 2</li>
	 * <li>retain true if messages shall be retained</li>
	 * </ul>
	 * 
	 * @param string
	 *            the string to parse. If null, null is returned
	 * @return the will instance, will be null only if parameter is null
	 */
	public static MqttWillAndTestament fromString(String string) {
		if (string == null) {
			return null;
		}
		MqttWillAndTestament result = new MqttWillAndTestament();
		String[] components = string.split(":");
		for (int i = 0; i < Math.min(components.length, 4); i++) {
			String value = StringUtils.trimToEmpty(components[i]);
			switch (i) {
			case 0:
				result.setTopic(value);
				break;
			case 1:
				result.setPayload(value.getBytes());
				break;
			case 2:
				if (!"".equals(value)) {
					result.setQos(Integer.valueOf(value));
				}
				break;
			case 3:
				result.setRetain(Boolean.valueOf(value));
				break;
			}
		}
		return result;

	}

	/**
	 * @return the topic for the last will.
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Set the topic for the last will.
	 * 
	 * @param topic
	 *            the topic
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return the payload of the last will.
	 */
	public byte[] getPayload() {
		return payload;
	}

	/**
	 * Set the payload of the last will.
	 * 
	 * @param payload
	 *            the payload
	 */
	public void setPayload(byte[] payload) {
		this.payload = payload;
	}

	/**
	 * @return quality of service level.
	 */
	public int getQos() {
		return qos;
	}

	/**
	 * Set quality of service. Valid values are 0,1,2
	 * 
	 * @param qos
	 *            level.
	 */
	public void setQos(int qos) {
		if (qos >= 0 && qos <= 2) {
			this.qos = qos;
		}
	}

	/**
	 * @return true if the last will should be retained by the broker.
	 */
	public boolean isRetain() {
		return retain;
	}

	/**
	 * Set whether the last will should be retained by the broker.
	 * 
	 * @param retain
	 *            true to retain.
	 */
	public void setRetain(boolean retain) {
		this.retain = retain;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(getClass());
		sb.append("] Send '");
		if (payload != null) {
			sb.append(new String(payload));
		} else {
			sb.append(payload);
		}
		sb.append("' to topic '");
		sb.append(topic);
		sb.append("'");
		if (retain) {
			sb.append(" retained");
		}
		sb.append(" using qos mode ").append(qos);
		return sb.toString();
	}

}
