package org.openhab.binding.lightwaverf.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LightwaveRfHeatPoller  {
	
	private static final Logger logger =  LoggerFactory.getLogger(LightwaveRfHeatPoller.class);
	private final LightwaveRFSender sender;
	private final LightwaverfConvertor convertor;
	private final Map<String, Integer> roomsToPoll = new HashMap<String, Integer>();
	private final Map<String, String> itemNameToRoomMap = new HashMap<String, String>();
	private final Map<String, ScheduledFuture<?>> tasksMap = new HashMap<String, ScheduledFuture<?>>();
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	
	public LightwaveRfHeatPoller(LightwaveRFSender sender, LightwaverfConvertor convertor) {
		this.sender = sender;
		this.convertor = convertor;
	}
	
	public void removeRoomToPoll(String itemName){
		logger.info("Cancelling polling for {}", itemName);
		String roomId = itemNameToRoomMap.remove(itemName);
		if(roomId != null){
			ScheduledFuture<?> currentFuture = tasksMap.remove(roomId);
			if(currentFuture != null){
				currentFuture.cancel(false);
			}
		}
	}
	
	public void addRoomToPoll(String itemName, String roomId, int requestedPoll){
		Integer currentPoll = roomsToPoll.get(roomId);
		if(currentPoll == null || requestedPoll < currentPoll.intValue()){
			currentPoll = requestedPoll;
			roomsToPoll.put(roomId, currentPoll);
			logger.info("Polling changed RoomId{{}], RequestedPoll[{}]", roomId, requestedPoll);
		}
		else{
			logger.info("Request to set poll interval is larger than the current poll interval so ignored RoomId{{}], RequestedPoll[{}], CurrentPoll[{}]", roomId, requestedPoll, currentPoll);
		}
		
		ScheduledFuture<?> currentFuture = tasksMap.remove(roomId);
		if(currentFuture != null){
			currentFuture.cancel(false);
		}
		
		ScheduledFuture<?> future = executor.scheduleWithFixedDelay(new SendMessageThread(roomId), currentPoll, currentPoll, TimeUnit.SECONDS);
		tasksMap.put(roomId, future);
		itemNameToRoomMap.put(itemName, roomId);
	}
	
	private class SendMessageThread implements Runnable {

		private final String roomId;
		
		private SendMessageThread(String roomId){
			this.roomId = roomId;
		}
		
		@Override
		public void run() {
			LightwaveRFCommand command = convertor.getHeatRequest(roomId);
			sender.sendUDP(command);
		}
	}

	public void stop() {
		executor.shutdownNow();
	}

}
