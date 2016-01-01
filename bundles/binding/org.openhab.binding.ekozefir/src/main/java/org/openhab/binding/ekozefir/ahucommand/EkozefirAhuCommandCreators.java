/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.openhab.binding.ekozefir.AhuCommandCreators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 * Implementation of AhuCommandCreators.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirAhuCommandCreators implements AhuCommandCreators {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirAhuCommandCreators.class);
	private final Map<String, AhuCommandCreator> creators = Maps.newHashMap();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(AhuCommandCreator creator) {
		logger.debug("Adding ahu command creator {}", creator);
		Objects.requireNonNull(creator);
		creators.put(creator.getId(), creator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(AhuCommandCreator creator) {
		logger.debug("Removing command creator {}", creator);
		Objects.requireNonNull(creator);
		creators.remove(creator.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<AhuCommandCreator> getOfType(String name) {
		Objects.requireNonNull(name);
		if (creators.containsKey(name)) {
			return Optional.of(creators.get(name));
		}
		return Optional.empty();
	}
}
