/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.model.sitemap.internal;

import org.openhab.model.core.ModelRepository;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.SitemapProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides access to the sitemap model files.
 * 
 * @author Kai Kreuzer
 *
 */
public class SitemapProviderImpl implements SitemapProvider {

	private static final Logger logger = LoggerFactory.getLogger(SitemapProviderImpl.class);
	
	private ModelRepository modelRepo = null;
	
	public void setModelRepository(ModelRepository modelRepo) {
		this.modelRepo = modelRepo;
	}
	
	public void unsetModelRepository(ModelRepository modelRepo) {
		this.modelRepo = null;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.model.sitemap.internal.SitemapProvider#getSitemap(java.lang.String)
	 */
	public Sitemap getSitemap(String sitemapName) {
		if(modelRepo!=null) {
			Sitemap sitemap = (Sitemap) modelRepo.getModel(sitemapName + ".sitemap");
			if(sitemap!=null) {
				return sitemap;
			} else {
				logger.debug("Sitemap {} can not be found", sitemapName);
				return null;
			}
		} else {
			logger.debug("No model repository service is available");
			return null;
		}
	}

}
