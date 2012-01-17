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
package org.openhab.io.rest.internal.resources;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.atmosphere.annotation.Suspend;
import org.atmosphere.annotation.Suspend.SCOPE;
import org.atmosphere.cpr.AtmosphereResource;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.TypeParser;
import org.openhab.io.rest.internal.RESTApplication;
import org.openhab.io.rest.internal.listeners.ItemTransportListener;
import org.openhab.io.rest.internal.listeners.TransportListener;
import org.openhab.io.rest.internal.resources.beans.GroupItemBean;
import org.openhab.io.rest.internal.resources.beans.ItemBean;
import org.openhab.io.rest.internal.resources.beans.ItemListBean;
import org.openhab.ui.items.ItemUIRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * <p>This class acts as a REST resource for items and provides different methods to interact with them,
 * like retrieving lists of items, sending commands to them or checking a single status.</p>
 * 
 * <p>The typical content types are plain text for status values and XML or JSON(P) for more complex data
 * structures</p>
 * 
 * <p>This resource is registered with the Jersey servlet.</p>
 *
 * @author Kai Kreuzer
 * @since 0.8.0
 */
@Path(ItemResource.PATH_ITEMS)
public class ItemResource {

	private static final Logger logger = LoggerFactory.getLogger(ItemResource.class); 
	
	/** The URI path to this resource */
    public static final String PATH_ITEMS = "items";
    
	@Context UriInfo uriInfo;

	@GET
    @Produces( { MediaType.WILDCARD })
    public Response getItems(
    		@Context HttpHeaders headers,
    		@QueryParam("type") String type, 
    		@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP GET request at '{}' for media type '{}'.", new String[] { uriInfo.getPath(), type });

		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes(), type);
		if(responseType!=null) {
	    	Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ?
	    			new JSONWithPadding(new ItemListBean(getItemBeans()), callback) : new ItemListBean(getItemBeans());
	    	return Response.ok(responseObject, responseType).build();
		} else {
			return Response.notAcceptable(null).build();
		}
    }

    @GET @Path("/{itemname: [a-zA-Z_0-9]*}/state") 
	@Suspend(outputComments = false, scope = SCOPE.REQUEST, listeners = {ItemTransportListener.class}, period = 300000)
    @Produces( { MediaType.TEXT_PLAIN })
    public String getPlainItemState(
    		@PathParam("itemname") String itemname,
    		@Context AtmosphereResource<HttpServletRequest, HttpServletResponse> resource) {
    	if(!TransportListener.isAtmosphereTransport((HttpServletRequest)resource.getRequest())) {
	    	Item item = getItem(itemname);
	    	if(item!=null) {
				logger.debug("Received HTTP GET request at '{}'.", uriInfo.getPath());
	    		return item.getState().toString();
	    	} else {
	    		logger.info("Received HTTP GET request at '{}' for the unknown item '{}'.", uriInfo.getPath(), itemname);
	    		throw new WebApplicationException(404);
	    	}
		}
		return null;
    }

    @GET @Path("/{itemname: [a-zA-Z_0-9]*}")
	@Suspend(outputComments = false, scope = SCOPE.REQUEST, listeners = {ItemTransportListener.class}, period = 300000)
    @Produces( { MediaType.WILDCARD })
    public Response getItemData(
    		@Context HttpHeaders headers,
    		@PathParam("itemname") String itemname, 
    		@QueryParam("type") String type, 
    		@QueryParam("jsoncallback") @DefaultValue("callback") String callback,
    		@Context AtmosphereResource<HttpServletRequest, HttpServletResponse> resource) {
		logger.debug("Received HTTP GET request at '{}' for media type '{}'.", new String[] { uriInfo.getPath(), type });
		if(!TransportListener.isAtmosphereTransport((HttpServletRequest)resource.getRequest())) {
			String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes(), type);
			if(responseType!=null) {
		    	Object responseObject = responseType.equals(MediaTypeHelper.APPLICATION_X_JAVASCRIPT) ?
		    			new JSONWithPadding(getItemDataBean(itemname), callback) : getItemDataBean(itemname);
		    	return Response.ok(responseObject, responseType).build();
			} else {
				return Response.notAcceptable(null).build();
			}
		}
		return null;
    }
    
    @PUT @Path("/{itemname: [a-zA-Z_0-9]*}/state")
	@Consumes(MediaType.TEXT_PLAIN)	
	public Response putItemState(@PathParam("itemname") String itemname, String value) {
    	Item item = getItem(itemname);
    	if(item!=null) {
    		State state = TypeParser.parseState(item.getAcceptedDataTypes(), value);
    		if(state!=null) {
    			logger.debug("Received HTTP PUT request at '{}' with value '{}'.", uriInfo.getPath(), value);
    			RESTApplication.getEventPublisher().postUpdate(itemname, state);
    			return Response.ok().build();
    		} else {
    			logger.warn("Received HTTP PUT request at '{}' with an invalid status value '{}'.", uriInfo.getPath(), value);
    			return Response.status(Status.BAD_REQUEST).build();
    		}
    	} else {
    		logger.info("Received HTTP PUT request at '{}' for the unknown item '{}'.", uriInfo.getPath(), itemname);
    		throw new WebApplicationException(404);
    	}
	}

	@Context UriInfo localUriInfo;
    @POST @Path("/{itemname: [a-zA-Z_0-9]*}")
	@Consumes(MediaType.TEXT_PLAIN)	
	public Response postItemCommand(@PathParam("itemname") String itemname, String value) {
    	Item item = getItem(itemname);
    	if(item!=null) {
    		Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), value);
    		if(command!=null) {
    			logger.debug("Received HTTP POST request at '{}' with value '{}'.", uriInfo.getPath(), value);
    			RESTApplication.getEventPublisher().postCommand(itemname, command);
    			return Response.created(localUriInfo.getAbsolutePathBuilder().path("state").build()).build();
    		} else {
    			logger.warn("Received HTTP POST request at '{}' with an invalid status value '{}'.", uriInfo.getPath(), value);
    			return Response.status(Status.BAD_REQUEST).build();
    		}
    	} else {
    		logger.info("Received HTTP POST request at '{}' for the unknown item '{}'.", uriInfo.getPath(), itemname);
    		throw new WebApplicationException(404);
    	}
	}

    public static ItemBean createItemBean(Item item, boolean drillDown, String uriPath) {
    	ItemBean bean;
    	if(item instanceof GroupItem && drillDown) {
    		GroupItem groupItem = (GroupItem) item;
    		GroupItemBean groupBean = new GroupItemBean();
    		Collection<ItemBean> members = new HashSet<ItemBean>();
    		for(Item member : groupItem.getMembers()) {
    			members.add(createItemBean(member, false, uriPath));
    		}
    		groupBean.members = members.toArray(new ItemBean[members.size()]);
    		bean = groupBean;
    	} else {
    		 bean = new ItemBean();
    	}
    	bean.name = item.getName();
    	bean.state = item.getState().toString();
    	bean.type = item.getClass().getSimpleName();
    	bean.link = UriBuilder.fromUri(uriPath).path(ItemResource.PATH_ITEMS).path(bean.name).build().toASCIIString();
    	
    	return bean;
    }
    
    static public Item getItem(String itemname) {
        ItemUIRegistry registry = RESTApplication.getItemUIRegistry();
        if(registry!=null) {
        	try {
				Item item = registry.getItem(itemname);
				return item;
			} catch (ItemNotFoundException e) {
				logger.debug(e.getMessage());
			} catch (ItemNotUniqueException e) {
				logger.debug(e.getMessage());
			}
        }
        return null;
    }

	private List<ItemBean> getItemBeans() {
		List<ItemBean> beans = new LinkedList<ItemBean>();
		ItemUIRegistry registry = RESTApplication.getItemUIRegistry();
		for(Item item : registry.getItems()) {
			beans.add(createItemBean(item, false, uriInfo.getBaseUri().toASCIIString()));
		}
		return beans;
	}

	private ItemBean getItemDataBean(String itemname) {
		Item item = getItem(itemname);
		if(item!=null) {
			return createItemBean(item, true, uriInfo.getBaseUri().toASCIIString());
		} else {
			logger.info("Received HTTP GET request at '{}' for the unknown item '{}'.", uriInfo.getPath(), itemname);
			throw new WebApplicationException(404);
		}
	}
}
