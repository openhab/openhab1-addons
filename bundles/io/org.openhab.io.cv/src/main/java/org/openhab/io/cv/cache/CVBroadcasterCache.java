/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.cv.cache;

import org.atmosphere.cache.BroadcastMessage;
import org.atmosphere.cache.BroadcasterCacheInspector;
import org.atmosphere.cache.CacheMessage;
import org.atmosphere.cpr.ApplicationConfig;
import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcasterCache;
import org.atmosphere.cpr.BroadcasterConfig;
import org.atmosphere.util.ExecutorsFactory;
import org.openhab.core.items.Item;
import org.openhab.io.cv.internal.resources.beans.ItemListBean;
import org.openhab.io.cv.internal.resources.beans.ItemStateListBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

/**
 * An {@link BroadcasterCache} implementation based on {@link UUIDBroadcasterCache} with some changes 
 * for the CometVisu binding.
 *
 * @author Tobias Bräutigam
 * @author Paul Khodchenkov
 * @author Jeanfrancois Arcand
 * 
 * @since 1.4.0
 */
public class CVBroadcasterCache implements BroadcasterCache {

    private final static Logger logger = LoggerFactory.getLogger(CVBroadcasterCache.class);

    private final Map<String, ClientQueue> messages = new HashMap<String, ClientQueue>();

    private final Map<String, Long> activeClients = new HashMap<String, Long>();
    protected final List<BroadcasterCacheInspector> inspectors = new LinkedList<BroadcasterCacheInspector>();
    private ScheduledFuture scheduledFuture;
    protected ScheduledExecutorService taskScheduler;
    private long clientIdleTime = TimeUnit.MINUTES.toMillis(2);//2 minutes
    private long invalidateCacheInterval = TimeUnit.MINUTES.toMillis(1);//1 minute
    private boolean shared = true;
    protected final List<Object> emptyList = Collections.<Object>emptyList();

    public final static class ClientQueue {

        private final LinkedList<CacheMessage> queue = new LinkedList<CacheMessage>();

        private final Set<String> ids = new HashSet<String>();

        public LinkedList<CacheMessage> getQueue() {
            return queue;
        }

        public Set<String> getIds() {
            return ids;
        }

        @Override
        public String toString() {
            return queue.toString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(BroadcasterConfig config) {
        Object o = config.getAtmosphereConfig().properties().get("shared");
        if (o != null) {
            shared = Boolean.parseBoolean(o.toString());
        }

        if (shared) {
            taskScheduler = ExecutorsFactory.getScheduler(config.getAtmosphereConfig());
        } else {
            taskScheduler = Executors.newSingleThreadScheduledExecutor();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        scheduledFuture = taskScheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                invalidateExpiredEntries();
            }
        }, 0, invalidateCacheInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        cleanup();

        if (taskScheduler != null) {
            taskScheduler.shutdown();
        }
    }

    @Override
    public void cleanup() {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CacheMessage addToCache(String broadcasterId, AtmosphereResource r, BroadcastMessage message) {

        Object e = message.message;
        if (logger.isTraceEnabled()) {
            logger.trace("Adding for AtmosphereResource {} cached messages {}", r != null ? r.uuid() : "null", e);
            logger.trace("Active clients {}", activeClients());
        }
        
        long now = System.currentTimeMillis();
        String messageId = UUID.randomUUID().toString();
        CacheMessage cacheMessage = new CacheMessage(messageId, e);
        synchronized (messages) {
            if (r == null) {
                //no clients are connected right now, caching message for all active clients
                for (Map.Entry<String, Long> entry : activeClients.entrySet()) {
                    addMessageIfNotExists(entry.getKey(), cacheMessage);
                }
            } else {
                String clientId = uuid(r);

                activeClients.put(clientId, now);
                addMessageIfNotExists(clientId, cacheMessage);
            }
        }
        return cacheMessage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> retrieveFromCache(String broadcasterId, AtmosphereResource r) {
        String clientId = uuid(r);
        long now = System.currentTimeMillis();

        List<Object> result = new ArrayList<Object>();

        ClientQueue clientQueue;
        synchronized (messages) {
            activeClients.put(clientId, now);
            clientQueue = messages.remove(clientId);
        }
        List<CacheMessage> clientMessages;
        if (clientQueue == null) {
            clientMessages = Collections.emptyList();
        } else {
            clientMessages = clientQueue.getQueue();
        }
        ItemStateListBean response = new ItemStateListBean(new ItemListBean());
        
        for (CacheMessage cacheMessage : clientMessages) {
        	if (cacheMessage.getMessage() instanceof ItemStateListBean) {
        		ItemStateListBean cachedStateList = (ItemStateListBean)cacheMessage.getMessage();
        		// add states to the response (maybe a comparison is needed here so that only the last state of an item is used)
        		response.stateList.entries.addAll(cachedStateList.stateList.entries);
        		if (response.index<cachedStateList.index) {
        			response.index=cachedStateList.index;
        		}
        	} else if (cacheMessage.getMessage() instanceof Item) {
        		Item item = (Item)cacheMessage.getMessage();
        		response.stateList.entries.add(new JAXBElement(new QName(item.getName()), 
                    String.class, item.getState().toString()));
        	}
//            result.add(cacheMessage.getMessage());
        }
        if (response.stateList.entries.size()>0) {
        	if (response.index==0) {
        		response.index = System.currentTimeMillis();
        	}
        	result.add(response);
        }

        if (logger.isTraceEnabled()) {
            synchronized (messages) {
                logger.trace("Retrieved for AtmosphereResource {} cached messages {}", r.uuid(), result);
                logger.trace("Available cached message {}", messages);
            }
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearCache(String broadcasterId, AtmosphereResource r, CacheMessage message) {
        if (message == null) {
            return;
        }

        String clientId = uuid(r);
        ClientQueue clientQueue;
        synchronized (messages) {
            clientQueue = messages.get(clientId);
            if (clientQueue != null) {
                logger.trace("Removing for AtmosphereResource {} cached message {}", r.uuid(), message.getMessage());
                clientQueue.getQueue().remove(message);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BroadcasterCache inspector(BroadcasterCacheInspector b) {
        inspectors.add(b);
        return this;
    }

    protected String uuid(AtmosphereResource r) {
        return r.transport() == AtmosphereResource.TRANSPORT.WEBSOCKET
                ? (String) r.getRequest().getAttribute(ApplicationConfig.SUSPENDED_ATMOSPHERE_RESOURCE_UUID) : r.uuid();
    }

    private void addMessageIfNotExists(String clientId, CacheMessage message) {
        if (!hasMessage(clientId, message.getId())) {
            addMessage(clientId, message);
        } else {
            logger.debug("Duplicate message {} for client {}", clientId, message);
        }
    }

    private void addMessage(String clientId, CacheMessage message) {
        ClientQueue clientQueue = messages.get(clientId);
        if (clientQueue == null) {
            clientQueue = new ClientQueue();
            messages.put(clientId, clientQueue);
        }
        clientQueue.getQueue().addLast(message);
        clientQueue.getIds().add(message.getId());
    }

    private boolean hasMessage(String clientId, String messageId) {
        ClientQueue clientQueue = messages.get(clientId);
        return clientQueue != null && clientQueue.getIds().contains(messageId);
    }

    public Map<String, ClientQueue> messages() {
        return messages;
    }

    public Map<String, Long> activeClients() {
        return activeClients;
    }

    protected boolean inspect(BroadcastMessage m) {
        for (BroadcasterCacheInspector b : inspectors) {
            if (!b.inspect(m)) return false;
        }
        return true;
    }

    public void setInvalidateCacheInterval(long invalidateCacheInterval) {
        this.invalidateCacheInterval = invalidateCacheInterval;
        scheduledFuture.cancel(true);
        start();
    }

    public void setClientIdleTime(long clientIdleTime) {
        this.clientIdleTime = clientIdleTime;
    }

    protected void invalidateExpiredEntries() {
        long now = System.currentTimeMillis();
        synchronized (messages) {

            Set<String> inactiveClients = new HashSet<String>();

            for (Map.Entry<String, Long> entry : activeClients.entrySet()) {
                if (now - entry.getValue() > clientIdleTime) {
                    logger.debug("Invalidate client {}", entry.getKey());
                    inactiveClients.add(entry.getKey());
                }
            }

            for (String clientId : inactiveClients) {
                activeClients.remove(clientId);
                messages.remove(clientId);
            }

        }
    }

    @Override
    public void excludeFromCache(String broadcasterId, AtmosphereResource r) {
        activeClients.remove(r.uuid());
    }

    @Override
    public String toString() {
        return this.getClass().getName();
    }
}

