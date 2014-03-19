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
import org.openhab.model.sitemap.Mapping;
import org.openhab.model.sitemap.Selection;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Selection widgets.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public class SelectionRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Selection;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		String snippet = getSnippet("selection");

		snippet = StringUtils.replace(snippet, "%icon%", escapeURLPath(itemUIRegistry.getIcon(w)));
		snippet = StringUtils.replace(snippet, "%label_header%", getLabel(w));
		
		String state = itemUIRegistry.getState(w).toString();
		Selection selection = (Selection) w;
		
		StringBuilder rowSB = new StringBuilder();
		for(Mapping mapping : selection.getMappings()) {
			String rowSnippet = getSnippet("selection_row");
			rowSnippet = StringUtils.replace(rowSnippet, "%item%", w.getItem()!=null ? w.getItem() : "");
			rowSnippet = StringUtils.replace(rowSnippet, "%cmd%", mapping.getCmd()!=null ? mapping.getCmd() : "");
			rowSnippet = StringUtils.replace(rowSnippet, "%label%", mapping.getLabel()!=null ? mapping.getLabel() : "");
			if(state.equals(mapping.getCmd())) {
				rowSnippet = StringUtils.replace(rowSnippet, "%checked%", "checked=\"true\"");
			} else {
				rowSnippet = StringUtils.replace(rowSnippet, "%checked%", "");
			}
			rowSB.append(rowSnippet);
		}
		snippet = StringUtils.replace(snippet, "%rows%", rowSB.toString());
		
		// Process the color tags
		snippet = processColor(w, snippet);

		sb.append(snippet);
		return null;
	}
}
