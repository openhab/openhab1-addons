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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.atmosphere.cpr.AtmosphereResource;
import org.openhab.io.cv.internal.resources.MediaTypeHelper;
import org.openhab.io.cv.internal.resources.beans.LoginBean;

/**
 * <p>This class acts as the login resource for the Cometvisu API.</p>
 * 
 * <p>The result is returned as JSON</p>
 * 
 * <p>This resource is registered with the Jersey servlet.</p>
 *
 * @author Tobias Bräutigam
 * @since 1.4.0
 */
@Path(LoginResource.PATH_LOGIN)
public class LoginResource {

    @Context UriInfo uriInfo;
    
    public static final String PATH_LOGIN = "l";

    @GET 
    @Produces( { MediaType.WILDCARD })
    public Response getLogin(
    		@Context HttpHeaders headers,
    		@QueryParam("u") String user,
    		@QueryParam("p") String password,
    		@QueryParam("d") String device,
    		@QueryParam("jsoncallback") @DefaultValue("callback") String callback,
    		@Context AtmosphereResource resource) {
    	String responseType = MediaTypeHelper.getResponseMediaType(headers.getAcceptableMediaTypes());
    	if(responseType!=null) {
	    	return Response.ok(getLoginBean(resource), responseType).build();
    	} else {
			return Response.notAcceptable(null).build();
    	}
    }

	private LoginBean getLoginBean(AtmosphereResource resource) {
		LoginBean bean = new LoginBean();
	    bean.version = "0.0.1";
	    bean.session = resource.uuid();
	    return bean;
	}

}
