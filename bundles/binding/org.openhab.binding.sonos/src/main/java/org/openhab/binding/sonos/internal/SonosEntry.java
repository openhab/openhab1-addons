/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
