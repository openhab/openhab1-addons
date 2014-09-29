/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.webapp.render;

/**
 * An exception used by {@link WidgetRenderer}s, if an error occurs.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public class RenderException extends Exception {

	private static final long serialVersionUID = -3801828613192343641L;

	public RenderException(String msg) {
		super(msg);
	}

}
