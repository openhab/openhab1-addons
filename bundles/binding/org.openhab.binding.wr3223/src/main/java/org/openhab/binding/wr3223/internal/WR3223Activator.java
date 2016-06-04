package org.openhab.binding.wr3223.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WR3223Activator implements BundleActivator {

    private static Logger logger = LoggerFactory.getLogger(WR3223Activator.class);

    private static BundleContext context;

    @Override
    public void start(BundleContext context) throws Exception {
        WR3223Activator.context = context;
        logger.debug("WR3223 Binding has been started.");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context = null;
        logger.debug("WR3223 Binding has been stopped.");

    }

}
