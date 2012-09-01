package org.openhab.binding.sonos.internal;

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

	  /*
	  public URL getAlbumArtUrl(ZonePlayer zp) throws MalformedURLException {
	    String uri = getAlbumArtUri();
	    if (uri.startsWith("/getAA")) {
	      // need to use mpath. what does this mean??
	      LOG.info("uri = " + uri);
	    } 

	    return uri.length() == 0 ? null : new URL("http", zp.getIP().getHostAddress(), zp.getPort(), uri);
	  }
	  */

	
}
