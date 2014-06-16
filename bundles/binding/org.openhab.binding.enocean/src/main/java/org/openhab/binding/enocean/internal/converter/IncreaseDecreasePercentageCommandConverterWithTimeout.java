package org.openhab.binding.enocean.internal.converter;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import org.openhab.binding.enocean.internal.converter.DataExchangeInterface;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class IncreaseDecreasePercentageCommandConverterWithTimeout extends CommandConverter<PercentType, IncreaseDecreaseType> {

    private static final Logger logger = LoggerFactory.getLogger(IncreaseDecreasePercentageCommandConverterWithTimeout.class);

    private Timer TimeOut = new Timer();
    private int[] dimValue = {0,22,27,32,39,47,57,68,83,100};
    private String currentItem;
    
    @Override
    protected PercentType convertImpl(State actualState, IncreaseDecreaseType command){
    	currentItem = DataExchangeInterface.currentItem.getCurrentItemName();
    	if(DataExchangeInterface.ackReceived.timerState(currentItem) == false)
    	{
    		return null;
    	}
        if (actualState instanceof PercentType) {
            PercentType actualPercentage = (PercentType) actualState;
            return calculateNewState(command, actualPercentage);
        } else if (actualState instanceof UnDefType) {
        	if (command.equals(IncreaseDecreaseType.INCREASE)) {
        		DataExchangeInterface.ackReceived.timerStart(currentItem);
        		TimeOut.schedule(new WaitForAck(currentItem), 2200);
        		return new PercentType(22);
        	} else {
        		DataExchangeInterface.ackReceived.timerStart(currentItem);
        		TimeOut.schedule(new WaitForAck(currentItem), 2200);
        		return new PercentType(0);
        	}
        } else {
            logger.warn("Could not increase / decrease actualState " + actualState + ". Expected PercentageType, got " + actualState.getClass());
            return null;
        }
    }

    private PercentType calculateNewState(IncreaseDecreaseType command, PercentType actualPercentage) {
    	if(DataExchangeInterface.ackReceived.hasAckReceived(currentItem) == true) {
    		DataExchangeInterface.ackReceived.setAckReceived(currentItem, false);
    		DataExchangeInterface.ackReceived.updateUI(currentItem, false);
    		DataExchangeInterface.ackReceived.resetRTACounter(currentItem);
	        if (command.equals(IncreaseDecreaseType.INCREASE)) {
	            int newValue = increaseValue(actualPercentage.intValue());
	            DataExchangeInterface.ackReceived.timerStart(currentItem);
	            TimeOut.schedule(new WaitForAck(currentItem), 2200);
	            return new PercentType(newValue);
	        } else {
	            int newValue = decreaseValue(actualPercentage.intValue());
	            DataExchangeInterface.ackReceived.timerStart(currentItem);
				TimeOut.schedule(new WaitForAck(currentItem), 2200);
	            return new PercentType(newValue);
	        }
    	} else {
    		DataExchangeInterface.ackReceived.increaseRTACounter(currentItem);
    		if(DataExchangeInterface.ackReceived.getRTACounter(currentItem) < 3) {
    			logger.info("No acknowledgement has been received. Starting retransmission attempt:" + DataExchangeInterface.ackReceived.getRTACounter(currentItem));
    			DataExchangeInterface.ackReceived.timerStart(currentItem);
    			TimeOut.schedule(new WaitForAck(currentItem), 2200);
    			return new PercentType(actualPercentage.intValue());
    		} else {
    			DataExchangeInterface.ackReceived.updateUI(currentItem, true);
    			DataExchangeInterface.ackReceived.timerStart(currentItem);
    			TimeOut.schedule(new WaitForAck(currentItem), 2200);
    			return new PercentType(actualPercentage.intValue());
    		}
    	}
    }

    private int decreaseValue(int value) {
    	int index = Arrays.binarySearch(dimValue, value);
    	if(index == 0) {
    		value = 0;
    	}else { 
            value = dimValue[index-1];
        }
        return value;
    }

    private int increaseValue(int value) {
    	int index = Arrays.binarySearch(dimValue, value);
        if (index == 9) {
            value = 100;
        }else {
        	value = dimValue[index+1];
        }
        return value;
    }
    
    
    public class WaitForAck extends TimerTask {
    	protected String itemName;
    	
    	public WaitForAck(String itemKey) {
    		super();
    		itemName = itemKey;
    	}
    	
    	@Override
    	public void run() {
    		DataExchangeInterface.ackReceived.timerEnd(itemName);
    	}
    }
}