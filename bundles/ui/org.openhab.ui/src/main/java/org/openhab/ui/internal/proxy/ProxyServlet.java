/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.ui.internal.proxy;

import java.io.IOException;
import java.net.URI;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.sitemap.Image;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.Video;
import org.openhab.model.sitemap.Widget;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The proxy servlet is used by image and video widgets. As its name suggests, it proxies the content, so
 * that it is possible to include resources (images/videos) from the LAN in the openHAB UI. This is
 * especially useful for webcams as you would not want to make them directly available to the internet.
 * 
 * The servlet registers as "/proxy" and expects the two parameters "sitemap" and "widgetId". It will
 * hence provide the data of the url specified in the according widget. Note that it does NOT allow
 * general access to any servers in the LAN - only urls that are specified in a sitemap are accessible.
 * 
 * It is also possible to use credentials in a url, e.g. "http://user:pwd@localserver/image.jpg" -
 * the proxy servlet will be able to access the content and provide it to the openHAB UIs through the
 * standard openHAB authentication mechanism (if enabled).
 * 
 * This servlet also supports data streams, such as a webcam video stream etc.
 * 
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class ProxyServlet extends HttpServlet {

	/** the alias for this servlet */
	public static final String PROXY_ALIAS = "proxy";

	private static final Logger logger = LoggerFactory.getLogger(ProxyServlet.class);

	private static final long serialVersionUID = -4716754591953017793L;
	
	protected HttpService httpService;
	protected ItemUIRegistry itemUIRegistry;
	protected ModelRepository modelRepository;
	
	protected void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = itemUIRegistry;
	}

	protected void unsetItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		this.itemUIRegistry = null;
	}

	protected void setModelRepository(ModelRepository modelRepository) {
		this.modelRepository = modelRepository;
	}

	protected void unsetModelRepository(ModelRepository modelRepository) {
		this.modelRepository = null;
	}

	protected void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	protected void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	protected void activate() {
		try {
			logger.debug("Starting up proxy servlet at /" + PROXY_ALIAS);

			Hashtable<String, String> props = new Hashtable<String, String>();
			httpService.registerServlet("/" + PROXY_ALIAS, this, props, createHttpContext());
		} catch (NamespaceException e) {
			logger.error("Error during servlet startup: {}", e.getMessage());
		} catch (ServletException e) {
			logger.error("Error during servlet startup: {}", e.getMessage());
		}
	}

	protected void deactivate() {
		httpService.unregister("/" + PROXY_ALIAS);
	}

	/**
	 * Creates a {@link SecureHttpContext} which handles the security for this servlet  
	 * @return a {@link SecureHttpContext}
	 */
	protected HttpContext createHttpContext() {
		HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
		return new SecureHttpContext(defaultHttpContext, "openHAB.org");
	}
	
	
	@Override
	public String getServletInfo() {
		return "Image and Video Widget Proxy";
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String sitemapName = request.getParameter("sitemap");
		String widgetId = request.getParameter("widgetId");

		if(sitemapName==null) {
			throw new ServletException("Parameter 'sitemap' must be provided!");
		}
		if(widgetId==null) {
			throw new ServletException("Parameter 'widget' must be provided!");
		}
		
		String uriString = null;
		
		Sitemap sitemap = (Sitemap) modelRepository.getModel(sitemapName);
		if(sitemap!=null) {
			Widget widget = itemUIRegistry.getWidget(sitemap, widgetId);
			if(widget instanceof Image) {
				Image image = (Image) widget;
				uriString = image.getUrl();
			} else if(widget instanceof Video) {
				Video video = (Video) widget;
				uriString = video.getUrl();
			} else {
				if(widget==null) {
					throw new ServletException("Widget '" + widgetId + "' could not be found!");
				} else {
					throw new ServletException("Widget type '" + widget.getClass().getName() + "' is not supported!");
				}
			}
		} else {
			throw new ServletException("Sitemap '" + sitemapName + "' could not be found!");
		}

		HttpClient httpClient = new HttpClient();

		try {
			// check if the uri uses credentials and configure the http client accordingly
			URI uri = URI.create(uriString);
			
			if(uri.getUserInfo()!=null) {
				String[] userInfo = uri.getUserInfo().split(":");
				httpClient.getParams().setAuthenticationPreemptive(true);
				Credentials creds = new UsernamePasswordCredentials(userInfo[0], userInfo[1]);
				httpClient.getState().setCredentials(new AuthScope(uri.getHost(), uri.getPort(), AuthScope.ANY_REALM), creds);
			}
		} catch(IllegalArgumentException e) {
			throw new ServletException("URI '" + uriString + "' is not valid: " + e.getMessage());
		}
		
		// do the client request
		GetMethod method = new GetMethod(uriString);
		httpClient.executeMethod(method);
		
		// copy all headers
		for(Header header : method.getResponseHeaders()) {
			response.setHeader(header.getName(), header.getValue());
		}
		
		// now copy/stream the body content
		IOUtils.copy(method.getResponseBodyAsStream(), response.getOutputStream());
		method.releaseConnection();
	}
}
