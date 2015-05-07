/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lcn.logic;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openhab.binding.lcn.logic.data.LCNInputModule;
import org.openhab.binding.lcn.logic.data.LCNInputModule.EngineStatus;
import org.openhab.binding.lcn.logic.data.LCNInputModule.ModuleType;
import org.openhab.binding.lcn.logic.data.LCNInputModule.SceneData;
import org.openhab.binding.lcn.logic.data.LCNModule;
import org.openhab.binding.lcn.logic.data.LCNSyntax;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Action;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Beep;
import org.openhab.binding.lcn.logic.data.LCNSyntax.ButtonAction;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Command;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Controller;
import org.openhab.binding.lcn.logic.data.LCNSyntax.ControllerAction;
import org.openhab.binding.lcn.logic.data.LCNSyntax.DaliCommand;
import org.openhab.binding.lcn.logic.data.LCNSyntax.DaliTarget;
import org.openhab.binding.lcn.logic.data.LCNSyntax.DataType;
import org.openhab.binding.lcn.logic.data.LCNSyntax.DataTypeOld;
import org.openhab.binding.lcn.logic.data.LCNSyntax.EngineAction;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Intensity;
import org.openhab.binding.lcn.logic.data.LCNSyntax.LEDAction;
import org.openhab.binding.lcn.logic.data.LCNSyntax.MRSActions;
import org.openhab.binding.lcn.logic.data.LCNSyntax.MRSCallActions;
import org.openhab.binding.lcn.logic.data.LCNSyntax.MRSRadioActions;
import org.openhab.binding.lcn.logic.data.LCNSyntax.MRSSourceActions;
import org.openhab.binding.lcn.logic.data.LCNSyntax.MRSVolumeActions;
import org.openhab.binding.lcn.logic.data.LCNSyntax.NameComment;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Speed;
import org.openhab.binding.lcn.logic.data.LCNSyntax.StatusAction;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Threshold;
import org.openhab.binding.lcn.logic.data.LCNSyntax.Time;
import org.openhab.binding.lcn.logic.data.LCNSyntax.TimeUnit;
import org.openhab.binding.lcn.logic.data.LCNSyntax.VarModifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parses the LCN-Short form, which is being used by the user in the item declarations, to actual PCHK conform messages. <br>
 * Parses input from the LCN (PCK) bus. <br>
 * Parses LCNInputModule into special requests. <br> 
 * 
 * @author Patrik Pastuschek
 * @since 1.7.0
 *
 */
public class LCNParser {

	/**The logger, which handles output.*/
	static private final Logger logger = LoggerFactory.getLogger(LCNParser.class);
	
	private static final String POSTAMBLE = "\r\n";
	private static final String HEADER = ">";
	private static final String REQ_RESPONSE = "!"; // set to '.' for no responses!
	private static final String MODULE = "M";
	private static final String GROUP = "G";
	private static final String OUTLET = "A";
	
	private static final List<Character> RELAY_CHARSET = new ArrayList<Character>();
	private static final List<Character> BINARY_CHARSET = new ArrayList<Character>();
	private static final List<Character> ADV_BUTTON_CHARSET = new ArrayList<Character>();
	private static final List<Character> LIST_CHARSET = new ArrayList<Character>();
	private static final List<Character> OPERATOR_CHARSET = new ArrayList<Character>();
	
	
	
	/**
	 * Parses LCNShortform into actual LCN-commands
	 * @param term The LCNShortform.
	 * @return The actual LCN-command.
	 * @throws LCNParserException
	 */
	public static String parse(String term) throws LCNParserException {
		return parse(term, 0);
	}
	
	/**
	 * Parses LCNShortform into actual LCN-commands
	 * @param term The LCNShortform.
	 * @return The actual LCN-command.
	 * @throws LCNParserException
	 */
	public static String parse(String term, String firmware) throws LCNParserException {
		return parse(term, 0, firmware);
	}
	
	/**
	 * Parses LCNShortform into actual LCN-commands
	 * @param term The LCNShortform.
	 * @param homeSegment The ID of the home segment.
	 * @return The actual LCN-command.
	 * @throws LCNParserException
	 */
	public static String parse(String term, int homeSegment) throws LCNParserException {
		return parse(term, homeSegment, null);
	}
	
	/**
	 * Parses LCNShortform into actual LCN-commands
	 * @param term The LCNShortform.
	 * @param homeSegment The ID of the home segment.
	 * @return The actual LCN-command.
	 * @throws LCNParserException
	 */
	public static String parse(String term, int homeSegment, String firmware) throws LCNParserException {
		
		/**
		ON("ON"), OFF("OFF"), DI("DIM"), DIV("DIM_VALUE"), TA("TOGGLE"), AD("ADD"), SB("SUB"), FL("FLICKER"), TH("TIMED"), 
		MT("MEMORY"), RS("RAMP_STOP"), TE("PTIMED"), AY("DIM_ALL"), AE("ALL_ON"), 
		AA("ALL_OFF"), AU("ALL_TOGGLE"), AH("ALL_BRIGHTNESS"), ST("QUICKTIMER"), 
		KT("QUICKTIMER_OLD"), AB("LIMIT"), JE("SHUTTER"), DL("DALI"), DLX("DALI_RAW"), 
		SZ("LIGHT_SCENE"), SZW("CHOOSE_SCENE"), SZD("WRITE_SCENE"), SZR("READ_SCENE"), 
		R8("RELAY"), R8T("RELAY_TIMER"), R8M("ENGINE"), TS("SEND_BUTTON"), TV("DELAY_BUTTON"),
		TX("LOCK_BUTTON"), TXZA("TIMELOCK_BUTTON"), LA("LED"), SMT("LED_STATUS"), MW("DATA"), MWO("DATA_OLD"),
		ZA("ADD_COUNTER"), ZS("SUB_COUNTER"), RE("SET_CTRL"), SP("REPEAT_THRESHOLD"), SL1("LIST_THRESHOLD"),
		SS("MOVE_THRESHOLD"), SSO("MOVE_THRESHOLD_OLD"), NM("GET_INFO"), NMO("GET_OEM"), NMOS("SET_OEM"), SN("SERIAL"), SK("GET_COUPLER"), 
		PM("PROGRAM"), GD("GROUPS"), PI("BEEP"), SM("STATUS"), GTDT("TEXT"), GTDD("TIMED_TEXT"), MR("MRS"), IL("LANGUAGE");
		*/
		
		String result = null;
		QList<String> subterms = null;
		
		try {
			
			term = term.replaceAll("(\\r|\\n)", "");
		
			subterms = new QList<String>(Arrays.asList(term.split("\\.")));
			
			boolean isModule = true;
			
			VarModifier modifier = VarModifier.NONE;
			
			if (subterms.size() > 0) {
				
				Command cmd = Command.getCmd(subterms.get(0));
				
				String tempMod = null;
				
				if (subterms.size() > 1) {
				
					try {
						
						tempMod = subterms.get(subterms.size() - 1);
						modifier = VarModifier.valueOf(tempMod);
						
					} catch (IllegalArgumentException exc) {
						//do nothing. This just means, that the modifier was not specified --> no modification needed!
						if (modifier == VarModifier.NONE) {
							subterms.add(tempMod);
						}
					}
				
				}
				
				if (subterms.size() > 0) {
					String possibleGroup = subterms.get(0);
					
					if (possibleGroup.equalsIgnoreCase("GROUP")) {
						isModule = false;
					} else if (possibleGroup.equalsIgnoreCase("MODULE")) {
						isModule = true;
					} else {
						isModule = true;
						subterms.add(0, possibleGroup);
					}
				}
				
				if (null != cmd) {
					
					result = HEADER;
					
					switch(cmd) {
					
						//SWITCH ON (command is only a shortcut for DI)
						case ON:
							
							cmd = Command.DI;
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment)
								+ "100";
							if (!subterms.isEmpty()) {
								result += addParameter(subterms, 1);
							} else {
								result += "000";
							}
							break;
					
						//SWITCH OFF (command is only a shortcut for DI)
						case OFF:
							
							cmd = Command.DI;
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment)
								+ "000";
							if (!subterms.isEmpty()) {
								result += addParameter(subterms, 1);
							} else {
								result += "000";
							}
							break;
							
						//DIM or TOGGLE:
						case DI:
						case TA:
							
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment);
							if (!subterms.isEmpty()) {
								result += addParameter(subterms, 1);
							} else {
								result += "000";
							}
							break;
						
						//DIM_ALL:
						case AY:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 2);
							
							if (subterms.size() >= 3) {
								result += addParameter(subterms, 3);
							}
							break;
							
						//RAMP_STOP:
						case RS:
							
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment);
							break;
							
						//MEMORY BUTTON
						case MT:
							
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment)
							+ addParameter(subterms, 1);
							break;
						
						//ALL_ON or ALL_OFF or ALL_TOGGLE:
						case AE:
						case AA:
						case AU:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							if (!subterms.isEmpty()) {
								result += addParameter(subterms, 1, 250, 3);
							} else {
								result += "000";
							}
							break;
							
						//ALL_BRIGHTNESS:
						case AH:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 1, 100, 3);
							break;
							
						//ADD or SUBTRACT from DIMMER:
						case AD:						
						case SB:
							
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment)
								+ addParameter(subterms, 1);						
							break;
							
						//FLICKER:
						case FL:
							
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment)
								+ Intensity.getIntensity(subterms.get(0)).name()
								+ Speed.getSpeed(subterms.get(0)).name()
								+ addParameter(subterms, 1, 15);						
							break;
							
						//TIMED LIGHT:
						case TH:
						case TE:
							
							result += addOutletCmdHeader(isModule, subterms, cmd, homeSegment)
								+ addParameter(subterms, 1)
								+ TimeUnit.valueOf(subterms.get(0));
							String speed = Speed.getSpeed(subterms.get(0)).name();
							if (speed == Speed.S.name()) {
								speed = Speed.K.name(); 
							}
							result += speed;
							break;
							
						//DALI COMMANDS:
						case DL:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							DaliTarget target = DaliTarget.getDaliTarget(subterms.get(0));
							
							result += target.name();
							
							if (target == DaliTarget.E) {
								result += addParameter(subterms, 1, 63, 3);
							} else if (target == DaliTarget.G) {
								result += addParameter(subterms, 1, 15, 3);
							}
							
							DaliCommand command = DaliCommand.getDaliCommand(subterms.get(0));
							
							result += command.name();
							
							if (command == DaliCommand.GOS) {
								
								result += addParameter(subterms, 1, 15, 3);
								
							} else if (command == DaliCommand.SET) {
								
								result += addParameter(subterms, 1, 254, 3);
								
							}							
							break;
							
						//DALI RAW:
						case DLX:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 2, 255, 3);
							break;
							
						//SHUTTER or THRESHOLD-LIST or SERIALNUMBER: (Commands that need no parameter other than their command name)
						case JE:
						case SN:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							break;
							
						case SL1:
							
							result += addHeader(isModule, subterms, homeSegment);
							if (LCNUtil.compareHexDate(firmware, "160B13") < 0) {
								
								result += cmd.name();
								
							} else {
								
								result += "RE";
								String temp = result;
								result = "";
								
								for (int i = 1; i < 5; i++) {
									result += temp + i;
									if (i != 4) {
										result += POSTAMBLE;
									}
								}
								
							}
							
							break;
							
						//LIGHTSCENE:
						case SZ:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ Action.getAction(subterms.get(0)).name();
							String outlet = addParameter(subterms, 1, 7, 1);
							result += outlet
								+ addParameter(subterms, 1, 15);
							if ("0".equals(outlet)) {
								result += addParameter(subterms, 1, 0, 8);
							} else {
								result += addParameter(subterms, 1, 250);
							}
							break;
							
						//WRITE LIGHTSCENE:
						case SZW:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 1, 9);
							break;
							
						//DIRECT LIGHTSCENE:
						case SZD:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 2, 9);
							for (int i = 0; i < 4; i++) {
								
								if (subterms.size() >= 2) {		
									result += addParameter(subterms, 2);					
								}
								
							}
							break;
							
						//READ LIGHTSCENE:
						case SZR:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 2, 9);
							break;
						
						//RELAYS:
						case R8:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addStringParameter(subterms, 1, 8, getRelayCharset());
							break;
							
						//RELAYS with timer:
						case R8T:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 1, 0, 3)
								+ addStringParameter(subterms, 1, 8, getRelayCharset());
							break;
							
						//ENGINES:
						case R8M:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							String interim = subterms.get(0);
							//result += addParameter(subterms, 1, 7, 1);
							EngineAction act;
							try {
								act = EngineAction.getEngineAction(interim);
								if (act != EngineAction.P1 && act != EngineAction.P2) {
									throw new IllegalArgumentException();
								}
								result += "1";
							} catch (IllegalArgumentException exc) {
								//it is NOT a report command!
								result += intToString(Integer.parseInt(interim), 7, 1);
								act = EngineAction.getEngineAction(subterms.get(0));
							}							
							
							result += act.name();
							if (act == EngineAction.A) {
								result += "!";
							} else if (act == EngineAction.SL || act == EngineAction.GO) {
								result += addParameter(subterms, 1, 100, 3);
							} else  if (act == EngineAction.GP || act == EngineAction.AP || act == EngineAction.SP) {
								result += addParameter(subterms, 1, 200, 3);
							}
							break;
							
						//SEND BUTTONS:
						case TS:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
	
							
							for (int i = 0; i < 4; i++) {
								String button = subterms.get(0);
								try {
									 button = ButtonAction.getButtonAction(button).name();
								} catch (IllegalArgumentException exc) {
									if (i == 4) {
										subterms.add(0, button);
										button = "";
									}
								} catch (NullPointerException exc) {
									if (i == 4) {
										subterms.add(0, button);
										button = "";
									}
								}
								if (button.equals("D")) {
									button = "-";
								}
								result += button;  
							}
							
							result += addStringParameter(subterms, 1, 8, getBinaryCharset());
							
							break;
							
						//DELEAYED SEND BUTTONS:
						case TV:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addStringParameter(subterms, 1, 1, getListCharset())
								+ addParameter(subterms, 1, 0, 3)
								+ Time.getTime(subterms.get(0)).name()
								+ addStringParameter(subterms, 1, 8, getAdvButtonCharset());
							break;
							
						//LOCK BUTTONS:
						case TX:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addStringParameter(subterms, 1, 1, getListCharset())
								+ addStringParameter(subterms, 1, 8, getRelayCharset());
							break;
							
						//TIMELOCK BUTTONS:
						case TXZA:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 1, 0, 3)
								+ Time.getTime(subterms.get(0)).name()
								+ addStringParameter(subterms, 1, 8, getBinaryCharset());
							break;
						
						//SET LED:
						case LA:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							String temp = subterms.get(0);
							//try {
							//	result += LEDTestAction.getLEDTestAction(temp).name();
							//} catch (IllegalArgumentException exc) {
								subterms.add(0, temp);
								result += addParameter(subterms, 1, 12, 3);
								result += LEDAction.getLEDAction(subterms.get(0)).name();
							//}												
							break;
							
						case SMT:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							break;
							
						case MWO:
							
							cmd = Command.MW;
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							result.substring(0, result.length() - 1); //Cut of the artificial 'O' to differentiate 'old' and new procedures.
							DataTypeOld oType = DataTypeOld.getDataTypeOld(subterms.get(0));
							if (oType == DataTypeOld.TR || oType == DataTypeOld.L || oType == DataTypeOld.LO) {
								System.out.println("THIS SHOULD NOT BE CALLED!");
								oType = DataTypeOld.T;
							}
							result += oType.name();
							break;
						
						case MW:
							break;
							
						//ADD or SUBTRACT to/from counter:
						case ZA:
						case ZS:
							
							result += addHeader(isModule, subterms, homeSegment);
	
							result += cmd.name().substring(0, 1);
							if (cmd == Command.ZA) {
								result += "+";
							} else {
								result += "-";
							}
								
							result += addParameter(subterms, 1, 12, 3);
							result += addParameter(String.valueOf(parseWithModToLCNStep(Double.valueOf(subterms.get(0).replaceAll("\\,", "\\.")), modifier)), 4000, 4);
							break;					
							
						case RE:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ Controller.getController(subterms.get(0)).name();
								String ctrlAct = ControllerAction.getControllerAction(subterms.get(0)).name();
								//If the control action needs a math operator, add it:
								if (!ctrlAct.equals(ControllerAction.AA.name()) && !ctrlAct.equals(ControllerAction.SA.name())) {
									result += 'S';
									result += ctrlAct;
									if (!ctrlAct.equals(ControllerAction.SE.name())) {
										result += addStringParameter(subterms, 1, 1, getOperatorCharset());
										result += addParameter(String.valueOf(parseWithModToLCNStep(Double.valueOf(subterms.get(0).replaceAll("\\,", "\\.")), modifier)), 2000, 4);
									} else {
										//result += addParameter(subterms, 1, 2999, 4);
										result += addParameter(String.valueOf(parseWithModToLCN(Double.valueOf(subterms.get(0).replaceAll("\\,", "\\.")), modifier)), 2999, 4);
									}
								//If the control action does not need any further parameters:
								} else {
									ctrlAct = ctrlAct.substring(0, ctrlAct.length() - 1);
									result += 'X';
									result += ctrlAct;
								}

								
							break;
							
						case SS:
							
							if ((LCNUtil.compareHexDate(firmware, "160B13") >= 0)) {
								result += addHeader(isModule, subterms, homeSegment)
									+ cmd.name()
									+ Threshold.getThreshold(subterms.get(0)).name()
									+ addParameter(String.valueOf(parseWithModToLCNStep(Double.valueOf(subterms.get(0).replaceAll("\\,", "\\.")), modifier)), 1000, 4);
									//+ ThresholdAction.getThresholdAction(subterms.get(0)).name()
									String op = addStringParameter(subterms, 1, 1, getOperatorCharset());
									if (op.equals("+")) {
										result += "A";
									} else {
										result += "S";
									}
								result += 'R'
									+ addParameter(subterms, 2, 4, 1);
							} else {
								logger.error("Target module for command: {}, needs to be addressed with command: {}", term, Command.SSO);
								throw new LCNParserException ("Invalid command, please use " + Command.SSO);
							}
							break;
							
						case SSO:
							
							if ((LCNUtil.compareHexDate(firmware, "160B13") < 0)) {
								result += addHeader(isModule, subterms, homeSegment)
									+ cmd.name().substring(0, cmd.name().length() - 1) //remove the internal identifier for old commands.
									+ Threshold.getThreshold(subterms.get(0)).name()
									+ addParameter(String.valueOf(parseWithModToLCNStep(Double.valueOf(subterms.get(0).replaceAll("\\,", "\\.")), modifier)), 1000, 4);
									//+ ThresholdAction.getThresholdAction(subterms.get(0)).name();
								String op = addStringParameter(subterms, 1, 1, getOperatorCharset());
								if (op.equals("+")) {
									result += "A";
								} else {
									result += "S";
								}
								result += addStringParameter(subterms, 1, 5, getAdvButtonCharset());
							} else {
								logger.error("Target module for command: {}, needs to be addressed with command: {}", term, Command.SS);
								throw new LCNParserException ("Invalid command, please use " + Command.SS);
							}
							break;
							
						case NM:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ NameComment.getNameComment(subterms.get(0)).name();
							break;
							
						//If OEM text need to be called:
						case NMO:
							
							String cName = cmd.name();
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cName
								+ addParameter(subterms, 1, 4, 1);						
							break;
							
						//Ask for segment-coupler (only segment-couplers will answer)
						case SK:
							
							result += "G003003.SK";
							break;
							
						//Add to dynamic group (probably not needed in openHAB, since openHAB has it's own group handling).
						case GD:
							
							result += addHeader(isModule, subterms, homeSegment)
							+ cmd.name()
							+ addStringParameter(subterms, 1, 1, getOperatorCharset())
							+ addParameter(subterms, 1, 255, 3);
							break;
							
						case PI:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ Beep.getBeep(subterms.get(0)).name()
								+ addParameter(subterms, 1, 15, 0);
							break;
							
						case SM:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ StatusAction.getStatusAction(subterms.get(0)).name();
							break;
							
						case GTDT:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 1, 4, 1)
								+ addParameter(subterms, 1, 5, 1);
							String info = "" + subterms.get(0);
							
							//Text may only be 12 characters long!
							if (info.length() > 12) {
								logger.warn("Message {} was too long, it is being reduced to it's first 12 characters!", info);
								info.substring(0, 12);
							}
							result += info;
							break;
							
						case GTDD:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ addParameter(subterms, 1, 4, 1)
								+ addParameter(subterms, 1, 250, 3);
							break;
							
						case MR:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
								 MRSActions action = LCNSyntax.MRSActions.getMRSActions(subterms.get(0));
								 result += action.name();
								 
								 switch (action) {
								 
								 	case G:
								 		
								 		MRSCallActions cAction = MRSCallActions.getMRSCallActions(subterms.get(0));
								 		result += cAction.name();
								 		if (cAction == MRSCallActions.S) {
								 			result += addParameter(subterms, 1, 6, 1);
								 		}
								 		break;
								 		
								 	case V:
								 		
								 		result += getMRSOutput(subterms);
								 		MRSVolumeActions vAction = MRSVolumeActions.getMRSVolumeActions(subterms.get(0));
								 		result += vAction.name();
								 		
								 		switch (vAction) {
								 		
								 			case ADS:
								 			case SBS:
								 			case ADC:
								 			case SBC:
								 				result += addParameter(subterms, 1, 7, 1);
								 				break;
								 				
								 			case AB:
								 			case SG:
								 				result += addParameter(subterms, 1, 100, 3);
								 				break;
								 				
								 			case RS:
								 			case MU:
								 				break;
								 				
								 			default:
								 				break;
								 		
								 		}
								 		break;
								 		
								 	case S:
								 		
								 		result += getMRSOutput(subterms);
								 		MRSSourceActions sAction = MRSSourceActions.getMRSSourceActions(subterms.get(0));
								 		result += sAction.name();
								 		
								 		if (sAction == MRSSourceActions.AB) {
								 			
								 			String source = subterms.get(0);
								 			
								 			if (source.equalsIgnoreCase("OPTICAL")) {
								 				result += "7";
								 			} else if (source.equalsIgnoreCase("WEBRADIO")) {
								 				result += "8";
								 			} else {
								 				List<String> tempList = new ArrayList<String>();
								 				tempList.add(source);
								 				result += addParameter(tempList, 1, 6, 1);
								 			}
								 			
								 		}
								 		
								 		break;
								 		
								 	case W:
								 		
								 		MRSRadioActions rAction = MRSRadioActions.getMRSRadioActions(subterms.get(0));
								 		
								 		result += rAction.name();
								 		
								 		if (rAction == MRSRadioActions.AB) {
								 			result += addParameter(subterms, 1, 255, 3);
								 		}
								 		
								 		break;
								 
								 	case E:
								 				
								 		result += getMRSOutput(subterms);
								 		result += addParameter(subterms, 1, 6, 1);
								 		String equ = subterms.get(0);
								 		int db = Integer.parseInt(equ);
									 	db = db + 15;
									 	result += db;
								 		
								 		break;
								 		
								 	case I:
								 		
								 		result += getMRSOutput(subterms);
								 		Action impAction = Action.getAction(subterms.get(0));
								 		result += impAction.name()
								 				+ addParameter(subterms, 1, 16, 2);
								 		
								 		break;

								 	default:
								 		break;							 
								 
								 }
								 
							break;
							
						//Change language at I-Port:
						case IL:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name()
								+ intToString(getLanguage(subterms.get(0)));
							
							break;
							
						case AB:
							
							result += addHeader(isModule, subterms, homeSegment)
								+ cmd.name();
							String out = addParameter(subterms, 1, 4, 1);
							result += out;
							if (!out.equals("0")) {
								result += addParameter(subterms, 1, 100, 3)
										+ addParameter(subterms, 1, 999, 3);
								result += TimeUnit.valueOf(subterms.get(0));
							}
							break;
							
						//Quicktimer
						//Quicktimer (OLD)
						case ST:
						case KT:
							
							if ((LCNUtil.compareHexDate(firmware, "100700") < 0)) {
								result += addOutletCmdHeader(isModule, subterms, Command.KT, homeSegment);
							} else {
								result += addOutletCmdHeader(isModule, subterms, Command.ST, homeSegment);
							}
							result += addParameter(subterms, 1, 100, 3);
							result += addParameter(subterms, 1, 250, 3);
							break;
							
						case NMODE:
							result = "!OM1P";
							break;
							
						case OMODE:
							result = "!OM0P";
							break;
							
						case RAW:
							result = "";
							while (!subterms.isEmpty()) {
								result += subterms.get(0);
							}
							break;
							
						default:
							
							throw new LCNParserException("Command not supported yet!");
							
					}
					
				}
				
			}
			
		} catch (NumberFormatException exc) {
			if (null != subterms) {
				throw new LCNParserException("Unable to parse Command: " + term + " ... " + subterms.last + " is String, but Integer was expected");
			}
		} catch (IllegalArgumentException exc) {
			if (null != subterms) {
				throw new LCNParserException("Unknown identifier: " + subterms.last);
			}
		} catch (IndexOutOfBoundsException exc) {
			throw new LCNParserException("Not enough parameters!");
		} 
		
		if (null == result) {
			result = term + POSTAMBLE;
		} else {
			result = result + POSTAMBLE;
		}
			
		return result;
		
	}
	
	/**
	 * Parses LCNShortform into actual LCN-commands
	 * @param buff The LCNShortform inside a ByteBuffer.
	 * @param homeSegment The ID of the home segment.
	 * @return The actual LCN-command.
	 * @throws LCNParserException
	 */
	public static ByteBuffer quickParse(ByteBuffer buff, int homeSegment, String firmware) throws LCNParserException {
		return ByteBuffer.wrap(parse(new String(buff.array()), homeSegment, firmware).getBytes());
	}
	
	/**
	 * Returns a simple LCNModule extracted from the input.
	 * @param buff The ByteBuffer
	 * @return The LCNInputModule
	 */
	public static LCNInputModule getModule(ByteBuffer buff) {
		
		String input = new String(buff.array());
		LCNInputModule mod = new LCNInputModule();
		
		String subs[] = input.split("\\.");
		
		if (subs.length > 3) {
			
			try {
			
				mod.segment = Integer.valueOf(subs[1]);
				mod.module = Integer.valueOf(subs[2]);
				
			} catch (NumberFormatException exc) {
				
				mod = null;
				
			}
			
		} else {
			
			mod = null;
			
		}
		
		return mod;		
		
	}
	
	/**
	 * Adds the standard header with outlet and command.
	 * @param isModule 
	 * @param subterms
	 * @return The String with the standard header, followed by the outlet.
	 */
	private static String addOutletCmdHeader(boolean isModule, List<String> subterms, Command cmd, int homeSegment) {
		
		return addHeader(isModule, subterms, homeSegment)
				+ addOutlet(subterms)
				+ cmd.name();
		
		
	}
	
	/**
	 * Adds the standard header to the transformed output.
	 * @param isModule true if addressing a module, false if addressing a group.
	 * @param subterms the list with parameters for the transformation.
	 * @return The String with the header.
	 */
	private static String addHeader(boolean isModule, List<String> subterms, int homeSegment) {
		
		String result = "";
		
		String mod = MODULE;
		
		if (!isModule) {
			mod = GROUP;
		}
		
		result += mod
				+ addAddress(subterms, homeSegment)
				+ REQ_RESPONSE;
		
		return result;
		
	}
	
	/**
	 * Returns an amount of string parameters, which have to be formed with chars from the charset. Length will be cut or advanced if necessary.
	 * @param subterms the list with parameters for the transformation.
	 * @param amount the amount of parameters that shall be extracted from the list.
	 * @param length desired length of the created parameter.
	 * @param charset A set of characters with allowed characters. Position 0 defines the NULL element, which is to be used for short substrings.
	 * @return the desired parameter, never NULL but can be empty.
	 */
	private static String addStringParameter(List<String> subterms, int amount, int length, List<Character> charset) throws LCNParserException {
		
		String result = "";
		
		if (charset.isEmpty()) {
			charset.add(' ');
		}
		
		if (!subterms.isEmpty()) {
			for (int i = 0; i < amount; i++) {
				String interim = subterms.get(0);
				
				for (int j = 0; j < interim.length(); j++) {
					if (!charset.contains(interim.charAt(j))) {
						throw new LCNParserException("Invalid character <" + interim.charAt(j) + "> at position: " + j);
					}
				}
				
				if (interim.length() <= length) {
					while (interim.length() < length) {
						interim = charset.get(0) + interim;
					}
				} else {
					interim = interim.substring(0, length);
				}
				
				result += interim;
			}			
		}
		
		return result;
		
	}
	
	/**
	 * Adds a given amount of numerical parameters from a List of Strings, with a maximum value and given length (parameters are 'stretched' or 'cut' to fit the length).
	 * @param subterms The List with the parameters.
	 * @param amount The amount of parameters that shall be extracted.
	 * @param max The maximum value of the parameters.
	 * @param length The length of each parameter.
	 * @return A String with the parameter.
	 */
	private static String addParameter(List<String> subterms, int amount, int max, int length) {
		
		String result = "";
		
		if (!subterms.isEmpty()) {
			for (int i = 0; i < amount; i++) {
				result += intToString(Integer.valueOf(subterms.get(0)), max, length);
			}
		} else {
			for (int i = 0; i < length; i++) {
				result += "0";
			}
		}
		
		return result;
		
	}
	
	/**
	 * Adds a numerical parameter from a String, with a maximum value and given length.
	 * @param term The numerical term.
	 * @param max The maximum value of the parameter.
	 * @param length The length of the parameter.
	 * @return A String with the parameter
	 */
	private static String addParameter(String term, int max, int length) {
		
		String result = "";
		
		if (!term.isEmpty()) {
			result += intToString(Integer.valueOf(term), max, length);
		} else {
			for (int i = 0; i < length; i++) {
				result += "0";
			}
		}
		
		return result;
		
	}
	
	/**
	 * Adds a given amount of numerical parameters from a List of Strings, witch a maximum value and given length (parameters are 'stretched' or 'cut' to fit the length).
	 * @param subterms The List with the parameters.
	 * @param amount The amount of parameters that shall be extracted.
	 * @param max The maximum value of the parameters.
	 * @return A String with the parameter.
	 */
	private static String addParameter(List<String> subterms, int amount, int max) {
		
		return addParameter(subterms, amount, max, 3);
		
	}

	/**
	 * Adds a given amount of numerical parameters from a List of Strings, witch a maximum value and given length (parameters are 'stretched' or 'cut' to fit the length).
	 * @param subterms The List with the parameters.
	 * @param amount The amount of parameters that shall be extracted.
	 * @return A String with the parameter.
	 */
	private static String addParameter(List<String> subterms, int amount) {
		
		return addParameter(subterms, amount, 0, 3);
		
	}
	
	/**
	 * Returns a parameter that is identified by a prefix.
	 * @param id The prefix to identify the parameter.
	 * @param subterms The current list of terms.
	 * @return A string with the parameter. Is empty if parameter was not found!
	 */
	@SuppressWarnings("unused")
	private static String getPrefixedParameter(String id, List<String> subterms) {
		
		String result = "";
		
		if (!subterms.isEmpty()) {
			String param = subterms.get(0);
			if (param.startsWith(id)) {
				result += param.substring(id.length());
			}
		}
		
		return result;
		
	}
	
	/**
	 * Adds the standard address parameters.
	 * @param subterms List of Strings.
	 * @param homeSegment ID of the home segment.
	 * @return The address as String.
	 */
	private static String addAddress(List<String> subterms, int homeSegment) {
		
		int seg = Integer.valueOf(subterms.get(0));
		int mo = Integer.valueOf(subterms.get(0));
		
		if (seg == homeSegment) {
			seg = 0;
		}
		
		String result = new String(intToString(seg) 
				+ intToString(mo));
		
		return result;
		
	}
	
	/**
	 * Adds the standard outlet parameters.
	 * @param subterms List of Strings.
	 * @return The outlet as String.
	 */
	private static String addOutlet(List<String> subterms) {
		
		String result = new String(OUTLET
				+ Integer.valueOf(subterms.get(0)));
		return result;
		
	}
	
	/**
	 * Returns the output for a MRS module.
	 * @param subterms List of Strings.
	 * @return The mrs output as String.
	 */
	private static String getMRSOutput(List<String> subterms) {
		
		String result = "";
		
		String equ = subterms.get(0);
 		if (equ.equalsIgnoreCase("OPTICAL")) {
 			result += "13";
 		} else {
 			List<String> tempList = new ArrayList<String>();
				tempList.add(equ);
				result += addParameter(tempList, 1, 12, 2);								 			
 		}
		
		return result;
		
	}
	
	/**
	 * Unparses the lcn short form into useable information.
	 * @param input String with input.
	 * @param id The id of the bus system, where the module is located.
	 */
	public static LCNInputModule reverseParse(String input, String id) {	
		
		LCNInputModule mod = new LCNInputModule();
		
		if (null != input) {

			List<String> subs = new ArrayList<String> (Arrays.asList(input.split("\\.")));
			
			try {
				
				mod.LcnShort = input;
				mod.id = id;
				
				mod.segment = Integer.parseInt(subs.get(1));
				mod.module = Integer.parseInt(subs.get(2));
				
				mod.modifier = VarModifier.NONE;
				
				try {			
					mod.modifier = VarModifier.valueOf(subs.get(subs.size() - 1));	
					subs.remove(subs.size() - 1);
				} catch (IllegalArgumentException exc) {
					//do nothing. This just means, that the modifier was not specified --> no modification needed!
				}
				
				if (subs.get(0).equals(Command.MW.toString())) {
					
					mod.type = LCNInputModule.ModuleType.DATA;
					//mod.datatype = LCNSyntax.DataType.getDataType(subs[3]);
					mod.datatype = DataType.V;
					
					if (LCNSyntax.DataType.getDataType(subs.get(3)) == DataType.S) {
						mod.datatype = DataType.S; //TODO: maybe remove this, once we changed the command?
					}
					
					try {
						mod.dataId = Integer.parseInt(subs.get(4));
						
					} catch (NumberFormatException exc) {
						//caused if the module has an old firmware!
					} catch (ArrayIndexOutOfBoundsException exc) {
						//caused if the module has an old firmware!
					}
	
				} else if(subs.get(0).equals(Command.RE.toString())) { 
				
					mod.type = LCNInputModule.ModuleType.DATA;
					mod.datatype = LCNSyntax.DataType.S;
					
					if (subs.get(3).endsWith("2")) {
						mod.dataId = 2;
					} else {
						mod.dataId = 1;
					}
					
				} else if(subs.get(0).equals(Command.BINARY.toString())) {
					
					mod.type = LCNInputModule.ModuleType.BINARY;
					mod.bools = getBooleanValue((int)Math.pow(2, (Integer.parseInt(subs.get(3)) - 1)));
					
				} else if(subs.get(0).equals(Command.R8.toString()) || subs.get(0).equals(Command.R8T.toString()) || subs.get(0).equals(Command.R8G.toString())) {
					
					mod.type = LCNInputModule.ModuleType.RELAY;
					
					if (subs.get(0).equals(Command.R8G.toString())) {
						mod.bools = getBooleanValue((int)Math.pow(2, (Integer.parseInt(subs.get(3)) - 1)));
					} else if (subs.get(0).equals(Command.R8.toString())){
						mod.bools = getBooleanValue(subs.get(3));
					} else if (subs.get(0).equals(Command.R8T.toString())) {
						mod.bools = getBooleanValue(subs.get(4));
					}
					
				} else if (subs.get(0).equals(Command.SN.toString())) {
					
					mod.type = LCNInputModule.ModuleType.SERIAL;
				
				} else if (subs.get(0).equals(Command.FM.toString())) {
					
					mod.type = LCNInputModule.ModuleType.FIRMWARE;
					
				} else if (subs.get(0).equals(Command.SZD.toString())) {
					
					mod.type = LCNInputModule.ModuleType.DATA;
					mod.datatype = DataType.LS;
					mod.sceneData = new SceneData();
					mod.sceneData.scene = Integer.parseInt(subs.get(3));
					mod.sceneData.register = Integer.parseInt(subs.get(4));
					
				} else if (subs.get(0).equals(Command.SZR.toString())) {
					
					mod.type = LCNInputModule.ModuleType.DATA;
					mod.datatype = DataType.LS;
					mod.sceneData = new SceneData();
					mod.sceneData.scene = Integer.parseInt(subs.get(3));
					mod.sceneData.register = Integer.parseInt(subs.get(4));
					
					if (subs.size() > 5) {
						
						mod.dataId = Integer.parseInt(subs.get(5));
						
						if (subs.size() > 6) {
							
							mod.sValue = subs.get(6);
							
						}
						
					}
				
				} else if (subs.get(0).equals(Command.SZ.toString())) {
					
					mod.type = LCNInputModule.ModuleType.DATA;
					mod.datatype = DataType.LS;
					mod.sceneData = new SceneData();
					mod.sceneData.scene = Integer.parseInt(subs.get(4));
					mod.sceneData.register = Integer.parseInt(subs.get(5));
					
				} else if (subs.get(0).equals(Command.SL1.toString())) {
					
					mod.type = LCNInputModule.ModuleType.THRESHOLD;
					mod.thresholds = new ArrayList<LCNInputModule.Threshold>();
					if (subs.size() > 3) {
						if (subs.get(3).equals("HYSTERESIS")) {
							mod.sValue = subs.get(3);
						} else {
							mod.dataId = Integer.parseInt(subs.get(3));
							if (subs.size() > 4) {
								mod.value = Integer.parseInt(subs.get(4));
							} else {
								mod.value = mod.dataId; //The thresholdNumber
								mod.dataId = 0; //The register!
							}
						}
					}
					
				} else if (subs.get(0).equals(Command.ZA.toString()) || subs.get(0).equals(Command.ZS.toString())) {
					
					mod.type = ModuleType.DATA;
					mod.datatype = DataType.V;
					mod.dataId = Integer.parseInt(subs.get(3));
					
				} else if (subs.get(0).equals(Command.SS.toString()) || subs.get(0).equals(Command.SSO.toString())) {
					
					mod.type = LCNInputModule.ModuleType.THRESHOLD;
					mod.thresholds = new ArrayList<LCNInputModule.Threshold>();
					if (subs.size() > 7) {
						mod.dataId = Integer.parseInt(subs.get(5)) + 1;
						mod.value = Integer.parseInt(subs.get(6)) + 1;
					} else if (subs.size() == 7) {
						mod.dataId = 0;
						for (int k = 0; k < subs.get(6).length(); k++) {
							if (subs.get(6).charAt(k) != '-') {
								mod.value = k + 1;
								break;
							}
						}
					}
					
				} else if (subs.get(0).equals(Command.NM.toString())) {
					
					mod.type = LCNInputModule.ModuleType.INFO;
					
					if (subs.get(3).startsWith("NAME")) {
						mod.dataId = Integer.parseInt(subs.get(3).substring(subs.get(3).length() - 1));
					} else {
						mod.dataId = Integer.parseInt(subs.get(3).substring(subs.get(3).length() - 1)) + 2;
					}
					
				} else if (subs.get(0).equals(Command.NMO.toString())) {
					
					mod.type = LCNInputModule.ModuleType.OEM;
					mod.dataId = Integer.parseInt(subs.get(3));
					
				} else if (subs.get(0).equals(Command.LA.toString()) || subs.get(0).equals(Command.SMT.toString())) {
					
					mod.type = LCNInputModule.ModuleType.LED;
					if (subs.size() > 3) {
						mod.dataId = Integer.parseInt(subs.get(3));
					}
					
				} else if (subs.get(0).equals(Command.R8M.toString())) {

					boolean wrongSyntax = subs.size() >= 5;
					
					if (subs.get(3).equals(EngineAction.P1.toString()) || subs.get(3).equals(EngineAction.P2.toString())) {
						mod.type = LCNInputModule.ModuleType.ENGINE_REPORT;
						if (subs.size() >= 5) {
							mod.sValue = subs.get(4);
						}
					} else if (wrongSyntax && (subs.get(4).equals(EngineAction.P1.toString()) || subs.get(4).equals(EngineAction.P2.toString()))) {
						mod.type = LCNInputModule.ModuleType.ENGINE_REPORT;
						if (subs.size() >= 6) {
							mod.dataId = Integer.parseInt(subs.get(3));
							mod.sValue = subs.get(5);
						}
					} else {
						mod.type = LCNInputModule.ModuleType.ENGINE;
					}
					
				} else if (LCNSyntax.isOutlet(subs.get(0))) {
					
					mod.outlet = Integer.parseInt(subs.get(3));
					mod.type = LCNInputModule.ModuleType.OUTLET;
					
				} else {
					
					mod.outlet = Integer.parseInt(subs.get(3));
					mod.type = LCNInputModule.ModuleType.NONE;
				
				}
				
			} catch (Exception exc) {
				
				//exc.printStackTrace();
				
			}
		
		}
		
		return mod;
		
	}
	
	/**
	 * Parses input coming from the LCN system into usable information.
	 * @param input String with input.
	 */
	public static LCNInputModule parseInput(String input, String id) {
		
		LCNInputModule mod = new LCNInputModule();
		
		mod.id = id;
		
		input.replaceAll("(\\r|\\n)", "");
		
		//Input is status update
		if (input.startsWith(":")) {
			
			try {
				
				mod.segment = Integer.parseInt(input.substring(2, 5));
				mod.module = Integer.parseInt(input.substring(5, 8));
				
				
				//Notification is about an outlet?
				if (input.substring(8, 9).equals("A")) {
					mod.outlet = Integer.parseInt(input.substring(9, 10));
					mod.status = Integer.parseInt(input.substring(10, 13));
					mod.type = LCNInputModule.ModuleType.OUTLET;
				//Notification is about a binary sensor?
				} else if(input.substring(8, 10).equals("Bx")) {
					mod.bools = getBooleanValue(Integer.parseInt(input.substring(10, 13)));
					mod.type = LCNInputModule.ModuleType.BINARY;
				//Notification is about relays?
				} else if(input.substring(8, 10).equals("Rx")) {
					mod.bools = getBooleanValue(Integer.parseInt(input.substring(10, 13)));
					mod.type = LCNInputModule.ModuleType.RELAY;
				//Notification is about a sum?
				} else if (input.substring(8, 9).equals("S") && !(input.substring(8, 10).equals("SZ"))) {
					
				} else {
					
				}
				
			} catch (NumberFormatException exc) {
				logger.debug(exc.getMessage());
			} catch (IndexOutOfBoundsException exc) {
				logger.debug("IndexOutOfBoundsException thrown by 'LCNParser' input started with : but was not formated as expected.");
			}
			
		} else if(input.startsWith("%")) { 
		
			try {
				
				mod.segment = Integer.parseInt(input.substring(2, 5));
				mod.module = Integer.parseInt(input.substring(5, 8));
				mod.type = LCNInputModule.ModuleType.DATA;
				
				input = input.substring(9);
				
				//old report system of sensors
				if (input.length() == 5) {
					
					mod.value = Integer.parseInt(input);
					
				//new report system of sensors
				} else {
					
					if (input.startsWith("A")) {
						mod.datatype = DataType.V;
						mod.type = LCNInputModule.ModuleType.DATA;
						mod.dataId = Integer.parseInt(input.substring(1, 4));
//						if (mod.dataId == 2 || mod.dataId == 3) {
//							mod.datatype = DataType.T;
//						}
						input = input.substring(4);
					} else if (input.startsWith("S")) {
						mod.datatype = DataType.S;
						mod.type = LCNInputModule.ModuleType.DATA;
						mod.dataId = Integer.parseInt(input.substring(1, 2));
						input = input.substring(2);
					} else if (input.startsWith("C")){
						mod.datatype = DataType.C;					
						mod.type = LCNInputModule.ModuleType.DATA;
						mod.dataId = Integer.parseInt(input.substring(1, 2));
						input = input.substring(2);
					} else if (input.startsWith("T")) {
						mod.type = LCNInputModule.ModuleType.THRESHOLD;
						mod.thresholds = new ArrayList<LCNInputModule.Threshold>();
						LCNInputModule.Threshold thres = new LCNInputModule.Threshold();
						thres.register = Integer.parseInt(input.substring(1, 2)); 
						thres.id = Integer.parseInt(input.substring(2, 3));
						thres.value = Integer.parseInt(input.substring(3, 8));
						mod.thresholds.add(thres);
						mod.value = mod.thresholds.get(0).id;
						mod.dataId = mod.thresholds.get(0).register;
					} else {
						throw new NumberFormatException("Failed!");
					}
					if (mod.type != LCNInputModule.ModuleType.THRESHOLD) {
						if ((input.length() == 5 && mod.datatype != DataType.C)
								|| (input.length() == 10 && mod.datatype == DataType.C)) {
							mod.value = Long.parseLong(input);
						} else if ((input.length() == 4 && mod.datatype != DataType.C)
								|| (input.length() == 8 && mod.datatype == DataType.C)) {
							mod.value = Long.parseLong(input);
						} else {
							throw new NumberFormatException("Failed!");
						}
					}
				}
				
				
			} catch (NumberFormatException exc) {
				logger.debug(exc.getMessage());
				mod = null;
			} catch (IndexOutOfBoundsException exc) {
				logger.debug("IndexOutOfBoundsException thrown by 'LCNParser' input started with % but was not formated as expected.");
			}
			
		} else if (input.startsWith("=")) {
			
			try {
			
				mod.segment = Integer.parseInt(input.substring(2, 5));
				mod.module = Integer.parseInt(input.substring(5, 8));
				
				if (input.substring(9, 11).equals("SN")) {
					mod.type = LCNInputModule.ModuleType.SERIAL;
					mod.serial = input.substring(11, 21);
					//System.out.println(mod.serial);
					mod.firmware = input.substring(25, 31);
				} else if (input.substring(9, 11).equals("SK")) {
					mod.isSegmentCoupler = true;
					mod.isHomeSegment = (mod.segment == 0);
					if (mod.segment == 0) {
						mod.segment = Integer.parseInt(input.substring(12, 14));
					}
				} else if (input.substring(9, 11).equals("SZ")) {
					
					mod.sceneData = new LCNInputModule.SceneData();
					mod.sceneData.scene = Integer.parseInt(input.substring(11, 14));
					mod.type = LCNInputModule.ModuleType.DATA;
					mod.datatype = DataType.LS;
					
					for (int i = 0; i < (input.substring(14).length() / 6); i++) {
						
						mod.sceneData.value[i] = Integer.parseInt(input.substring(14 + (i * 6), 14 + (i * 6) + 3));
						mod.sceneData.ramp[i] = Integer.parseInt(input.substring(14 + (i * 6) + 3, 14 + (i * 6) + 6));
						
					}
					
				} else if (input.substring(9, 11).equals("S1")) {
					
					mod.type = LCNInputModule.ModuleType.THRESHOLD;
					mod.thresholds = new ArrayList<LCNInputModule.Threshold>();
					
					for (int i = 0; i < 5; i++) {
					
						LCNInputModule.Threshold threshold = new LCNInputModule.Threshold();
						threshold.value = Integer.parseInt(input.substring(11 + i * 5, 16 + i * 5));
						threshold.id = i + 1;
						mod.thresholds.add(threshold);
						
					}
					LCNInputModule.Threshold hystersis = new LCNInputModule.Threshold();
					hystersis.value = Integer.parseInt(input.substring(11 + 25, 16 + 25));
					hystersis.isHysteresis = true;
					hystersis.id = 0;
					mod.thresholds.add(hystersis);
					
				} else if (LCNUtil.equalsAny(input.substring(9, 11), Arrays.asList("N1", "N2", "K1", "K2", "K3"))) {
					
					mod.type = LCNInputModule.ModuleType.INFO;
					mod.sValue = input.substring(11);
					
					if (input.substring(9, 10).equals("N")) {
						mod.dataId = Integer.parseInt(input.substring(10, 11));
					} else {
						mod.dataId = Integer.parseInt(input.substring(10, 11)) + 2;
					}
					
				} else if (input.substring(9, 10).equals("O")) {
					
					mod.type = LCNInputModule.ModuleType.OEM;
					mod.dataId = Integer.parseInt(input.substring(10, 11));
					mod.sValue = input.substring(11);
					
				} else if (input.substring(9, 11).equals("TL")) {
					
					mod.type = LCNInputModule.ModuleType.LED;
					mod.led = new LCNInputModule.LEDInfo(input.substring(11));
					
				} else if (input.substring(9,11).equals("RM")) {
					
					int offset = 0;
					mod.type = LCNInputModule.ModuleType.ENGINE_REPORT;
					
					mod.engines = new ArrayList<EngineStatus>();
					
					input += "xxxxx"; //used to be able to handle big offsets.
					
					for (int i = 0; i < 2; i++) {
						
						EngineStatus newEngine = new EngineStatus();
						
						if (!checkForUnknown(input.substring(11 + offset, 12 + offset))) {
							newEngine.ID = Integer.parseInt(input.substring(11 + offset, 12 + offset));	
						}
						
						if (!checkForUnknown(input.substring(12 + offset, 15 + offset))) {
							newEngine.position = Integer.parseInt(input.substring(12 + offset, 15 + offset));
						} else {
							offset -= 2;
						}
						
						if (!checkForUnknown(input.substring(15 + offset, 18 + offset))) {
							newEngine.limit = Integer.parseInt(input.substring(15 + offset, 18 + offset));
						} else {
							offset -= 2;
						}
						
						if (!checkForUnknown(input.substring(18 + offset, 23 + offset))) {
							newEngine.stepOut = Integer.parseInt(input.substring(18 + offset, 23 + offset));
						} else {
							offset -= 4;
						}
						
						if (!checkForUnknown(input.substring(23 + offset, 28 + offset))) {
							newEngine.stepIn = Integer.parseInt(input.substring(23 + offset, 28 + offset));
						} else {
							offset -= 4;
						}
						
						mod.engines.add(newEngine);
						offset += 19;
						
					}
					
				} else {
					
					mod = null;
					
				}
			
			} catch (NumberFormatException exc) {
				
				logger.debug(exc.getMessage());
				mod = null;
				
			} catch (IndexOutOfBoundsException exc) {
				
				logger.debug(exc.getMessage());
				mod = null;
				
			}
			
		} else if (input.startsWith("-")) {
			
			try {
				
				mod.segment = Integer.parseInt(input.substring(2, 5));
				mod.module = Integer.parseInt(input.substring(5, 8));
				
				int errorCode = Integer.parseInt(input.substring(8, 11));
				
				switch (errorCode) {
				
					case 5:
						logger.warn("Unknown command was sent to module: " + mod.module + ", in segment: " + mod.segment + ".");
						break;
					case 6:
						logger.warn("Illegal amount of parameters was send to module: " + mod.module + ", in segment: " + mod.segment + ".");
						break;
					case 7:
						logger.warn("Illegal parameter values were sent to module: " + mod.module + ", in segment: " + mod.segment + ".");
						break;
					case 8:
						logger.warn("Command send to module: " + mod.module + ", in segment: " + mod.segment + ", could not be executed.");
						break;
					case 9:
						logger.warn("Unavailable command was sent to module: " + mod.module + ", in segment: " + mod.segment + ".");
						break;
					case 10:
						logger.warn("Module: " + mod.module + ", in segment: " + mod.segment + ", can not process sent command.");
						break;
					case 11:
						logger.warn("Command sent to module: " + mod.module + ", in segment: " + mod.segment + ", targets unavailable periphery.");
						break;
					case 12:
						logger.warn("Command sent to module: " + mod.module + ", in segment: " + mod.segment + ", requires programming mode.");
						break;
					case 14:
						logger.warn("Circuit breaker (230V) for module: " + mod.module + ", in segment: " + mod.segment + ", defect.");
						break;
					default:
						logger.warn("Unknown error happened when command was send to module: " + mod.module + ", in segment: " + mod.segment + ".");
						break;
					
				}
				
				mod = null;
				
			} catch (NumberFormatException exc) {
				
				//should never occur!
				logger.debug(exc.getMessage());
				mod = null;
				
			} catch (IndexOutOfBoundsException exc) {
				
				//happens if report is no error report!
				mod = null;
				
			}
			
		} else {
			
			mod = null;
			
		}
		
		return mod;
		
	}
	
	/**
	 * Checks whether a certain input is unknown (aka a ?).
	 * @param input The input to check
	 * @return True if the value is unknown, false otherwise.
	 */
	private static boolean checkForUnknown(String input) {
		return input.contains("?");
	}
	
	/**
	 * Returns an array of booleans from an input integer.
	 * @param input The integer with boolean information.
	 * @return Array of booleans. Always size 8.
	 */
	public static boolean[] getBooleanValue(int input) {
		
		boolean[] result = new boolean[8];
		
		for (int i = 7; i >= 0; i--) {
			
			result[i] = ((input - Math.pow(2, i)) >= 0);
			if (result[i]) {
				input -= Math.pow(2, i);
			}
			
		}		
		
		return result;
		
	}
	
	/**
	 * Returns an array of booleans from an input String (consisting of '-', '0', '1')
	 * @param input The String with boolean information.
	 * @return Array of booleans. Always size 8.
	 */
	public static boolean[] getBooleanValue(String input) {
		
		boolean[] result = new boolean[8];
		
		for (int i = 0; i < 8; i++) {
			
			if (input.substring(i, i + 1).equals("1")) {
				result[i] = true;
			}
			
		}
		
		return result;
		
	}
	
	/**
	 * Transforms an <b>int</b> into a <b>String</b> with no maximum value and a total forced length of 3.<br>
	 * Negative parameters are forbidden.
	 * @see #intToString(int, int, int, boolean)
	 * @param i the <b>int</b> to convert.
	 * @return <b>String</b> with the <b>int</b> in given format.
	 */
	private static String intToString(int i) {
		return intToString(i, 0);
	}
	
	/**
	 * Transforms an <b>int</b> into a <b>String</b> with given maximum value and a total forced length of 3.
	 * Negative parameters are forbidden.
	 * @see #intToString(int, int, int, boolean)
	 * @param i the <b>int</b> to convert.
	 * @param max the maximum value for the <b>int</b>.
	 * @return <b>String</b> with the <b>int</b> in given format.
	 */
	private static String intToString(int i, int max) {
		return intToString(i, max, 3);
	}
	
	/**
	 * Transforms an <b>int</b> into a <b>String</b> with given maximum value and total forced length.
	 * Negative parameters are forbidden.
	 * @see #intToString(int, int, int, boolean)
	 * @param i the <b>int</b> to convert.
	 * @param max the maximum value for the <b>int</b>.
	 * @param length the maximum length of the converted <b>String</b>.
	 * @return <b>String</b> with the <b>int</b> in given format.
	 */
	private static String intToString(int i, int max, int length) {
		return intToString(i, max, length, false);
	}
	
	/**
	 * Transforms an <b>int</b> into a <b>String</b> with given maximum value and total forced length.
	 * Negative parameters can be allowed or forbidden.
	 * @param i the <b>int</b> to convert.
	 * @param max the maximum value for the <b>int</b>.
	 * @param length the maximum length of the converted <b>String</b>.
	 * @param canBeNegative true if the <b>int</b> is allowed to be negative.
	 * @return <b>String</b> with the <b>int</b> in given format.
	 */
	private static String intToString(int i, int max, int length, boolean canBeNegative) {
		
		if (max != 0 && i > max) {
			i = max;
		}
		
		if (!canBeNegative && i < 0) {
			throw new IllegalArgumentException("Parameter is negative, but negative parameters are forbidden.");
		}
		
		String result = "" + i;
		
		if (length != 0) {
			if (result.length() <= length) {
			
				while (result.length() < length) {
					result = "0" + result;
				}
			
			} else {
				
				result = result.substring(0, length);
				
			}
		}
		
		return result;
		
	}
	
	/**
	 * Extracts the LED number from a LCNShort Command.
	 * @param LCNShort The Command.
	 * @return The number as int.
	 */
	public static int getLEDNumber(String LCNShort) {
		
		return (Integer.parseInt(LCNShort.split("\\.")[3]) - 1);
		
	}
	
	/**
	 * Transforms a LCNModule to a status request.
	 * @param mod The LCNModule
	 * @param homeSegment The homeSegment for the LCNBus.
	 * @return String with the status request.
	 */
	public static String moduleToStatusRequest(LCNInputModule mod, int homeSegment) {
		
		String result = "";
		
		int seg = mod.segment;
		if (seg == homeSegment) {
			seg = 0;
		}
		
		result = LCNSyntax.Command.RAW + "." + ">M" + intToString(seg) + intToString(mod.module) + "!SMMF" + POSTAMBLE;
		
		return result;
		
	}
	
	/**
	 * Transforms a LCNModule to a serial request.
	 * @param mod The LCNModule
	 * @param homeSegment The homeSegment for that LCNBus.
	 * @return String with the serial request.
	 */
	public static String moduleToSerialRequest(LCNModule mod, int homeSegment) {
		
		String result = "";
		
		int seg = mod.segment;
		if (seg == homeSegment) {
			seg = 0;
		}
		
		result = LCNSyntax.Command.RAW + "." + ">M" + intToString(seg) + intToString(mod.module) + "!SN" + POSTAMBLE;
		
		return result;
		
	}
	
	/**
	 * Transforms a LCNModule to a data request.
	 * @param mod The LCNModule
	 * @param homeSegment The homeSegment for that LCNBus.
	 * @return String with the data request.
	 */
	public static String moduleToDataRequest(LCNInputModule mod, int homeSegment) {
		
		String result = "";
		
		int seg = mod.segment;
		if (seg == homeSegment) {
			seg = 0;
		}
		
		if (mod.datatype != null) {
		
			DataType type = mod.datatype;
			
			result = LCNSyntax.Command.RAW + "." + ">M" + intToString(seg) + intToString(mod.module);
			
			if (type == DataType.LS && null != mod.sceneData) {
				
				result += "!SZR";
				result += intToString(mod.sceneData.scene, 9, 3);
				result += intToString(mod.sceneData.register, 9, 3);
				
			} else {
				
				result += "!MW";

				type = DataType.V;
				result += type.name(); 
				if (type != DataType.V && mod.dataId != 0) {
					if (mod.dataId == 2) {
						result += "B";
					} else {
						result += "A";
					}
				}
				
			}
			
			result += POSTAMBLE;
			
		}
		
		return result;
		
	}
	
	/**
	 * Transforms a LCNModule to a threshold request.
	 * @param mod The LCNModule
	 * @return String with the threshold request.
	 */
	public static String moduleToThresholdRequest(LCNInputModule mod, String firmware, int homeSegment) {
		
		String result = "";
		
		int seg = mod.segment;
		if (seg == homeSegment) {
			seg = 0;
		}
		
		if (mod.type == LCNInputModule.ModuleType.THRESHOLD) {
			
			if (LCNUtil.compareHexDate(firmware, "160B13") < 0) {
			
				result += LCNSyntax.Command.RAW + "." + ">M" + intToString(seg) + intToString(mod.module) + "!SL1" + POSTAMBLE;
			
			} else {
				
				if (mod.dataId != 0) {
					
					result += LCNSyntax.Command.RAW + "." + ">M" + intToString(seg) + intToString(mod.module) + "!SE" + mod.dataId + POSTAMBLE;
					
				} else {
				 
					for (int i = 1; i < 5; i++) {
						result += LCNSyntax.Command.RAW + "." + ">M" + intToString(seg) + intToString(mod.module) + "!SE" + i + POSTAMBLE;
					}
				
				}
				
			}
			
		}
		
		return result;
		
	}
	
	/**
	 * Transforms a LCNModule to a data request (for new modules).
	 * @param mod The LCNModule
	 * @param homeSegment The homeSegment for that LCNBus.
	 * @return String with the data request.
	 */
	public static String moduleToNewDataRequest(LCNInputModule mod, int homeSegment) {
		
		String result = "";
		
		int seg = mod.segment;
		if (seg == homeSegment) {
			seg = 0;
		}
		
		if (mod.datatype != DataType.NONE) {
		
			result = LCNSyntax.Command.RAW + "." + ">M" + intToString(seg) + intToString(mod.module) + "!MW";
			 //+ mod.datatype.name();
			DataType type = mod.datatype;
			
			if (!(type == DataType.S) && !(type == DataType.C)) {
				result += "T";
			} else {
				result += type.name();
			}
//			if (type == DataType.TR || type == DataType.L || type == DataType.LO || type == DataType.A || type == DataType.VO || type == DataType.CO || type == DataType.M || type == DataType.W) {
//				type = DataType.T;
//			}
//			if (type == DataType.T){
//				//mod.dataId += 1;
//			}
//			if (type == DataType.V) {
//				type = DataType.T;
//				mod.dataId = 1;
//			}
//			result += type.name();
			result += mod.dataId;
			result += POSTAMBLE;
			
		}
		
		return result;
		
	}
	
	/**
	 * Transforms LCN raw values to lux values (old T-port calculation).
	 * @param value The raw value.
	 * @return The transformed lux value.
	 */
	public static double LcnToLuxOld(long value) {
		return (long)Math.exp(0.010380664 * (double)value + 1.689646994) + 0;
	}
	
	/**
	 * Transforms lux values to LCN raw values (old T-port calculation).
	 * @param value The lux value.
	 * @return The transformed raw value.
	 */
	public static long LuxOldToLcn(double value) {
		return (long)((Math.log(value - 0) - 1.689646994) / 0.010380664);
	}
	
	/**
	 * Transforms LCN raw values to lux values (new I-port calculation).
	 * @param value The raw value.
	 * @return The transformed lux value.
	 */
	public static double LcnToLux(long value) {
		return Math.exp((double)value / 100);
	}
	
	/**
	 * Transforms lux values to LCN raw values (old I-port calculation).
	 * @param value The lux value.
	 * @return The transformed raw value.
	 */
	public static long LuxToLcn(double value) {
		return (long)(Math.log(value) * 100);
	}
	
	/**
	 * Transforms LCN raw values to celsius values.
	 * @param value The raw value.
	 * @return The transformed celsius value.
	 */
	public static double LcnToCelsius(long value) {
		return ((((double) value) - 1000) / 10);
	}
	
	/**
	 * Transforms celsius values to LCN raw values.
	 * @param value The celsius value.
	 * @return The transformed raw value.
	 */
	public static long CelsiusToLcn(double value) {
		return (long) (value * 10 + 1000);
	}
	
	/**
	 * Transforms LCN raw values to amp values.
	 * @param value The raw value.
	 * @return The transformed amp value.
	 */
	public static double LcnToAmp(long value) {
		return ((double) value) / 200;
	}
	
	/**
	 * Transforms from amp to the LCN raw value.
	 * @param value The amp value.
	 * @return The lcn value.
	 */
	public static long AmpToLcn(double value) {
		return (long) (value * 200);
	}
	
	/**
	 * Transforms LCN raw values to volt values.
	 * @param value The raw value.
	 * @return The transformed volt value.
	 */
	public static double LcnToVolt(long value) {
		return ((double) value) / 400;
	}
	
	/**
	 * Transforms from volt to the lcn value.
	 * @param value The volt value.
	 * @return The lcn value.
	 */
	public static long VoltToLcn(double value) {
		return (long) (value * 400);
	}

	/**
	 * Transforms LCN raw values to m/s values.
	 * @param value The raw value.
	 * @return The transformed m/s value.
	 */
	public static double LcnToWind(long value) {
		return ((double) value) / 10;
	}
	
	/**
	 * Transforms the wind value to lcn value.
	 * @param value The wind value.
	 * @return The lcn value.
	 */
	public static long WindToLcn(double value) {
		return (long) (value * 10);
	}

	/**
	 * Transforms LCN raw values to percent values.
	 * @param value The raw value.
	 * @return The transformed percent value.
	 */
	public static double LcnToMoist(long value) {
		return (double) value;
	}
	
	/**
	 * Transforms moisture values to lcn values.
	 * @param value The moisture value.
	 * @return The transformed lcn value.
	 */
	public static long MoistToLcn(double value) {
		return (long) value;
	}

	/**
	 * Transforms LCN raw values to ppm (parts per million) values.
	 * @param value The raw value.
	 * @return The transformed ppm value.
	 */
	public static double LcnToCO2(long value) {
		return (double) value;
	}
	
	/**
	 * Transforms ppm to lcn value.
	 * @param value The ppm value.
	 * @return The lcn value.
	 */
	public static long CO2ToLcn(double value) {
		return (long) value;
	}


	/**
	 * Transforms a normal value with a given modifier to the lcn representation.
	 * @param value The value.
	 * @param mod The modifier.
	 * @return The transformed value.
	 */
	public static long parseWithModToLCN(double value, VarModifier mod) {
		long result = 0;
		
		switch (mod) {
		
			case CELSIUS:
				result = CelsiusToLcn(value);
				break;
		
			case AMP:
				result = AmpToLcn(value);
				break;
				
			case VOLT:
				result = VoltToLcn(value);
				break;
				
			case CO2:
				result = CO2ToLcn(value);
				break;
				
			case LUX:
				result = LuxToLcn(value);
				break;
				
			case LUX_T:
				result = LuxOldToLcn(value);
				break;
				
			case WIND:
				result = WindToLcn(value);
				break;
				
			case MOISTURE:
				result = MoistToLcn(value);
				break;
				
			default:
				result = (long) value;
				break;
			
		}
		
		return result;
	}
	
	/**
	 * Transforms a normal value with a given modifier to the lcn representation steps.
	 * @param value The value.
	 * @param mod The modifier.
	 * @return The transformed value.
	 */
	public static long parseWithModToLCNStep(double value, VarModifier mod) {
		long result = 0;
		
		switch (mod) {
		
			case CELSIUS:
				result = CelsiusToLcn(value) - 1000;
				break;
		
			case AMP:
				result = AmpToLcn(value);
				break;
				
			case VOLT:
				result = VoltToLcn(value);
				break;
				
			case CO2:
				result = CO2ToLcn(value);
				break;
				
//			case LUX:
//				result = LuxToLcn(value);
//				break;
				
//			case LUX_T:
//				result = LuxOldToLcn(value);
//				break;
				
			case WIND:
				result = WindToLcn(value);
				break;
				
			case MOISTURE:
				result = MoistToLcn(value);
				break;
				
			default:
				result = (long) value;
				break;
			
		}
		
		return result;
	}
	
	/**
	 * Transforms a lcn value with a given modifier to a normal representation.
	 * @param value The value.
	 * @param mod The modifier.
	 * @return The transformed value.
	 */
	public static double parseWithModFromLCN(long value, VarModifier mod) {
		double result = 0;
		
		switch (mod) {
		
			case CELSIUS:
				result = LcnToCelsius(value);
				break;
		
			case AMP:
				result = LcnToAmp(value);
				break;
				
			case VOLT:
				result = LcnToVolt(value);
				break;
				
			case CO2:
				result = LcnToCO2(value);
				break;
				
			case LUX:
				result = round(LcnToLux(value), 2);
				break;
				
			case LUX_T:
				result = round(LcnToLuxOld(value), 2);
				break;
				
			case WIND:
				result = LcnToWind(value);
				break;
				
			case MOISTURE:
				result = LcnToMoist(value);
				break;
				
			default:
				result = (double) value;
				break;
			
		}
		
		return result;
	}

	/**
	 * Returns the charset for relay string parameters.
	 * The charset is newly created, if required.
	 * @return The charset.
	 */
	private static List<Character> getRelayCharset() {
		
		if (RELAY_CHARSET.isEmpty()) {
			RELAY_CHARSET.add('-');
			RELAY_CHARSET.add('0');
			RELAY_CHARSET.add('1');
			RELAY_CHARSET.add('U');			
		}
		return RELAY_CHARSET;
		
	}
	
	/**
	 * Returns the charset for button string parameters.
	 * The charset is newly created, if required.
	 * @return The charset.
	 */
	private static List<Character> getBinaryCharset() {
		
		if (BINARY_CHARSET.isEmpty()) {
			BINARY_CHARSET.add('0');
			BINARY_CHARSET.add('1');
		}
		return BINARY_CHARSET;
		
	}
	
	/**
	 * Returns the charset for advanced button string parameters.
	 * The charset is newly created, if required.
	 * @return The charset.
	 */
	private static List<Character> getAdvButtonCharset() {
		
		if (ADV_BUTTON_CHARSET.isEmpty()) {
			ADV_BUTTON_CHARSET.add('-');
			ADV_BUTTON_CHARSET.add('0');
			ADV_BUTTON_CHARSET.add('1');
		}
		return ADV_BUTTON_CHARSET;
		
	}
	
	/**
	 * Returns the charset for button-list string parameters.
	 * The charset is newly created, if required.
	 * @return The charset.
	 */
	private static List<Character> getListCharset() {
		
		if (LIST_CHARSET.isEmpty()) {
			LIST_CHARSET.add('A');
			LIST_CHARSET.add('B');
			LIST_CHARSET.add('C');
			LIST_CHARSET.add('D');
		}
		return LIST_CHARSET;
		
	}
	
	/**
	 * Returns the charset for math operator string parameters.
	 * The charset is newly created, if required.
	 * @return The charset.
	 */
	private static List<Character> getOperatorCharset() {
		
		if (OPERATOR_CHARSET.isEmpty()) {
			OPERATOR_CHARSET.add('+');
			OPERATOR_CHARSET.add('-');
		}
		return OPERATOR_CHARSET;
		
	}
	
	/**
	 * Represents langauge naming.
	 * 1=de 2=en 3=es 4=fr 5=ru 6=ar
     * 7=pl 8=tr
     * 0=“others“
	 * @param name String with the language.
	 * @return the corresponding integer value. 
	 */
	private static int getLanguage(String name) {
		int value = 0;
		
		if ("de".equalsIgnoreCase(name)) {
			value = 1;
		} else if ("en".equalsIgnoreCase(name)) {
			value = 2;
		} else if ("es".equalsIgnoreCase(name)) {
			value = 3;
		} else if ("fr".equalsIgnoreCase(name)) {
			value = 4;
		} else if ("ru".equalsIgnoreCase(name)) {
			value = 5;
		} else if ("ar".equalsIgnoreCase(name)) {
			value = 6;
		} else if ("pl".equalsIgnoreCase(name)) {
			value = 7;
		} else if ("tr".equalsIgnoreCase(name)) {
			value = 8;
		}
		
		return value;
	}		
	
	/**
	 * Will round a double down with a maximum number of trailing digits.
	 * @param value The double value to transform.
	 * @param max The maximum number of trailing digits.
	 * @return double with the desired result.
	 */
	public static double round(double value, int max) {
		
		String result = "" + value;		
		
		try {
			
			if (result.contains(".")) {
			
				int last = result.lastIndexOf(".");
				
				if (result.length() > (last + max + 1)) {
					int rounder = Integer.parseInt("" + result.charAt(last + max + 1));
					result = result.substring(0, last + max + 1);
					if (rounder >= 5) {
						int preRounder = Integer.parseInt("" + result.charAt(result.length() - 1)) + 1;
						result = result.substring(0, result.length() - 1) + (preRounder);
					}
				}
				
			}
			
		} catch (Exception exc) {
			result = "ERROR";
			logger.warn("Unable to round the value {}, with a maximum of trailing digits {}...", value, max);
			if (logger.isDebugEnabled()) {
				exc.printStackTrace();
			}
		}
		
		return Double.parseDouble(result);
		
	}

	
	public static void main(String[] args) {
		try {
			
			//System.out.println(LCNParser.parse("ON.0.HALLO.1.VAR.222"));
			//System.out.println(LCNParser.parse("RAW.012.asd.123123."));
			
			LCNParser.parse("ON.0.2.2");
			
			LCNInputModule m1 = LCNParser.reverseParse("MOVE_THRESHOLD_OLD.0.23.CURRENT.10.+.1----", "");
			LCNInputModule m2 = LCNParser.parseInput("=M000023.S1005200020000300004000050000010", "");
			LCNUtil.printModule(m1);
			LCNUtil.printModule(m2);
			System.out.println(m1.sValue);
			System.out.println(m2.getThreshold(0, (int)m1.value).value);
			
			m1 = LCNParser.reverseParse("VAR_ADD.0.11.1.10", "");
			m2 = LCNParser.parseInput("%M000011.01236", "");
			LCNInputModule m3 = LCNParser.reverseParse("VAR_VALUE.0.11.VAR.1", "");
			
			System.out.println(m1.equals(m2, 0));
			System.out.println(m3.equals(m2, 0));
			LCNUtil.printModule(m1);
			LCNUtil.printModule(m2);
			LCNUtil.printModule(m3);
			
			System.out.println(LCNParser.parse("ON.0.23.2"));
			m1 = LCNParser.reverseParse("VAR_ADD.0.11.1.10", "");
			System.out.println(m1.modifier);
			System.out.println(LCNParser.parse("GET_COUPLER"));
			
			m1 = LCNParser.reverseParse("VAR_VALUE.0.5.VAR.3.CO2", "");
			m2 = LCNParser.reverseParse("VAR_VALUE.0.8.VAR.2", "");
			
			LCNUtil.printModule(m1);
			LCNUtil.printModule(m2);
			
			m1 = LCNParser.reverseParse("SETPOINT_VALUE.0.8.REGULATOR1.ACTIVATE.CELSIUS", "");
			
			LCNUtil.printModule(m1);
			
			m1 = LCNParser.reverseParse("VAR_ADD.0.11.1.10", "");
			m2 = LCNParser.reverseParse("VAR_ADD.0.11.1.10.CELSIUS", "");
			
			LCNUtil.printModule(m1);
			LCNUtil.printModule(m2);
			
			m1 = LCNParser.reverseParse("THRESHOLD_VALUE.0.23.1.CELSIUS", "");
			m2 = LCNParser.reverseParse("THRESHOLD_VALUE.0.23.1", "");
			
			System.out.println("---");
			LCNUtil.printModule(m1);
			LCNUtil.printModule(m2);
			
			System.out.println(LCNParser.parse("MOVE_THRESHOLD_OLD.0.23.CURRENT.10.+.1----.CELSIUS", "160B12"));
			
			
			m1 = LCNParser.reverseParse("VAR_VALUE.0.8.VAR.2", "");
			System.out.println(LCNParser.moduleToDataRequest(m1, 0));
			System.out.println(LCNParser.moduleToNewDataRequest(m1, 0));
			
		} catch (LCNParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}



/**
 * Special List, which automatically deletes any element read from it.
 * @param <String>
 * @author Patrik Pastuschek
 * @since 1.7.0
 */
@SuppressWarnings("hiding")
class QList<String> extends ArrayList<String> {
	
	/**Generated serial.*/
	private static final long serialVersionUID = 6869958639891195716L;

	public String last = null;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String get(int i) {
		
		String result = super.get(i);
		last = result;
		super.remove(i);
		return result;
		
	}
	
	/**
	 * Returns the first element and removes it from the list.
	 * @return The first element.
	 */
	public String pop() {
		
		String result = super.get(0);
		super.remove(0);
		return result;
		
	}
	
	/**
	 * Constructor of the QList.
	 * @param list A List of Strings.
	 */
	public QList(List<String> list) {
		super(list);
	}
	
}
