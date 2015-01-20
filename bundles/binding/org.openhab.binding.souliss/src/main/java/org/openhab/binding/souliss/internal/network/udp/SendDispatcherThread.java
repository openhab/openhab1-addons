/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provide to take packet, and send it to regular interval to Souliss Network
 * @author Antonino-Fazio
 */
public class SendDispatcherThread  extends Thread {

	static ArrayList<SocketAndPacket> packetsList = new ArrayList<SocketAndPacket>();	
	protected boolean bExit = false;
	static int iDelay=0; //equal to 0 if array is empty
	int SEND_DELAY;
	int SEND_MIN_DELAY;
	static boolean bPopSuspend=false;
	private static Logger LOGGER = LoggerFactory.getLogger(SendDispatcherThread.class);
		
	public SendDispatcherThread(int SEND_DELAY, int SEND_MIN_DELAY) throws IOException {
	    this("SendDispatcher");
	    this.SEND_DELAY=SEND_DELAY;
	    this.SEND_MIN_DELAY=SEND_MIN_DELAY;
	    LOGGER.info("Avvio SendDispatcherThread");
	}
	
	public SendDispatcherThread(String name) {
		  super(name);

	}

	public synchronized static void put(DatagramSocket socket, DatagramPacket packetToPUT) {
			
		bPopSuspend=true;
			boolean bPacchettoGestito=false;
			//estraggo il nodo indirizzato dal pacchetto in ingresso
			int node=getNode(packetToPUT);
			LOGGER.debug("Push");
			if (packetsList.size()==0 || node < 0) {
				bPacchettoGestito=false;
			} else{
			for(int i=0; i<packetsList.size();i++){
				//se il nodo da inserire � un comando FORCE restituisce valore > -1 uguale al numero di nodo indirizzato
				//e se il nodo indirizzato � uguale a quello packetsList.get(i).packet presente in lista
				//e se il socket � lo stesso uguale a quello packetsList.get(i).socket presente in lista
				//if(node >=0 && getNode(packetsList.get(i).packet)==node && packetsList.get(i).socket==socket)
				if(node >=0 && getNode(packetsList.get(i).packet)==node ) {
					LOGGER.debug("Frame UPD per nodo " + node + " già presente il lista. Esecuzione ottimizzazione.");
					bPacchettoGestito=true;
					//se il pacchetto da inserire  è più corto (o uguale) di quello in lista allora sovrascrivo i byte del pacchetto presente in lista
					if(packetToPUT.getData().length<=packetsList.get(i).packet.getData().length){
						//scorre i byte di comando e se il byte � diverso da zero sovrascrive il byte presente nel pacchetto in lista
						LOGGER.debug("Optimizer.             Packet to push: " + MaCacoToString(packetToPUT.getData()));
						LOGGER.debug("Optimizer.             Previous frame: " + MaCacoToString(packetsList.get(i).packet.getData()));
						for (int j=12;j<packetToPUT.getData().length;j++){
							//se il j-esimo byte è diverso da zero allora lo sovrascrivo al byte del pacchetto già presente 
							if(packetToPUT.getData()[j]!=0){
								packetsList.get(i).packet.getData()[j]=packetToPUT.getData()[j];
							}
						}
						LOGGER.debug("Optimizer. Previous frame modified to: " + MaCacoToString(packetsList.get(i).packet.getData()));
					}else {
						//se il pacchetto da inserire  è più lungo di quello in lista allora sovrascrivo i byte del pacchetto da inserire, poi elimino quello in lista ed inserisco quello nuovo 
						if(packetToPUT.getData().length>packetsList.get(i).packet.getData().length){
							for (int j=12;j<packetsList.get(i).packet.getData().length;j++){
								//se il j-esimo byte è diverso da zero allora lo sovrascrivo al byte del pacchetto gi� presente 
								if(packetsList.get(i).packet.getData()[j]!=0){
									if(packetToPUT.getData()[j]==0){
										//sovrascrive i byte dell'ultimo frame soltanto se il byte è uguale a zero. Se è diverso da zero l'ultimo frame ha la precedenza e deve sovrascrivere
									packetToPUT.getData()[j]=packetsList.get(i).packet.getData()[j];
									}
								}
							}
							//rimuove il pacchetto
							LOGGER.debug("Optimizer. Remove frame: " + MaCacoToString( packetsList.get(i).packet.getData()));
							packetsList.remove(i);
							//inserisce il nuovo
							LOGGER.debug("Optimizer.    Add frame: " + MaCacoToString(packetToPUT.getData()));
							packetsList.add( new SocketAndPacket(socket, packetToPUT));
						}
					}
				}
			}
			}
			
			if(!bPacchettoGestito){
				LOGGER.debug("Add frame: " + MaCacoToString(packetToPUT.getData()));
				packetsList.add( new SocketAndPacket(socket, packetToPUT));
			}
			bPopSuspend=false;
		}

	

	private static int getNode(DatagramPacket packet) {
		//7  è il byte del frame VNet al quale trovo il codice comando
		//10 è il byte del frame VNet al quale trovo l'ID del nodo
		if (packet.getData()[7]==(byte) ConstantsUDP.Souliss_UDP_function_force){
			return packet.getData()[10];
		}
		return -1;
	}

	long t,t_prec=0;
	private synchronized SocketAndPacket pop(){
		synchronized (this) {
			//non esegue il pop se bPopSuspend=true
			//bPopSuspend è impostato dal metodo put
			if(!bPopSuspend) {
				t=System.currentTimeMillis();
				//riporta l'intervallo al minimo solo se:
				//- la lista è minore o uguale a 1;
				//- se è trascorso il tempo SEND_DELAY.

				if (packetsList.size()<=1)
					iDelay=SEND_MIN_DELAY; 
				else 
					iDelay=SEND_DELAY;

				boolean tFlag=(t-t_prec)>=SEND_DELAY;
				if (packetsList.size()>0 && tFlag){
					t_prec=System.currentTimeMillis();
					SocketAndPacket sp=packetsList.remove(0);
					LOGGER.debug("Pop frame "+ MaCacoToString(sp.packet.getData()) + " - Delay for 'SendDispatcherThread' setted to " + iDelay + " mills.");
					return sp;
				}
			}
		}
		return null;
	}
	
	
	public void run() {

		while (!bExit) {

			try {
				Thread.sleep(iDelay);		  
				SocketAndPacket sp= pop();
				if(sp!=null) {
					LOGGER.debug("SendDispatcherThread - Functional Code 0x" + Integer.toHexString(sp.packet.getData()[7]) + " - Packet: " + MaCacoToString(sp.packet.getData()) +  " - Elementi rimanenti in lista: " + packetsList.size());	
					sp.socket.send(sp.packet);
				}
			} catch (IOException |InterruptedException e) {
				e.printStackTrace();
				LOGGER.error(e.getMessage());
				bExit = true;
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LOGGER.error(e.getMessage());
			}
		}
	}
	
	private static String MaCacoToString(byte[] frame2) {
		byte[] frame= frame2.clone();
		StringBuilder sb = new StringBuilder();
		sb.append("HEX: [");
	    for (byte b : frame) {
	        sb.append(String.format("%02X ", b));
	    }
	    sb.append("]");
	    return sb.toString();
	}
	
}


