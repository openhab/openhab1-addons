package org.followmemusic.location;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.followmemusic.io.AbstractDatabaseConnection;
import org.followmemusic.io.repository.IncompleteResultException;
import org.followmemusic.io.repository.LocationRepository;
import org.followmemusic.io.repository.UserRepository;
import org.followmemusic.model.Location;
import org.followmemusic.model.Room;
import org.followmemusic.model.SortedSensorLocations;
import org.followmemusic.model.User;
import org.openhab.core.types.Command;

import biz.source_code.dsp.swing.SignalPlot;

public class DatabaseLocationManager extends LocationManager {

	private AbstractDatabaseConnection connection;
	private static final float THRESHOLD_SENSOR_VALUE = 130.0f;
	
	public DatabaseLocationManager(AbstractDatabaseConnection connection) {
		this.connection = connection;
	}
	
	@Override
	protected User findUser(int id) throws ObjectNotFoundException {
		UserRepository repo = new UserRepository(this.connection);
		try {
			return repo.findById(id);
		}
		catch(IncompleteResultException e) {
			throw new ObjectNotFoundException("User", id);
		}
	}

	@Override
	protected int extractUserId(Command command) throws CommandFormatException {
		
		String cmdStr = command.toString();
		int length = cmdStr.length();
		
		if(length != (4*3 + 2)) {
			throw new CommandFormatException("Command must respesct the format 1aaabbbccc1ddd");
		}			
				
		cmdStr = cmdStr.substring(1, 10);
		byte[] bytes = new byte[3];
		byte i;
		
		for(i=0; i<3; i++) {
			bytes[i] = (byte) Integer.parseInt(cmdStr.substring(i*3, i*3 + 3));
		}
		
		int userId = (bytes[0] * 100) + (bytes[1] * 10) + (bytes[2]);
		
		return userId;
	}

	@Override
	protected int extractSensorValue(Command command) throws CommandFormatException {
		
		String cmdStr = command.toString();
		int length = cmdStr.length();
		
		if(length != (4*3 + 2)) {
			throw new CommandFormatException("Command must respesct the format 1aaabbbccc1ddd");
		}
		
		cmdStr = cmdStr.substring(2 + 3*3, 2 + 4*3);
				
		return Integer.parseInt(cmdStr);
	}
	
	private  int framePositionX = 0, framePositionY = 0;
	private Hashtable<Integer, JFrame> frames = new Hashtable<Integer, JFrame>();
	
	private void showSensorValues(int sensorId, final float[] signal) {
		
		int w = 500;
		int h = 300;
		
		if(!frames.containsKey(sensorId)) {
			logger.warn("New frame for id "+sensorId);
			
			Dimension screenSize = new Dimension(640, 480);
						
	        final JFrame frame = new JFrame();
	        frame.setSize(w, h);
	        frame.setTitle("Sensor "+sensorId);
	        frame.setLocation(framePositionX*w, framePositionY*h);
	        
	        framePositionX++;
	        if(framePositionX*w >= screenSize.getWidth()) {
	        	framePositionX = 0;
	        	framePositionY++;
	        }
	        
	        SwingUtilities.invokeLater(new Runnable() {
			    public void run() {
			      // Here, we can safely update the GUI
			      // because we'll be called from the
			      // event dispatch thread
			    frame.setVisible(true);
			    }
			});
	        	        
	        frames.put(sensorId, frame);
		}
		
		logger.warn("Update frame for sensor "+sensorId);
		final SignalPlot plot = new SignalPlot(signal, 0, 256);
		
		final JFrame frame = frames.get(sensorId);
		
		SwingUtilities.invokeLater(new Runnable() {
		    public void run() {
		      // Here, we can safely update the GUI
		      // because we'll be called from the
		      // event dispatch thread
		    	frame.getContentPane().removeAll();
				frame.getContentPane().add(plot);
				frame.getContentPane().revalidate();
				frame.repaint();
		    }
		});
	}
	
	@Override
	protected Room computeUserLocation(User user) {
		
		logger.info("Computing user location ...");
		
		LocationRepository repo = new LocationRepository(this.connection);
		
		// Get recent user locations
		ArrayList<Location> locations = null;
		try {
			locations = repo.findRecentLocationsOfUser(user, false);
		} catch (IncompleteResultException e) {
			e.printStackTrace();
		} catch (ObjectNotFoundException e) {
			e.printStackTrace();
		}
		
		if(locations != null && locations.size() > 0)
		{
			// Sort them by sensor
			ArrayList<SortedSensorLocations> sortedLocations = SortedSensorLocations.sortLocations(locations);
						
			// Find sensors that have values higher than the threshold
			ArrayList<Hashtable<String, Object>> thresholdSensors = new ArrayList<Hashtable<String, Object>>();
			for(SortedSensorLocations l : sortedLocations)
			{
				// Get filtered values
				float values[] = l.getOutputValues();
				float max = 0;
				int index = 0, i, size = values.length;
												
				// Study values
				for(i=0; i<size; i++) {
					float v = values[i];
					
					//Find max 
					if(v > max) {
						max = v;
						//rssi id
						index = i;
					}
				}
				
				// If there is a max > threshold
				if(max > THRESHOLD_SENSOR_VALUE) {
					// Save result
					Hashtable<String, Object> val = new Hashtable<String, Object>();
					val.put("sensor", l);
					val.put("index", index);
					val.put("max", max);
					
					thresholdSensors.add(val);
				}
			}
			
			if(thresholdSensors.size() > 0)
			{
				logger.warn("------------- ALGO COMPUTING -----------");
				
				
				Collections.sort(thresholdSensors, new Comparator<Hashtable<String, Object>>() {

					@Override
					public int compare(Hashtable<String, Object> o1,
							Hashtable<String, Object> o2) {				
						
						Integer max1 = (Integer)o1.get("max");
						Integer max2 = (Integer)o2.get("max");
						
						return max1.compareTo(max2);
					}
					
				});
								
				// Extract data
				Hashtable<String, Object> data = thresholdSensors.get(0);
				
				// Get sensors data
				SortedSensorLocations s = (SortedSensorLocations) data.get("sensor");
				
				return s.getSensor().getRoom();
			}
			else {
				logger.warn("----------- ALGO FAILURE : "+thresholdSensors.size()+" sensors -------------");
			}
			
		}
		
		return null;
	}
	
	
	/**
	 * Save user location if wanted
	 * Should be overridden in child class
	 * @param location
	 */
	@Override
	protected void persistLocation(Location location)
	{
		LocationRepository repo = new LocationRepository(this.connection);
		repo.insert(location);
	}
	
	/**
	 * Save user data if wanted
	 * Should be overridden in child class
	 * @param user
	 */
	@Override
	protected void updateUser(User user)
	{
		UserRepository repo = new UserRepository(this.connection);
		repo.updateRelationship(user);
	}
	
	/**
	 * Do some additional work after the room is updated
	 * @param user
	 */
	@Override
	protected void newRoomUpdated(User user) {
		// Delete all locations related to the user
		LocationRepository repo = new LocationRepository(this.connection);
		
		repo.exportLocationsOfUser(user);
		
		if(!repo.deleteLocationsOfUser(user)) {
			logger.error("Could not delete locations");
		}
	}
	
}
