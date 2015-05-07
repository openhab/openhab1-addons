/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic.data;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.lcn.logic.data.LCNSyntax.DataType;
import org.openhab.binding.lcn.logic.data.LCNSyntax.VarModifier;

/**
 * Represents a LCN Module that delivers input (e.g. a sensor).
 * This class is not only used to represent the Modules, but also their input.
 * Basically it is a interim representation of LCNShort Command, LCN Commands and LCN Input,
 * this allows us to compare all three types and figure out in how far they are related.
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNInputModule extends LCNModule {

	/**The type of data contained in the LCNInputModule. Only relevant for ModuleTypes DATA / DATAO.*/
	public LCNSyntax.DataType datatype = LCNSyntax.DataType.NONE;
	/**The data 'port' from where the data needs to be read.*/
	public int dataId;
	/**The value of the module (i.e. sensor data).*/
	public long value;
	/**Binary values for Modules that represent relays or binary sensors.*/
	public boolean[] bools = new boolean[8];
	/**Defines what kind of data are to be expected from this module.*/
	public ModuleType type = ModuleType.NONE;
	/**A flag that is set when a request was sent to this specific module.*/
	public boolean wasRequested = false;
	/**A flag that is set if this module represents a coupler.*/
	public boolean isSegmentCoupler = false;
	/**A flag that is set if the module is inside the home segment.*/
	public boolean isHomeSegment = false;
	/**A special data format for light scenes.*/
	public SceneData sceneData = null;
	/**LCN Short form of the parent (if available).*/
	public String LcnShort = null;
	/**The string value of the module (for non numeric values).*/
	public String sValue = null;
	/**LED information.*/
	public LEDInfo led = null;
	/**IP*/
	public String id = null;
	/**Engines*/
	public List<EngineStatus> engines = null;
	/**Thresholds (and Hysteresis)*/
	public List<Threshold> thresholds = null;
	/**The modifier of this module.*/
	public VarModifier modifier = VarModifier.NONE;
	
	/**
	 * Represents the type of module and thus also the kind of data that this module will contain.
	 * 
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum ModuleType {
		
		NONE, OUTLET, BINARY, RELAY, DATA, DATAO, SERIAL, FIRMWARE, THRESHOLD, INFO, OEM, LED, ENGINE, ENGINE_REPORT;		
		
	}
	
	/**
	 * Special data class for light scenes.
	 * 
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 *
	 */
	public static class SceneData {
		
		public int scene;
		public Integer register;
		public Integer[] value = new Integer[4];
		public Integer[] ramp = new Integer[4];
		
		/**
		 * Simple visible constructor. Necessary for the LCNParser.
		 */
		public SceneData() {}
		
		/**
		 * Copies an existing Scene.
		 * @param original The original.
		 * @param copy The copy.
		 */
		public SceneData(SceneData original, SceneData copy) {
			
			if (null != copy) {
				this.scene = copy.scene;
				if (copy.register != null) {
					this.register = copy.register;
				} else if (null != original.register) {			
					this.register = original.register;
				}
				
				for (int i = 0; i < 4; i++) {
					this.value[i] = copy.value[i];
					this.ramp[i] = copy.ramp[i];
				}
			}
		}
		
		/**
		 * Returns a String, representing the values inside the object.
		 * @return A String with the representation.
		 */
		@Override
		public String toString() {
			
			String result = "";
			
			result = "Scene: " + scene + " Register: " + register;
			
			for (int i = 0; i < 4; i++) {
				
				if (null != value[i] && null != ramp[i]) {
					result += " Value " + (i + 1) + ": " + value[i] + " Ramp " + (i + 1) + ": " + ramp[i];
				} 
				
			}
			
			return result;
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object o) {
			
			boolean result = true;
			
			try {
			
				SceneData other = (SceneData) o;
				
				if (null == other || this.scene != other.scene) {
					result = false;
				} else if (this.register != other.register && null != other.register) {
					result = false;
				}
				
				if (result) {
					
					for (int i = 0; i < 4; i++) {
						
						if (!((null != this.value[i] && null == other.value[i]) || (null == this.value[i] && null != other.value[i]))) { 
						
							if (!(null == this.value[i] && null == other.value[i]) && (this.value[i].intValue() != other.value[i].intValue())) {
								
								result = false;
								break;
								
							}
							
							if (!((null != this.ramp[i] && null == other.ramp[i]) || (null == this.ramp[i] && null != other.ramp[i]))) { 
								
								if (!(null == this.ramp[i] && null == other.ramp[i]) && (this.ramp[i].intValue() != other.ramp[i].intValue())) {
								
									result = false;
									break;
									
								}
								
							} else {
								
								result = false;
								break;
								
							}
							
						} else {
							
							result = false;
							break;
							
						}						
						
					}
					
				}
			
			} catch (ClassCastException exc) {
				result = false;
			}
			
			return result;
			
		}
		
	}
	
	/**
	 * Special data class for thresholds.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 *
	 */
	public static class Threshold {
		
		public int id = 0;
		public int register = 0;
		public int value = 0;
		public boolean isHysteresis = false;
		
		public Threshold() {
			
		}
		
		public static List<Threshold> copy(List<Threshold> list) {
			
			List<Threshold> copy = new ArrayList<Threshold>();
			
			for (Threshold thres : list) {
				
				Threshold newThres = new Threshold();
				newThres.id = thres.id;
				newThres.register = thres.register;
				newThres.value = thres.value;
				newThres.isHysteresis = thres.isHysteresis;
				
			}
			
			return copy;
			
		}
		
		/**
		 * Returns a String, representing the values inside the object.
		 * @return A String with the representation.
		 */
		@Override
		public String toString() {
			
			String result = "";
			
			if (this.isHysteresis) {
				result += "Hysteresis: ";
			} else {
				result += "Threshold: ";
			}
			
			if (this.register != 0) {
				result += " Register: " + register;
			}
			
			if (this.id != 0) {
				result += " ID: " + id;
			}
			
			result += " Value: " + value;
			
			return result;
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object o) {
			
			boolean result = true;
			try {
				Threshold other = (Threshold) o;
				if (other.id != this.id || other.value != this.value || other.register != this.register || other.isHysteresis != this.isHysteresis) {
					result = false;
				}
			} catch (ClassCastException exc) {
				result = false;
			}
			return result;
			
		}
		
	}
	
	/**
	 * A simple class to represent the state of different LEDs.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public static class LEDInfo {
		
		public List<State> states = new ArrayList<State>();
		public List<Sum> sums = new ArrayList<Sum>();
		
		/**
		 * Representing the state of a LED. ON/OFF/FLICKERING/BLINKING
		 */
		public static enum State {
			
			A, E, B, F;
			
			public String toString() {
				
				String result = "";
				
				if (this == A) {
					result += "OFF";					
				} else if (this == E) {
					result += "ON";					
				} else if (this == B) {
					result += "BLINK";					
				} else if (this == F) {
					result += "FLICKER";				
				}
				
				return result;
				
			}
			
		}
		
		/**
		 * Representing the state. SATISFIED/NOT_SATISFIED/PARTIALLY_SATISFIED
		 */
		public static enum Sum {
			
			V, N, T;
			
			public String toString() {
				
				String result = "";
				
				if (this == V) {
					result += "SATISFIED";					
				} else if (this == N) {
					result += "NOT SATISFIED";					
				} else if (this == T) {
					result += "PARTIALLY SATISFIED";					
				} 
				
				return result;
				
			}
			
		}
		
		/**
		 * Simple (empty) constructor.
		 */
		public LEDInfo() {
			
		}
		
		/**
		 * Simple constructor to copy an existing LEDInfo.
		 * @param copy The LEDInfo to copy from.
		 */
		public LEDInfo(LEDInfo copy) {
			
			for (State state : copy.states) {
				this.states.add(state);
			}
			
			for (Sum sum : copy.sums) {
				this.sums.add(sum);
			}
			
		}
		
		/**
		 * Simple constructor to turn a String into a LEDInfo representation.
		 * @param input The String representing the LEDInfo.
		 */
		public LEDInfo(String input) {
			
			for (char c : input.toCharArray()) {
				
				State tState = null;
				Sum tSum = null;
				
				try {
					tState = State.valueOf(String.valueOf(c));		
				} catch (IllegalArgumentException exc) {					
					//do nothing
				}
				
				try {
					tSum = Sum.valueOf(String.valueOf(c));		
				} catch (IllegalArgumentException exc) {					
					//do nothing
				}
				
				if (tState != null) {			
					states.add(tState);			
				} else if (tSum != null) {		
					sums.add(tSum);								
				}
				
			}
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			
			String result = "";
			
			result += "States: ";
			
			for (State state : states) {
				
				result += state.toString() + "; ";
				
			}
			
			result += "   Sums: ";
			
			for (Sum sum : sums) {
				
				result += sum.toString() + "; ";
				
			}
			
			return result;
			
		}
		
		/**
		 * Compares the actual contained values!
		 * {@inheritDoc}
		 */
		@Override
		public boolean equals(Object o) {
			
			boolean result = true;
			
			try {
				LEDInfo info = (LEDInfo) o;
				
				if (this.states.size() == info.states.size() && this.sums.size() == info.sums.size()) {
					for (int i = 0; i < this.states.size(); i++) {
						if (this.states.get(i) != info.states.get(i)) {
							result = false;
							break;
						}
					}
					for (int i = 0; i < this.sums.size(); i++) {
						if (this.sums.get(i) != info.sums.get(i)) {
							result = false;
							break;
						}
					}
				}
			} catch (ClassCastException exc) {
				result = false;
			}
			
			return result;
			
		}
		
		
	}
	
	/**
	 * Represents the states of a single engine.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public static class EngineStatus {
		
		public int ID = -1;
		public int position = -1;
		public int limit = -1;
		public int stepOut = -1;
		public int stepIn = -1;
		
		/**
		 * Checks whether or not a certain component is 'unknown'.
		 * @param input The component.
		 * @return "unknown" if the component is unknown, otherwise the value of the component is returned.
		 */
		private String testUnknown(int input) {
			String result = "";
			return  result += (input == -1) ? "unknown" : input;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String toString() {
			
			String result = "";
			
			result += "Engine: #";
			result += testUnknown(ID);
			result += " Position: ";
			result += testUnknown(position);
			result += " Limit: ";
			result += testUnknown(limit);
			result += " StepOut: ";
			result += testUnknown(stepOut);
			result += " StepIn: ";
			result += testUnknown(stepIn);
			
			return result;
			
		}
		
	}
	
	/**
	 * Compares two LCNInputModules. This method will compare the content of the objects and not the objects themselves.
	 * @param o The object to compare to.
	 */
	@Override
	@Deprecated
	public boolean equals(Object o) {
		return equals(o, false, 0);
	}
	
	/**
	 * Simplified equals method.
	 */
	public boolean equals(Object o, int homeSegment) {
		return equals(o, false, homeSegment);
	}
	
	/**
	 * Compares two LCNInputModules. This method will compare the content of the objects and not the objects themselves.
	 * An additional flag can be set for softer comparison.
	 * @param o The object to compare to.
	 * @param soft True if the comparison shall be soft, false otherwise.
	 * @param homeSegment the home segment of one of the modules.
	 * @return True if the LCNInputModules are reasonably similar, false otherwise.
	 */
	public boolean equals(Object o, boolean soft, int homeSegment) {
		
		
		boolean result = super.equals(o, soft, homeSegment);
		
		try {
			
			LCNInputModule mod = (LCNInputModule) o;
			
			if (result && null != id) {
				result = (id.equals(mod.id));
			} else if (result && null == id && null == mod.id) {
				result = true;
			}
			
			if (result && !soft) {
						
					if (mod.datatype != this.datatype) { //&& !mod.similarDataType(this)) {
						result = false;
					}
					
					if (mod.type != this.type && !((mod.type == ModuleType.FIRMWARE || mod.type == ModuleType.SERIAL) && (this.type == ModuleType.FIRMWARE 
							|| this.type == ModuleType.SERIAL)) || (this.datatype != DataType.LS && this.type != ModuleType.LED && this.type != ModuleType.ENGINE_REPORT && mod.dataId != this.dataId)) { //|| !compareSimple(this.bools, mod.bools)) { && this.type != ModuleType.THRESHOLD 
						result = false;
					}
					
			}			
			
		} catch (ClassCastException e) {
			result = false;
		}
		
		return result;
		
	}
	
	/**
	 * Returns if the value of the two modules are the same.
	 * @param mod The first module.
	 * @param inputMod The second module.
	 * @return True if the values inside the modules are the same, false otherwise.
	 */
	public static boolean sameValue(LCNInputModule mod, LCNInputModule inputMod) {
		boolean same = false;
		if (!((null != mod && null == inputMod) || (null == mod && null != inputMod))) { 
		
			if (mod.type == LCNInputModule.ModuleType.DATA || mod.type == LCNInputModule.ModuleType.DATAO) {
				
				if (mod.datatype == LCNSyntax.DataType.LS && null != mod.sceneData) {
					same = (mod.sceneData.equals(inputMod.sceneData));
				} else {
					same = (mod.value == inputMod.value);
				}
				
			} else if (mod.type == LCNInputModule.ModuleType.THRESHOLD && null != mod.thresholds && null != inputMod.thresholds) {
				same = true;
				for (Threshold thres : mod.thresholds) {
					
					if (null == inputMod.thresholds && !thres.equals(inputMod.getThreshold(thres.register, thres.id))) {
						same = false;
						break;
					}
					
				}
				
			} else if (mod.type == LCNInputModule.ModuleType.SERIAL && null != mod.serial) {
				same = (mod.serial.equals(inputMod.serial));
			} else if (mod.type == LCNInputModule.ModuleType.FIRMWARE && null != mod.firmware) {
				same = (mod.firmware.equals(inputMod.firmware));
			} else if (null != mod.sValue && null != inputMod.sValue){
				same = (mod.sValue.equals(inputMod.sValue));
			} else if (mod.type == ModuleType.LED && null != mod.led && null != inputMod.led) {
				same = (mod.led.equals(inputMod.led));
			}
			
		}		
		return same;
	}
	
	/**
	 * Adopt the value of another LCNInputModule.
	 * @param inputMod the LCNInputModule to adopt from.
	 */
	public void adoptValue(LCNInputModule inputMod) {
		
		if (this.type == LCNInputModule.ModuleType.DATA || this.type == LCNInputModule.ModuleType.DATAO) {
			
			if (this.datatype == LCNSyntax.DataType.LS) {
				this.sceneData = new SceneData(this.sceneData, inputMod.sceneData);
			} else {
				this.value = inputMod.value;
			}
			
		} else if (this.type == LCNInputModule.ModuleType.THRESHOLD && null != inputMod.thresholds) {
			this.thresholds = Threshold.copy(inputMod.thresholds);	
		} else if (this.type == LCNInputModule.ModuleType.SERIAL) {
			this.serial = new String(inputMod.serial);
		} else if (this.type == LCNInputModule.ModuleType.FIRMWARE) {
			this.firmware = new String(inputMod.firmware);
		} else if (null != inputMod.sValue){
			this.sValue = new String(inputMod.sValue);
		} else if (this.type == ModuleType.LED) {
			this.led = new LEDInfo(inputMod.led);
		}
		
	}
	
	/**
	 * Returns the matching threshold to an ID.
	 * @param register The register of the target threshold.
	 * @param id The id of the target threshold.
	 * @return The threshold, or NULL if no matching threshold was found.
	 */
	public Threshold getThreshold(int register, int id) {
		
		for (Threshold thres : this.thresholds) {
			
			if (thres.register == register && thres.id == id) {
				return thres;
			}
			
		}
		
		return null;
		
	}
	
	/**
	 * Returns the hysteresis.
	 * @return The hysteresis, or NULL of none exists.
	 */
	public Threshold getHystersis() {
		
		for (Threshold thres : this.thresholds) {
			
			if (thres.isHysteresis) {
				return thres;
			}
			
		}
		
		return null;
				
	}
	
	/**
	 * Returns a certain engine.
	 * @param id The id of the engine.
	 * @return The EngineStatus or NULL of no matching EngineStatus was found.
	 */
	public EngineStatus getEngine(int id) {
		
		if (null != this.engines) {
		
			for (EngineStatus eng : this.engines) {
			
				if (eng.ID == id) {
					return eng;
				}
				
			}
		
		}
		
		return null;
		
	}
	
	/**
	 * Generates a key that depends solely on the actual underlying LCN Module.
	 * @param mod The LCNInputModule for the generation.
	 * @return A key as String, that is unique for each LCN Module.
	 */
	public static String generateKey(LCNInputModule mod, int homeSegment) {
		
		int seg = mod.segment;
		if (mod.segment == homeSegment) {
			seg = 0;
		}
		
		return mod.id + seg + mod.module;
		
	}
	
}