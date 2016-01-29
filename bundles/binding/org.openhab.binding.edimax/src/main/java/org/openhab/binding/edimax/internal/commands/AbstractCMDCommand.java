/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal.commands;

import java.util.List;

/**
 * Base class for Commands of type CMD.
 * 
 * @author Heinz
 *
 * @param <T>
 */
public abstract class AbstractCMDCommand<T> extends AbstractCommand<T> {

	/**
	 * GET constructor.
	 */
	public AbstractCMDCommand() {
	}

	/**
	 * SET constructor.
	 * 
	 * @param newValue
	 */
	public AbstractCMDCommand(T newValue) {
		setValue = newValue;
	}

	@Override
	protected List<String> getPath() {
		List<String> list = super.getPath();
		list.add("CMD");
		return list;
	}

}
