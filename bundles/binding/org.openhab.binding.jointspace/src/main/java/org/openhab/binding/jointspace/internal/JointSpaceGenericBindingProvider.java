/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jointspace.internal;

import java.util.HashMap;

import org.openhab.binding.jointspace.JointSpaceBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

import org.apache.commons.lang.StringUtils;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * Configuration string should be of the format: <openhab command>:<jointspace command>
 * multiple of such command combinations can be added as a comma-seperated list
 * <openhab command> can be for example ON, OFF for switch items, but special treatment is done for commands of type 
 * 		HSB: for HSBType commands, 
 * 		DEC: if command should be handled for all decimal types, 
 * 		*: if all commands execute the jointspace command
 * 		POLL: poll this item state
 * <jointspace command> can currently be
 * 		key.X: sends key like from the remote. possible values for X can be found here: http://jointspace.sourceforge.net/projectdata/documentation/jasonApi/1/doc/API-Method-input-key-POST.html
 * 		ambilight[layers,optional].color, sets and gets the color of the layer/pixel of the ambilight
 * 		ambilight.mode.X, where X can be manual, internal, expert
 * 		volume: sets and gets the volume. possible for NumberItems
 * 		source: gets the source
 * 		source.X: sets the source to X, where X can be for example hdmi1, tv, ...
 * 
 * Some valid configuration strings are:
 * 
 * "ON:key.Mute, OFF:key.Mute, POLL:volume.mute"
 * "HSB:ambilight.color"
 * "HSB:ambilight[layer1[left[0]]].color, POLL:ambilight[layer1[left[0]]].color"
 * "0:ambilight.mode.internal, 1:ambilight.mode.manual, 2:ambilight.mode.expert"
 * "1:source.tv, 2:source.hdmi1, 3:source.hdmi2, 4:source.hdmi3, 5:source.hdmiside"
 * "*:volume, POLL:volume"
 * 
 * @author David Lenz
 * @since 1.5.0
 */
public class JointSpaceGenericBindingProvider extends
		AbstractGenericBindingProvider implements JointSpaceBindingProvider {

	private jointSpaceBindingConfig pollItemsConfig = new jointSpaceBindingConfig();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "jointspace";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		jointSpaceBindingConfig config = new jointSpaceBindingConfig();

		parseBindingConfig(bindingConfig, config);

		addBindingConfig(item, config);
	}

	/**
	 * Helper function to parse a config string to a @see
	 * jointSpaceBindingConfig
	 * 
	 * @param bindingConfigs
	 *            String containing (possibly multiple) configuration string(s)
	 * @param config
	 *            Config that will be filled in with the parsed @see
	 *            bindingConfigs
	 * @throws BindingConfigParseException
	 */
	protected void parseBindingConfig(String bindingConfigs,
			jointSpaceBindingConfig config) throws BindingConfigParseException {

		String bindingConfig = StringUtils.substringBefore(bindingConfigs, ",");
		String bindingConfigTail = StringUtils.substringAfter(bindingConfigs,
				",");

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length != 2) {
			throw new BindingConfigParseException(
					"JointSpace binding must contain two parts separated by ':', e.g. <command>:<tvcommand>");
		}

		String command = StringUtils.trim(configParts[0]);
		String tvCommand = StringUtils.trim(configParts[1]);

		// if there are more commands to parse do that recursively ...
		if (StringUtils.isNotBlank(bindingConfigTail)) {
			parseBindingConfig(bindingConfigTail, config);
		}

		config.put(command, tvCommand);
		if (command.contains("POLL")) {
			pollItemsConfig.put(command, tvCommand);
		}
	}

	@Override
	public String getTVCommand(String itemName, String command) {
		jointSpaceBindingConfig config = (jointSpaceBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.get(command) : null;
	}

	
	class jointSpaceBindingConfig extends HashMap<String, String> implements BindingConfig {

		/** generated serial version uid */
		private static final long serialVersionUID = -1723443134323559493L;
		
	}

}
