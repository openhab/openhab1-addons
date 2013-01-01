/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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

/**
 * SonosMetaData is a structure to capture and store meta information of songs, tracks, URI's and so forth
 * 
 * @author Karel Goderis 
 * @since 1.1.0
 * 
 */
public class SonosMetaData {

	  private final String id;
	  private final String parentId;
	  private final String resource;
	  private final String streamContent;
	  private final String albumArtUri;
	  private final String title;
	  private final String upnpClass;
	  private final String creator;
	  private final String album;
	  private final String albumArtist;
	  
	  public SonosMetaData(String id, String parentId, String res, String streamContent, 
	      String albumArtUri, String title, String upnpClass, String creator, String album, 
	      String albumArtist) {
	    this.id = id;
	    this.parentId = parentId;
	    this.resource = res;
	    this.streamContent = streamContent;
	    this.albumArtUri = albumArtUri;
	    this.title = title;
	    this.upnpClass = upnpClass;
	    this.creator = creator;
	    this.album = album;
	    this.albumArtist = albumArtist;
	  }
	  
	  @Override
	  public String toString() {
		  return "SonosMetaData [id=" + id + ", parentID=" + parentId + ", resource="+resource+" ,streamContent="+
	  streamContent+", arturi="+albumArtUri+", title="+title+", upnpclass="+upnpClass+", creator="+creator+
	  ", album="+album+", albumtartist="+albumArtist+"]";
	  }

	  public String getAlbum() {
	    return album;
	  }

	  public String getAlbumArtist() {
	    return albumArtist;
	  }

	  public String getAlbumArtUri() {
	    return albumArtUri;
	  }

	  public String getCreator() {
	    return creator;
	  }

	  public String getResource() {
	    return resource;
	  }

	  public String getStreamContent() {
	    return streamContent;
	  }

	  public String getTitle() {
	    return title;
	  }

	  public String getUpnpClass() {
	    return upnpClass;
	  }

	  public String getId() {
	    return id;
	  }

	  public String getParentId() {
	    return parentId;
	  }	
}
