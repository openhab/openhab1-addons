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

package org.openhab.ui.webapp.internal.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.openhab.core.items.ItemRegistry;
import org.openhab.io.net.http.SecureHttpContext;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;


/**
 * This is the base servlet class for other servlet in the WebApp UI. 
 * 
 * @author Thomas.Eichstaedt-Engelen
 */
public abstract class BaseServlet implements Servlet {
	
	/** the root path of this web application */
	public static final String WEBAPP_ALIAS = "/";
		
	protected HttpService httpService;
	protected ItemRegistry itemRegistry;

	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}

	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	/**
	 * Creates a {@link HttpContext} with respect to the 
	 * <code>SECURITY_SYSTEM_PROPERTY</code>. If the property is set (with no
	 * value) the UI is secured by HTTP Basic Authentication. There is no security
	 * provided if this property is not set.  
	 * 
	 * @return {@link SecureHttpContext} if <code>SECURITY_SYSTEM_PROPERTY</code>
	 * is set or DefaultHttpContext in all other cases.
	 */
	protected HttpContext createHttpContext() {
		HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
		boolean isSecur = System.getProperty(SecureHttpContext.SECURITY_SYSTEM_PROPERTY) != null;
		return (isSecur ? new SecureHttpContext(defaultHttpContext, "openHAB.org") : defaultHttpContext);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void init(ServletConfig config) throws ServletException {
	}

	/**
	 * {@inheritDoc}
	 */
	public ServletConfig getServletConfig() {
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String getServletInfo() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public void destroy() {
	}

}
