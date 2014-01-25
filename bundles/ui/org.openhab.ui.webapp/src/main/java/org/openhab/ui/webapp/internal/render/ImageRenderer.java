/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.webapp.internal.render;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.openhab.model.sitemap.Image;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Image widgets.
 * 
 * @author Kai Kreuzer
 * @since 0.6.0
 *
 */
public class ImageRenderer extends AbstractWidgetRenderer {
	
	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Image;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Image image = (Image) w;
		String snippet = (image.getChildren().size() > 0) ? 
				getSnippet("image_link") : getSnippet("image");			

		if(image.getRefresh()>0) {
			snippet = StringUtils.replace(snippet, "%setrefresh%", "<script type=\"text/javascript\">imagesToRefreshOnPage=1</script>");
			snippet = StringUtils.replace(snippet, "%refresh%", "id=\"%id%\" onload=\"setTimeout('reloadImage(\\'%url%\\', \\'%id%\\')', " + image.getRefresh() + ")\"");
		} else {
			snippet = StringUtils.replace(snippet, "%setrefresh%", "");
			snippet = StringUtils.replace(snippet, "%refresh%", "");
		}
		
		String widgetId = itemUIRegistry.getWidgetId(w);
		snippet = StringUtils.replace(snippet, "%id%", widgetId);
		
		String sitemap = w.eResource().getURI().path();
		
		String url = "proxy?sitemap=" + sitemap + "&widgetId=" + widgetId + "&t=" + (new Date()).getTime();
		snippet = StringUtils.replace(snippet, "%url%", url);
		
		sb.append(snippet);
		return null;
	}
}
