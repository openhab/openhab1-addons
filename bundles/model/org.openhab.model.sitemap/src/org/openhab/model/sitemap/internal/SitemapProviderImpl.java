/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

package org.openhab.model.sitemap.internal;

import java.util.Dictionary;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.openhab.model.SitemapStandaloneSetup;
import org.openhab.model.sitemap.Sitemap;
import org.openhab.model.sitemap.SitemapProvider;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Injector;

/**
 * This class provides access to the sitemap model files.
 * 
 * @author Kai Kreuzer
 *
 */
public class SitemapProviderImpl implements ManagedService, SitemapProvider {

	private static final Logger logger = LoggerFactory.getLogger(SitemapProviderImpl.class);
	
	/** The sitemaps should all be stored in a common folder */
	private String sitemapFolder = null;
	
	@SuppressWarnings("rawtypes")
	@Override
	public void updated(Dictionary config) throws ConfigurationException {
		if(config!=null) {
			sitemapFolder = (String) config.get("sitemapFolder");
		}
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.model.sitemap.internal.SitemapProvider#getSitemap(java.lang.String)
	 */
	@Override
	public Sitemap getSitemap(String sitemapName) {
		if(sitemapFolder!=null) {
			new org.eclipse.emf.mwe.utils.StandaloneSetup().setPlatformUri("../");
			Injector injector = new SitemapStandaloneSetup().createInjectorAndDoEMFRegistration();
			XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
			resourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
			Resource resource = resourceSet.getResource(
			    URI.createFileURI(sitemapFolder + "/" + sitemapName + ".sitemap"), true);
			if(resource.getContents().size()>0) {
				return (Sitemap) resource.getContents().get(0);
			} else {
				logger.debug("Sitemap {} can not be found in folder {}", sitemapName, sitemapFolder);
				return null;
			}
		} else {
			logger.debug("No sitemap folder has been configured");
			return null;
		}
	}

}
