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
package org.openhab.io.cv.internal.resources;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.atmosphere.cpr.Broadcaster;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.TypeParser;
import org.openhab.io.cv.CVApplication;
import org.openhab.io.cv.internal.resources.beans.SuccessBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>This class acts as a Cometvisu resource for writing to Items.</p>
 * 
 * <p>The typical content types are XML or JSON.</p>
 * 
 * <p>This resource is registered with the Jersey servlet.</p>
 *
 * @author Tobias Bräutigam
 * @since 1.4.0
 */
@Path(WriteResource.PATH_WRITE)
public class WriteResource {

	private static final Logger logger = LoggerFactory.getLogger(WriteResource.class); 

	public static final String PATH_WRITE = "w";
    
	@Context UriInfo uriInfo;
	@Context Broadcaster itemBroadcaster;

	@GET
    @Produces( { MediaTypeHelper.APPLICATION_X_JAVASCRIPT })
    public Response getResults(
    		@Context HttpHeaders headers,
    		@QueryParam("a") String itemName,
    		@QueryParam("v") String value,
    		@QueryParam("ts") long timestamp,
    		@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
		logger.debug("Received HTTP GET request at '{}' for item '{}'.", new String[] { uriInfo.getPath(), itemName });
		String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes());
		if(responseType!=null) {
			Item item = ReadResource.getItem(itemName);
			boolean commandSend = false;
			if (item!=null) {
				Command command = TypeParser.parseCommand(item.getAcceptedCommandTypes(), value);
				if (command!=null) {
					CVApplication.getEventPublisher().postCommand(item.getName(),command);
					commandSend = true;
				}
			}
	    	return Response.ok(getSuccessBean(commandSend),responseType).build();
	    	
		} else {
			return Response.notAcceptable(null).build();
		}
    }
	
	private SuccessBean getSuccessBean(boolean ok) {
		SuccessBean bean = new SuccessBean();
		bean.success = ok ? 1 : 0;
		return bean;
	}
	
}
