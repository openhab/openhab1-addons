package org.openhab.binding.lcn.mappingtarget;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lcn.common.LcnAddr;
import org.openhab.binding.lcn.common.LcnDefs;
import org.openhab.binding.lcn.common.PckGenerator;
import org.openhab.binding.lcn.connection.Connection;
import org.openhab.binding.lcn.input.ModStatusBinSensors;
import org.openhab.binding.lcn.input.ModStatusKeyLocks;
import org.openhab.binding.lcn.input.ModStatusLedsAndLogicOps;
import org.openhab.binding.lcn.input.ModStatusOutput;
import org.openhab.binding.lcn.input.ModStatusRelays;
import org.openhab.binding.lcn.input.ModStatusVar;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;

/**
 * Sets dynamic text in an LCN-GTxD.
 * 
 * @author Tobias Jüttner
 *
 */
public class DynText extends TargetWithLcnAddr {
	
	/** Pattern to parse dynamic-text commands. */ 
	private static final Pattern PATTERN_DYNTEXT =
		Pattern.compile("(?<rowId>[1234])\\.(?<text>.*)",
			Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	
	/** 0..3 */
	private final int rowId;
	
	/** The text to set. */
	private final String text;
	
	/**
	 * Constructor.
	 * 
	 * @param addr the target LCN address
	 * @param rowId 0..3
	 * @param text the text to set
	 */
	DynText(LcnAddr addr, int rowId, String text) {
		super(addr);
		this.rowId = rowId;
		this.text = text;
	}
	
	/**
	 * Tries to parse the given input text.
	 * 
	 * @param input the text to parse
	 * @return the parsed {@link DynText} or null
	 */
	static Target tryParseTarget(String input) {
		CmdAndAddressRet header = CmdAndAddressRet.parse(input, true);
		if (header != null) {
			Matcher matcher;
			switch (header.getCmd().toUpperCase()) {
				case "DYNTEXT":
					if ((matcher = PATTERN_DYNTEXT.matcher(header.getRestInput())).matches()) {
						return new DynText(header.getAddr(), Integer.parseInt(matcher.group("rowId")) - 1, matcher.group("text"));
					}
					break;
			}
		}
		return null; 
	}

	/** {@inheritDoc} */
	@Override
	public void send(Connection conn, Item item, Command cmd) {
		// Encode the text and split it into parts with max. 12 bytes each
		LinkedList<ByteBuffer> l = new LinkedList<ByteBuffer>();
		try {
			byte[] buf = this.text.getBytes(LcnDefs.LCN_ENCODING);
			int pos = 0, part = 0;
			while (pos < buf.length) {
				ByteBuffer data = ByteBuffer.allocate(64);
				data.put(PckGenerator.dynTextHeader(this.rowId, part++).getBytes(LcnDefs.LCN_ENCODING));
				int partPos = 0;
				while (pos < buf.length && partPos < 12) {
					data.put(buf[partPos++]);
				}
				data.flip();
				l.add(data);
				pos += partPos;
				// Write empty termination-packet if everything fitted exactly (see LCN-PCK docs)
				if (pos == buf.length && partPos == 12 && part < 5) {
					data = ByteBuffer.allocate(64);
					data.put(PckGenerator.dynTextHeader(this.rowId, part++).getBytes(LcnDefs.LCN_ENCODING));
					data.flip();
					l.add(data);
				}
			}
		} catch (UnsupportedEncodingException ex) { }
		if (l.size() <= 5) {
			for (ByteBuffer data : l) {
				conn.queue(this.addr, !this.addr.isGroup(), data);
			}
		}
		else {
			logger.error(String.format("Cannot send dynamic text (too long): %s", this.text));
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public void register(Connection conn) { }

	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleOutputStatus(ModStatusOutput pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }

	/** {@inheritDoc} */
	@Override
	public boolean visualizationHandleRelaysStatus(ModStatusRelays pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }

	/** {@inheritDoc} */
	@Override
	public boolean visualizationBinSensorsStatus(ModStatusBinSensors pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }

	/** {@inheritDoc} */
	@Override
	public boolean visualizationVarStatus(ModStatusVar pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationLedsAndLogicOpsStatus(ModStatusLedsAndLogicOps pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
	/** {@inheritDoc} */
	@Override
	public boolean visualizationKeyLocksStatus(ModStatusKeyLocks pchkInput, Command cmd, Item item, EventPublisher eventPublisher) { return false; }
	
}
