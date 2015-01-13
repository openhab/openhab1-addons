/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.conditionalaction.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.scriptengine.action.ActionDoc;
import org.openhab.core.scriptengine.action.ParamDoc;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class contains the methods that are made available in scripts and rules for ConditionalAction.
 * 
 * @author Juergen Richtsfeld
 * @since 1.7.0
 */
public class ConditionalAction {

	private static final String DISABLED_POSTFIX = "_Disabled";
	private static final Logger logger = LoggerFactory.getLogger(ConditionalAction.class);

	@ActionDoc(text="A method that sends a command the the actionItem if a item with the same name + _Disabled is OFF", 
			returns="<code>true</code>, if the command was sent to the actionItem and <code>false</code> otherwise.")
	public static boolean sendConditionalCommand(
			@ParamDoc(name="actionItem", text="the item that receives the command") final String actionItemName,
			@ParamDoc(name="commandString", text="the command to be sent to the item") final String commandString) {
		return sendConditionalCommand(actionItemName, actionItemName + DISABLED_POSTFIX, commandString);
	}
	
	@ActionDoc(text="A method that sends a command to the actionItem if the disabledItem is OFF", 
			returns="<code>true</code>, if the command was sent to the actionItem and <code>false</code> otherwise.")
	public static boolean sendConditionalCommand(
			@ParamDoc(name="actionItemName", text="the item that receives the command") final String actionItemName,
			@ParamDoc(name="disabledItemName", text="the item that must be disabled so the command is sent") final String disabledItemName,
			@ParamDoc(name="commandString", text="the command to be sent to the item") final String commandString) {
		
		if (!ConditionalActionActionService.isProperlyConfigured) {
			logger.debug("ConditionalAction action is not yet configured - execution aborted!");
			return false;
		}
		
		final ItemRegistry registry = (ItemRegistry) ConditionalActionActivator.itemRegistryTracker.getService();
		final EventPublisher publisher = (EventPublisher) ConditionalActionActivator.eventPublisherTracker.getService();
		if(publisher!=null && registry!=null) {
			try {
				final Item actionItem = registry.getItem(actionItemName);
				final Command command = TypeParser.parseCommand(actionItem.getAcceptedCommandTypes(), commandString);
				
				final Collection<Item> disabledItems;
				try {
					final Item disabledItem = registry.getItem(disabledItemName);
					if(disabledItem instanceof GroupItem) {
						final GroupItem disabledGroupItem = (GroupItem) disabledItem;
						disabledItems = disabledGroupItem.getAllMembers();
					} else {
						disabledItems = Collections.singleton(disabledItem);
					}
				} catch (ItemNotFoundException e) {
					// the disable item is not required, ignore
					logger.error("Did not find a item " + disabledItemName);
					return false;
				}
				
				for (final Item disabledItem : disabledItems) {
					if(disabledItem.getState() == OnOffType.ON) {
						logger.debug("not sending command to " + actionItemName + " because " + disabledItem.getName() + " is ON");
						return false;
					}				
				}
				
				publisher.sendCommand(actionItemName, command);
				return true;
			} catch (ItemNotFoundException e) {
				logger.error("Item '" + actionItemName + "' does not exist.");
				return false;
			}
		}
		
		return false;
	}
}
