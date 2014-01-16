/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
