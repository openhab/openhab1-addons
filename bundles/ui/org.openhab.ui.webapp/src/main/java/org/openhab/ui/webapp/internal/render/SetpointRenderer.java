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

import java.math.BigDecimal;

import org.eclipse.emf.common.util.EList;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Setpoint;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Setpoint widgets.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class SetpointRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Setpoint;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Setpoint sp = (Setpoint) w;

		State state = itemUIRegistry.getState(w);
		String newLowerState = state.toString();
		String newHigherState = state.toString();

		// if the current state is a valid value, we calculate the up and down step values
		if(state instanceof DecimalType) {
			DecimalType actState = (DecimalType) state;
			BigDecimal newLower = actState.toBigDecimal().subtract(sp.getStep());
			BigDecimal newHigher = actState.toBigDecimal().add(sp.getStep());
			if(newLower.compareTo(sp.getMinValue()) < 0) {
				newLower = sp.getMinValue();
			}
			if(newHigher.compareTo(sp.getMaxValue()) > 0) {
				newHigher = sp.getMaxValue();
			}
			newLowerState = newLower.toString();
			newHigherState = newHigher.toString();
		}
		
		String snippetName = "setpoint";
		String snippet = getSnippet(snippetName);

		snippet = snippet.replaceAll("%id%", itemUIRegistry.getWidgetId(w));
		snippet = snippet.replaceAll("%icon%", itemUIRegistry.getIcon(w));
		snippet = snippet.replaceAll("%item%", w.getItem());
		snippet = snippet.replaceAll("%state%", state.toString());
		snippet = snippet.replaceAll("%newlowerstate%", newLowerState);
		snippet = snippet.replaceAll("%newhigherstate%", newHigherState);
		snippet = snippet.replaceAll("%label%", getLabel(w));
		snippet = snippet.replaceAll("%servletname%", WebAppServlet.SERVLET_NAME);
		snippet = snippet.replaceAll("%minValue%", sp.getMinValue().toString());
		snippet = snippet.replaceAll("%maxValue%", sp.getMaxValue().toString());
		snippet = snippet.replaceAll("%step%", sp.getStep().toString());
		
		sb.append(snippet);
		return null;
	}
}
