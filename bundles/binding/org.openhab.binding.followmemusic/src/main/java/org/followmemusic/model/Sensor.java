package org.followmemusic.model;

public class Sensor {

	private int id;
	private String name;
	private Room room;
	private Door door;
	
	public Sensor(int id, String name, Room room, Door door) {
		this.id = id;
		this.name = name;
		this.room = room;
		this.door = door;
	}
	
	public Sensor(int id, String name) {
		this(id, name, null, null);
	}
	
	public Sensor(int id) {
		this(id, null);
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setRoom(Room room){
		this.room = room;
	}
	
	public Room getRoom(){
		return this.room;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Door getDoor() {
		return door;
	}

	public void setDoor(Door door) {
		this.door = door;
	}
}
