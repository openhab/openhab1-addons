/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.udp;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openhab.binding.souliss.internal.network.typicals.Constants;
import org.openhab.binding.souliss.internal.network.typicals.SoulissGenericTypical;
import org.openhab.binding.souliss.internal.network.typicals.SoulissNetworkParameter;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT16;
import org.openhab.binding.souliss.internal.network.typicals.SoulissT1A;
import org.openhab.binding.souliss.internal.network.typicals.SoulissTServiceUpdater;
import org.openhab.binding.souliss.internal.network.typicals.SoulissTypicals;

/**
 * This class decodes incoming Souliss packets, starting from decodevNet
 * 
 * @author Alessandro Del Pex
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class UDPSoulissDecoder {

	private SoulissTypicals soulissTypicalsRecipients;
	private static Logger LOGGER = LoggerFactory
			.getLogger(UDPSoulissDecoder.class);

	public UDPSoulissDecoder(SoulissTypicals typicals) {
		soulissTypicalsRecipients = typicals;
	}

	/**
	 * Get packet from VNET Frame
	 * 
	 * @param packet
	 *            incoming datagram
	 */
	public void decodeVNetDatagram(DatagramPacket packet) {
		int checklen = packet.getLength();
		ArrayList<Short> mac = new ArrayList<Short>();
		for (int ig = 7; ig < checklen; ig++) {
			mac.add((short) (packet.getData()[ig] & 0xFF));
		}
		decodeMacaco(mac);
	}

	/**
	 * Decodes lower level MaCaCo packet
	 * 
	 * @param macacoPck
	 */
	private void decodeMacaco(ArrayList<Short> macacoPck) {
		int functionalCode = macacoPck.get(0);
		LOGGER.info("decodeMacaco: Received functional code: 0x"
				+ Integer.toHexString(functionalCode));
		switch (functionalCode) {

		case (byte) ConstantsUDP.Souliss_UDP_function_ping_resp:
			LOGGER.info("function_ping_resp");
			decodePing(macacoPck);
			break;
		case (byte) ConstantsUDP.Souliss_UDP_function_subscribe_resp:
		case (byte) ConstantsUDP.Souliss_UDP_function_poll_resp:
			LOGGER.info("Souliss_UDP_function_subscribe_resp / Souliss_UDP_function_poll_resp");
			decodeStateRequest(macacoPck);
			break;

		case ConstantsUDP.Souliss_UDP_function_typreq_resp:// Answer for
															// assigned
			// typical logic
			LOGGER.info("** TypReq answer");
			decodeTypRequest(macacoPck);
			break;
		case (byte) ConstantsUDP.Souliss_UDP_function_health_resp:// Answer
																	// nodes
																	// healty
			LOGGER.info("function_health_resp");
			decodeHealthRequest(macacoPck);
			break;
		case (byte) ConstantsUDP.Souliss_UDP_function_db_struct_resp:// Answer
																		// nodes
			LOGGER.info("function_db_struct_resp");
			decodeDBStructRequest(macacoPck);
			break;
		case 0x83:
			LOGGER.info("Functional code not supported");
			break;
		case 0x84:
			LOGGER.info("Data out of range");
			break;
		case 0x85:
			LOGGER.info("Subscription refused");
			break;
		default:
			LOGGER.info("Unknown functional code");
			break;
		}
	}

	// /**
	// * @param mac
	// */
	private void decodePing(ArrayList<Short> mac) {
		int putIn_1 = mac.get(1);
		int putIn_2 = mac.get(2);
		LOGGER.info("decodePing: putIn code: " + putIn_1 + ", " + putIn_2);
	}

	/**
	 * Sovrascrive la struttura I nodi e la struttura dei tipici e richiama
	 * UDPHelper.typicalRequest(opzioni, nodes, 0);
	 * 
	 * @param mac
	 */
	private void decodeDBStructRequest(ArrayList<Short> mac) {
		try {
			int nodes = mac.get(5);
			int maxnodes = mac.get(6);
			int maxTypicalXnode = mac.get(7);
			int maxrequests = mac.get(8);
			int MaCaco_IN_S = mac.get(9);
			int MaCaco_TYP_S = mac.get(10);
			int MaCaco_OUT_S = mac.get(11);

			SoulissNetworkParameter.nodes = nodes;
			SoulissNetworkParameter.maxnodes = maxnodes;
			SoulissNetworkParameter.maxTypicalXnode = maxTypicalXnode;
			SoulissNetworkParameter.maxrequests = maxrequests;
			SoulissNetworkParameter.MaCacoIN_s = MaCaco_IN_S;
			SoulissNetworkParameter.MaCacoTYP_s = MaCaco_TYP_S;
			SoulissNetworkParameter.MaCacoOUT_s = MaCaco_OUT_S;

			LOGGER.debug("decodeDBStructRequest");
			LOGGER.debug("Nodes: " + nodes);
			LOGGER.debug("maxnodes: " + maxnodes);
			LOGGER.debug("maxTypicalXnode: " + maxTypicalXnode);
			LOGGER.debug("maxrequests: " + maxrequests);
			LOGGER.debug("MaCaco_IN_S: " + MaCaco_IN_S);
			LOGGER.debug("MaCaco_TYP_S: " + MaCaco_TYP_S);
			LOGGER.debug("MaCaco_OUT_S: " + MaCaco_OUT_S);

		} catch (Exception e) {
			LOGGER.error("decodeDBStructRequest: SoulissNetworkParameter update ERROR");
		}
	}

	private void decodeTypRequest(ArrayList<Short> mac) {
		try {
			short tgtnode = mac.get(3);
			int numberOf = mac.get(4);
			
			int typXnodo = SoulissNetworkParameter.maxnodes;
			LOGGER.info("--DECODE MACACO OFFSET:" + tgtnode + " NUMOF:"
					+ numberOf + " TYPICALSXNODE: " + typXnodo);
			// creates Souliss nodes
			for (int j = 0; j < numberOf; j++) {
				if (mac.get(5 + j) != 0) {// create only not-empty typicals
					if (!(mac.get(5 + j) == Constants.Souliss_T_related)) {
						String hTyp = Integer.toHexString(mac.get(5 + j));
						short slot = (short) (j % typXnodo);
						short node = (short) (j / typXnodo + tgtnode);

						System.out.println("{souliss=\"T" + hTyp + ":" + node
								+ ":" + slot + "}");
					}
				}
			}
		} catch (Exception uy) {
			LOGGER.error("decodeTypRequest ERROR");
		}
	}

	private void decodeStateRequest(ArrayList<Short> mac) {
		boolean bDecoded_forLOG = false;
		int tgtnode = mac.get(3);
		// QUI. AGGIORNAMENTO DEL TIMESTAMP PER OGNI NODO. DA FARE USANDO NODI
		// FITTIZI
		SoulissTServiceUpdater.updateTIMESTAMP(soulissTypicalsRecipients,
				tgtnode);
		
		// sfoglio hashtable e scelgo tipici nodo indicato nel frame
		// leggo valore tipico in base allo slot
		synchronized (this) {
			Iterator<Entry<String, SoulissGenericTypical>> iteratorTypicals = soulissTypicalsRecipients
					.getIterator();
			while (iteratorTypicals.hasNext()) {
				SoulissGenericTypical typ = iteratorTypicals.next().getValue();
				// se il tipico estratto appartiene al nodo che il frame deve
				// aggiornare...
				bDecoded_forLOG = false;
				if (typ.getSoulissNodeID() == tgtnode) {

					// ...allora controllo lo slot
					int slot = typ.getSlot();
					// ...ed aggiorno lo stato in base al tipo
					int iNumBytes = 0;

					try {
						String sHex = Integer.toHexString(typ.getType());
						String sRes = SoulissNetworkParameter
								.getPropTypicalBytes(sHex.toUpperCase());
						if (sRes != null)
							iNumBytes = Integer.parseInt(sRes);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						iNumBytes = 0;
					}
					float val = 0;
					if (typ.getType() == 0x1A) {
						short sVal = getByteAtSlot(mac, slot);
						((SoulissT1A) typ).setState(sVal);
						bDecoded_forLOG = true;
					} else if (typ.getType() == 0x19) {
						// set value of T19 at number of second slot
						short sVal = getByteAtSlot(mac, slot + 1);
						typ.setState(sVal);
						bDecoded_forLOG = true;
					} else if (iNumBytes == 1) {
						// caso valori digitali
						val = getByteAtSlot(mac, slot);
						typ.setState(val);
						bDecoded_forLOG = true;
					} else if (iNumBytes == 2) {
						// caso valori float
						val = getFloatAtSlot(mac, slot);
						typ.setState(val);
						bDecoded_forLOG = true;
					} else if (iNumBytes == 4) {
						// T16 RGB
						val = getByteAtSlot(mac, slot);
						typ.setState(val);
						((SoulissT16) typ).setStateRED(getByteAtSlot(mac,
								slot + 1));
						((SoulissT16) typ).setStateGREEN(getByteAtSlot(mac,
								slot + 2));
						((SoulissT16) typ).setStateBLU(getByteAtSlot(mac,
								slot + 3));
						bDecoded_forLOG = true;
					}

					if (typ.getType() != 152 && typ.getType() != 153) // non
																		// esegue
																		// per
																		// healt
																		// e
																		// timestamp,
																		// perchè
																		// il
																		// LOG
																		// viene
																		// inserito
																		// in un
																		// altro
																		// punto
																		// del
																		// codice
						if (iNumBytes == 4)
							// RGB Log
							LOGGER.debug("decodeStateRequest:  "
									+ typ.getName() + " ( "
									+ Short.valueOf(typ.getType()) + ") = "
									+ ((SoulissT16) typ).getState() + ". RGB= "
									+ ((SoulissT16) typ).getStateRED() + ", "
									+ ((SoulissT16) typ).getStateGREEN() + ", "
									+ ((SoulissT16) typ).getStateBLU());
						else if (bDecoded_forLOG) {
							if (typ.getType() == 0x1A) {
								LOGGER.debug("decodeStateRequest: "
										+ typ.getName()
										+ " (0x"
										+ Integer.toHexString(typ.getType())
										+ ") = "
										+ Integer
												.toBinaryString(((SoulissT1A) typ)
														.getRawState()));
							} else
								LOGGER.debug("decodeStateRequest: "
										+ typ.getName() + " (0x"
										+ Integer.toHexString(typ.getType())
										+ ") = " + Float.valueOf(val));
						}
				}
			}
		}
	}

	private Short getByteAtSlot(ArrayList<Short> mac, int slot) {
		return mac.get(5 + slot);

	}

	private float getFloatAtSlot(ArrayList<Short> mac, int slot) {
		int iOutput = mac.get(5 + slot);
		int iOutput2 = mac.get(5 + slot + 1);
		// ora ho i due bytes, li converto
		int shifted = iOutput2 << 8;
		float ret = HalfFloatUtils.toFloat(shifted + iOutput);
		return ret;
	}

	/**
	 * Decodes a souliss nodes health request
	 * 
	 * @param macaco
	 *            packet
	 */
	private void decodeHealthRequest(ArrayList<Short> mac) {
		int numberOf = mac.get(4);
		// build an array containing healths
		for (int i = 5; i < 5 + numberOf; i++) {
			// healths.add(Short.valueOf(mac.get(i)));
			SoulissTServiceUpdater.updateHEALTY(soulissTypicalsRecipients,
					i - 5, Short.valueOf(mac.get(i)));
		}
	}
}
