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

package org.openhab.io.rest.internal.listeners;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.atmosphere.cpr.AtmosphereResourceEvent;
import org.openhab.io.rest.internal.RESTApplication;
import org.openhab.io.rest.internal.resources.MediaTypeHelper;
import org.openhab.io.rest.internal.resources.SitemapResource;
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.openhab.model.sitemap.Frame;
import org.openhab.model.sitemap.LinkableWidget;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Widget;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * This is the {@link TransportListener} implementation for sitemap REST requests.
 * Note: We only support suspended requests for page requests, not for complete sitemaps.
 * 
 * @author Kai Kreuzer
 * @since 0.9.0
 *
 */
public class SitemapTransportListener extends TransportListener {

	@Override
	protected Object getResponseObject(
			AtmosphereResourceEvent<HttpServletRequest, HttpServletResponse> event) {
		HttpServletRequest request = event.getResource().getRequest();
		String pathInfo = request.getPathInfo();
		String responseType = getResponseType(request);
		
		if(responseType!=null) {
			URI basePath = UriBuilder.fromUri(request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath() + RESTApplication.REST_SERVLET_ALIAS +"/").build();
			if (pathInfo.startsWith("/" + SitemapResource.PATH_SITEMAPS)) {
	        	String[] pathSegments = pathInfo.substring(1).split("/");
	            if(pathSegments.length>=3) {
	            	String sitemapName = pathSegments[1];
	            	String pageId = pathSegments[2];

	            	Sitemap sitemap = (Sitemap) RESTApplication.getModelRepository().getModel(sitemapName + ".sitemap");
	            	if(sitemap!=null) {
						PageBean pageBean = SitemapResource.getPageBean(sitemapName, pageId, basePath);
				    	Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ?
				    			new JSONWithPadding(pageBean, getQueryParam(request, "callback")) : pageBean;
		    	    	return Response.ok(responseObject, responseType).build();
	            	}
	            }
	        }
		}
		return null;
	}

	@Override
	protected Set<String> getRelevantItemNames(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();

        // check, if it is a request for a page of a sitemap 
        if (pathInfo.startsWith("/" + SitemapResource.PATH_SITEMAPS)) {
        	String[] pathSegments = pathInfo.substring(1).split("/");

            if(pathSegments.length>=3) {
            	String sitemapName = pathSegments[1];
            	String pageName = pathSegments[2];

            	Sitemap sitemap = (Sitemap) RESTApplication.getModelRepository().getModel(sitemapName + ".sitemap");
            	if(sitemap!=null) {
            		List<Widget> children = null;
            		if(pageName.equals(sitemapName)) {
            			children = sitemap.getChildren();
            		} else {            		
	            		Widget widget = RESTApplication.getItemUIRegistry().getWidget(sitemap, pageName);
	            		if(widget instanceof LinkableWidget) {
	            			LinkableWidget page = (LinkableWidget) widget;
	            			children = RESTApplication.getItemUIRegistry().getChildren(page);
	            		}
            		}
            		if(children!=null) {
	            		return getRelevantItemNamesForWidgets(children);
            		}
				}
            }
        }
        return new HashSet<String>();
	}

	private Set<String> getRelevantItemNamesForWidgets(List<Widget> children) {
		Set<String> itemNames = new HashSet<String>();
		for(Widget child : children) {
			if (child instanceof Frame) {
				Frame frame = (Frame) child;
				itemNames.addAll(getRelevantItemNamesForWidgets(frame.getChildren()));
			} else {
				String itemName = child.getItem();
				if(itemName!=null) {
					itemNames.add(itemName);
				}
			}
		}
		return itemNames;
	}
}
