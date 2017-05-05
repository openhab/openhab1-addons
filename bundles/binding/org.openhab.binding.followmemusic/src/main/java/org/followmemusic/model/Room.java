package org.followmemusic.model;

import java.util.ArrayList;

public class Room {

	private int id;
	private String name;
	private ArrayList<Sensor> sensors;
	private ArrayList<User> currentUsers;
	
	public Room(int id, String name) {
		this.id = id;
		this.name = name;
		this.sensors = new ArrayList<Sensor>();
		this.currentUsers = new ArrayList<User>();
	}
	
	public Room(int id) {
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
	public ArrayList<Sensor> getSensors() {
		return sensors;
	}
	public void setSensors(ArrayList<Sensor> sensors) {
		this.sensors = sensors;
	}
	public boolean addSensor(Sensor sensor) {
		if(sensor != null && this.getSensors() != null && !this.getSensors().contains(sensor)) {
			this.getSensors().add(sensor);
			return true;
		}
		return false;
	}
	public boolean removeSensor(Sensor sensor) {
		if(sensor != null && this.getSensors() != null && this.getSensors().contains(sensor)) {
			this.getSensors().remove(sensor);
			return true;
		}
		return false;
	}
	public ArrayList<User> getCurrentUsers() {
		return currentUsers;
	}
	public void setCurrentUsers(ArrayList<User> currentUsers) {
		this.currentUsers = currentUsers;
	}
	public boolean addCurrentUser(User user) {
		if(user != null && this.getCurrentUsers() != null && !this.getCurrentUsers().contains(user)) {
			this.getCurrentUsers().add(user);
			return true;
		}
		return false;
	}
	public boolean removeCurrentUser(User user) {
		if(user != null && this.getCurrentUsers() != null && this.getCurrentUsers().contains(user)) {
			this.getCurrentUsers().remove(user);
			return true;
		}
		return false;
	}
	
}
