package org.followmemusic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import biz.source_code.dsp.filter.FilterCharacteristicsType;
import biz.source_code.dsp.filter.FilterPassType;
import biz.source_code.dsp.filter.IirFilter;
import biz.source_code.dsp.filter.IirFilterCoefficients;
import biz.source_code.dsp.filter.IirFilterDesignFisher;

public class SortedSensorLocations {

	private Sensor sensor;
	private Date[] dates;
	private float[] inputValues;
	private float[] outputValues;
	private ArrayList<Location> locations;
	
	private static float[] lowpass(float[] inputs) {
		
		// low pass	
		FilterPassType filterPassType = FilterPassType.lowpass;
        FilterCharacteristicsType filterCharacteristicsType = FilterCharacteristicsType.butterworth;
        int filterOrder = 1;
        double ripple =  2;
        double fcf1 = 0.05;
        IirFilterCoefficients coeffs = IirFilterDesignFisher.design(filterPassType, filterCharacteristicsType, filterOrder, ripple, fcf1, 0);
		
        IirFilter filter = new IirFilter(coeffs);
        
        int i, size = inputs.length;
        float[] outputs = new float[size];

        // 
        for(i=0; i<size; i++) {
            outputs[i] = (float)filter.step(inputs[i]);
        }
		
		return outputs;
	}
	
	public SortedSensorLocations(Sensor sensor, ArrayList<Location> locations) {
		
		this.locations = new ArrayList<Location>();
		this.sensor = sensor;
				
		for(Location l : locations) {
			if(l.getSensor().getId() == sensor.getId()) {
				// Add location
				this.locations.add(l);
			}
		}
		
		Collections.sort(this.locations, new Comparator<Location>() {
	        @Override
	        public int compare(Location  l1, Location  l2)
	        {
	            return  l1.getDate().compareTo(l2.getDate());
	        }
	    });
		
		int i, size = this.locations.size();
		this.dates = new Date[size];
		this.inputValues = new float[size];
		
		for(i=0; i<size; i++) {
			Location l = this.locations.get(i);
			this.dates[i] = l.getDate();
			this.inputValues[i] = l.getSensorValue();
		}
		
		this.outputValues = lowpass(this.inputValues);
	}
	
	public static ArrayList<SortedSensorLocations> sortLocations(ArrayList<Location> locations)
	{
		ArrayList<SortedSensorLocations> sortedSensorLocations = new ArrayList<SortedSensorLocations>();		
		Hashtable<Integer, ArrayList<Location>> map = new Hashtable<Integer, ArrayList<Location>>();
		
		// Separate each sensors
		for(Location l : locations) {
			int id = l.getSensor().getId();
			
			// Add key
			if(!map.containsKey(id)) {
				map.put(id, new ArrayList<Location>());
			}
			
			// Add location to list
			ArrayList<Location> list = map.get(id);
			list.add(l);
		}
		
		// Create each SortedSensorLocations
		Iterator<Entry<Integer, ArrayList<Location>>> it = map.entrySet().iterator();
		while(it.hasNext()) {
			Entry<Integer, ArrayList<Location>> entry = it.next();
			
			ArrayList<Location> value = entry.getValue();
			if(value.size() > 0) {
				Sensor s = value.get(0).getSensor();
				
				sortedSensorLocations.add(new SortedSensorLocations(s, value));
			}
		}
		
		return sortedSensorLocations;
	}

	public Sensor getSensor() {
		return sensor;
	}

	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}

	public Date[] getDates() {
		return dates;
	}

	public void setDates(Date[] dates) {
		this.dates = dates;
	}

	public float[] getInputValues() {
		return inputValues;
	}

	public void setInputValues(float[] inputValues) {
		this.inputValues = inputValues;
	}

	public float[] getOutputValues() {
		return outputValues;
	}

	public void setOutputValues(float[] outputValues) {
		this.outputValues = outputValues;
	}
}
