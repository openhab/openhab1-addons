package org.openhab.io.caldav.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.Period;
import net.fortuna.ical4j.model.PeriodList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.CompatibilityHints;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.service.AbstractActiveService;
import org.openhab.io.caldav.CalDavEvent;
import org.openhab.io.caldav.CalDavLoader;
import org.openhab.io.caldav.EventNotifier;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.DavResource;
import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;

/**
 * 
 * @author Robert Delbrück
 * @since 1.6.1
 *
 */
public class CalDavLoaderImpl extends AbstractActiveService implements ManagedService, CalDavLoader {
	private static final Logger LOG = LoggerFactory.getLogger(CalDavLoaderImpl.class);
	
	private Map<String, CalDavConfig> configMap = new HashMap<String, CalDavConfig>();
	private int refreshInterval = 30 * 60 * 1000;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private Map<String, List<CalDavEvent>> eventMap = new HashMap<String, List<CalDavEvent>>();
	private List<EventNotifier> eventListenerList = new ArrayList<EventNotifier>();

	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
			String refreshString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshString)) {
				refreshInterval = Integer.parseInt(refreshString);
			}
			
			Enumeration<String> iter = config.keys();
			while (iter.hasMoreElements()) {
				String key = iter.nextElement();
				if (key.equals("service.pid")) {
					continue;
				}
				String[] keys = key.split(":");
				String id = keys[0];
				String paramKey = keys[1];
				CalDavConfig calDavConfig = configMap.get(id);
				if (calDavConfig == null) {
					calDavConfig = new CalDavConfig();
					configMap.put(id, calDavConfig);
				}
				String value = config.get(key) + "";
				
				calDavConfig.setKey(id);
				if (paramKey.equals("username")) {
					calDavConfig.setUsername(value);
				} else if (paramKey.equals("password")) {
					calDavConfig.setPassword(value);
				} else if (paramKey.equals("url")) {
					calDavConfig.setUrl(value);
				}
			}
	        
			for (String id : this.configMap.keySet()) {
				LOG.trace("config for id '{}': {}", id, configMap.get(id));
			}
			
			setProperlyConfigured(true);
		}
	}
	
	public void addListener(EventNotifier notifier) {
		this.eventListenerList.add(notifier);
	}
	
	public void removeListener(EventNotifier notifier) {
		this.eventListenerList.remove(notifier);
	}
	
	public void updateEventList(List<CalDavEvent> eventListNew, CalDavConfig config) {
		if (!this.eventMap.containsKey(config.getKey())) {
			LOG.debug("loading events for the first time");
			List<CalDavEvent> eventList = new ArrayList<CalDavEvent>(eventListNew);
			this.eventMap.put(config.getKey(), eventList);
			for (CalDavEvent event : eventList) {
				for (EventNotifier notifier : eventListenerList) {
	            	notifier.eventAdded(event);
				}
            }
		} else {
			LOG.debug("loading events");
			List<CalDavEvent> eventList = this.eventMap.get(config.getKey());
			for (CalDavEvent event : eventList) {
				if (!eventListNew.contains(event)) {
					for (EventNotifier notifier : eventListenerList) {
	                	notifier.eventRemoved(event);
	                }	
				} else {
					CalDavEvent eventNew = eventListNew.get(eventListNew.indexOf(event));
					if (eventNew.getLastChanged().after(event.getLastChanged())) {
						for (EventNotifier notifier : eventListenerList) {
		                	notifier.eventRemoved(event);
		                	notifier.eventAdded(event);
		                }
					}
				}
				
			}
			
			for (CalDavEvent eventNew : eventListNew) {
				if (!eventList.contains(eventNew)) {
					for (EventNotifier notifier : eventListenerList) {
						notifier.eventRemoved(eventNew);
	                }	
				}
			}
			
			this.eventMap.put(config.getKey(), new ArrayList<CalDavEvent>(eventListNew));
		}
	}
	
	private List<CalDavEvent> loadEvents(CalDavConfig config) throws IOException, ParserException {
		List<CalDavEvent> eventList = new ArrayList<CalDavEvent>();
		
		Sardine sardine = SardineFactory.begin(config.getUsername(), config.getPassword());

        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);

        List<DavResource> list = sardine.list(config.getUrl());
        
		for (DavResource resource : list) {
            if (resource.isDirectory()) {
                continue;
            }
            
            URL url = new URL(config.getUrl());
            url = new URL(url.getProtocol(), url.getHost(), url.getPort(), resource.getPath());
            
            InputStream inputStream = sardine.get(url.toString().replaceAll(" ", "%20"));

            CalendarBuilder builder = new CalendarBuilder();

            Calendar calendar = builder.build(inputStream);
            for (CalendarComponent comp : calendar.getComponents()) {
                if (comp instanceof VEvent) {
                    VEvent vEvent = (VEvent) comp;
//                    System.out.println(vEvent.getSummary());
                    java.util.Calendar instance1 = java.util.Calendar.getInstance();
                    instance1.add(java.util.Calendar.HOUR_OF_DAY, -1);
//                    instance1.set(java.util.Calendar.MINUTE, 0);
//                    instance1.set(java.util.Calendar.SECOND, 0);

                    java.util.Calendar instance2 = java.util.Calendar.getInstance();
                    instance2.add(java.util.Calendar.HOUR_OF_DAY, 2);
//                    instance2.set(java.util.Calendar.MINUTE, 59);
//                    instance2.set(java.util.Calendar.SECOND, 59);
                    PeriodList periods = vEvent.calculateRecurrenceSet(new Period(new DateTime(instance1.getTime()), new DateTime(instance2.getTime())));
                    
                    for (Period p : periods) {
                    	CalDavEvent event = new CalDavEvent(vEvent.getSummary().getValue(), 
                    			vEvent.getUid().getValue(), 
                    			config.getKey(),
                        		new Date(p.getStart().getTime()), 
                        		new Date(p.getEnd().getTime()));
                    	event.setLastChanged(vEvent.getLastModified().getDate());
                    	event.setLocation(vEvent.getLocation().getValue());
//                    	System.out.println("event: " + vEvent.getSummary().getValue());
                        eventList.add(event);
                    }

                }
            }
        }
		
		return eventList;
	}

	@Override
	protected void execute() {
		new Timer(true).schedule(new TimerTask() {
			
			@Override
			public void run() {
				try {
					lock.lock();
					for (CalDavConfig config : configMap.values()) {
						List<CalDavEvent> eventList = loadEvents(config);
						updateEventList(eventList, config);
					}
					lock.unlock();
				} catch (IOException e) {
					LOG.error("error while loading calendar entries", e);
				} catch (ParserException e) {
					LOG.error("error while loading calendar entries", e);
				}
			}
		}, 100, this.refreshInterval);
	}

	@Override
	protected long getRefreshInterval() {
		return this.refreshInterval;
	}

	@Override
	protected String getName() {
		return "CalDav Loader";
	}

}
