package org.openhab.model.sitemap;

import org.openhab.model.sitemap.Sitemap;

public interface SitemapProvider {

	/**
	 * This method provides access to sitemap model files, loads them and returns the object model tree.
	 * 
	 * @param sitemapName the name of the sitemap to load
	 * @return the object model tree, null if it is not found
	 */
	public Sitemap getSitemap(String sitemapName);

}