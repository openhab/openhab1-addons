/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.common;

import java.text.DecimalFormat;

/**
 * Common definitions and helpers for LCN.
 * 
 * @author Tobias Jüttner
 */
public final class LcnDefs {
	
	/** Text encoding used by LCN-PCHK. */
	public static final String LCN_ENCODING = "UTF-8";
	
	/**
	 * LCN dimming mode.
	 * If solely modules with firmware 170206 or newer are present, LCN-PRO automatically programs {@link #STEPS200}.
	 * Otherwise the default is {@link #STEPS50}.
	 * Since LCN-PCHK doesn't know the current mode, it must explicitly be set.
	 */
	public enum OutputPortDimMode {
		STEPS50,  // 0..50 dimming steps (all LCN module generations)
		STEPS200  // 0..200 dimming steps (since 170206)
	}
	
	/**
	 * Tells LCN-PCHK how to format output-port status-messages.
	 * {@link #NATIVE} allows to show the status in half-percent steps (e.g. "10.5").
	 * {@link #NATIVE} is completely backward compatible and there are no restrictions
	 * concerning the LCN module generations. It requires LCN-PCHK 2.3 or higher though.
	 */
	public enum OutputPortStatusMode {
		PERCENT,  // Default (compatible with all versions of LCN-PCHK)
		NATIVE  // 0..200 steps (since LCN-PCHK 2.3)
	}
	
	/**
	 * Converts the given time into an LCN ramp value.
	 * 
	 * @param timeMSec the time in milliseconds
	 * @return the (LCN-internal) ramp value (0..250) 
	 */
	public static int timeToRampValue(int timeMSec) {
		int ret;
		if (timeMSec < 250) {
			ret = 0;
		}
		else if (timeMSec < 500) {
			ret = 1;
		}
		else if (timeMSec < 660) {
			ret = 2;
		}
		else if (timeMSec < 1000) {
			ret = 3;
		}
		else if (timeMSec < 1400) {
			ret = 4;
		}
		else if (timeMSec < 2000) {
			ret = 5;
		}
		else if (timeMSec < 3000) {
			ret = 6;
		}
		else if (timeMSec < 4000) {
			ret = 7;
		}
		else if (timeMSec < 5000) {
			ret = 8;
		}
		else if (timeMSec < 6000) {
			ret = 9;
		}
		else {
			ret = (timeMSec / 1000 - 6) / 2 + 10;
			if (ret >= 250) {
				ret = 250;
			}
		}
		return ret;
	}
	
	/** LCN variable types. */
	public enum Var {
		
		UNKNOWN,  // Used if the real type is not known (yet)
		VAR1ORTVAR, VAR2ORR1VAR, VAR3ORR2VAR,
		VAR4, VAR5, VAR6, VAR7, VAR8, VAR9, VAR10, VAR11, VAR12,  // Since 170206
		R1VARSETPOINT, R2VARSETPOINT,  // Set-points for regulators
		THRS1, THRS2, THRS3, THRS4, THRS5,  // Register 1 (THRS5 only before 170206)
		THRS2_1, THRS2_2, THRS2_3, THRS2_4,  // Register 2 (since 2012)
		THRS3_1, THRS3_2, THRS3_3, THRS3_4,  // Register 3 (since 2012)
		THRS4_1, THRS4_2, THRS4_3, THRS4_4,  // Register 4 (since 2012)		
		S0INPUT1, S0INPUT2, S0INPUT3, S0INPUT4;  // LCN-BU4L
		
		/** Helper array to get {@link Var} by numeric id. */
		private static final Var[] varIdToVarArray = new Var[] {
			Var.VAR1ORTVAR, Var.VAR2ORR1VAR, Var.VAR3ORR2VAR, Var.VAR4, Var.VAR5,
			Var.VAR6, Var.VAR7, Var.VAR8, Var.VAR9, Var.VAR10, Var.VAR11, Var.VAR12
		};
		
		/** Helper array to get set-point {@link Var} by numeric id. */
		private static final Var[] setVarIdToVarArray = new Var[] {
			Var.R1VARSETPOINT, Var.R2VARSETPOINT
		};
		
		/** Helper arrays to get threshold {@link Var} by numeric id. */
		private static final Var[][] thrsToVarArrays = new Var[][] {
			new Var[] { THRS1, THRS2, THRS3, THRS4, THRS5 },
			new Var[] { THRS2_1, THRS2_2, THRS2_3, THRS2_4 },
			new Var[] { THRS3_1, THRS3_2, THRS3_3, THRS3_4 },
			new Var[] { THRS4_1, THRS4_2, THRS4_3, THRS4_4 }
		};
		
		/** Helper array to get S0-input {@link Var} by numeric id. */
		private static final Var[] s0IdToVarArray = new Var[] {
			Var.S0INPUT1, Var.S0INPUT2, Var.S0INPUT3, Var.S0INPUT4
		};
	
		/**
		 * Translates a given id into a variable type.
		 * 
		 * @param varId 0..11
		 * @return the translated {@link Var}
		 * @throws IllegalArgumentException if out of range
		 */
		public static Var varIdToVar(int varId) throws IllegalArgumentException {
			if (varId < 0 || varId >= varIdToVarArray.length) {
				throw new IllegalArgumentException();
			}
			return varIdToVarArray[varId];
		}
		
		/**
		 * Translates a given id into a LCN set-point variable type.
		 * 
		 * @param setPointId 0..1
		 * @return the translated {@link Var}
		 * @throws IllegalArgumentException if out of range
		 */
		public static Var setPointIdToVar(int setPointId) throws IllegalArgumentException {
			if (setPointId < 0 || setPointId >= setVarIdToVarArray.length) {
				throw new IllegalArgumentException();
			}
			return setVarIdToVarArray[setPointId];
		}
		
		/**
		 * Translates given ids into a LCN threshold variable type.
		 * 
		 * @param registerId 0..3
		 * @param thrsId 0..4 for register 0, 0..3 for registers 1..3
		 * @return the translated {@link Var}
		 * @throws IllegalArgumentException if out of range
		 */
		public static Var thrsIdToVar(int registerId, int thrsId) throws IllegalArgumentException {
			if (registerId < 0 || registerId >= thrsToVarArrays.length ||
				thrsId < 0 || thrsId >= (registerId == 0 ? 5 : 4)) {
				throw new IllegalArgumentException();
			}
			return thrsToVarArrays[registerId][thrsId];
		}		
		
		/**
		 * Translates a given id into a LCN S0-input variable type.
		 * 
		 * @param s0Id 0..3
		 * @return the translated {@link Var}
		 * @throws IllegalArgumentException if out of range
		 */
		public static Var s0IdToVar(int s0Id) throws IllegalArgumentException {
			if (s0Id < 0 || s0Id >= s0IdToVarArray.length) {
				throw new IllegalArgumentException();
			}
			return s0IdToVarArray[s0Id];
		}
		
		/**
		 * Translates a given variable type into a variable id.
		 *  
		 * @param var the variable type to translate
		 * @return 0..11 or -1 if wrong type
		 */
		public static int toVarId(Var var) {
			switch (var) {
				case VAR1ORTVAR: return 0;
				case VAR2ORR1VAR: return 1;
				case VAR3ORR2VAR: return 2;
				case VAR4: return 3;
				case VAR5: return 4;
				case VAR6: return 5;
				case VAR7: return 6;
				case VAR8: return 7;
				case VAR9: return 8;
				case VAR10: return 9;
				case VAR11: return 10;
				case VAR12: return 11;
				default: return -1;
			}
		}
		
		/**
		 * Translates a given variable type into a set-point id.
		 * 
		 * @param var the variable type to translate
		 * @return 0..1 or -1 if wrong type
		 */
		public static int toSetPointId(Var var) {
			switch (var) {
				case R1VARSETPOINT: return 0;
				case R2VARSETPOINT: return 1;
				default: return -1;
			}
		}
		
		/**
		 * Translates a given variable type into a threshold register id.
		 * 
		 * @param var the variable type to translate
		 * @return 0..3 or -1 if wrong type
		 */
		public static int toThrsRegisterId(Var var) {
			switch (var) {
				case THRS1: case THRS2: case THRS3: case THRS4: case THRS5: return 0;
				case THRS2_1: case THRS2_2: case THRS2_3: case THRS2_4: return 1;
				case THRS3_1: case THRS3_2: case THRS3_3: case THRS3_4: return 2;
				case THRS4_1: case THRS4_2: case THRS4_3: case THRS4_4: return 3;
				default: return -1;
			}
		}
		
		/**
		 * Translates a given variable type into a threshold id.
		 * 
		 * @param var the variable type to translate
		 * @return 0..4 or -1 if wrong type
		 */
		public static int toThrsId(Var var) {
			switch (var) {
				case THRS1: case THRS2_1: case THRS3_1: case THRS4_1: return 0;
				case THRS2: case THRS2_2: case THRS3_2: case THRS4_2: return 1;
				case THRS3: case THRS2_3: case THRS3_3: case THRS4_3: return 2;
				case THRS4: case THRS2_4: case THRS3_4: case THRS4_4: return 3;
				case THRS5: return 4;
				default: return -1;
			}
		}
		
		/**
		 * Translates a given variable type into an S0-input id. 
		 * 
		 * @param var the variable type to translate
		 * @return 0..3 or -1 if wrong type
		 */
		public static int toS0Id(Var var) {
			switch (var) {
				case S0INPUT1: return 0;
				case S0INPUT2: return 1;
				case S0INPUT3: return 2;
				case S0INPUT4: return 3;
				default:
					return -1;
			}
		}
		
		/**
		 * Checks if the the given variable type is lockable.
		 * 
		 * @param var the variable type to check
		 * @return true if lockable
		 */
		public static boolean isLockableRegulatorSource(Var var) {
            return var == R1VARSETPOINT || var == Var.R2VARSETPOINT;
        }

		/**
		 * Checks if the given variable type uses special values.
		 * Examples for special values: "No value yet", "sensor defective" etc.  
		 * 
		 * @param var the variable type to check
		 * @return true if special values are in use
		 */
		public static boolean useLcnSpecialValues(Var var) {
        	return var != S0INPUT1 && var != S0INPUT2 && var != S0INPUT3 && var != S0INPUT4;
        }
        
		/**
		 * Module-generation check.
		 * Checks if the given variable type would receive a typed response if
		 * its status was requested. 
		 * 
		 * @param var the variable type to check
		 * @param swAge the target LCN-modules firmware version
		 * @return true if a response would contain the variable's type
		 */
		public static boolean hasTypeInResponse(Var var, int swAge) {
        	if (swAge < 0x170206) {
	        	switch (var) {
					case VAR1ORTVAR: 
					case VAR2ORR1VAR:
					case VAR3ORR2VAR:
					case R1VARSETPOINT:
					case R2VARSETPOINT:
						return false;
					default:
						break;
	        	}
        	}
        	return true;
        }
        
		/**
		 * Module-generation check.
		 * Checks if the given variable type automatically sends status-updates on
		 * value-change. It must be polled otherwise.
		 * 
		 * @param var the variable type to check
		 * @param swAge the target LCN-module's firmware version
		 * @return true if the LCN module supports automatic status-messages for this {@link Var}
		 */
		public static boolean isEventBased(Var var, int swAge) {
        	if (toSetPointId(var) != -1 || toS0Id(var) != -1) {
        		return true;
        	}
        	return swAge >= 0x170206;
        }

		/**
		 * Module-generation check.
		 * Checks if the target LCN module would automatically send status-updates if
		 * the given variable type was changed by command. 
		 * 
		 * @param var the variable type to check
		 * @param is2013 the target module's-generation
		 * @return true if a poll is required to get the new status-value
		 */
		public static boolean shouldPollStatusAfterCommand(Var var, boolean is2013) {
        	// Regulator set-points will send status-messages on every change (all firmware versions)
        	if (toSetPointId(var) != -1) {
        		return false;
        	}
        	// Thresholds since 170206 will send status-messages on every change
        	if (is2013 && toThrsRegisterId(var) != -1) {
        		return false;
        	}
        	// Others:
        	// - Variables before 170206 will never send any status-messages
        	// - Variables since 170206 only send status-messages on "big" changes
        	// - Thresholds before 170206 will never send any status-messages
        	// - S0-inputs only send status-messages on "big" changes
        	// (all "big changes" cases force us to poll the status to get faster updates)
        	return true;
        }
		
		/**
		 * Module-generation check.
		 * Checks if the target LCN module would automatically send status-updates if
		 * the given regulator's lock-state was changed by command. 
		 * 
		 * @param swAge the target LCN-module's firmware version
		 * @param lockState the lock-state sent via command
		 * @return true if a poll is required to get the new status-value
		 */
		public static boolean shouldPollStatusAfterRegulatorLock(int swAge, boolean lockState) {
			// LCN modules before 170206 will send an automatic status-message for "lock", but not for "unlock"
			return lockState == false && swAge < 0x170206;
		}
        
	}
	
	/** Measurement units used with LCN variables. */
	public enum VarUnit {
		
		NATIVE,  // LCN internal representation (0 = -100°C for absolute values)
		CELSIUS, KELVIN, FAHRENHEIT,
		LUX_T, LUX_I,
		METERPERSECOND,  // Used for LCN-WIH wind speed
		PERCENT,  // Used for humidity
		PPM,  // Used by CO2 sensor
		VOLT, AMPERE,
		DEGREE;  // Used for angles
		
		/**
		 * Parses the given input text into a variable unit.
		 * 
		 * @param input the text to parse
		 * @return the parsed {@link VarValue}
		 * @throws IllegalArgumentException if input could not be parsed
		 */
		public static VarUnit parse(String input) throws IllegalArgumentException {
			switch (input.toUpperCase()) {
				case "LCN":
					return NATIVE;
				case "CELSIUS":
				case "°CELSIUS":
				case "°C":
					return CELSIUS;
				case "KELVIN":
				case "°KELVIN":
				case "°K":
					return KELVIN;
				case "FAHRENHEIT":
				case "°FAHRENHEIT":
				case "°F":
					return FAHRENHEIT;
				case "LUX_T":
				case "LX_T":
					return LUX_T;					
				case "LUX":
				case "LX":
					return LUX_I;
				case "M/S":
					return METERPERSECOND;
				case "%":
					return PERCENT;
				case "PPM":
					return PPM;
				case "VOLT":
				case "V":
					return VOLT;
				case "AMPERE":
				case "AMP":  // Also a correct abbreviation
				case "A":
					return AMPERE;
				case "DEGREE":
				case "°":
					return DEGREE;
				default:
					throw new IllegalArgumentException();
			}
		}
		
	}
	
	/**
	 * A value of an LCN variable.
	 * <p>
	 * It internally stores the native LCN value and allows to convert from/into other units.
	 * Some conversions allow to specify whether the source value is absolute or relative.
	 * Relative values are used to create {@link VarValue}s that can be added/subtracted from
	 * other (absolute) {@link VarValue}s.    
	 */
	public static class VarValue {
		
		/** The absolute, native LCN value.*/
		private int nativeValue;
		
		/**
		 * Returns the lock-state if value comes from a regulator set-point.
		 * Only valid if {@link Var#isLockableRegulatorSource(Var)} returns true. 
		 * 
		 * @return the lock state
		 */
		public boolean isLockedRegulator() {
			return (this.nativeValue & 0x8000) != 0;
		}
		
		/**
		 * Constructor with native LCN value.
		 * 
		 * @param nativeValue the native value
		 */
		private VarValue(int nativeValue) {
			this.nativeValue = nativeValue;
		}
		
		/**
		 * Creates a variable value from any input.
		 * 
		 * @param v the input value
		 * @param unit the input value's unit
		 * @param abs true for absolute values (relative values are used to add/subtract from other {@link VarValue}s)
		 * @return the variable value (never null)
		 */
		public static VarValue fromVarUnit(double v, VarUnit unit, boolean abs) {
			switch (unit) {
				case NATIVE:
					return fromNative((int)v);
				case CELSIUS:
					return fromCelsius(v, abs);
				case KELVIN:
					return fromKelvin(v, abs);
				case FAHRENHEIT:
					return fromFahrenheit(v, abs);
				case LUX_T:
					return fromLuxT(v);
				case LUX_I:
					return fromLuxI(v);					
				case METERPERSECOND:
					return fromMetersPerSec(v);
				case PERCENT:
					return fromPercent(v);
				case PPM:
					return fromPpm(v);
				case VOLT:
					return fromVolt(v);
				case AMPERE:
					return fromAmpere(v);
				case DEGREE:
					return fromDegree(v);
			}
			throw new Error();
		}
		
		/**
		 * Creates a variable value from native input.
		 * 
		 * @param n the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromNative(int n){
			return new VarValue(n);
		}
		
		/**
		 * Creates a variable value from °C input.
		 *  
		 * @param c the input value
		 * @param abs true for absolute values (relative values are used to add/subtract from other {@link VarValue}s)
		 * @return the variable value (never null)
		 */
		public static VarValue fromCelsius(double c, boolean abs) {
			int n = (int)Math.round(c * 10);
			return new VarValue(abs ? n + 1000 : n);
		}

		/**
		 * Creates an absolute variable value from °C input.
		 *  
		 * @param c the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromCelsius(double c) {
			return fromCelsius(c, true);
		}
		
		/**
		 * Creates a variable value from °K input.
		 *  
		 * @param k the input value
		 * @param abs true for absolute values (relative values are used to add/subtract from other {@link VarValue}s)
		 * @return the variable value (never null)
		 */
		public static VarValue fromKelvin(double k, boolean abs) {
			if (abs) {
		        k -= 273.15;
			}
			int n = (int)Math.round(k * 10);
			return new VarValue(abs ? n + 1000 : n);
		}
		
		/**
		 * Creates an absolute variable value from °K input.
		 *  
		 * @param k the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromKelvin(double k) {
			return fromKelvin(k, true);
		}
		
		/**
		 * Creates a variable value from °F input.
		 *  
		 * @param f the input value
		 * @param abs true for absolute values (relative values are used to add/subtract from other {@link VarValue}s)
		 * @return the variable value (never null)
		 */
		public static VarValue fromFahrenheit(double f, boolean abs) {
			if (abs) {
		        f -= 32;
			}
		    int n = (int)Math.round(f / 0.18);
		    return new VarValue(abs ? n + 1000 : n);
		}
		
		/**
		 * Creates an absolute variable value from °F input.
		 *  
		 * @param f the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromFahrenheit(double f) {
			return fromFahrenheit(f, true);
		}
		
		/**
		 * Creates a variable value from lx input.
		 * Target must be connected to T-port. 
		 *  
		 * @param l the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromLuxT(double l) {
			return new VarValue((int)Math.round((Math.log(l) - 1.689646994) / 0.010380664));
		}
		
		/**
		 * Creates a variable value from lx input.
		 * Target must be connected to I-port.
		 *  
		 * @param l the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromLuxI(double l) {
			return new VarValue((int)Math.round(Math.log(l) * 100));
		}
		
		/**
		 * Creates a variable value from % input.
		 *  
		 * @param p the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromPercent(double p) {
			return new VarValue((int)Math.round(p));
		}
		
		/**
		 * Creates a variable value from ppm input.
		 * Used for CO2 sensors.
		 *  
		 * @param p the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromPpm(double p) {
			return new VarValue((int)Math.round(p));
		}

		/**
		 * Creates a variable value from m/s input.
		 * Used for LCN-WIH wind speed.
		 *  
		 * @param ms the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromMetersPerSec(double ms) {
			return new VarValue((int)Math.round(ms * 10));
		}
		
		/**
		 * Creates a variable value from V input.
		 *  
		 * @param v the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromVolt(double v) {
			return new VarValue((int)Math.round(v * 400));
		}
		
		/**
		 * Creates a variable value from A input.
		 *  
		 * @param a the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromAmpere(double a) {
			return new VarValue((int)Math.round(a * 100));
		}
		
		/**
		 * Creates a variable value from ° (angle) input.
		 *  
		 * @param d the input value
		 * @param abs true for absolute values (relative values are used to add/subtract from other {@link VarValue}s)
		 * @return the variable value (never null)
		 */
		public static VarValue fromDegree(double d, boolean abs) {
			int n = (int)Math.round(d * 10);
			return new VarValue(abs ? n + 1000 : n);
		}
		
		/**
		 * Creates an absolute variable value from ° (angle) input.
		 *  
		 * @param d the input value
		 * @return the variable value (never null)
		 */
		public static VarValue fromDegree(double d) {
			return fromDegree(d, true);
		}

		/**
		 * Converts the value to double using the given variable unit.
		 * 
		 * @param unit the target {@link VarUnit}
		 * @param isLockableRegulatorSource use {@link Var#isLockableRegulatorSource(Var)}
		 * @return the converted value (as double)
		 */
		public double toVarUnit(VarUnit unit, boolean isLockableRegulatorSource) {
			VarValue v = new VarValue(isLockableRegulatorSource ? this.nativeValue & 0x7fff : this.nativeValue);
			switch (unit) {
				case NATIVE:
					return v.toNative();
				case CELSIUS:
					return v.toCelsius();
				case KELVIN:
					return v.toKelvin();
				case FAHRENHEIT:
					return v.toFahrenheit();
				case LUX_T:
					return v.toLuxT();
				case LUX_I:
					return v.toLuxI();					
				case METERPERSECOND:
					return v.toMetersPerSec();
				case PERCENT:
					return v.toPercent();
				case PPM:
					return v.toPpm();
				case VOLT:
					return v.toVolt();
				case AMPERE:
					return v.toAmpere();
				case DEGREE:
					return v.toDegree();
			}
			throw new Error();
		}
		
		/**
		 * Converts to native value.
		 * 
		 * @return the converted value
		 */
		public int toNative() {
			return this.nativeValue;
		}
		
		/**
		 * Converts to °C value.
		 * 
		 * @return the converted value
		 */
		public double toCelsius() {
			return (double)(this.nativeValue - 1000) / 10;
		}
		
		/**
		 * Converts to °K value.
		 * 
		 * @return the converted value
		 */
		public double toKelvin() {
			return (double)(this.nativeValue - 1000) / 10 + 273.15;
		}
		
		/**
		 * Converts to °F value.
		 * 
		 * @return the converted value
		 */
		public double toFahrenheit() {
			return (double)(this.nativeValue - 1000) * 0.18 + 32;
		}
		
		/**
		 * Converts to lx value.
		 * Source must be connected to T-port.
		 * 
		 * @return the converted value
		 */
		public double toLuxT() {
			return Math.exp(0.010380664 * (double)this.nativeValue + 1.689646994);
		}
		
		/**
		 * Converts to lx value.
		 * Source must be connected to I-port.
		 * 
		 * @return the converted value
		 */
		public double toLuxI() {
			return Math.exp((double)this.nativeValue / 100);
		}
		
		/**
		 * Converts to % value.
		 * 
		 * @return the converted value
		 */
		public double toPercent() {
			return this.nativeValue;
		}
		
		/**
		 * Converts to ppm value.
		 * 
		 * @return the converted value
		 */
		public double toPpm() {
			return this.nativeValue;
		}
		
		/**
		 * Converts to m/s value.
		 * 
		 * @return the converted value
		 */
		public double toMetersPerSec() {
			return (double)this.nativeValue / 10;
		}
		
		/**
		 * Converts to V value.
		 * 
		 * @return the converted value
		 */
		public double toVolt() {
			return (double)this.nativeValue / 400;
		}
		
		/**
		 * Converts to A value.
		 * 
		 * @return the converted value
		 */
		public double toAmpere() {
			return (double)this.nativeValue / 100;
		}
		
		/**
		 * Converts to ° (angle) value.
		 * 
		 * @return the converted value
		 */
		public double toDegree() {
			return (double)(this.nativeValue - 1000) / 10;
		}
		
		/**
		 * Converts the value to a human readable form using the given variable unit.
		 *  
		 * @param unit the target {@link VarUnit}
		 * @param isLockableRegulatorSource use {@link Var#isLockableRegulatorSource(Var)}
		 * @param useLcnSpecialValues use {@link Var#useLcnSpecialValues(Var)}
		 * @return the value as text
		 */
		public String toVarUnitString(VarUnit unit, boolean isLockableRegulatorSource, boolean useLcnSpecialValues) {
		    String ret;
		    if (useLcnSpecialValues && this.nativeValue == 0xffff) {  // No value
		        ret = "---";
		    }
		    else if (useLcnSpecialValues && (this.nativeValue & 0xff00) == 0x8100) {  // Undefined
		        ret = "---";
		    }
		    else if (useLcnSpecialValues && (this.nativeValue & 0xff00) == 0x7f00) {  // Defective
		        ret = "--- (!!!)";
		    }
		    else {
			    VarValue v = new VarValue(isLockableRegulatorSource ? this.nativeValue & 0x7fff : this.nativeValue);
				switch (unit) {
					case NATIVE:
						ret = new DecimalFormat("0").format(v.toNative());
						break;
					case CELSIUS:
						ret = new DecimalFormat("0.0").format(v.toCelsius());
						break;
					case KELVIN:
						ret = new DecimalFormat("0.0").format(v.toKelvin());
						break;
					case FAHRENHEIT:
						ret = new DecimalFormat("0.0").format(v.toFahrenheit());
						break;
					case LUX_T:
						if (v.toNative() > 1152) {  // Max. value the HW can do
							ret = "---";
						}
						else {
							ret = new DecimalFormat("0").format(v.toLuxT());
						}
					case LUX_I:
						if (v.toNative() > 1152) {  // Max. value the HW can do
							ret = "---";
						}
						else {
							ret = new DecimalFormat("0").format(v.toLuxI());
						}
						break;						
					case METERPERSECOND:
						ret = new DecimalFormat("0").format(v.toMetersPerSec());
						break;
					case PERCENT:
						ret = new DecimalFormat("0").format(v.toPercent());
						break;
					case PPM:
						ret = new DecimalFormat("0").format(v.toPpm());
						break;
					case VOLT:
						ret = new DecimalFormat("0").format(v.toVolt());
						break;
					case AMPERE:
						ret = new DecimalFormat("0").format(v.toAmpere());
						break;
					case DEGREE:
						ret = new DecimalFormat("0.0").format(v.toDegree());
						break;
					default:
						throw new Error();
				}
				// Handle locked regulators
				if (isLockableRegulatorSource && this.isLockedRegulator()) {
					ret = "(" + ret + ")";
				}
		    }
			return ret;
		}
		
	}
	
	/** Possible states for LCN LEDs. */
	public enum LedStatus {
		OFF,
		ON,
		BLINK,
		FLICKER
	}
	
	/** Possible states for LCN logic-operations. */
	public enum LogicOpStatus {
		NOT,
		OR,  // Note: Actually not correct since AND won't be OR also
		AND
	}
	
	/** Time units used for several LCN commands. */
	public enum TimeUnit {

		SECONDS,
		MINUTES,
		HOURS,
		DAYS;
		
		/**
		 * Parses the given input into a time unit.
		 * It supports several alternative terms.
		 * 
		 * @param input the text to parse
		 * @return the parsed {@link TimeUnit}
		 * @throws IllegalArgumentException if input could not be parsed
		 */
		public static TimeUnit parse(String input) throws IllegalArgumentException {
			switch (input.toUpperCase()) {
				case "SECONDS":
				case "SECOND":  // Allow singular too
				case "SEC":
				case "S":
					return SECONDS;
				case "MINUTES":
				case "MINUTE":  // Allow singular too
				case "MIN":
				case "M":
					return MINUTES;
				case "HOURS":
				case "HOUR":  // Allow singular too
				case "H":
					return HOURS;
				case "DAYS":
				case "DAY":   // Allow singular too
				case "D":
					return DAYS;
			}
			throw new IllegalArgumentException();
		}
		
	}
	
	/** Relay-state modifiers used in LCN commands. */
	public enum RelayStateModifier {
		ON,
		OFF,
		TOGGLE,
		NOCHANGE
	}	
	
	/** Value-reference for relative LCN variable commands. */
	public enum RelVarRef {
		CURRENT,
		PROG  // Programmed value (LCN-PRO). Relevant for set-points and thresholds. 
	}
	
	/** Command types used when sending LCN keys. */
	public enum SendKeyCommand {
		HIT,
		MAKE,
		BREAK,
		DONTSEND
	}
	
	/** Key-lock modifiers used in LCN commands. */
	public enum KeyLockStateModifier {
		ON,
		OFF,
		TOGGLE,
		NOCHANGE
	}
	
}
