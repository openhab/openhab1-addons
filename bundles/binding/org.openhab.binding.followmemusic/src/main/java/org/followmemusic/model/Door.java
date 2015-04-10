package org.followmemusic.model;

public class Door {

	private int id;
	private String name;
	private Sensor[] sensors;
	
	public Door(int id, String name) {
		this.id = id;
		this.name = name;
		this.sensors = new Sensor[2];
	}
	
	public Door(int id) {
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
	public Sensor[] getSensors() {
		return sensors;
	}
	public void setSensors(Sensor[] sensors) {
		this.sensors = sensors;
	}
}
