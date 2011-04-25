/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
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

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.openhab.model.sitemap.Slider;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.internal.servlet.WebAppServlet;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;

/**
 * <p>This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Slider widgets.</p>
 * 
 * <p>Note: As the WebApp.Net framework cannot render real sliders in the UI,
 * we instead show buttons to increase or decrease the value.</p> 
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 *
 */
public class SliderRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Slider;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Slider s = (Slider) w;
		
		String snippetName = "slider";

		String snippet = getSnippet(snippetName);

		// set the default send-update frequency to 200ms  
		String frequency = StringUtils.isEmpty(s.getFrequency()) ? "200" : s.getFrequency();

		snippet = snippet.replaceAll("%id%", getWidgetId(s));
		snippet = snippet.replaceAll("%icon%", getIcon(s));
		snippet = snippet.replaceAll("%item%", w.getItem());
		snippet = snippet.replaceAll("%label%", getLabel(s));
		snippet = snippet.replaceAll("%state%", getState(s).toString());
		snippet = snippet.replaceAll("%frequency%", frequency);
		snippet = snippet.replaceAll("%switch%", s.isSwitchEnabled() ? "1" : "0");
		snippet = snippet.replaceAll("%servletname%", WebAppServlet.SERVLET_NAME);

		sb.append(snippet);
		return null;
	}
}
