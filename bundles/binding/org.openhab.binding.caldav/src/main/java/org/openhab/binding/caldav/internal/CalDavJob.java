package org.openhab.binding.caldav.internal;

import org.openhab.core.events.EventPublisher;
import org.openhab.core.library.types.OnOffType;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class CalDavJob implements Job {

	public static final String JOB_DATA_CONTENT_KEY = "content";
	
	private static EventPublisher eventPublisher;
	
	

	public static EventPublisher getEventPublisher() {
		return eventPublisher;
	}



	public static void setEventPublisher(EventPublisher eventPublisher) {
		CalDavJob.eventPublisher = eventPublisher;
	}



	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		System.out.println("do something");
		String item = (String) arg0.getJobDetail().getJobDataMap().get("item");
		boolean state = (Boolean) arg0.getJobDetail().getJobDataMap().get("state");
		if (state) {
			eventPublisher.postUpdate(item, OnOffType.ON);
		} else {
			eventPublisher.postUpdate(item, OnOffType.OFF);
		}
	}

}
