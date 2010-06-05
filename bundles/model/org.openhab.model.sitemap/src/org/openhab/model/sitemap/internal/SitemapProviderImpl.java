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
