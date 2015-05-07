/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic.data;

/**
 * This class defines most of the LCNSyntax, its identifiers etc.
 * It should (can) not be instantiated.
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public final class LCNSyntax {
	
	/**
	 * This enum represents the target of the command, i.e. whether a single module or a whole group is targeted.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Target {
		
		MODULE, GROUP
		
	}
	
	/**
	 * This enum represents the type of command.<br>
	 * Commands followed by a '?' are not implemented yet.<br>
	 * ON = Switch ON<br>
	 * OFF = Switch OFF<br>
	 * DI = Dim<br>
	 * DIV = The Dim value of the lamp<br>
	 * TA = Toggle<br>
	 * AD = Add to Dim<br>
	 * SB = Subtract from Dim<br>
	 * FL = Flicker<br>
	 * TH = Timed<br>
	 * MT = Memory button<br>
	 * RS = Ramp stop<br>
	 * TE = Timed (preserving) <br>
	 * AY = Dim all outlets<br>
	 * AE = Activate all outlets<br>
	 * AA = Deactivate all outlets<br>
	 * AU = Toggle all outlets<br>
	 * AH = Set brightness for all outlets<br>
	 * ST, KT = Quick timer ?<br>
	 * AB = Limit outlet<br>
	 * JE = Shutter<br>
	 * DL = DALI ?<br>
	 * DLX = DALI raw ?<br>
	 * SZ = Save/load light scene<br>
	 * SZW = Choose register of light scene<br>
	 * SZD = Write light scene directly<br>
	 * SZR = Read light scene directly<br>
	 * R8 = External relay<br>
	 * R8T = Quick timer for external relay<br>
	 * R8M = Engine positioning<br>
	 * TS = Send button<br>
	 * TV = Delayed send button<br>
	 * TX = Lock button<br>
	 * TXZA = Timelock button<br>
	 * LA = Control LED<br>
	 * SMT = LED status request<br>
	 * MW = Report data<br>
	 * MWO = Report data (old firmware)<br>
	 * ZA = Add counter<br>
	 * ZS = Subtract counter<br>
	 * RE = Set controller<br>
	 * SP = Repeat threshold<br>
	 * SL1 = List thresholds<br>
	 * SS = Move threshold<br>
	 * SSO = Move threshold (old firmware)<br>
	 * NM = Request name of bus module<br>
	 * NMO = Request/Set OEM-text of a bus module<br>
	 * NMOS = Set text of bus module<br>
	 * SN = Request serial number<br>
	 * SK = Search all segment couplers<br>
	 * PM = Programming mode ?<br>
	 * GD = Dynamic groups<br>
	 * PI = Beep<br>
	 * SM = Status request<br>
	 * GTDT = Dynamic text<br>
	 * GTDD = Timed dynamic text<br>
	 * MR = LCN-MRS<br>
	 * IL = Change language for I-port<br> 
	 * BINARY = Binary sensor<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Command {
		
		ON("ON"), OFF("OFF"), DI("DIM"), DIV("DIM_VALUE"), TA("TOGGLE"), AD("ADD"), SB("SUB"), FL("FLICKER"), TH("TIMED"), 
		MT("MEMORY"), RS("RAMP_STOP"), TE("PTIMED"), AY("DIM_ALL"), AE("ALL_ON"), 
		AA("ALL_OFF"), AU("ALL_TOGGLE"), AH("ALL_BRIGHTNESS"), ST("QUICKTIMER"), 
		KT("QUICKTIMER_OLD"), AB("LIMIT"), JE("SHUTTER"), DL("DALI"), DLX("DALI_RAW"), 
		SZ("LIGHT_SCENE"), SZW("CHOOSE_REGISTER"), SZD("WRITE_SCENE"), SZR("READ_SCENE"), 
		R8("RELAY"), R8T("RELAY_TIMER"), R8G("RELAY_STATE"), R8M("MOTOR"), TS("SEND_KEYS"), TV("DELAY_KEYS"),
		TX("LOCK_KEYS"), TXZA("TIMELOCK_KEYS"), LA("LED"), SMT("LED_STATE"), MW("VAR_VALUE"), MWO("DATA_OLD"),
		ZA("VAR_ADD"), ZS("VAR_SUB"), RE("SETPOINT_VALUE"), SL1("THRESHOLD_VALUE"),
		SS("MOVE_THRESHOLD"), SSO("MOVE_THRESHOLD_OLD"), NM("GET_INFO"), NMO("GET_OEM"), SN("SN"), FM("FW"), SK("GET_COUPLER"), 
		PM("PROGRAM"), GD("GROUPS"), PI("BEEP"), SM("STATUS"), GTDT("TEXT"), GTDD("TIMED_TEXT"), MR("MRS"), IL("LANGUAGE"), BINARY("BINARY_STATE"),
		NMODE("NEW_MODE"), OMODE("OLD_MODE"), RAW("RAW");
		
		private String value = null;
		
		Command(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Command getCmd(String value) {
			
			for (Command c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes the intensity of a light.<br>
	 * G = Low<br>
	 * M = Medium<br>
	 * S = Strong<br>
	 * A = off
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Intensity {
		
		G("LOW"), M("MEDIUM") , S("HIGH") , A("OFF");
		
		private String value = null;
		
		Intensity(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Intensity getIntensity(String value) {
			
			for (Intensity c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes internal special commands.
	 * B = get binary sensor.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum InternalCommands {
		
		B("BINARY"), I("INVALID");
		
		private String value = null;
		
		InternalCommands(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static InternalCommands getInternalCommands(String value) {
			
			for (InternalCommands c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes beep types.<br>
	 * N = Normal beep.<br>
	 * S = Special beep.<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Beep {
		
		N("NORMAL"), S("SPECIAL");
		
		private String value = null;
		
		Beep(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Beep getBeep(String value) {
			
			for (Beep c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes MRS actions.<br>
	 * G = General call<br>
	 * V = Volume<br>
	 * S = Source<br>
	 * W = Webradio<br>
	 * E = Equalizer<br>
	 * I = Sound impression<br>
	 * X = Standby<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum MRSActions {
		
		G("CALL"), V("VOLUME"), S("SOURCE"), W("RADIO"), E("EQUALIZER"), I("IMPRESSION"), X("STANDBY");
		
		private String value = null;
		
		MRSActions(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static MRSActions getMRSActions(String value) {
			
			for (MRSActions c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes MRS call actions.<br>
	 * S = Start<br>
	 * E = End<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum MRSCallActions {
		
		S("START"), E("END");
		
		private String value = null;
		
		MRSCallActions(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static MRSCallActions getMRSCallActions(String value) {
			
			for (MRSCallActions c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	
	/**
	 * Describes MRS volume actions.<br>
	 * ADS = step up<br>
	 * SBS = step down<br>
	 * ADC = ongoing up<br>
	 * SBC = ongoing down<br>
	 * RS = stop ramp for ADC | SBC<br>
	 * AB = set to volume<br>
	 * SG = set volume for syncgroup<br>
	 * MU = mute<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum MRSVolumeActions {
		
		ADS("UP"), SBS("DOWN"), ADC("ONGOING_UP"), SBC("ONGOING_DOWN"), RS("RAMP_STOP"), AB("SET"), SG("SET_GROUP"), MU("MUTE");
		
		private String value = null;
		
		MRSVolumeActions(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static MRSVolumeActions getMRSVolumeActions(String value) {
			
			for (MRSVolumeActions c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes MRS source actions.<br>
	 * AB = set source<br>
	 * PR = previous source<br>
	 * NE = next source<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum MRSSourceActions {
		
		AB("SET"), PR("PREVIOUS"), NE("NEXT");
		
		private String value = null;
		
		MRSSourceActions(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static MRSSourceActions getMRSSourceActions(String value) {
			
			for (MRSSourceActions c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes MRS webradio actions.<br>
	 * PL = play<br>
	 * ST = stop<br>
	 * PR = previous channel<br>
	 * NE = next channel<br>
	 * AB = set channel<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum MRSRadioActions {
		
		PL("PLAY"), ST("STOP"), PR("PREVIOUS"), NE("NEXT"), AB("SET");
		
		private String value = null;
		
		MRSRadioActions(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static MRSRadioActions getMRSRadioActions(String value) {
			
			for (MRSRadioActions c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes thresholds.<br>
	 * E = Relative to programmed threshold.<br>
	 * R = Relative to current threshold.<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Threshold {
		
		E("PROG"), R("CURRENT");
		
		private String value = null;
		
		Threshold(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Threshold getThreshold(String value) {
			
			for (Threshold c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes threshold actions.<br>
	 * A = Add<br>
	 * S = Subtract<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum ThresholdAction {
		
		A("ADD"), S("SUB");
		
		private String value = null;
		
		ThresholdAction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static ThresholdAction getThresholdAction(String value) {
			
			for (ThresholdAction c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes targets for DALI commands.<br>
	 * E = single EVG<br>
	 * G = DALI-group<br>
	 * B = broadcast<br> 
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum DaliTarget {
		
		E("SINGLE"), G("GROUP"), B("BROADCAST");
		
		private String value = null;
		
		DaliTarget(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static DaliTarget getDaliTarget(String value) {
			
			for (DaliTarget c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes DALI commands.<br>
	 * GOS = select lighscene<br>
	 * SET = set brightness<br>
	 * OFF = off<br>
	 * DW = down<br>
	 * STUP = step up<br>
	 * STDW = step down<br>
	 * RMAX = recall max level<br>
	 * RMIN = recall min level<br>
	 * STDWOFF = step down and off<br>
	 * ONSTUP = on and step up<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum DaliCommand {
		
		GOS("LIGHT_SCENE"), SET("SET"), OFF("OFF"), DW("DOWN"), STUP("STEP_UP"), STDW("SETP_DOWN"), RMAX("MAX"), RMIN("MIN"), STDWOFF("DOWN_OFF"), ONSTUP("ON_UP");
		
		private String value = null;
		
		DaliCommand(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static DaliCommand getDaliCommand(String value) {
			
			for (DaliCommand c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes status request targets.<br>
	 * A = Outlets will answer.<br>
	 * P = Pports will answer (up to 1998).<br>
	 * R = Relays will answer.<br>
	 * B = Binary sensors will answer.<br>
	 * M = All A, P, R, B will answer.<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum StatusAction {
		
		A("OUTPUTS"), P("PPORT"), R("RELAYS"), B("BINARY"), M("ALL");
		
		private String value = null;
		
		StatusAction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static StatusAction getStatusAction(String value) {
			
			for (StatusAction c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes controller addresses.<br>
	 * A = Controller 1<br>
	 * B = Controller 2<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Controller {
		
		A("REGULATOR1"), B("REGULATOR2");
		
		private String value = null;
		
		Controller(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Controller getController(String value) {
			
			for (Controller c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes Controller Actions.<br>
	 * A = Controller 1<br>
	 * B = Controller 2<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum ControllerAction {
		
		SE("SET"), P("PUSH_PROG"), A("PUSH_CURRENT"), AA("ACTIVATE"), SA("DEACTIVATE");
		
		private String value = null;
		
		ControllerAction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static ControllerAction getControllerAction(String value) {
			
			for (ControllerAction c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes different types of data.<br>
	 * V = Tvar<br>
	 * TA = Temperature variable<br>
	 * TB = Temperature variable<br>
	 * SA = Current setpoint of controller 1
	 * SB = Current setpoint of controller 2
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum DataTypeOld {
		
		V("VAR"), T("TEMP"), TR("RAW"), L("LUX"), LO("LUX_T"), S("SETPOINT"), W("WIND"), M("MOIST"), CO("CO2"), VO("VOLT"), A("AMP");
		
		private String value = null;
		
		DataTypeOld(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static DataTypeOld getDataTypeOld(String value) {
			
			for (DataTypeOld c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes different types of data.<br>
	 * V = Tvar<br>
	 * TA = Temperature variable<br>
	 * TB = Temperature variable<br>
	 * SA = Current setpoint of controller 1<br>
	 * SB = Current setpoint of controller 2<br>
	 * LO = Lux at T-Port (old)<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum DataType {
		
		NONE("NONE"), V("VAR"), S("SETPOINT"), C("COUNTER"), LS("LIGHT_SCENE"); // T("TEMP"), TR("RAW"), L("LUX"), LO("LUX_T"), W("WIND"), M("MOIST"), CO("CO2"), VO("VOLT"), A("AMP"), 
		
		private String value = null;
		
		DataType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static DataType getDataType(String value) {
			
			for (DataType c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Defines possible modifier for the different variables.
	 * This is used in order to enable celsius values for setpoints etc.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum VarModifier {
		
		NONE, CELSIUS, LUX, LUX_T, WIND, MOISTURE, CO2, VOLT, AMP;
		
	}
	
	
	/**
	 * Describes possible actions with buttons.<br>
	 * K / H = Short push<br>
	 * L / M = Long push<br>
	 * O / B = Release<br>
	 * - = Don't use this table
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum ButtonAction {
		
		K("SHORT"), L("LONG"), O("RELEASE"), D("-");
		
		private String value = null;
		
		ButtonAction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static ButtonAction getButtonAction(String value) {
			
			for (ButtonAction c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes an action for lightscenes.<br>
	 * A = Load<br>
	 * S = Save
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Action {
		
		A("LOAD"), S("SAVE");
		
		private String value = null;
		
		Action(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Action getAction(String value) {
			
			for (Action c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes an action for engines.
	 * ZU = close<br>
	 * AU = open<br>
	 * A = force open (a '!' has to be added)<br>
	 * ST = stop<br>
	 * SL = set limit<br>
	 * GO = goto (in steps of 1% --> 100 = completely open)<br>
	 * GP = goto (in steps of 0.5% --> 200 = completely open)<br>
	 * AP = add to position (in steps of 0.5%)<br>
	 * SP = subtract from position (in steps of 0.5%)<br>
	 * LE = engine learns required time<br>
	 * LR = report engine status (modules until 8/2008)<br>
	 * P1 = report engine M1 + M2 status (modules from 8/2008)<br>
	 * P2 = report engine M3 + M4 status (modules from 8/2008)<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum EngineAction {
		
		ZU("CLOSE"), AU("OPEN"), A("FORCE_OPEN"), ST("STOP"),
		SL("LIMIT"), GO("GOTO"), GP("GOTO_HIGH"), AP("ADD"), SP("SUB"),
		LE("LEARN"), P1("REPORT1"), P2("REPORT2");
		
		private String value = null;
		
		EngineAction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static EngineAction getEngineAction(String value) {
			
			for (EngineAction c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Defines the different parts of an engine report.
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum EngineReport {
		
		POSITION, LIMIT, STEP_OUT, STEP_IN;
		
	}
	
	/**
	 * Describes the speed of a certain process.<br>
	 * L = Slow<br>
	 * M = Medium<br>
	 * S = Fast<br>
	 * K = Fast<br>
	 * (K and S are equivalent in meaning but must be differentiated for certain commands)
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Speed {
		
		L("SLOW"), M("MEDIUM"), S("FAST"), K("QUICK");
		
		private String value = null;
		
		Speed(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Speed getSpeed(String value) {
			
			for (Speed c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes the speed of a certain process.<br>
	 * L = Slow<br>
	 * M = Medium<br>
	 * S = Fast<br>
	 * K = Fast<br>
	 * (K and S are equivalent in meaning but must be differentiated for certain commands)
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum NameComment {
		
		N1("NAME1"), N2("NAME2"), K1("COMMENT1"), K2("COMMENT2"), K3("COMMENT3");
		
		private String value = null;
		
		NameComment(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static NameComment getNameComment(String value) {
			
			for (NameComment c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes possible actions with LEDs.<br>
	 * A = OFF<br>
	 * E = ON<br>
	 * B = FLASH<br>
	 * F = FLICKER<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum LEDAction {
		
		A("OFF"), E("ON"), B("BLINK"), F("FLICKER");
		
		private String value = null;
		
		LEDAction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static LEDAction getLEDAction(String value) {
			
			for (LEDAction c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes possible test actions with LEDs.<br>
	 * T1 = all on<br>
	 * T0 = all off<br>
	 * TE = end test<br>
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum LEDTestAction {
		
		T1("ON"), T0("OFF"), TE("END");
		
		private String value = null;
		
		LEDTestAction(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static LEDTestAction getLEDTestAction(String value) {
			
			for (LEDTestAction c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Describes the Timeunit.<br>
	 * S = Seconds<br>
	 * M = Minutes<br>
	 * H = Hours<br>
	 * D = Days
	 * @author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum Time {
		
		S("S"), M("M"), H("H"), D("D");
		
		private String value = null;
		
		Time(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.getValue();
		}
		
		public static Time getTime(String value) {
			
			for (Time c : values()) {
				if (c.getValue().equalsIgnoreCase(value)) {
					return c;
				}
			}
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/**
	 * Represents the time unit used.<br>
	 * S = Seconds<br>
	 * M = Minutes
	 *@author Patrik Pastuschek
	 * @since 1.7.0
	 */
	public enum TimeUnit {
		
		M, S
		
	}
	
	/**Make this class uninstantiable, even by reflection.*/
	private LCNSyntax() {
		throw new AssertionError();
	}
	
	/**
	 * Returns whether an input represents an outlet of a module or not.
	 * @param input The input as String.
	 * @return True if the input represents an outlet, false otherwise.
	 */
	public static boolean isOutlet(String input) {
		
		boolean result = false;
		
		if (input.equals(Command.ON.value) || input.equals(Command.OFF.value) || input.equals(Command.DI.value) || input.equals(Command.DIV.value) || input.equals(Command.TA.value) 
				|| input.equals(Command.AD.value) || input.equals(Command.SB.value) || input.equals(Command.FL.value) || input.equals(Command.TH.value) || input.equals(Command.MT.value) 
				|| input.equals(Command.RS.value) || input.equals(Command.TE.value) || input.equals(Command.ST.value) || input.equals(Command.KT.value) || input.equals(Command.AB.value)) {
			
			return true;
			
		}
		
		return result;
		
	}
	
}
