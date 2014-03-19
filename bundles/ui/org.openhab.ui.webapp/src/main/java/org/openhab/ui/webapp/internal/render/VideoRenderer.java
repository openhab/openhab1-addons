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
import org.openhab.model.sitemap.Video;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Video widgets.
 * 
 * @author Kai Kreuzer
 * @since 1.0.0
 *
 */
public class VideoRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Video;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Video videoWidget = (Video) w;
		String snippet = null;
		
		String widgetId = itemUIRegistry.getWidgetId(w);		
		String sitemap = w.eResource().getURI().path();
		
		if(videoWidget.getEncoding() !=null && videoWidget.getEncoding().contains("mjpeg")) {
			// we handle mjpeg streams as an html image as browser can usually handle this
			snippet = getSnippet("image");
			snippet = StringUtils.replace(snippet, "%setrefresh%", "");
			snippet = StringUtils.replace(snippet, "%refresh%", "");
		} else {
			snippet = getSnippet("video");			
		}
		String url = "proxy?sitemap=" + sitemap + "&widgetId=" + widgetId;
		snippet = StringUtils.replace(snippet, "%url%", url);
		sb.append(snippet);
		return null;
	}
}
