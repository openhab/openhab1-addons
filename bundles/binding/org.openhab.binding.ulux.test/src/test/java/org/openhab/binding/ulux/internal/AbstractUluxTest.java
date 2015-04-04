package org.openhab.binding.ulux.internal;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.StringTokenizer;

import org.openhab.binding.ulux.internal.ump.UluxMessage;

public abstract class AbstractUluxTest {

	protected final ByteBuffer toBuffer(CharSequence data) {
		ByteBuffer buffer = ByteBuffer.wrap(toBytes(data));
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		return buffer;
	}

	protected final byte[] toBytes(UluxMessage message) {
		return message.getBuffer().array();
	}

	protected final byte[] toBytes(CharSequence data) {
		final StringTokenizer tokenizer = new StringTokenizer(data.toString(), ":");
		final byte[] bytes = new byte[tokenizer.countTokens()];

		for (int i = 0; i < bytes.length; i++) {
			// TODO remove cast to byte ?
			bytes[i] = (byte) Short.parseShort(tokenizer.nextToken(), 16);
		}

		return bytes;
	}

}
