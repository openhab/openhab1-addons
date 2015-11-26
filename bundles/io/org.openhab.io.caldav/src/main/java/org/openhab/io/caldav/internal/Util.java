package org.openhab.io.caldav.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Clazz;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTimeZone;
import org.openhab.io.caldav.CalDavEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.Sardine;
import com.github.sardine.impl.SardineImpl;

public final class Util {
	private static final Logger log = LoggerFactory.getLogger(Util.class);
	
	private Util() {}
	
	public static Calendar createCalendar(CalDavEvent calDavEvent, DateTimeZone timeZone) {
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance().createRegistry();
		TimeZone timezone = registry.getTimeZone(timeZone.getID());
		
		Calendar calendar = new Calendar();
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(new ProdId("openHAB"));
		VEvent vEvent = new VEvent();
		vEvent.getProperties().add(new Summary(calDavEvent.getName()));
		vEvent.getProperties().add(new Description(calDavEvent.getContent()));
		final DtStart dtStart = new DtStart(new net.fortuna.ical4j.model.DateTime(calDavEvent.getStart().toDate()));
		dtStart.setTimeZone(timezone);
		vEvent.getProperties().add(dtStart);
		final DtEnd dtEnd = new DtEnd(new net.fortuna.ical4j.model.DateTime(calDavEvent.getEnd().toDate()));
		dtEnd.setTimeZone(timezone);
		vEvent.getProperties().add(dtEnd);
		vEvent.getProperties().add(new Uid(calDavEvent.getId()));
		vEvent.getProperties().add(Clazz.PUBLIC);
		vEvent.getProperties().add(new LastModified(new net.fortuna.ical4j.model.DateTime(calDavEvent.getLastChanged().toDate())));
		calendar.getComponents().add(vEvent);
		
		return calendar;
	}
	
	public static File getCachePath(String calendarKey) {
		return new File(CalDavLoaderImpl.CACHE_PATH + "/" + calendarKey);
	}
	
	public static String getFilename(String name) {
		name = FilenameUtils.getBaseName(name);
		name = name.replaceAll("[^a-zA-Z0-9-_]", "_");
		return name;
	}
	
	public static File getCacheFile(String calendarId, String filename) {
		return new File(getCachePath(calendarId), filename + ".ics");
	}
	
	public static void storeToDisk(String calendarId, String filename, Calendar calendar) {
		try {
			FileOutputStream fout = new FileOutputStream(getCacheFile(calendarId, filename));
			CalendarOutputter outputter = new CalendarOutputter();
			outputter.setValidating(false);
			outputter.output(calendar, fout);
			fout.flush();
			fout.close();
		} catch (IOException e) {
			log.error("cannot store event '{}' to disk", filename);
		} catch (ValidationException e) {
			log.error("cannot store event '{}' to disk", filename);
		}
	}
	
	public static Sardine getConnection(CalDavConfig config) {
		if (config.isDisableCertificateVerification()) {
			HttpClientBuilder httpClientBuilder = HttpClientBuilder.create().setHostnameVerifier(new AllowAllHostnameVerifier());
			try {
				httpClientBuilder.setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
				{
				    public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException
				    {
				        return true;
				    }
				}).build());
			} catch (KeyManagementException e) {
				log.error("error verifying certificate", e);
			} catch (NoSuchAlgorithmException e) {
				log.error("error verifying certificate", e);
			} catch (KeyStoreException e) {
				log.error("error verifying certificate", e);
			}
			if (StringUtils.isEmpty(config.getUsername()) && StringUtils.isEmpty(config.getPassword())) {
				return new SardineImpl(httpClientBuilder);
			} else {
				return new SardineImpl(httpClientBuilder, config.getUsername(), config.getPassword());
			}
		} else {
			if (StringUtils.isEmpty(config.getUsername()) && StringUtils.isEmpty(config.getPassword())) {
				return new SardineImpl();
			} else {
				return new SardineImpl(config.getUsername(), config.getPassword());
			}
		}
	}
}
