/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.io.cv;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.ServletException;
import javax.ws.rs.ApplicationPath;

import org.atmosphere.cpr.AtmosphereServlet;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.ItemRegistry;
import org.openhab.io.cv.internal.resources.LoginResource;
import org.openhab.io.cv.internal.resources.ReadResource;
import org.openhab.io.cv.internal.resources.RrdResource;
import org.openhab.io.cv.internal.resources.WriteResource;
import org.openhab.io.net.http.SecureHttpContext;
import org.openhab.io.servicediscovery.DiscoveryService;
import org.openhab.io.servicediscovery.ServiceDescription;
import org.openhab.model.core.ModelRepository;
import org.openhab.ui.items.ItemUIRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.core.util.FeaturesAndProperties;

/**
 * This is the main component of the Cometvisu API; it gets all required services injected,
 * registers itself as a servlet on the HTTP service and adds the different rest resources
 * to this service.
 * 
 * @author Tobias Bräutigam
 * @since 1.4.0
 */
@ApplicationPath(CVApplication.CV_SERVLET_ALIAS)
public class CVApplication extends PackagesResourceConfig  {

	public static final String CV_SERVLET_ALIAS = "/services/cv";

	private static final Logger logger = LoggerFactory.getLogger(CVApplication.class);
	
	private int httpSSLPort;

	private int httpPort;

	private HttpService httpService;

	private DiscoveryService discoveryService;

	static private EventPublisher eventPublisher;
	
	static private ItemUIRegistry itemUIRegistry;

	static private ModelRepository modelRepository;
	
	public CVApplication() {
		super("org.openhab.io.cv.internal.resources");
	}

	public void setHttpService(HttpService httpService) {
		this.httpService = httpService;
	}
	
	public void unsetHttpService(HttpService httpService) {
		this.httpService = null;
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		CVApplication.eventPublisher = eventPublisher;
	}
	
	public void unsetEventPublisher(EventPublisher eventPublisher) {
		CVApplication.eventPublisher = null;
	}

	static public EventPublisher getEventPublisher() {
		return eventPublisher;
	}

	public void setItemUIRegistry(ItemUIRegistry itemUIRegistry) {
		CVApplication.itemUIRegistry = itemUIRegistry;
	}
	
	public void unsetItemUIRegistry(ItemRegistry itemUIRegistry) {
		CVApplication.itemUIRegistry = null;
	}

	static public ItemUIRegistry getItemUIRegistry() {
		return itemUIRegistry;
	}

	public void setModelRepository(ModelRepository modelRepository) {
		CVApplication.modelRepository = modelRepository;
	}
	
	public void unsetModelRepository(ModelRepository modelRepository) {
		CVApplication.modelRepository = null;
	}

	static public ModelRepository getModelRepository() {
		return modelRepository;
	}

	public void setDiscoveryService(DiscoveryService discoveryService) {
		this.discoveryService = discoveryService;
	}
	
	public void unsetDiscoveryService(DiscoveryService discoveryService) {
		this.discoveryService = null;
	}

	public void activate() {			    
        try {
        	// we need to call the activator ourselves as this bundle is included in the lib folder
        	com.sun.jersey.core.osgi.Activator jerseyActivator = new com.sun.jersey.core.osgi.Activator();
        	BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass())
                    .getBundleContext();
        	try {
        		jerseyActivator.start(bundleContext);
			} catch (Exception e) {
				logger.error("Could not start Jersey framework", e);
			}
        	
    		httpPort = Integer.parseInt(bundleContext.getProperty("jetty.port"));
    		httpSSLPort = Integer.parseInt(bundleContext.getProperty("jetty.port.ssl"));

			httpService.registerServlet(CV_SERVLET_ALIAS,
				new AtmosphereServlet(), getJerseyServletParams(), createHttpContext());

 			logger.info("Started Cometvisu API at "+CV_SERVLET_ALIAS);

 			if (discoveryService != null) {
 				discoveryService.registerService(getDefaultServiceDescription());
 				discoveryService.registerService(getSSLServiceDescription());
			}
        } catch (ServletException se) {
            throw new RuntimeException(se);
        } catch (NamespaceException se) {
            throw new RuntimeException(se);
        }
	}
	
	public void deactivate() {
        if (this.httpService != null) {
            httpService.unregister(CV_SERVLET_ALIAS);
            logger.info("Stopped CometVisu API");
        }
        
        if (discoveryService != null) {
 			discoveryService.unregisterService(getDefaultServiceDescription());
			discoveryService.unregisterService(getSSLServiceDescription()); 			
 		}
	}
	
	/**
	 * Creates a {@link SecureHttpContext} which handles the security for this
	 * Servlet  
	 * @return a {@link SecureHttpContext}
	 */
	protected HttpContext createHttpContext() {
		HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
		return new SecureHttpContext(defaultHttpContext, "CV-openHAB.org");
	}
	
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> result = new HashSet<Class<?>>();
        result.add(LoginResource.class);
        result.add(ReadResource.class);
        result.add(WriteResource.class);
        result.add(RrdResource.class);
        return result;
    }

    private Dictionary<String, String> getJerseyServletParams() {
        Dictionary<String, String> jerseyServletParams = new Hashtable<String, String>();
        jerseyServletParams.put("javax.ws.rs.Application", CVApplication.class.getName());
        jerseyServletParams.put("org.atmosphere.core.servlet-mapping", CV_SERVLET_ALIAS+"/*");
        jerseyServletParams.put("org.atmosphere.useWebSocket", "true");
        jerseyServletParams.put("org.atmosphere.useNative", "true");
        jerseyServletParams.put("org.atmosphere.cpr.padding", "whitespace");    
        
        jerseyServletParams.put("org.atmosphere.cpr.broadcasterLifeCyclePolicy", "IDLE_DESTROY");
        jerseyServletParams.put("org.atmosphere.cpr.CometSupport.maxInactiveActivity", "300000");
        
        jerseyServletParams.put("com.sun.jersey.spi.container.ResourceFilter", "org.atmosphere.core.AtmosphereFilter");
        jerseyServletParams.put("org.atmosphere.cpr.broadcasterCacheClass", "org.openhab.io.cv.cache.CVBroadcasterCache");
        
        
        // required because of bug http://java.net/jira/browse/JERSEY-361
        jerseyServletParams.put(FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, "true");

        return jerseyServletParams;
    }
    
    private ServiceDescription getDefaultServiceDescription() {
		Hashtable<String, String> serviceProperties = new Hashtable<String, String>();
		serviceProperties.put("uri", CV_SERVLET_ALIAS);
		return new ServiceDescription("_openhab-cv-server._tcp.local.", "openHAB-CV", httpPort, serviceProperties);
    }

    private ServiceDescription getSSLServiceDescription() {
    	ServiceDescription description = getDefaultServiceDescription();
    	description.serviceType = "_openhab-cv-server-ssl._tcp.local.";
    	description.serviceName = "openHAB-CV-ssl";
		description.servicePort = httpSSLPort;
		return description;
    }
}
