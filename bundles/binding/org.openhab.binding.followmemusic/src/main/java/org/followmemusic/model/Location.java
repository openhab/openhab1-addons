package org.followmemusic.model;

import java.util.Date;

public class Location {

	private int id;
	private Date date;
	private User user;
	private Sensor sensor;
	private int sensorValue;
	
	public Location(int id, Date date, User user, Sensor sensor, int sensorValue)
	{
		this.id = id;
		this.date = date;
		this.user = user;
		this.sensor = sensor;
		this.sensorValue = sensorValue;
	}
	
	public Location(User user, Sensor sensor, int sensorValue)
	{
		this(-1, new Date(), user, sensor, sensorValue);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Sensor getSensor() {
		return sensor;
	}
	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	public int getSensorValue() {
		return sensorValue;
	}
	public void setSensorValue(int sensorValue) {
		this.sensorValue = sensorValue;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
