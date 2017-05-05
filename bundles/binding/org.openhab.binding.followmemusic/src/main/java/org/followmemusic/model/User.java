package org.followmemusic.model;

import java.util.ArrayList;

public class User {
	
	private int id;
	private String name;
	private ArrayList<Location> locations;
	private Room currentRoom;
	
	public User(int id, String name, ArrayList<Location> locations) {
		this.id = id;
		this.name = name;
		this.locations = locations;
	}
	
	public User(int id, String name) {
		this(id, name, new ArrayList<Location>());
	}
	
	public User(int id) {
		this(id, null);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Room getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(Room currentRoom) {
		this.currentRoom = currentRoom;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}
	public void setLocations(ArrayList<Location> locations) {
		this.locations = locations;
	}
	
	/**
	 * Add new location
	 * @param location
	 * @return TRUE if successfully added, FALSE otherwise
	 */
	public boolean addLocation(Location location) {
		if(location != null && this.getLocations() != null && !this.getLocations().contains(location)) {
			this.getLocations().add(location);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove location
	 * @param location
	 * @return TRUE if successfully removed, FALSE otherwise
	 */
	public boolean removeLocation(Location location) {
		if(location != null && this.getLocations() != null && this.getLocations().contains(location)) {
			this.getLocations().remove(location);
			return true;
		}
		return false;
	}
}
