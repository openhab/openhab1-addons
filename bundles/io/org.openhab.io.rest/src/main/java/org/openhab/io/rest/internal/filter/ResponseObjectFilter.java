/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.rest.internal.filter;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.openhab.core.items.Item;
import org.openhab.io.rest.internal.resources.ResponseTypeHelper;
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.openhab.io.rest.internal.resources.beans.WidgetBean;
import org.openhab.io.rest.internal.resources.beans.WidgetListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This Filter filters out the updated widgets from the page and returns them to streaming and websocket connections   
 *  
 * @author Oliver Mazur
 * @since 1.0
 *
 */
public class ResponseObjectFilter implements PerRequestBroadcastFilter {

	private static final Logger logger = LoggerFactory.getLogger(ResponseObjectFilter.class);
	
	@Override
	public BroadcastAction filter(String broadcasterId, Object originalMessage, Object message) {
		return new BroadcastAction(message);
	}

	@Override
	public BroadcastAction filter(String broadcasterId, AtmosphereResource resource, Object originalMessage, Object message) {
		final  HttpServletRequest request = resource.getRequest();
		
		// websocket and HTTP streaming
		if(ResponseTypeHelper.isStreamingTransport(request) && message instanceof PageBean && originalMessage instanceof Item) {
			return new BroadcastAction(ACTION.CONTINUE,  getSingleResponseObject((PageBean)message, (Item)originalMessage, request)	);
		}
			
		// pass message to next filter
		return new BroadcastAction(ACTION.CONTINUE,  message);
	}
	
	private Object getSingleResponseObject(PageBean pageBean, Item item, HttpServletRequest request) {
		if(pageBean!=null) {
	    	return new WidgetListBean( getItemsOnPage(pageBean.widgets, item));
    	}
		return null;
	}
	
	private List<WidgetBean> getItemsOnPage(List<WidgetBean> widgets,
			Item searchItem) {
		List<WidgetBean> foundWidgets = new ArrayList<WidgetBean>();
		try {
			for (WidgetBean widget : widgets) {
				if (widget.item != null
						&& widget.item.name.equals(searchItem.getName())) {
					foundWidgets.add(widget);
				} else {
					if (!widget.widgets.isEmpty()) {
						List<WidgetBean> tmpWidgets = getItemsOnPage(
								widget.widgets, searchItem);
						if (!tmpWidgets.isEmpty()) {
							foundWidgets.addAll(tmpWidgets);
						}

					}
				}

				if (widget.linkedPage != null
						&& widget.linkedPage.widgets != null) {
					List<WidgetBean> tmpWidgets = getItemsOnPage(
							widget.linkedPage.widgets, searchItem);
					if (!tmpWidgets.isEmpty()) {
						foundWidgets.addAll(tmpWidgets);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return foundWidgets;
	}

}
