/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
