package android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Log {

	private static final Logger l = LoggerFactory.getLogger(Log.class);

	public static int v(java.lang.String tag, java.lang.String msg) {
		l.trace(String.format("%s - %s", tag, msg));
		return 0; // did not find in android documentation what return codes
					// mean
	}

	public static int v(java.lang.String tag, java.lang.String msg,
			java.lang.Throwable tr) {
		l.trace(String.format("%s - %s", tag, msg), tr);
		return 0;
	}

	public static int d(java.lang.String tag, java.lang.String msg) {
		l.debug(String.format("%s - %s", tag, msg));
		return 0;
	}

	public static int d(java.lang.String tag, java.lang.String msg,
			java.lang.Throwable tr) {
		l.debug(String.format("%s - %s",tag, msg),tr);
		return 0;
	}

	public static int i(java.lang.String tag, java.lang.String msg) {
		l.info(String.format("%s - %s", tag, msg));
		return 0;
	}

	public static int i(java.lang.String tag, java.lang.String msg,
			java.lang.Throwable tr) {
		l.info(String.format("%s - %s",tag, msg),tr);
		return 0;
	}

	public static int w(java.lang.String tag, java.lang.String msg) {
		l.warn(String.format("%s - %s", tag, msg));
		return 0;
	}

	public static int w(java.lang.String tag, java.lang.String msg,
			java.lang.Throwable tr) {
		l.warn(String.format("%s - %s",tag, msg), tr);
		return 0;
	}

	public static boolean isLoggable(java.lang.String tag, int level) {
		switch (level) {
		case VERBOSE:
			return l.isTraceEnabled();
		case DEBUG:
			return l.isDebugEnabled();
		case INFO:
			return l.isInfoEnabled();
		case WARN:
			return l.isWarnEnabled();
		case ERROR:
		case ASSERT:
			return l.isErrorEnabled();
		default:
			return false;
		}
	}

	public static int w(java.lang.String tag, java.lang.Throwable tr) {
		l.warn(tag, tr);
		return 0;
	}

	public static int e(java.lang.String tag, java.lang.String msg) {
		l.error(String.format("%s - %s",tag, msg));
		return 0;
	}

	public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
		l.error(String.format("%s - %s",tag, msg), tr);
		return 0;
	}

	public static int wtf(java.lang.String tag, java.lang.String msg) {
		l.error(String.format("%s - %s",tag, msg));
		return 0;

	}

	public static int wtf(java.lang.String tag, java.lang.Throwable tr) {
		l.error(tag, tr);
		return 0;
	}

	public static int wtf(java.lang.String tag, java.lang.String msg,
			java.lang.Throwable tr) {
		l.error(String.format("%s - %s",tag, msg), tr);
		return 0;
	}

	public static java.lang.String getStackTraceString(java.lang.Throwable tr) {
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	public static int println(int priority, java.lang.String tag,
			java.lang.String msg) {
		switch (priority) {
		case VERBOSE:
			return v(tag,msg);
		case DEBUG:
			return d(tag,msg);
		case INFO:
			return i(tag,msg);
		case WARN:
			return w(tag,msg);
		case ERROR:
		case ASSERT:
			return e(tag,msg);
		default:
			return -1;
		}
	}

	public static final int VERBOSE = 2;
	public static final int DEBUG = 3;
	public static final int INFO = 4;
	public static final int WARN = 5;
	public static final int ERROR = 6;
	public static final int ASSERT = 7;
}
