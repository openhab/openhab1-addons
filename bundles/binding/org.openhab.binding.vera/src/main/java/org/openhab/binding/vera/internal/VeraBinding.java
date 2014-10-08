/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.vera.internal;

import static org.openhab.binding.vera.internal.VeraSubscriptionState.Rescheduled;
import static org.openhab.binding.vera.internal.VeraSubscriptionState.Subscribed;
import static org.openhab.binding.vera.internal.VeraSubscriptionState.Subscribing;
import static org.openhab.binding.vera.internal.VeraSubscriptionState.Unsubscribed;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.controlpoint.ActionCallback;
import org.fourthline.cling.controlpoint.SubscriptionCallback;
import org.fourthline.cling.model.action.ActionArgumentValue;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Action;
import org.fourthline.cling.model.meta.ActionArgument;
import org.fourthline.cling.model.meta.RemoteDevice;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.state.StateVariableValue;
import org.openhab.binding.vera.VeraBindingConfig;
import org.openhab.binding.vera.VeraBindingConstants;
import org.openhab.binding.vera.internal.cling.UpnpServiceProvider;
import org.openhab.binding.vera.internal.converter.VeraCommandConverter;
import org.openhab.binding.vera.internal.converter.VeraConverter;
import org.openhab.binding.vera.internal.converter.VeraStateConverter;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a binding between an openHAB {@link Item} and a Vera <code>device:(service,variable)</code> pair.
 *
 * @author Matthew Bowman
 * @since 1.6.0
 */
public class VeraBinding {
	
	private static final Logger logger = LoggerFactory.getLogger(VeraBinding.class);
	
	/**
	 * The default re-subscription backoff in seconds.
	 */
	private static final int DEFAULT_SUBSCRIPTION_BACKOFF = 2;
	
	/**
	 * The re-subscription backoff multiplier. A value of <code>2</code>
	 * doubles the backoff with each iteration.
	 */
	private static final int DEFAULT_SUBSCRIPTION_BACKOFF_MULTIPLIER = 2;
	
	/**
	 * The max value the backoff is allow to grow to. After that,
	 * the backoff stays at this value. A value of <code>900</code> 
	 * makes the maximum 15 minutes.
	 */
	private static final int DEFAULT_SUBSCRIPTION_BACKOFF_MAX = 900;
	
	/**
	 * The binding config.
	 */
	private VeraBindingConfig bindingConfig;
	
	/**
	 * The unit timeout in seconds.
	 */
	private int unitTimeout;
	
	/**
	 * The ohab event publisher.
	 */
	private EventPublisher eventPublisher;
	
	/**
	 * The Cling {@link RemoteDevice} instance.
	 */
	private RemoteDevice remoteDevice = null;
	
	/**
	 * The Cling {@link RemoteService} instance.
	 */
	private RemoteService remoteService = null;
	
	/**
	 * The Cling {@link Action} instance; 
	 * <code>null</code> for read-only bindings.
	 */
	private Action<RemoteService> action = null;
	
	/**
	 * The Cling {@link ActionArgument} instance; 
	 * <code>null</code> for read-only bindings.
	 */
	private ActionArgument<RemoteService> actionArgument = null;
	
	/**
	 * Thread-safe (atomic) bounded state.
	 */
	private AtomicBoolean bound = new AtomicBoolean(false);
	
	/**
	 * Lock for modifying the subscription state of this binding.
	 * 
	 * Since openHAB and the {@link UpnpService} (Cling) are running 
	 * in separate threads and both calling into this binding, 
	 * we have to synchronize access to the subscription
	 * related instance variables.
	 */
	private Lock subscriptionLock = new ReentrantLock();
	
	/**
	 * The {@link SubscriptionCallback} used for receiving updates from Vera.
	 */
	private SubscriptionCallback subscription;
	
	/**
	 * The current {@link VeraSubscriptionState} of this binding.
	 */
	private VeraSubscriptionState subscriptionState = Unsubscribed;
	
	/**
	 * The value of the backoff in seconds. This is the amount of period
	 * to wait each time there is an error with a subscription.
	 * The value grows with each successive erro and resets to the 
	 * default once a subscription is established.
	 */
	private int subscriptionBackoff = DEFAULT_SUBSCRIPTION_BACKOFF;
	
	/**
	 * The {@link JobKey} used to store the re-subscription job.
	 * This is needed in case of a reload / shutdown to cancel the job.
	 */
	private JobKey subscriptionJobKey = null;
	
	/**
	 * The last {@link State} sent by Vera. Caching the last
	 * state avoids state flapping when invoking actions. Vera sends 
	 * the old value right after an action invocation. The new value
	 * usually arrives shortly after.
	 */
	private State lastState = null;

	public VeraBinding(VeraBindingConfig bindingConfig, VeraUnit unit, EventPublisher eventPublisher) {
		this.bindingConfig = bindingConfig;
		this.unitTimeout = unit.getTimeout();
		this.eventPublisher = eventPublisher;
		
		// the following are derived yet cached as instance vars
		// to avoid unnecessary, frequent lookups
		remoteDevice = unit.getDevice(bindingConfig.getDeviceId());
		remoteService = remoteDevice.findService(bindingConfig.getServiceId());
		String actionName = bindingConfig.getActionName();
		if (StringUtils.isNotBlank(actionName)) {
			action = remoteService.getAction(actionName);
			actionArgument = action.getInputArgument(bindingConfig.getActionArgumentName());
		}
	}
	
	/**
	 * Gets the {@link VeraBindingConfig} of this {@link VeraBinding}.
	 * @return the {@link VeraBindingConfig} of this {@link VeraBinding}.
	 */
	public VeraBindingConfig getBindingConfig() {
		return bindingConfig;
	}
	
	/**
	 * Binds the item by subscribing to Vera.
	 */
	/*default*/ void bind() {
		logger.trace("[{}] bind()", bindingConfig.getItemName());
		bound.set(true);
		UpnpService upnpService = UpnpServiceProvider.getDefaultService();
		upnpService.getRegistry().addDevice(remoteDevice);
		subscribe();
	}
	
	/**
	 * Unbinds the item by unsubscribing from Vera.
	 */
	/*default*/ void unbind() {
		logger.trace("[{}] unbind()", bindingConfig.getItemName());
		bound.set(false);
		unsubscribe();
		UpnpService upnpService = UpnpServiceProvider.getDefaultService();
		upnpService.getRegistry().removeDevice(remoteDevice);
	}
	
	/**
	 * Touches the binding. This is a safety net that is called every
	 * <code>refresh</code> period by the binding service and should be use
	 * to re-establish any dead or dangling subscriptions.
	 */
	/*default*/ void touch() {
		logger.trace("[{}] touch()", bindingConfig.getItemName());
		subscriptionLock.lock();
		if (bound.get() && subscriptionState == Unsubscribed) {
			logger.error("[{}] bound but no active subscription!", bindingConfig.getItemName());
		}
		subscriptionLock.unlock();
	}
	
	/**
	 * Subscribes to Vera.
	 * 
	 * Executes a {@link VeraSubscriptionCallback} to start receiving updates from Vera.
	 */
	/*default*/ void subscribe() {
		logger.trace("[{}] subscribe()", bindingConfig.getItemName());
		try {
			subscriptionLock.lock();
			subscription = new VeraSubscriptionCallback(this, remoteService, unitTimeout);
			UpnpService upnpService = UpnpServiceProvider.getDefaultService();
			upnpService.getControlPoint().execute(subscription);
			subscriptionState = Subscribing;
		} catch (Exception e) {
			logger.warn(String.format("[%s] error during subscribe: %s", bindingConfig.getItemName(), e.getMessage()), e);
		} finally {
			subscriptionLock.unlock();
		}
	}
	
	/**
	 * Unsubscribes from Vera.
	 * 
	 * Cancels any active {@link VeraSubscriptionCallback}.
	 */
	private void unsubscribe() {
		logger.trace("[{}] unbsubscribe()", bindingConfig.getItemName());
		try {
			subscriptionLock.lock();
			// cancel the subscription if it exists
			if (subscriptionState == Subscribed
					&& subscription != null) {
				subscription.end();
				subscription = null;
			}
			// can the subscription job if it exists
			if (subscriptionState == Rescheduled
					&& subscriptionJobKey != null) {
				Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
				scheduler.deleteJob(subscriptionJobKey);
				subscriptionJobKey = null;
			}
			subscriptionState = Unsubscribed;
		} catch (Exception e) {
			logger.warn(String.format("[%s] error during unsubscribe: %s", bindingConfig.getItemName(), e.getMessage()), e);
		} finally {
			subscriptionLock.unlock();
		}
	}
	
	/**
	 * Handles updated {@link StateVariableValue>s received from Vera.
	 * @param values the updated values from Vera
	 */
	/*default*/ void updateValues(Map<String, StateVariableValue<RemoteService>> values) {
		
		if (logger.isTraceEnabled()) {
			logger.trace("[{}] updateValues(...)", bindingConfig.getItemName());
			for (Map.Entry<String, StateVariableValue<RemoteService>> entry: values.entrySet()) {
				String k = entry.getKey();
				StateVariableValue<RemoteService> v = entry.getValue();
				logger.trace("[{}] - {} = {}", bindingConfig.getItemName(), k, v);
			}
		}
		
		StateVariableValue<RemoteService> stateVariableValue = values.get(bindingConfig.getStateVariableName());
		if (stateVariableValue == null) {
			logger.warn("[{}] could not find state variable {}", bindingConfig.getItemName(), bindingConfig.getStateVariableName());
			return;
		}
			
		Object value = stateVariableValue.getValue();
		if (value == null) {
			logger.warn("[{}] ignoring null value for state variable {}", bindingConfig.getItemName(), bindingConfig.getStateVariableName());
			return;
		}
		
		VeraStateConverter<?, ? extends State> converter = VeraConverter.getStateConverter(bindingConfig.getStateType(), bindingConfig.getUpnpType());
		if (converter == null) {
			logger.warn("[{}] no converter found for state = {}", bindingConfig.getItemName(), bindingConfig.getStateType().getSimpleName());
			return;
		}
		
		State state = converter.convertFromValueToState(value);
		if (state == null) {
			logger.warn("[{}] could not convert from {} to {}", bindingConfig.getItemName(), value.getClass().getSimpleName(), bindingConfig.getStateType().getSimpleName());
			return;
		}
		
		// short circuit to avoid duplicate updates
		if (state.equals(lastState)) {
			logger.debug("[{}] skipping update, repeat state {} (value = {})", bindingConfig.getItemName(), state, value);
			return;
		}
	
		logger.debug("[{}] posting update, state = {} (value = {})", bindingConfig.getItemName(), state, value);
		eventPublisher.postUpdate(bindingConfig.getItemName(), state);
		lastState = state;
		
	}

	/**
	 * Handles {@link Command}s received from openHAB.
	 * @param command the command from openHAB
	 */
	/*default*/ void receiveCommand(Command command) {
		logger.trace("receiveCommand(command = {})", command);
		
		VeraCommandConverter<? extends Command, ?> converter = VeraConverter.getCommandConverter(command.getClass(), bindingConfig.getUpnpType());
		if (converter == null) {
			logger.warn("[{}] no converter found for command: {}", bindingConfig.getItemName(), command);
			return;
		}
		
		Object value = converter.convertFromCommandToValue(command);
		if (value == null) {
			logger.warn("[{}] could not convert {} to {}", bindingConfig.getItemName(), command, bindingConfig.getUpnpType());
			return;
		}
		
		@SuppressWarnings("unchecked")
		ActionInvocation<RemoteService> invocation = 
			new ActionInvocation<RemoteService>(action, 
				new ActionArgumentValue[] { 
					new ActionArgumentValue<RemoteService>(actionArgument, value) 
				}); 
		
		logger.debug("[{}] invoking action = {}, value = {} (command = {})", bindingConfig.getItemName(), action, value, command);
		UpnpService upnpService = UpnpServiceProvider.getDefaultService();
		upnpService.getControlPoint().execute(new ActionCallback(invocation) {

			@Override
			@SuppressWarnings("rawtypes")
			public void success(ActionInvocation invocation) {
				logger.trace("[{}] success {}", bindingConfig.getItemName(), invocation);
			}

			@Override
			@SuppressWarnings("rawtypes")
			public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
				logger.warn("[{}] failure {}, operation = {}, defaultMsg = {}", bindingConfig.getItemName(), bindingConfig.getActionName(), invocation, operation, defaultMsg);
			}
			
		});
		
	}
	
	/**
	 * Called by the {@link SubscriptionCallback} to notify that a subscription has been established.
	 */
	/*default*/ void established() {
		try {
			subscriptionLock.lock();
			// clear any cached state
			lastState = null;
			// reset the subscription job params
			subscriptionJobKey = null;
			subscriptionBackoff = DEFAULT_SUBSCRIPTION_BACKOFF;
			subscriptionState = Subscribed;
		} catch (Exception e) {
			logger.warn(String.format("[%s] error during established: %s", bindingConfig.getItemName(), e.getMessage()), e);
		} finally {
			subscriptionLock.unlock();
		}
	}
	
	/**
	 * Called by the {@link SubscriptionCallback} to notify that a subscription has encountered an error.
	 */
	/*default*/ void reschedule() {
		try {
			subscriptionLock.lock();
			// if we're bound, reschedule the subscription
			if (bound.get()) {
				JobDetail job = createSubscriptionJob();
				Trigger trigger = nextSubscriptionJobTrigger();
				StdSchedulerFactory.getDefaultScheduler()
					.scheduleJob(job, trigger);
				logger.debug("[{}] scheduled re-subscription for {}", bindingConfig.getItemName(), trigger.getNextFireTime());
				subscriptionJobKey = job.getKey();
				subscriptionState = Rescheduled;
			}
		} catch (Exception e) {
			logger.warn(String.format("[%s] error during reschedule: %s", bindingConfig.getItemName(), e.getMessage()), e);
		} finally {
			subscriptionLock.unlock();
		}
	}
	
	/**
	 * Factory method for creating {@link JobDetail}s that reschedule subscriptions.
	 * 
	 * @see {@link VeraSubscriptionJob}
	 * @return the JobDetail
	 */
	private JobDetail createSubscriptionJob() {
		JobDataMap jobData = new JobDataMap();
		jobData.put(VeraBindingConstants.SUBSCRIPTION_JOB_DATA_KEY, this);
		JobDetail jobDetail = JobBuilder.newJob(VeraSubscriptionJob.class)
				.usingJobData(jobData)
				.build();
		return jobDetail;
	}
	
	/**
	 * Factory method for creating {@link Trigger}s that reschedule subscriptions.
	 * 
	 * This method makes use of a back-off. If a subscription job fails 
	 * while re-subscribing, the next call to this method returns a trigger with a
	 * longer delay than the previous trigger. This delay will continue to increase with 
	 * each successive call to this method until the subscription has been re-established,
	 * then the delay will reset to its initial value. 
	 *
	 * @see {@link VeraBinding#subscriptionBackoff}
	 * @return the Trigger
	 */
	private Trigger nextSubscriptionJobTrigger() {
		Date futureDate = DateBuilder.futureDate(subscriptionBackoff, IntervalUnit.SECOND);
		if (subscriptionBackoff != DEFAULT_SUBSCRIPTION_BACKOFF_MAX) {
			subscriptionBackoff *= DEFAULT_SUBSCRIPTION_BACKOFF_MULTIPLIER;
			subscriptionBackoff = Math.min(subscriptionBackoff, DEFAULT_SUBSCRIPTION_BACKOFF_MAX);
		}
		Trigger trigger = TriggerBuilder.newTrigger()
				.startAt(futureDate)
				.build();
		return trigger;
		
	}
	
	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public String toString() {
		return String.format("%s ( config = %s )", VeraBinding.class.getSimpleName(), bindingConfig);
	}

}
