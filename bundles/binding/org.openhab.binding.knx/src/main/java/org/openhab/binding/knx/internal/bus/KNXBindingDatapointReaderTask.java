/**
 * 
 */
package org.openhab.binding.knx.internal.bus;

import java.util.concurrent.BlockingQueue;

import org.openhab.binding.knx.internal.connection.KNXConnection;
import org.openhab.binding.knx.internal.connection.KNXConnectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;
import tuwien.auto.calimero.process.ProcessCommunicator;

/**
 * @author vdaube
 *
 */
public class KNXBindingDatapointReaderTask extends Thread implements KNXConnectionListener {

	private final BlockingQueue<Datapoint> readQueue;
	private final static Logger sLogger = LoggerFactory.getLogger(KNXBindingDatapointReaderTask.class);
	private boolean mInterrupted=false;
	private boolean mKNXConnected=true;

	public KNXBindingDatapointReaderTask(BlockingQueue<Datapoint> queue) {
		this.setName("KNXBinding/DatapointReaderTask");
		this.readQueue = queue;
		KNXConnection.addConnectionEstablishedListener(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while (!mInterrupted ) {
				sLogger.debug("Autorefresh: Waiting for new item in reader queue");
				Datapoint dp=readQueue.take();
				sLogger.debug("Autorefresh: got new item in reader queue");
				if (mKNXConnected) {
					sLogger.debug("Autorefresh: Trying to read form KNX bus: {}", dp);
					readFromKNXBus(dp);

					long readingPause = KNXConnection.getReadingPause();
					if (readingPause > 0) {
						try {
							sLogger.debug("Autorefresh: DatapointReaderTask Waiting {} msecs to prevent KNX bus overload", readingPause);
							Thread.sleep(readingPause);
						} catch (InterruptedException e) {
							sLogger.debug("Autorefresh: DatapointReaderTask KNX reading pause has been interrupted: {}", e.getMessage());
						}
					}
				}
				else {
					sLogger.debug("Autorefresh: Not connected. Skipping bus read.");
				}
			}
			sLogger.debug("Autorefresh: DatapointReaderTask interrupted.");
		} catch (InterruptedException ex) { 
			sLogger.debug("Autorefresh: DatapointReaderTask wait on blockingqueue interrupted: {}", ex.getMessage());
		}
	}

	public void setInterrupted(boolean interrupted) {
		this.mInterrupted=interrupted;
	}
	
	private void readFromKNXBus(Datapoint datapoint) {
		try {
			ProcessCommunicator pc = KNXConnection.getCommunicator();
			if (pc != null) {
				sLogger.debug("Autorefresh: Sending read request to KNX for item '{}'", datapoint.getName());
				pc.read(datapoint);
			}
			else {
				sLogger.debug("Autorefresh: Couldn't sent read request to KNX for item '{}'. Connection to KNX bus not (yet) established.", datapoint.getName());
			}
		} catch (KNXException e) {
			sLogger.warn("Autorefresh: Cannot read value for item '{}' from KNX bus: {}", datapoint.getName(), e.getMessage() );
		} catch (KNXIllegalArgumentException e) {
			sLogger.warn("Autorefresh: Error sending KNX read request for '{}': {}", datapoint.getName(), e.getMessage() );
		} catch (InterruptedException e) {
			sLogger.warn("Autorefresh: Cannot read value for item '{}' from KNX bus: {}", datapoint.getName(), e.getMessage() );
		}

	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.knx.internal.connection.KNXConnectionListener#connectionEstablished()
	 */
	@Override
	public void connectionEstablished() {
		mKNXConnected=true;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.knx.internal.connection.KNXConnectionListener#connectionLost()
	 */
	@Override
	public void connectionLost() {
		mKNXConnected=false;
	}

}
