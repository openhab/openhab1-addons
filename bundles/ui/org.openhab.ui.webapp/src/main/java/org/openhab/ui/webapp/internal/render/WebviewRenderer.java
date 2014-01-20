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
import org.openhab.model.sitemap.Webview;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Webview widgets.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class WebviewRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Webview;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Webview webview = (Webview) w;
		String snippet = getSnippet("webview");			

		int height = webview.getHeight();
		if(height==0) {
			height = 1;
		}
		
		snippet = StringUtils.replace(snippet, "%url%", webview.getUrl());
		snippet = StringUtils.replace(snippet, "%height%", Integer.toString(height*36));
		
		sb.append(snippet);
		return null;
	}
}
