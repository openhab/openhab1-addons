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
package org.openhab.io.rest.test.internal;

import org.osgi.service.http.HttpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * this Service registers the resources for the TestApp
 * 
 * @author Oliver Mazur
 * @since 0.9.0
 */
public class TestService
{
	private static final Logger logger = LoggerFactory.getLogger(TestService.class);
	 
   protected void bindHttpService( HttpService httpService )
   {
      try { 	 
          httpService.registerResources( "/resttest", "/html", null );
          httpService.registerResources( "/resttest/jquery", "/jquery", null );
          logger.info("Registered resources for the REST TestApp");
      } catch( Exception ex ) {
         ex.printStackTrace();
      }
   }

   protected void unbindHttpService( HttpService httpService )
   {
      httpService.unregister( "/resttest" );
      httpService.unregister( "/resttest/jquery" );
   }
}
