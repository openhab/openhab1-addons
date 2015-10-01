/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.core.jsr223.internal.engine;

import static org.openhab.core.events.EventConstants.TOPIC_PREFIX;
import static org.openhab.core.events.EventConstants.TOPIC_SEPERATOR;

import java.util.Collection;

import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.jsr223.internal.engine.scriptmanager.ScriptManager;
import org.openhab.core.jsr223.internal.shared.Rule;
import org.openhab.core.jsr223.internal.shared.TriggerType;
import org.openhab.core.types.Command;
import org.openhab.core.types.EventType;
import org.openhab.core.types.State;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is the core of the openHAB jsr223 engine. It listens to changes to the rules folder, evaluates the trigger
 * conditions of the rules and schedules them for execution dependent on their triggering conditions.
 * 
 * @author Simon Merschjohann
 * @since 1.7.0
 */
public class Jsr223Engine implements EventHandler, ItemRegistryChangeListener, StateChangeListener {

	static private final Logger logger = LoggerFactory.getLogger(Jsr223Engine.class);

	private ItemRegistry itemRegistry;

	private RuleTriggerManager triggerManager;
	private ScriptManager scriptManager;

	private Scheduler scheduler;

	private boolean activated;

	public void activate() {
		if (activated)
			return;

		activated = true;

		try {
			scheduler = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			logger.error("initializing scheduler throws exception", e);
		}

		logger.debug("activate()");

		triggerManager = new RuleTriggerManager(scheduler);
		scriptManager = new ScriptManager(triggerManager, itemRegistry);

		if (!isEnabled()) {
			logger.info("jsr223 engine is disabled.");
			return;
		}

		logger.debug("Started jsr223 engine");

		// register us on all items which are already available in the registry
		for (Item item : itemRegistry.getItems()) {
			internalItemAdded(item);
		}

		runStartupRules();
	}

	public void deactivate() {
		// execute all scripts that were registered for system shutdown
		scriptManager.executeRules(triggerManager.getRules(TriggerType.SHUTDOWN), new org.openhab.core.jsr223.internal.shared.Event(TriggerType.SHUTDOWN, null, null, null, null));
		triggerManager.clearAll();
		triggerManager = null;
	}

	public void setItemRegistry(ItemRegistry itemRegistry) {
		logger.debug("itemRegistry set");
		this.itemRegistry = itemRegistry;
		itemRegistry.addItemRegistryChangeListener(this);
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		itemRegistry.removeItemRegistryChangeListener(this);
		this.itemRegistry = null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void allItemsChanged(Collection<String> oldItemNames) {
		// add the current items again
		Collection<Item> items = itemRegistry.getItems();
		for (Item item : items) {
			internalItemAdded(item);
		}
		if (activated)
			runStartupRules();
	}

	/**
	 * {@inheritDoc}
	 */
	public void itemAdded(Item item) {
		internalItemAdded(item);
	}

	/**
	 * {@inheritDoc}
	 */
	public void itemRemoved(Item item) {
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.removeStateChangeListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stateChanged(Item item, State oldState, State newState) {
		if (triggerManager != null) {
			Iterable<Rule> rules = triggerManager.getRules(TriggerType.CHANGE, item, oldState, newState);
			scriptManager.executeRules(rules, new org.openhab.core.jsr223.internal.shared.Event(TriggerType.CHANGE, item, oldState, newState, null));
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void stateUpdated(Item item, State state) {
		if (triggerManager != null) {
			Iterable<Rule> rules = triggerManager.getRules(TriggerType.UPDATE, item, state);
			scriptManager.executeRules(rules, new org.openhab.core.jsr223.internal.shared.Event(TriggerType.UPDATE, item, null, state, null));
		}
	}

	public void receiveCommand(String itemName, Command command) {
		if (triggerManager != null && itemRegistry != null) {
			try {
				Item item = itemRegistry.getItem(itemName);
				Iterable<Rule> rules = triggerManager.getRules(TriggerType.COMMAND, item, command);
				scriptManager.executeRules(rules, new org.openhab.core.jsr223.internal.shared.Event(TriggerType.COMMAND, item, null, null, command));
			} catch (ItemNotFoundException e) {
				// ignore commands for non-existent items
				logger.trace("Received Command(ItemName={}, command={}), but item not found.", itemName, command);
			}
		}
	}

	private void internalItemAdded(Item item) {
		if (item instanceof GenericItem) {
			GenericItem genericItem = (GenericItem) item;
			genericItem.addStateChangeListener(this);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void handleEvent(Event event) {
		String itemName = (String) event.getProperty("item");

		String topic = event.getTopic();
		String[] topicParts = topic.split(TOPIC_SEPERATOR);

		if (!(topicParts.length > 2) || !topicParts[0].equals(TOPIC_PREFIX)) {
			return; // we have received an event with an invalid topic
		}
		String operation = topicParts[1];

		if (operation.equals(EventType.COMMAND.toString())) {
			Command command = (Command) event.getProperty("command");
			if (command != null)
				receiveCommand(itemName, command);
		}
	}

	private void runStartupRules() {
		if (triggerManager != null) {
			Iterable<Rule> startupRules = triggerManager.getRules(TriggerType.STARTUP);

			scriptManager.executeRules(startupRules, new org.openhab.core.jsr223.internal.shared.Event(TriggerType.STARTUP, null, null, null, null));
		}
	}

	/**
	 * we need to be able to deactivate the rule execution, otherwise the openHAB designer would also execute the rules.
	 * 
	 * @return true, if rules should be executed, false otherwise
	 */
	private boolean isEnabled() {
		return !"true".equalsIgnoreCase(System.getProperty("noJsr223"));
	}

}
