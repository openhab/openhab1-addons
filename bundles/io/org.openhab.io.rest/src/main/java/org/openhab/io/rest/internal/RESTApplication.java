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

package org.openhab.io.rest.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.UriBuilder;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.AtmosphereServlet;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.jersey.JerseyBroadcaster;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.GenericItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.items.StateChangeListener;
import org.openhab.core.types.State;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.io.rest.internal.resources.ItemResource;
import org.openhab.io.rest.internal.resources.RootResource;
import org.openhab.io.rest.internal.resources.SitemapResource;
import org.openhab.io.rest.internal.resources.beans.ItemBean;
import org.openhab.io.rest.internal.resources.beans.PageBean;
import org.openhab.io.rest.internal.resources.beans.SitemapBean;
import org.openhab.io.rest.internal.resources.beans.WidgetBean;
import org.openhab.model.core.EventType;
import org.openhab.model.core.ModelRepository;
import org.openhab.model.core.ModelRepositoryChangeListener;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.core.util.FeaturesAndProperties;

/**
 * This is the main component of the REST API; it gets all required services injected,
 * registers itself as a servlet on the HTTP service and adds the different rest resources
 * to this service.
 * 
 * @author Kai Kreuzer
 * @since 0.8.0
 */
public class RESTApplication extends Application implements ItemRegistryChangeListener, StateChangeListener, ModelRepositoryChangeListener {

	private static final String REST_SERVLET_ALIAS = "/rest";

	private static final Logger logger = LoggerFactory.getLogger(RESTApplication.class);
	
	private HttpService httpService;
	
	static private EventPublisher eventPublisher;
	
	static private ItemUIRegistry itemUIRegistry;

	static private ModelRepository modelRepository;

	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}
	
	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		RESTApplication.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		RESTApplication.eventPublisher = null;
	}

	static public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		RESTApplication.itemUIRegistry = itemUIRegistry;
		RESTApplication.itemUIRegistry.addItemRegistryChangeListener(this);
	}
	
	public void unsetItemUIRegistry(ItemRegistry itemUIRegistry) {
		RESTApplication.itemUIRegistry.removeItemRegistryChangeListener(this);
		RESTApplication.itemUIRegistry = null;
	}

	static public ItemUIRegistry getItemUIRegistry() {
		return itemUIRegistry;
	}

	public void setModelRepository(ModelRepository modelRepository) {
		RESTApplication.modelRepository = modelRepository;
		RESTApplication.modelRepository.addModelRepositoryChangeListener(this);
	}
	
	public void unsetModelRepository(ModelRepository modelRepository) {
		RESTApplication.modelRepository.removeModelRepositoryChangeListener(this);
		RESTApplication.modelRepository = null;
	}

	static public ModelRepository getModelRepository() {
		return modelRepository;
	}

	public void activate() {
        try {
        	com.sun.jersey.core.osgi.Activator jerseyActivator = new com.sun.jersey.core.osgi.Activator();
        	BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass())
                    .getBundleContext();

        	try {
				jerseyActivator.start(bundleContext);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			httpService.registerServlet(REST_SERVLET_ALIAS,
				new AtmosphereServlet(), getJerseyServletParams(), createHttpContext());
			// register a StateChangeListener for all available items in the UIRegistry
 			for(Item item : getItemUIRegistry().getItems()) {
 				((GenericItem) item).addStateChangeListener(this);
 			}
            logger.info("Registered REST servlet as /rest");
        } catch (ServletException se) {
            throw new RuntimeException(se);
        } catch (NamespaceException se) {
            throw new RuntimeException(se);
        }
	}
	
	public void deactivate() {
        if (this.httpService != null) {
            httpService.unregister(REST_SERVLET_ALIAS);
            logger.info("Unregistered REST servlet");
        }
	}
	
	/**
	 * Creates a {@link SecureHttpContext} which handles the security for this
	 * Servlet  
	 * @return a {@link SecureHttpContext}
	 */
	protected HttpContext createHttpContext() {
		HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
		return new SecureHttpContext(defaultHttpContext, "openHAB.org");
	}
	
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> result = new HashSet<Class<?>>();
        result.add(RootResource.class);
        result.add(ItemResource.class);
        result.add(SitemapResource.class);
        return result;
    }

    private Dictionary<String, String> getJerseyServletParams() {
        Dictionary<String, String> jerseyServletParams = new Hashtable<String, String>();
        jerseyServletParams.put("javax.ws.rs.Application", RESTApplication.class.getName());
        jerseyServletParams.put("org.atmosphere.core.servlet-mapping", "/rest/*");
        jerseyServletParams.put("org.atmosphere.useWebSocket", "true");
        jerseyServletParams.put("org.atmosphere.useNative", "true");
        jerseyServletParams.put("org.atmosphere.cpr.broadcastFilterClasses", "org.atmosphere.client.FormParamFilter");
        jerseyServletParams.put("com.sun.jersey.spi.container.ResourceFilter", "org.atmosphere.core.AtmosphereFilter");
        
        //jerseyServletParams.put(ApplicationConfig.PROPERTY_COMET_SUPPORT, JettyCometSupportWithWebSocket.class.getName());
       // jerseyServletParams.put(ApplicationConfig.WEBSOCKET_SUPPORT, "true");
       // jerseyServletParams.put(ApplicationConfig.PROPERTY_NATIVE_COMETSUPPORT, "true");
        
        
        // required because of bug http://java.net/jira/browse/JERSEY-361
        jerseyServletParams.put(FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, "true");

        return jerseyServletParams;
    }
    

	
	/**
	 * {@inheritDoc}
	 */
	public void allItemsChanged(Collection<String> oldItemNames) {
		ItemUIRegistry registry = RESTApplication.getItemUIRegistry();
		for(Item item : registry.getItems()) {
				((GenericItem) item).addStateChangeListener(this);
			}	
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void itemAdded(Item item) {
		((GenericItem) item).addStateChangeListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void itemRemoved(Item item) {
		((GenericItem) item).removeStateChangeListener(this);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void stateChanged(Item item, State oldState, State newState) {
		String basePath;
		ItemBean itemBean = null;
		Collection<Broadcaster> broadcasters = BroadcasterFactory.getDefault().lookupAll();
		Broadcaster mainBroadcaster = broadcasters.iterator().next();
		Collection<AtmosphereResource<?, ?>> ars = mainBroadcaster.getAtmosphereResources();
	    if (ars!= null && !ars.isEmpty()) {
		    Broadcaster itemBroadcaster =  BroadcasterFactory.getDefault().lookup(JerseyBroadcaster.class, "ItemBroadcaster", true);
		    for (AtmosphereResource<?, ?> ar : ars) {
		        Object req = ar.getRequest();
		        if (req instanceof HttpServletRequest) {
		        	HttpServletRequest request = (HttpServletRequest) req;
		        	basePath =         	
		        			request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath() + REST_SERVLET_ALIAS +"/";
		        	itemBean = ItemResource.createItemBean(item, true, basePath);
		            String pathInfo = ((HttpServletRequest) req).getPathInfo();
		            String servletLink = UriBuilder.fromUri(basePath).path(ItemResource.PATH_ITEMS).path("/").build().toASCIIString();
		            String requestLink = UriBuilder.fromUri(basePath).path(pathInfo).path("/").build().toASCIIString();
		            String itemLink = UriBuilder.fromUri(itemBean.link).path("/").build().toASCIIString();
		
		            
		            //get subscribers for item channels
		            if (itemLink.equals(requestLink) || servletLink.equals(requestLink)) {
		                itemBroadcaster.addAtmosphereResource(ar);
		            } 
		            
		            
		            //get subscribers for sitemap channels 
		            if (pathInfo.contains("sitemaps")){
			            String[] split = pathInfo.split("/");
			            ArrayList<String> sitemapPathParams = new ArrayList<String>(split.length);
			            
			            for (String res : split)
			                if (res.length() > 0)
			                	sitemapPathParams.add(res);
			            
			            if(sitemapPathParams.size() >= 1) {
				            UriBuilder baseUriBuilder = UriBuilder.fromUri(basePath);
				            SitemapResource sr = new SitemapResource();
				            
				            switch (sitemapPathParams.size()) {
				            	case 1: //check if item is on a sitemap
				            			Collection<SitemapBean> sitemaps = sr.getSitemapBeans(baseUriBuilder.build());
				            			for(SitemapBean sitemapBean : sitemaps){
				            				if(isItemOnPage(sitemapBean.homepage.widgets, itemBean))
						            		{
						            			itemBroadcaster.addAtmosphereResource(ar);
						            		}
				            			}
				            			break;
				            			
					            case 2: //check if item is on the sitemap
					            		SitemapBean sitemapBean = sr.getSitemapBean(sitemapPathParams.get(1), baseUriBuilder.build() );
					            		if(isItemOnPage(sitemapBean.homepage.widgets, itemBean))
					            		{
					            			itemBroadcaster.addAtmosphereResource(ar);
					            		}
					                    break;
					            case 3: //check if item is on the page
					            		PageBean pageBean = sr.getPageBean(sitemapPathParams.get(1), sitemapPathParams.get(2),baseUriBuilder.build());
					            		if(isItemOnPage(pageBean.widgets, itemBean)){
					            			itemBroadcaster.addAtmosphereResource(ar);
					            		}
					            		break;      
				            }
			            }
		            }
		        }
		    }
		    itemBroadcaster.broadcast(itemBean);
		    
	    }
		
	}
	
	private boolean isItemOnPage(List<WidgetBean> widgets, ItemBean searchItem){
		boolean isFound = false;
		for(WidgetBean widget : widgets) {
			if(widget.item !=null)
				logger.info(widget.item.link);
			
			if(widget.item !=null && widget.item.link.equals(searchItem.link)){
				isFound =  true;
			}
			else{
				if (!widget.widgets.isEmpty()){
					isFound =  isItemOnPage(widget.widgets, searchItem);
				}
			}
			
			if (widget.linkedPage != null) {
				isFound = isItemOnPage(widget.linkedPage.widgets, searchItem);
			}
			if(isFound){
				return isFound;
				
			}
		}
		return isFound;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public void stateUpdated(Item item, State state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modelChanged(String modelName, EventType type) {
		logger.info("modelChanged: " + modelName + " - " + type);
		
	}

}
