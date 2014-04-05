/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
		String frequency = s.getFrequency()==0 ? "200" : Integer.toString(s.getFrequency());

		snippet = StringUtils.replace(snippet, "%id%", itemUIRegistry.getWidgetId(s));
		snippet = StringUtils.replace(snippet, "%icon%", escapeURLPath(itemUIRegistry.getIcon(s)));
		snippet = StringUtils.replace(snippet, "%item%", w.getItem());
		snippet = StringUtils.replace(snippet, "%label%", getLabel(s));
		snippet = StringUtils.replace(snippet, "%state%", itemUIRegistry.getState(s).toString());
		snippet = StringUtils.replace(snippet, "%frequency%", frequency);
		snippet = StringUtils.replace(snippet, "%switch%", s.isSwitchEnabled() ? "1" : "0");
		snippet = StringUtils.replace(snippet, "%servletname%", WebAppServlet.SERVLET_NAME);

		// Process the color tags
		snippet = processColor(w, snippet);

		sb.append(snippet);
		return null;
	}
}
