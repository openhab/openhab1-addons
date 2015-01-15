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
 * <p>
 * The purpose of this is to have a easy way to prevent commands being sent to an item sill allowing clean scripts.
 * <p>
 * The idea (especially with the support for using a group item as a disabledItem) is to have several reasons
 * for disabling some automation (e.g. disable shutter movement by manual disable, too much wind, or window contacts).
 * 
 * @author Juergen Richtsfeld
 * @since 1.7.0
 */
public class ConditionalAction {

	private static final String DISABLED_POSTFIX = "_Disabled";
	private static final Logger logger = LoggerFactory.getLogger(ConditionalAction.class);

	/**
	 * Same as {@link #sendConditionalCommand(Item, Item, String)} but the condition item is resolved via a name 
	 * convention (same as the action item name postfixed by &quot;_Disabled&quot;). 
	 * 
	 * @param actionItem the item that should receive a command
	 * @param command the command to send to the item
	 * @return if the command was sent
	 * 
	 * @see #sendConditionalCommand(Item, Item, String)
	 */
	@ActionDoc(text="A method that sends a command the the actionItem if a item with the same name + _Disabled is OFF", 
			returns="<code>true</code>, if the command was sent to the actionItem and <code>false</code> otherwise.")
	public static boolean sendConditionalCommand(
			@ParamDoc(name="actionItem", text="the item that receives the command") final Item actionItem,
			@ParamDoc(name="command", text="the command to be sent to the item") final Command command) {

		final ItemRegistry registry = (ItemRegistry) ConditionalActionActivator.itemRegistryTracker.getService();
		if(registry != null) {
			final String disabledItemName = actionItem.getName() + DISABLED_POSTFIX;
			try {
				final Item disabledItem = registry.getItem(disabledItemName);
				return sendConditionalCommand(actionItem, disabledItem, command);
			} catch (ItemNotFoundException e) {
				logger.error("did not find a item " + disabledItemName);
			}
		}

		return false;
	}
	
	/**
	 * Sends the given command to the given actionItem only if the aDisabledItem argument is disabled. The other way
	 * round, if the aDisabledItem is enabled, the command is not sent to the actionItem.<br />
	 * If the aDisabledItem is a group item, any of the members is enough to be enabled to prevent sending the command.
	 * 
	 * @param actionItem the item that receives the command if the disabledItem is disabled
	 * @param aDisabledItem the item that prevents sending the command if enabled
	 * @param command the command to be sent.
	 * @return if the command was sent to the actionItem.
	 */
	@ActionDoc(text="A method that sends a command to the actionItem if the disabledItem is OFF", 
			returns="<code>true</code>, if the command was sent to the actionItem and <code>false</code> otherwise.")
	public static boolean sendConditionalCommand(
			@ParamDoc(name="actionItem", text="the item that receives the command") final Item actionItem,
			@ParamDoc(name="disabledItem", text="the item that must be disabled so the command is sent") final Item aDisabledItem,
			@ParamDoc(name="command", text="the command to be sent to the item") final Command command) {
		
		if (!ConditionalActionActionService.isProperlyConfigured) {
			logger.debug("ConditionalAction action is not yet configured - execution aborted!");
			return false;
		}
		
		final ItemRegistry registry = (ItemRegistry) ConditionalActionActivator.itemRegistryTracker.getService();
		final EventPublisher publisher = (EventPublisher) ConditionalActionActivator.eventPublisherTracker.getService();
		if(publisher!=null && registry!=null) {
			final Collection<Item> disabledItems;
			//					final Item disabledItem = registry.getItem(disabledItemName);
			if(aDisabledItem instanceof GroupItem) {
				final GroupItem disabledGroupItem = (GroupItem) aDisabledItem;
				disabledItems = disabledGroupItem.getAllMembers();
			} else {
				disabledItems = Collections.singleton(aDisabledItem);
			}

			for (final Item disabledItem : disabledItems) {
				if(disabledItem.getState() == OnOffType.ON) {
					logger.debug("not sending command to " + actionItem.getName() + " because " + disabledItem.getName() + " is ON");
					return false;
				}				
			}

			publisher.sendCommand(actionItem.getName(), command);
			return true;
		}
		
		return false;
	}
	
	public static boolean sendConditionalCommand(
			@ParamDoc(name="actionItem", text="the item that receives the command") final Item actionItem,
			@ParamDoc(name="commandName", text="the command to be sent to the item") final String commandName) {
		final Command command = TypeParser.parseCommand(actionItem.getAcceptedCommandTypes(), commandName);
		return sendConditionalCommand(actionItem, command);
	}
	
	public static boolean sendConditionalCommand(
			@ParamDoc(name="actionItem", text="the item that receives the command") final Item actionItem,
			@ParamDoc(name="disabledItem", text="the item that must be disabled so the command is sent") final Item aDisabledItem,
			@ParamDoc(name="commandName", text="the command to be sent to the item") final String commandName) {
		final Command command = TypeParser.parseCommand(actionItem.getAcceptedCommandTypes(), commandName);
		return sendConditionalCommand(actionItem, aDisabledItem, command);
	}
}
