/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.astro.internal.job;

import org.openhab.binding.astro.internal.common.AstroType;
import org.openhab.core.library.types.OnOffType;

/**
 * Publishes the noon event.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class NoonJob extends AbstractBaseJob {

	@Override
	protected void executeJob() {
		publishState(AstroType.NOON, OnOffType.ON);
	}

}
