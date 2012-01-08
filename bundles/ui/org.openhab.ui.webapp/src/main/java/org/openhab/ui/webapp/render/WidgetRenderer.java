/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
