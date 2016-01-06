/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.webapp.internal.render;

import org.apache.commons.lang.StringUtils;
import org.eclipse.emf.common.util.EList;
import org.openhab.core.library.types.PointType;
import org.openhab.core.types.State;
import org.openhab.model.sitemap.Mapview;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.webapp.render.RenderException;
import org.openhab.ui.webapp.render.WidgetRenderer;

/**
 * This is an implementation of the {@link WidgetRenderer} interface, which
 * can produce HTML code for Text widgets.
 * 
 * @author Gaël L'hopital
 * @since 1.7.0
 *
 */
public class MapviewRenderer extends AbstractWidgetRenderer {

	/**
	 * {@inheritDoc}
	 */
	public boolean canRender(Widget w) {
		return w instanceof Mapview;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public EList<Widget> renderWidget(Widget w, StringBuilder sb) throws RenderException {
		Mapview mapview = (Mapview) w;
		String snippet = getSnippet("mapview");
		
		State state = itemUIRegistry.getState(mapview);
		if(state instanceof PointType) {
			PointType pointState = (PointType) state;
			double latitude = pointState.getLatitude().doubleValue();
			double longitude = pointState.getLongitude().doubleValue();
			snippet = StringUtils.replace(snippet, "%lat%", Double.toString(latitude));
			snippet = StringUtils.replace(snippet, "%lon%", Double.toString(longitude));
			snippet = StringUtils.replace(snippet, "%lonminus%", Double.toString(longitude-0.01));
			snippet = StringUtils.replace(snippet, "%lonplus%", Double.toString(longitude+0.01));
			snippet = StringUtils.replace(snippet, "%latminus%", Double.toString(latitude-0.01));
			snippet = StringUtils.replace(snippet, "%latplus%", Double.toString(latitude+0.01));
		}
		
		int height = mapview.getHeight();
		if(height==0) {
			height = 4;	// set default height to something viewable
		}
		height = height * 36;
		snippet = StringUtils.replace(snippet, "%height%", Integer.toString(height));
		
		sb.append(snippet);
		return null;
	}
}
