/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
package org.openhab.ui.webapp.internal.render;

import java.awt.Color;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Colorpicker;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;

/**
 * <p>This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Colorpicker widgets.</p>
 * 
 * <p>Note: This renderer requires the files "jquery.miniColors.css" and "jquery.miniColors.js" in the web folder
 * of this bundle</p>
 * 
 * @author Kai Kreuzer
 * @since 1.2.0
 *
 */
public class ColorpickerRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Colorpicker;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Colorpicker cp = (Colorpicker) w;
		
		String snippetName = "colorpicker";

		String snippet = getSnippet(snippetName);

		// set the default send-update frequency to 200ms  
		String frequency = cp.getFrequency()==0 ? "200" : Integer.toString(cp.getFrequency());
		
		// get RGB hex value
		State state = itemUIRegistry.getState(cp);
		String hexValue = "#ffffff";
		if(state instanceof HSBType) {
			HSBType hsbState = (HSBType) state;
			Color color = hsbState.toColor();
			hexValue = "#" + Integer.toHexString(color.getRGB()).substring(2);
		}
		String label = getLabel(cp);
		String purelabel = label;
		if(label.contains("<span>")) {
			purelabel = purelabel.substring(0, label.indexOf("<span>"));
		}

		snippet = snippet.replaceAll("%id%", itemUIRegistry.getWidgetId(cp));
		snippet = snippet.replaceAll("%icon%", itemUIRegistry.getIcon(cp));
		snippet = snippet.replaceAll("%item%", w.getItem());
		snippet = snippet.replaceAll("%label%", label);
		snippet = snippet.replaceAll("%purelabel%", purelabel);
		snippet = snippet.replaceAll("%state%", hexValue);
		snippet = snippet.replaceAll("%frequency%", frequency);
		snippet = snippet.replaceAll("%servletname%", WebAppServlet.SERVLET_NAME);

		sb.append(snippet);
		return null;
	}
}
