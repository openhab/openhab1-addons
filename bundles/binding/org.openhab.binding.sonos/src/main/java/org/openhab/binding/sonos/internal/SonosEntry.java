/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sonos.internal;

import java.io.Serializable;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * SonosEntry is a datastructure to represent (music) entries in the Sonos device's queue, libraries, and so forth
 * 
 * @author Karel Goderis 
 * @since 1.1.0
 * 
 */
public class SonosEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4543607156929701588L;
	private final String id;
    private final String title;
    private final String parentId;
    private final String upnpClass;
    private final String res;
    private final String album;
    private final String albumArtUri;
    private final String creator;
    private final int originalTrackNumber;
    
    public SonosEntry(String id, String title, String parentId, String album, String albumArtUri, 
            String creator, String upnpClass, String res) {
    	this(id, title, parentId, album, albumArtUri, creator, upnpClass, res, -1);
    }
    
    public SonosEntry(String id, String title, String parentId, String album, String albumArtUri, 
        String creator, String upnpClass, String res, int originalTrackNumber) {
      this.id = id;
      this.title=title;
      this.parentId = parentId;
      this.album = album;
      this.albumArtUri = albumArtUri;
      this.creator = creator;
      this.upnpClass = upnpClass;
      this.res = res;
      this.originalTrackNumber = originalTrackNumber;
    }
    
    /**
     * @return the title of the entry.
     */
    @Override
    public String toString() {
      return title;
    }
    
    /**
     * @return the unique identifier of this entry.
     */
    public String getId() {
      return id;
    }
    
    /**
     * @return the title of the entry.
     */
    public String getTitle() {
      return title;
    }
    
    /**
     * @return the unique identifier of the parent of this entry.
     */
    public String getParentId() {
      return parentId;
    }

    /**
     * @return a URI of this entry.
     */
    public String getRes() {
      return res;
    }

    /**
     * @return the UPnP classname for this entry.
     */
    public String getUpnpClass() {
      return upnpClass;
    }

    /**
     * @return the name of the album.
     */
    public String getAlbum() {
      return album;
    }

    /**
     * @return the URI for the album art.
     */
    public String getAlbumArtUri() {
      return StringEscapeUtils.unescapeXml(albumArtUri);
    }

    /**
     * @return the name of the artist who created the entry.
     */
    public String getCreator() {
      return creator;
    }
    
    public int getOriginalTrackNumber() {
    	return originalTrackNumber;
    }
}
