/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command to retrieve information at the same time.
 * 
 * @author Heinz
 *
 */
public class GetNowPowerCommandCompound extends
		AbstractCMDCommand<Map<AbstractCMDCommand<?>, Object>> {

	/**
	 * List of commands.
	 */
	private List<AbstractCMDCommand<?>> getCommands = new ArrayList<AbstractCMDCommand<?>>();

	/**
	 * Add command to compoun.
	 * @param aCommand
	 */
	public void addCommand(AbstractCMDNowPowerCommand<?> aCommand) {
		getCommands.add(aCommand);
	}

	@Override
	protected String createLeafTag(String aName) {
		// hook in here
		StringBuffer allLeafs = new StringBuffer();
		for (AbstractCMDCommand<?> cmd : getCommands) {
			String lastElement = lastPathSegmentsElement(cmd);
			allLeafs.append(super.createLeafTag(lastElement));
		}
		return allLeafs.toString();
	}

	@Override
	protected List<String> getPath() {
		// hook in here
		return getCommands.get(0).getPath();
	}

	private String lastPathSegmentsElement(AbstractCMDCommand<?> cmd) {
		return (String) cmd.getPath().get(cmd.getPath().size() - 1);
	}

	/**
	 * Result of all requests.
	 */
	protected Map<AbstractCMDCommand<?>, Object> getResultValue(String aResponse) {
		Map<AbstractCMDCommand<?>, Object> results = new HashMap<AbstractCMDCommand<?>, Object>();
		for (AbstractCMDCommand<?> cmd : getCommands) {
			Object individualResult = cmd.getResultValue(aResponse);
			results.put(cmd, individualResult);
		}

		return results;
	}

}
