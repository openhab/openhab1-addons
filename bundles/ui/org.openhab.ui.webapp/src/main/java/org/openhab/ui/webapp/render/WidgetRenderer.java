/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.webapp.render;

import org.eclipse.emf.common.util.EList;
import org.openhab.model.sitemap.Widget;

/**
 * This interface must be implemented by classes, which can render HTML for the WebApp UI for certain widget types.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public interface WidgetRenderer {

	/**
	 * Defines, whether this renderer can handle a given widget
	 * 
	 * @param w the widget to check
	 * @return true, if this renderer can handle the widget
	 */
	public boolean canRender(Widget w);
	
	/**
	 * Produces HTML code for a given widget and writes it to a string builder.
	 * 
	 * @param w the widget to produce HTML code for
	 * @param sb the string builder to append the HTML code to
	 * @return a list of widgets that need to be rendered as children of the widget; the HTML code in sb should contain a "%children%" placeholder for them.
	 * @throws RenderException if an error occurs during rendering
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException;

}
