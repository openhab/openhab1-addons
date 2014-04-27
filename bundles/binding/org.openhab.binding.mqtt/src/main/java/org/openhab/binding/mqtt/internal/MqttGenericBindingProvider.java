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
import org.openhab.binding.mqtt.MqttBindingProvider;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.io.transport.mqtt.MqttService;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQTT Binding provider implementation. Creates MQTT configuration for OpenHAB
 * items.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class MqttGenericBindingProvider extends AbstractGenericBindingProvider implements MqttBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(MqttGenericBindingProvider.class);

	private MqttService mqttService;

	@Override
	public String getBindingType() {
		return "mqtt";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// all Item types are accepted for now..
		return;
	}

	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		final String itemName = item.getName();
		logger.trace("Starting to load MQTT config for item {}", itemName);

		if (StringUtils.isEmpty(bindingConfig)) {
			throw new BindingConfigParseException(
					"Missing mqtt binding configuration for item " + itemName);
		}

		final MqttItemConfig itemConfig = new MqttItemConfig(itemName, bindingConfig);

		// register all message consumers
		for (MqttMessageSubscriber subscriber : itemConfig.getMessageSubscribers()) {
			subscriber.setItemName(item.getName());
			mqttService.registerMessageConsumer(subscriber.getBroker(), subscriber);
		}

		// add binding change listener to clean up message consumers on item removal
		addBindingChangeListener(new BindingChangeListener() {

			@Override
			public void bindingChanged(BindingProvider provider, String changedItemName) {
				if (itemName.equals(changedItemName) && !provider.providesBindingFor(itemName)) {
					logger.debug("Removing message consumers for item {}", itemName);
					for (MqttMessageSubscriber subscriber : itemConfig.getMessageSubscribers()) {
						mqttService.unregisterMessageConsumer(subscriber.getBroker(), subscriber);
					}
				}
			}

			@Override
			public void allBindingsChanged(BindingProvider provider) {
				if (!provider.providesBindingFor(itemName)) {
					logger.debug("Removing message consumers for item {}", itemName);
					for (MqttMessageSubscriber subscriber : itemConfig.getMessageSubscribers()) {
						mqttService.unregisterMessageConsumer(subscriber.getBroker(), subscriber);
					}
				}
			}
		});

		// register all message producers
		for (MqttMessagePublisher publisher : itemConfig.getMessagePublishers()) {
			publisher.setItemName(item.getName());
			mqttService.registerMessageProducer(publisher.getBroker(), publisher);
		}

		// add binding change listener to clean up message publishers on item
		// removal
		addBindingChangeListener(new BindingChangeListener() {

			@Override
			public void bindingChanged(BindingProvider provider, String changedItemName) {
				if (itemName.equals(changedItemName) && !provider.providesBindingFor(itemName)) {
					logger.debug("Removing message publishers for item {}", itemName);
					for (MqttMessagePublisher publisher : itemConfig.getMessagePublishers()) {
						mqttService.unregisterMessageProducer(publisher.getBroker(), publisher);
					}
				}
			}

			@Override
			public void allBindingsChanged(BindingProvider provider) {
				if (!provider.providesBindingFor(itemName)) {
					logger.debug("Removing message publishers for item {}", itemName);
					for (MqttMessagePublisher publisher : itemConfig.getMessagePublishers()) {
						mqttService.unregisterMessageProducer(publisher.getBroker(), publisher);
					}
				}
			}
		});

		addBindingConfig(item, itemConfig);
	}

	@Override
	public MqttItemConfig getItemConfig(String itemName) {
		return (MqttItemConfig) bindingConfigs.get(itemName);
	}

	/**
	 * Setter for Declarative Services. Adds the MqttService instance.
	 * 
	 * @param mqttService
	 *            Service.
	 */
	public void setMqttService(MqttService mqttService) {
		this.mqttService = mqttService;
	}

	/**
	 * Unsetter for Declarative Services.
	 * 
	 * @param mqttService
	 *            Service to remove.
	 */
	public void unsetMqttService(MqttService mqttService) {
		this.mqttService = null;
	}

}
