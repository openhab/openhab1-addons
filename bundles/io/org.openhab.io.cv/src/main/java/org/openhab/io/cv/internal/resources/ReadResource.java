/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.internal.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.atmosphere.annotation.Suspend.SCOPE;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;
import org.atmosphere.jersey.SuspendResponse;
import org.openhab.core.items.GroupItem;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.types.State;
import org.openhab.io.cv.internal.ReturnType;
import org.openhab.io.cv.internal.broadcaster.CometVisuBroadcaster;
import org.openhab.io.cv.internal.listeners.ItemStateChangeListener;
import org.openhab.io.cv.internal.resources.beans.GroupItemBean;
import org.openhab.io.cv.internal.resources.beans.ItemBean;
import org.openhab.io.cv.internal.resources.beans.ItemListBean;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * This class acts as a Cometvisu resource for reading Items.
 * </p>
 * 
 * <p>
 * The typical content types are XML or JSON.
 * </p>
 * 
 * <p>
 * This resource is registered with the Jersey servlet.
 * </p>
 * 
 * @author Tobias Bräutigam
 * @since 1.4.0
 */
@Path(ReadResource.PATH_READ)
public class ReadResource {

	private static final Logger logger = LoggerFactory
			.getLogger(ReadResource.class);

	public static final String PATH_READ = "r";
	
	
	@Context
	UriInfo uriInfo;
	@Context
	Broadcaster itemBroadcaster;

	@GET
	@Produces({ MediaType.WILDCARD })
	public SuspendResponse<Response> getResults(
			@Context HttpHeaders headers,
			@QueryParam("a") List<String> itemNames,
			@QueryParam("i") long index,
			@QueryParam("t") long time,
			@QueryParam("jsoncallback") @DefaultValue("callback") String callback,
    		@Context AtmosphereResource resource) {
		final String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes());
		if (logger.isDebugEnabled()) {
			logger.debug("Received HTTP GET request at '{}' for {} items at index '{}', ResponseType: '{}'.",
				uriInfo.getPath(), itemNames.size(), index, responseType);
		}
		List<ReturnType> rts = getReturnTypes(itemNames);
		if(index==0) {
			// first request => return all values
			if (responseType != null) {
				throw new WebApplicationException(Response.ok(getItemStateListBean(rts,System.currentTimeMillis()), responseType).build());
			} else {
				throw new WebApplicationException(Response.notAcceptable(null).build());
			}
		}
		
		BroadcasterFactory broadcasterFactory = resource.getAtmosphereConfig().getBroadcasterFactory();
		CometVisuBroadcaster itemBroadcaster = (CometVisuBroadcaster) broadcasterFactory.lookup(CometVisuBroadcaster.class, resource.getRequest().getPathInfo(), true);
		itemBroadcaster.addStateChangeListener(new ItemStateChangeListener(rts));
		
		return new SuspendResponse.SuspendResponseBuilder<Response>()
			.scope(SCOPE.REQUEST)
			.resumeOnBroadcast(!ResponseTypeHelper.isStreamingTransport(resource.getRequest()))
			.broadcaster(itemBroadcaster)
			.outputComments(true).build();
	}
	
	private List<ReturnType> getReturnTypes(List<String> itemNames) {
		List<ReturnType> rts = new ArrayList<ReturnType>();
		for (String cvItemName : itemNames) {
			try {
				ReturnType rt = new ReturnType(cvItemName);
				rts.add(rt);
			} catch (ItemNotFoundException e) {
				logger.trace(e.getMessage());
			}
		}
		return rts;
	}
	
	public ItemStateListBean getItemStateListBean(List<ReturnType> rts, long index) {
		ItemStateListBean stateList = new ItemStateListBean(new ItemListBean(getItemBeans(rts)));
		stateList.index = index+1;
		return stateList;
	}
	
	public Collection<ItemBean> getItemBeans(List<ReturnType> rts) {
		Collection<ItemBean> beans = new LinkedList<ItemBean>();
		for (ReturnType rt : rts) {
			beans.add(createItemBean(rt));
		}
		return beans;
	}
	
	public static ItemBean createItemBean(ReturnType rt) {
		return createItemBean(rt.getItem(),false,rt.getStateClass(),rt.getClientItemName());
	}

	public static ItemBean createItemBean(Item item, boolean drillDown, Class<? extends State> stateClass, String clientItemName) {
		ItemBean bean;
		if (item instanceof GroupItem && drillDown) {
			GroupItem groupItem = (GroupItem) item;
			GroupItemBean groupBean = new GroupItemBean();
			Collection<ItemBean> members = new HashSet<ItemBean>();
			for (Item member : groupItem.getMembers()) {
				members.add(createItemBean(member, false, null, null));
			}
			groupBean.members = members.toArray(new ItemBean[members.size()]);
			bean = groupBean;
		} else {
			String state = stateClass==null ? item.getState().toString() : item.getStateAs(stateClass).toString();
			String name = clientItemName==null ? item.getName() : clientItemName;
			bean = new ItemBean(name, state);
		}
		return bean;
	}
}
