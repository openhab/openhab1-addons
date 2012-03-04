package org.openhab.persistence.db4o.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Db4oActivator implements BundleActivator{      

  private static BundleContext context;
  
  private static Logger logger = LoggerFactory.getLogger(Db4oActivator.class); 
  
  /**
	* Called whenever the OSGi framework starts our bundle
	*/
  public void start(BundleContext bc) throws Exception {
	 
	context = bc;
	logger.debug("db4o persistence bundle has been started."); 
  }

  /**
   * Called whenever the OSGi framework stops our bundle
   */
  public void stop(BundleContext context) throws Exception {
	  context = null;
	  logger.debug("db4o persistence bundle has been stopped.");
  }   	  
  
  /**
   * Returns the bundle context of this bundle
   * @return the bundle context
   */
  public static BundleContext getContext() {
	  return context;
  }
  
}