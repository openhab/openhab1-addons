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

package org.openhab.io.rest.internal.resources;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.openhab.io.rest.internal.resources.beans.RootBean;

import com.sun.jersey.api.json.JSONWithPadding;

/**
 * <p>This class acts as an entry point / root resource for the REST API.</p>
 * <p>In good HATEOAS manner, it provides links to other offered resources.</p>
 * 
 * <p>The result is returned as XML or JSON</p>
 * 
 * <p>This resource is registered with the Jersey servlet.</p>
 *
 * @author Kai Kreuzer
 * @since 0.8.0
 */
@Path("/")
public class RootResource {

    @Context UriInfo uriInfo;

    @GET 
    @Produces( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public RootBean getRoot() {
        return getRootBean();

    }

	@GET
    @Produces( { "application/x-javascript" })
    public JSONWithPadding getJSONPRoot(@QueryParam("jsoncallback") @DefaultValue("callback") String callback) {
   		return new JSONWithPadding(getRootBean(), callback);
    }

	private RootBean getRootBean() {
		RootBean bean = new RootBean();
	    
	    bean.links.put("items", uriInfo.getBaseUriBuilder().path(ItemResource.PATH_ITEMS).build().toASCIIString());
	    bean.links.put("sitemaps", uriInfo.getBaseUriBuilder().path(SitemapResource.PATH_SITEMAPS).build().toASCIIString());
	    
	    return bean;
	}

}
