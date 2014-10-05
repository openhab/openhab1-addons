/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal.cling;

import org.fourthline.cling.DefaultUpnpServiceConfiguration;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.transport.impl.jetty.StreamClientConfigurationImpl;
import org.fourthline.cling.transport.impl.jetty.StreamClientImpl;
import org.fourthline.cling.transport.spi.StreamClient;

/**
 * A {@link UpnpServiceConfiguration} required when running inside 
 * an OSGi container. See <a href="http://4thline.org/projects/cling/core/manual/cling-core-manual.html#section.BasicAPI.UpnpService.Configuration">
 * customizing configuration settings</a> in the Cling manual for more.
 * 
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class JettyUpnpServiceConfiguration extends DefaultUpnpServiceConfiguration {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StreamClient<?> createStreamClient() {
		return new StreamClientImpl(new StreamClientConfigurationImpl(getSyncProtocolExecutorService()));
	}
	
}
