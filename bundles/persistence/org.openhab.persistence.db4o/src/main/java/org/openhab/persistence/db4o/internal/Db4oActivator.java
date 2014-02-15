/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.persistence.db4o.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Db4oActivator implements BundleActivator{      
  
  private static Logger logger = LoggerFactory.getLogger(Db4oActivator.class); 
  
  /**
	* Called whenever the OSGi framework starts our bundle
	*/
  public void start(BundleContext bc) throws Exception {
	logger.debug("db4o persistence bundle has been started."); 
  }

  /**
   * Called whenever the OSGi framework stops our bundle
   */
  public void stop(BundleContext context) throws Exception {
	  logger.debug("db4o persistence bundle has been stopped.");
  }   	  

}