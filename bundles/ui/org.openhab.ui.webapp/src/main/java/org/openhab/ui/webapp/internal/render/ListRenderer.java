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
import org.openhab.model.sitemap.List;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for List widgets.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public class ListRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof List;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		String snippet = getSnippet("list");
		snippet = snippet.replaceAll("%label%", getLabel(w));
		
		String rowSnippet = getSnippet("list_row");
		String state = itemUIRegistry.getState(w).toString();
		String[] rowContents = state.split(((List) w).getSeparator());
		StringBuilder rowSB = new StringBuilder();
		for(String row : rowContents) {
			rowSB.append(StringUtils.replace(rowSnippet, "%title%", row));
		}
		snippet = StringUtils.replace(snippet, "%rows%", rowSB.toString());

		// Process the color tags
		snippet = processColor(w, snippet);

		sb.append(snippet);
		return null;
	}
}
