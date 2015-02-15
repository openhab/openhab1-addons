/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.action.tinkerforge.internal;

import java.util.Dictionary;

import org.openhab.core.scriptengine.action.ActionService;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class registers an OSGi service for the TinkerForge action.
 * 
 * @author Theo Weiss
 * @since 1.7.0
 */
public class TinkerForgeActionService implements ActionService, ManagedService {

  private static final Logger logger = LoggerFactory.getLogger(TinkerForgeActionService.class);

  /**
   * Indicates whether this action is properly configured which means all necessary configurations
   * are set. This flag can be checked by the action methods before executing code.
   */
  /* default */static boolean isProperlyConfigured = false;

  public TinkerForgeActionService() {
    isProperlyConfigured = true;
  }

  public void activate() {
    logger.debug("TinkerForge action activated");
  }

  public void deactivate() {
    logger.debug("TinkerForge action deactivated");
  }

  @Override
  public String getActionClassName() {
    return TinkerForge.class.getCanonicalName();
  }

  @Override
  public Class<?> getActionClass() {
    return TinkerForge.class;
  }

  /**
   * @{inheritDoc
   */
  @Override
  public void updated(Dictionary<String, ?> config) throws ConfigurationException {
    isProperlyConfigured = true;
  }

}
