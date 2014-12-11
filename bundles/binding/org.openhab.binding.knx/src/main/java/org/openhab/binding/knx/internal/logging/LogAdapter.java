package org.openhab.binding.knx.internal.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.log.LogLevel;
import tuwien.auto.calimero.log.LogWriter;

/**
 * An adapter that works as a bridge between the logging API of calimero and slf4j.
 * 
 * Calimero (the KNX I/O library used by this binding) has its own and very specific logging framework, 
 * which is not compatible with any default logging framework. Thus, this specific adapter is necessary
 * in order to build a bridge between the calimero logging framework and the API of slf4j.
 * 
 * All logging events from calimero are written to a single slf4j logger named <code>tuwien.auto.calimero</code>.
 * 
 * @author Martin Renner
 * @since 1.5.0
 */
public class LogAdapter extends LogWriter {

	private final static Logger logger = LoggerFactory.getLogger("tuwien.auto.calimero");

	@Override
	public void write(String logService, LogLevel logLevel, String msg) {
		// Simply delegate to the method with exception.
		// org.slf4j.helpers.MessageFormatter will handle this for us.
		write(logService, logLevel, msg, null);
	}

	@Override
	public void write(String logService, LogLevel logLevel, String msg, Throwable t) {
		// Unfortunately, calimero does not use package names for its loggers. So we cannot use "logService" and
		// we have to map all calimero-loggers to a single slf4j logger.

		// slf4j does not offer a "log(level, msg)" api, so we have to dispatch in this rather ugly if-else block.
		if (LogLevel.ALWAYS.equals(logLevel) || LogLevel.FATAL.equals(logLevel) || LogLevel.ERROR.equals(logLevel)) {
			logger.error("{}: {}", logService, msg, t);
		} else if (LogLevel.WARN.equals(logLevel)) {
			logger.warn("{}: {}", logService, msg, t);
		} else if (LogLevel.INFO.equals(logLevel)) {
			logger.info("{}: {}", logService, msg, t);
		} else if (LogLevel.TRACE.equals(logLevel)) {
			logger.debug("{}: {}", logService, msg, t);
		} else {
			logger.info("{}: {}", logService, msg, t);
		}
	}

	@Override
	public void close() {
		// empty
	}

	@Override
	public void flush() {
		// empty
	}
}
