/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.LcnBindingProvider;
import org.openhab.binding.lcn.common.LcnAddrMod;
import org.openhab.binding.lcn.input.Input;
import org.openhab.binding.lcn.input.Mod;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.transform.TransformationException;
import org.openhab.core.transform.TransformationHelper;
import org.openhab.core.transform.TransformationService;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Manages item-configurations.
 * 
 * @author Tobias Jüttner
 */
public class LcnGenericBindingProvider extends AbstractGenericBindingProvider implements LcnBindingProvider {
	
	/** Pattern used to parse bindings in general. */
	private static final Pattern PATTERN_BINDING_GENERAL = Pattern.compile("(\\[.*?\\])*");
	
	/** Pattern used to parse bindings which are bound to openHAB commands. */
	private static final Pattern PATTERN_BINDING_WITH_OPENHAB = Pattern.compile("\\[(.*?):(.*?):\'?(.*?)\'?\\]");
	
	/** Pattern used to parse bindings which don't have an openHAB command. */
	private static final Pattern PATTERN_BINDING_PURE = Pattern.compile("\\[(.*):\'?(.*?)\'?\\]");
	
	/** Pattern to identify mappings in item bindings. */
	private static final Pattern PATTERN_MAPPING = Pattern.compile("(.*?)\\((.*)\\)");
	
	/**
	 * Stores all item names related to an LCN module address.
	 * Used for optimization.
	 * This map is never cleaned and might contain out-dated mappings or items that no longer exist.
	 */ 
	private final HashMap<LcnAddrMod, HashSet<String>> itemNamesByModulCache = new HashMap<LcnAddrMod, HashSet<String>>();
	
	/**
	 * Item processing for the LCN bindings.
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		Matcher matcher = PATTERN_BINDING_GENERAL.matcher(bindingConfig);
		if (!matcher.matches()) {
			throw new BindingConfigParseException(bindingConfig + "' contains no valid binding!");
		}
		matcher.reset();
		LcnBindingConfig bc = new LcnBindingConfig(item);		
		while (matcher.find()) {
			String binding = matcher.group(1);
			if (binding != null && !binding.trim().isEmpty()) {
				String openHabCmd = null;
				String connId, lcnTarget;
				Matcher openHabMatcher = PATTERN_BINDING_WITH_OPENHAB.matcher(binding);
				Matcher pureMatcher = PATTERN_BINDING_PURE.matcher(binding);
				if (openHabMatcher.matches()) {
					openHabCmd = openHabMatcher.group(1);
					connId = openHabMatcher.group(2);
					lcnTarget = openHabMatcher.group(3);	
				} else if (pureMatcher.matches()) {
					connId = pureMatcher.group(1);
					lcnTarget = pureMatcher.group(2);
				} else {
					throw new BindingConfigParseException("Invalid binding configuration for " + binding + "!");
				}
				String lcnShort = resolveMappings(lcnTarget, openHabCmd);
				if (lcnShort == null || lcnShort.equals(openHabCmd)) {
					lcnShort = lcnTarget;
				}
				Command cmd = openHabCmd == null ?
					TypeParser.parseCommand(new StringItem("").getAcceptedCommandTypes(), "") :
					openHabCmd.equals("%i") ? new StringType("%i") :
					TypeParser.parseCommand(item.getAcceptedCommandTypes(), openHabCmd);
				bc.add(new LcnBindingConfig.Mapping(cmd, connId, lcnShort));
			}
		}
		// Finished
		this.addBindingConfig(item, bc);
		for (LcnAddrMod addr : bc.getRelatedModules()) {
			HashSet<String> l = this.itemNamesByModulCache.get(addr);
			if (l == null) {
				l = new HashSet<String>();
				this.itemNamesByModulCache.put(addr, l);
			}
			l.add(item.getName());
		}
	}
	
	/** {inheritDoc} */
	@Override
	public String getBindingType() {
		return "lcn";
	}

	/** {inheritDoc} */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// Everything is accepted
	}
	
	/**
	 * Resolves LCN commands (with mappings) to plain commands.
	 * 
	 * @param lcnTarget the target or a mapping
	 * @param openHABcmd the command send by openHAB
	 * @return the resolved result (can be null)
	 */
	private static String resolveMappings(String lcnTarget, String openHABcmd) {
		String result = null;
		Matcher matcher = PATTERN_MAPPING.matcher(lcnTarget);
		if (!matcher.matches()) {
			result = lcnTarget;
		}
		else {
			matcher.reset();
			matcher.find();			
			String s1 = matcher.group(1);
			String s2 = matcher.group(2);
			TransformationService transformationService = TransformationHelper.getTransformationService(LcnBindingActivator.getContext(), s1);
			if (transformationService != null) {
				try {
					result = transformationService.transform(s2, openHABcmd);
				} catch (TransformationException e) {
					result = lcnTarget;
				}
			} else {
				result = lcnTarget;
			}
		}
		return result;
	}
	
	/**
	 * Finds an LCN item by name.
	 * 
	 * @param itemName the item's name to search for
	 * @return the {@link LcnBindingConfig} or null
	 */
	public LcnBindingConfig getLcnItemConfig(String itemName) {
		return (LcnBindingConfig)this.bindingConfigs.get(itemName);
	}
	
	/**
	 * Gets all LCN item names.
	 * 
	 * @return the list of item names
	 */
	public Collection<String> getItemNames() {
		return this.bindingConfigs.keySet();
	}
	
	/**
	 * Gets all LCN item names associated with the given {@link Input}.
	 * Might return items that no longer exist.
	 * 
	 * @param pchkInput the input from LCN-PCHK
	 * @return the list of item names (note that some items might no longer exist)
	 */
	public Collection<String> getItemNamesForPchkInput(Input pchkInput) {
		// Optimization: If the PckInput has a module address, we get a reduced list of item names from cache
		if (pchkInput instanceof Mod) {
			LcnAddrMod addr = ((Mod)pchkInput).getLogicalSourceAddr();
			if (addr.isValid() && this.itemNamesByModulCache.containsKey(addr)) {
				return this.itemNamesByModulCache.get(addr);
			}
		}
		return this.getItemNames();
	}

}
