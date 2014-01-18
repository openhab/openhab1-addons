/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mqtt.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for in and outbound MQTT message configurations on an openHAB
 * item.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public abstract class AbstractMqttMessagePubSub implements BindingConfig {

	private static final Logger logger = LoggerFactory.getLogger(AbstractMqttMessagePubSub.class);

	private static final String TEMP_COLON_REPLACEMENT = "@COLON@";

	public enum MessageType {
		COMMAND, STATE
	}

	private String broker;

	private String topic;

	private String transformationRule;

	private MessageType messageType;

	private String itemName;

	private TransformationService transformationService;

	/**
	 * Get the name of broker to use for sending/receiving MQTT messages.
	 * 
	 * @return name as defined in configuration file.
	 */
	public String getBroker() {
		return broker;
	}

	/**
	 * Set the name of broker to use for sending/receiving MQTT messages.
	 * 
	 * @param broker
	 *            name as defined in configuration file.
	 */
	public void setBroker(String broker) {
		this.broker = broker;
	}

	/**
	 * Get the MQTT topic to which to publish/subscribe to.
	 * 
	 * @return MQTT Topic string
	 */
	public String getTopic() {
		return topic;
	}

	/**
	 * Set the MQTT topic to which to publish/subscribe to. Subscription topics
	 * may contain wild cards.
	 * 
	 * @param topic
	 *            MQTT topic string.
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}

	/**
	 * @return type of messages supported.
	 */
	public MessageType getMessageType() {
		return messageType;
	}

	/**
	 * @return item name for which this publisher/subscriber is used.
	 */
	public String getItemName() {
		return itemName;
	}

	/**
	 * @param itemName
	 *            item name for which this publisher/subscriber is used.
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	/**
	 * Split the given string into a string array using ':' as the separator. If
	 * the separator is escaped like '\:', the separator is ignored.
	 * 
	 * @param configString
	 * @return configString split into array.
	 */
	protected String[] splitConfigurationString(String configString) {

		if (StringUtils.isEmpty(configString)) {
			return new String[0];
		}

		String[] result = StringUtils
				.replaceEachRepeatedly(configString, new String[] { "\\:" },
						new String[] { TEMP_COLON_REPLACEMENT }).split(":");
		for (int i = 0; i < result.length; i++) {
			result[i] = StringUtils.replaceEachRepeatedly(result[i],
					new String[] { TEMP_COLON_REPLACEMENT },
					new String[] { ":" });
		}
		return result;
	}

	/**
	 * Set the supported message type.
	 * 
	 * @param messageType
	 */
	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	/**
	 * @return TransformationService to use to transform message content.
	 */
	public TransformationService getTransformationService() {
		return transformationService;
	}

	/**
	 * @param TransformationService
	 *            to use to transform message content.
	 */
	public void setTransformationService(TransformationService transformationService) {
		this.transformationService = transformationService;
	}

	/**
	 * @return transformation rule to use on the message content.
	 */
	public String getTransformationRule() {
		return transformationRule;
	}

	public void setTransformationRule(String transformationRule) {
		this.transformationRule = transformationRule;
	}

	/**
	 * @return transformation service name as defined in the first part of the
	 *         transformation rule.
	 */
	public String getTransformationServiceName() {
		if (StringUtils.isEmpty(getTransformationRule())
				|| getTransformationRule().equalsIgnoreCase("default")) {
			return null;
		}

		int pos = getTransformationRule().indexOf('(');
		if (pos != -1) {
			return getTransformationRule().substring(0, pos);
		} else {
			return null;
		}
	}

	/**
	 * @return param to provide to the transformation service.
	 */
	public String getTransformationServiceParam() {

		if (StringUtils.isEmpty(getTransformationServiceName())) {
			return null;
		}

		int pos = getTransformationRule().indexOf('(');
		if (pos != -1) {
			return getTransformationRule().substring(pos + 1, getTransformationRule().length() - 1);
		} else {
			return null;
		}
	}

	/**
	 * Start transformation service.
	 */
	protected void initTransformService() {

		if (getTransformationService() != null) {
			return;
		}

		BundleContext context = MqttActivator.getContext();
		transformationService = 
			TransformationHelper.getTransformationService(context, getTransformationServiceName());
		if (transformationService == null) {
			logger.debug("No transformation service found for {}", getTransformationServiceName());
		}
	}
}
