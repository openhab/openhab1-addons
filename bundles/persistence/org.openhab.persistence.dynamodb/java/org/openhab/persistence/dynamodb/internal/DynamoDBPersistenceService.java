/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.persistence.dynamodb.internal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.persistence.FilterCriteria;
import org.openhab.core.persistence.HistoricItem;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.persistence.QueryablePersistenceService;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper.FailedBatch;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.PaginationLoadingStrategy;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.document.BatchWriteItemOutcome;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;
import com.amazonaws.services.dynamodbv2.model.WriteRequest;

/**
 * This is the implementation of the DynamoDB {@link PersistenceService}. It persists item values
 * using the <a href="https://aws.amazon.com/dynamodb/">Amazon DynamoDB</a> database. The states (
 * {@link State}) of an {@link Item} are persisted in DynamoDB tables.
 *
 * The service creates tables automatically, one for numbers, and one for strings.
 *
 * @see AbstractDynamoDBItem.fromState for details how different items are persisted
 *
 * @author Sami Salonen
 *
 */
public class DynamoDBPersistenceService extends AbstractBufferedPersistenceService<DynamoDBItem<?>>
        implements QueryablePersistenceService {

    private class ExponentialBackoffRetry implements Runnable {
        private int retry;
        private Map<String, List<WriteRequest>> unprocessedItems;
        private Exception lastException;

        public ExponentialBackoffRetry(Map<String, List<WriteRequest>> unprocessedItems) {
            this.unprocessedItems = unprocessedItems;
        }

        @Override
        public void run() {
            logger.debug("Error storing object to dynamo, unprocessed items: {}. Retrying with exponential back-off",
                    unprocessedItems);
            lastException = null;
            while (!unprocessedItems.isEmpty() && retry < WAIT_MILLIS_IN_RETRIES.length) {
                if (!sleep()) {
                    // Interrupted
                    return;
                }
                retry++;
                try {
                    BatchWriteItemOutcome outcome = DynamoDBPersistenceService.this.db.getDynamoDB()
                            .batchWriteItemUnprocessed(unprocessedItems);
                    unprocessedItems = outcome.getUnprocessedItems();
                    lastException = null;
                } catch (AmazonServiceException e) {
                    if (e instanceof ResourceNotFoundException) {
                        logger.debug(
                                "DynamoDB query raised unexpected exception: {}. This might happen if table was recently created",
                                e.getMessage());
                    } else {
                        logger.debug("DynamoDB query raised unexpected exception: {}.", e.getMessage());
                    }
                    lastException = e;
                    continue;
                }
            }
            if (unprocessedItems.isEmpty()) {
                logger.debug("After {} retries successfully wrote all unprocessed items", retry);
            } else {
                logger.warn(
                        "Even after retries failed to write some items. Last exception: {} {}, unprocessed items: {}",
                        lastException == null ? "null" : lastException.getClass().getName(),
                        lastException == null ? "null" : lastException.getMessage(), unprocessedItems);
            }
        }

        private boolean sleep() {
            try {
                long sleepTime;
                if (retry == 1 && lastException != null && lastException instanceof ResourceNotFoundException) {
                    sleepTime = WAIT_ON_FIRST_RESOURCE_NOT_FOUND_MILLIS;
                } else {
                    sleepTime = WAIT_MILLIS_IN_RETRIES[retry];
                }
                Thread.sleep(sleepTime);
                return true;
            } catch (InterruptedException e) {
                logger.debug("Interrupted while writing data!");
                return false;
            }
        }

        public Map<String, List<WriteRequest>> getUnprocessedItems() {
            return unprocessedItems;
        }
    }

    private static final int WAIT_ON_FIRST_RESOURCE_NOT_FOUND_MILLIS = 5000;
    private static final int[] WAIT_MILLIS_IN_RETRIES = new int[] { 100, 100, 200, 300, 500 };
    private static final String DYNAMODB_THREADPOOL_NAME = "dynamodbPersistenceService";

    private ItemRegistry itemRegistry;
    private DynamoDBClient db;
    private final Logger logger = LoggerFactory.getLogger(DynamoDBPersistenceService.class);
    private boolean isProperlyConfigured;
    private DynamoDBConfig dbConfig;
    private DynamoDBTableNameResolver tableNameResolver;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, new NamedThreadFactory());
    private ScheduledFuture<?> writeBufferedDataFuture;

    /**
     * For testing. Allows access to underlying DynamoDBClient.
     *
     * @return DynamoDBClient connected to AWS Dyanamo DB.
     */
    DynamoDBClient getDb() {
        return db;
    }

    public void setItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = itemRegistry;
    }

    public void unsetItemRegistry(ItemRegistry itemRegistry) {
        this.itemRegistry = null;
    }

    public void activate(final BundleContext bundleContext, final Map<String, Object> config) {
        resetClient();
        dbConfig = DynamoDBConfig.fromConfig(config);
        if (dbConfig == null) {
            // Configuration was invalid. Abort service activation.
            // Error is already logger in fromConfig.
            return;
        }

        tableNameResolver = new DynamoDBTableNameResolver(dbConfig.getTablePrefix());
        try {
            if (!ensureClient()) {
                logger.error("Error creating dynamodb database client. Aborting service activation.");
                return;
            }
        } catch (Exception e) {
            logger.error("Error constructing dynamodb client", e);
            return;
        }

        writeBufferedDataFuture = null;
        resetWithBufferSize(dbConfig.getBufferSize());
        long commitIntervalMillis = dbConfig.getBufferCommitIntervalMillis();
        if (commitIntervalMillis > 0) {
            writeBufferedDataFuture = scheduler.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    DynamoDBPersistenceService.this.flushBufferedData();
                }
            }, 0, commitIntervalMillis, TimeUnit.MILLISECONDS);
        }
        isProperlyConfigured = true;
        logger.debug("dynamodb persistence service activated");
    }

    public void deactivate() {
        logger.debug("dynamodb persistence service deactivated");
        if (writeBufferedDataFuture != null) {
            writeBufferedDataFuture.cancel(false);
            writeBufferedDataFuture = null;
        }
        resetClient();
    }

    /**
     * Initializes DynamoDBClient (db field)
     *
     * If DynamoDBClient constructor throws an exception, error is logged and false is returned.
     *
     * @return whether initialization was successful.
     */
    private boolean ensureClient() {
        if (db == null) {
            try {
                db = new DynamoDBClient(dbConfig);
            } catch (Exception e) {
                logger.error("Error constructing dynamodb client", e);
                return false;
            }
        }
        return true;
    }

    @Override
    public DynamoDBItem<?> persistenceItemFromState(String name, State state, Date time) {
        return AbstractDynamoDBItem.fromState(name, state, time);
    }

    /**
     * Create table (if not present) and wait for table to become active.
     *
     * Synchronized in order to ensure that at most single thread is creating the table at a time
     *
     * @param mapper
     * @param dtoClass
     * @return whether table creation succeeded.
     */
    private synchronized boolean createTable(DynamoDBMapper mapper, Class<?> dtoClass) {
        if (db == null) {
            return false;
        }
        String tableName;
        try {
            ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput(dbConfig.getReadCapacityUnits(),
                    dbConfig.getWriteCapacityUnits());
            CreateTableRequest request = mapper.generateCreateTableRequest(dtoClass);
            request.setProvisionedThroughput(provisionedThroughput);
            if (request.getGlobalSecondaryIndexes() != null) {
                for (GlobalSecondaryIndex index : request.getGlobalSecondaryIndexes()) {
                    index.setProvisionedThroughput(provisionedThroughput);
                }
            }
            tableName = request.getTableName();
            try {
                db.getDynamoClient().describeTable(tableName);
            } catch (ResourceNotFoundException e) {
                // No table present, continue with creation
                db.getDynamoClient().createTable(request);
            } catch (AmazonClientException e) {
                logger.error("Table creation failed due to error in describeTable operation", e);
                return false;
            }

            // table found or just created, wait
            return waitForTableToBecomeActive(tableName);

        } catch (AmazonClientException e) {
            logger.error("Exception when creating table", e);
            return false;
        }

    }

    private boolean waitForTableToBecomeActive(String tableName) {
        try {
            logger.debug("Checking if table '{}' is created...", tableName);
            final TableDescription tableDescription;
            try {
                tableDescription = db.getDynamoDB().getTable(tableName).waitForActive();
            } catch (IllegalArgumentException e) {
                logger.warn("Table '{}' is being deleted: {} {}", tableName, e.getClass().getSimpleName(),
                        e.getMessage());
                return false;
            } catch (ResourceNotFoundException e) {
                logger.warn("Table '{}' was deleted unexpectedly: {} {}", tableName, e.getClass().getSimpleName(),
                        e.getMessage());
                return false;
            }
            boolean success = TableStatus.ACTIVE.equals(TableStatus.fromValue(tableDescription.getTableStatus()));
            if (success) {
                logger.debug("Creation of table '{}' successful, table status is now {}", tableName,
                        tableDescription.getTableStatus());
            } else {
                logger.warn("Creation of table '{}' unsuccessful, table status is now {}", tableName,
                        tableDescription.getTableStatus());
            }
            return success;
        } catch (AmazonClientException e) {
            logger.error("Exception when checking table status (describe): {}", e.getMessage());
            return false;
        } catch (InterruptedException e) {
            logger.error("Interrupted while trying to check table status: {}", e.getMessage());
            return false;
        }
    }

    private void resetClient() {
        if (db == null) {
            return;
        }
        db.shutdown();
        db = null;
        dbConfig = null;
        tableNameResolver = null;
        isProperlyConfigured = false;
    }

    private DynamoDBMapper getDBMapper(String tableName) {
        try {
            DynamoDBMapperConfig mapperConfig = new DynamoDBMapperConfig.Builder()
                    .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName))
                    .withPaginationLoadingStrategy(PaginationLoadingStrategy.LAZY_LOADING).build();
            return new DynamoDBMapper(db.getDynamoClient(), mapperConfig);
        } catch (AmazonClientException e) {
            logger.error("Error getting db mapper: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    protected boolean isReadyToStore() {
        return isProperlyConfigured && ensureClient();
    }

    @Override
    public String getName() {
        return "dynamodb";
    }

    @Override
    protected void flushBufferedData() {
        if (buffer.isEmpty()) {
            return;
        }
        logger.debug("Writing buffered data. Buffer size: {}", buffer.size());

        for (;;) {
            Map<String, Deque<DynamoDBItem<?>>> itemsByTable = readBuffer();
            // Write batch of data, one table at a time
            for (Entry<String, Deque<DynamoDBItem<?>>> entry : itemsByTable.entrySet()) {
                String tableName = entry.getKey();
                Deque<DynamoDBItem<?>> batch = entry.getValue();
                if (!batch.isEmpty()) {
                    flushBatch(getDBMapper(tableName), batch);
                }
            }
            if (buffer.isEmpty()) {
                break;
            }
        }
    }

    private Map<String, Deque<DynamoDBItem<?>>> readBuffer() {
        Map<String, Deque<DynamoDBItem<?>>> batchesByTable = new HashMap<String, Deque<DynamoDBItem<?>>>(2);
        // Get batch of data
        while (!buffer.isEmpty()) {
            DynamoDBItem<?> dynamoItem = buffer.poll();
            if (dynamoItem == null) {
                break;
            }
            String tableName = tableNameResolver.fromItem(dynamoItem);
            Deque<DynamoDBItem<?>> batch = batchesByTable.computeIfAbsent(tableName,
                    new Function<String, Deque<DynamoDBItem<?>>>() {
                        @Override
                        public Deque<DynamoDBItem<?>> apply(String t) {
                            return new ArrayDeque<DynamoDBItem<?>>();
                        }
                    });
            batch.add(dynamoItem);
        }
        return batchesByTable;
    }

    /**
     * Flush batch of data to DynamoDB
     *
     * @param mapper mapper associated with the batch
     * @param batch batch of data to write to DynamoDB
     */
    private void flushBatch(DynamoDBMapper mapper, Deque<DynamoDBItem<?>> batch) {
        long currentTimeMillis = System.currentTimeMillis();
        List<FailedBatch> failed = mapper.batchSave(batch);
        for (FailedBatch failedBatch : failed) {
            if (failedBatch.getException() instanceof ResourceNotFoundException) {
                // Table did not exist. Try writing everything again.
                retryFlushAfterCreatingTable(mapper, batch, failedBatch);
                break;
            } else {
                logger.debug("Batch failed with {}. Retrying next with exponential back-off",
                        failedBatch.getException().getMessage());
                new ExponentialBackoffRetry(failedBatch.getUnprocessedItems()).run();
            }
        }
        if (failed.isEmpty()) {
            logger.debug("flushBatch ended with {} items in {} ms: {}", batch.size(),
                    System.currentTimeMillis() - currentTimeMillis, batch);
        } else {
            logger.warn(
                    "flushBatch ended with {} items in {} ms: {}. There were some failed batches that were retried -- check logs for ERRORs to see if writes were successful",
                    batch.size(), System.currentTimeMillis() - currentTimeMillis, batch);
        }
    }

    /**
     * Retry flushing data after creating table associated with mapper
     *
     * @param mapper mapper associated with the batch
     * @param batch original batch of data. Used for logging and to determine table name
     * @param failedBatch failed batch that should be retried
     */
    private void retryFlushAfterCreatingTable(DynamoDBMapper mapper, Deque<DynamoDBItem<?>> batch,
            FailedBatch failedBatch) {
        logger.debug("Table was not found. Trying to create table and try saving again");
        if (createTable(mapper, batch.peek().getClass())) {
            logger.debug("Table creation successful, trying to save again");
            if (!failedBatch.getUnprocessedItems().isEmpty()) {
                ExponentialBackoffRetry retry = new ExponentialBackoffRetry(failedBatch.getUnprocessedItems());
                retry.run();
                if (retry.getUnprocessedItems().isEmpty()) {
                    logger.debug("Successfully saved items after table creation");
                }
            }
        } else {
            logger.warn("Table creation failed. Not storing some parts of batch: {}. Unprocessed items: {}", batch,
                    failedBatch.getUnprocessedItems());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterable<HistoricItem> query(FilterCriteria filter) {
        logger.debug("got a query");
        if (!isProperlyConfigured) {
            logger.warn("Configuration for dynamodb not yet loaded or broken. Not storing item.");
            return Collections.emptyList();
        }
        if (!ensureClient()) {
            logger.warn("DynamoDB not connected. Not storing item.");
            return Collections.emptyList();
        }

        String itemName = filter.getItemName();
        Item item = getItemFromRegistry(itemName);
        if (item == null) {
            logger.warn("Could not get item {} from registry!", itemName);
            return Collections.emptyList();
        }

        Class<DynamoDBItem<?>> dtoClass = AbstractDynamoDBItem.getDynamoItemClass(item.getClass());
        String tableName = tableNameResolver.fromClass(dtoClass);
        DynamoDBMapper mapper = getDBMapper(tableName);
        logger.debug("item {} (class {}) will be tried to query using dto class {} from table {}", itemName,
                item.getClass(), dtoClass, tableName);

        List<HistoricItem> historicItems = new ArrayList<HistoricItem>();

        DynamoDBQueryExpression<DynamoDBItem<?>> queryExpression = DynamoDBQueryUtils.createQueryExpression(dtoClass,
                filter);
        @SuppressWarnings("rawtypes")
        final PaginatedQueryList<? extends DynamoDBItem> paginatedList;
        try {
            paginatedList = mapper.query(dtoClass, queryExpression);
        } catch (AmazonServiceException e) {
            logger.error(
                    "DynamoDB query raised unexpected exception: {}. Returning empty collection. "
                            + "Status code 400 (resource not found) might occur if table was just created.",
                    e.getMessage());
            return Collections.emptyList();
        }
        for (int itemIndexOnPage = 0; itemIndexOnPage < filter.getPageSize(); itemIndexOnPage++) {
            int itemIndex = filter.getPageNumber() * filter.getPageSize() + itemIndexOnPage;
            DynamoDBItem<?> dynamoItem;
            try {
                dynamoItem = paginatedList.get(itemIndex);
            } catch (IndexOutOfBoundsException e) {
                logger.debug("Index {} is out-of-bounds", itemIndex);
                break;
            }
            if (dynamoItem != null) {
                HistoricItem historicItem = dynamoItem.asHistoricItem(item);
                logger.trace("Dynamo item {} converted to historic item: {}", item, historicItem);
                historicItems.add(historicItem);
            }

        }
        return historicItems;
    }

    /**
     * Retrieves the item for the given name from the item registry
     *
     * @param itemName
     * @return item with the given name, or null if no such item exists in item registry.
     */
    private Item getItemFromRegistry(String itemName) {
        Item item = null;
        try {
            if (itemRegistry != null) {
                item = itemRegistry.getItem(itemName);
            }
        } catch (ItemNotFoundException e1) {
            logger.error("Unable to get item {} from registry", itemName);
        }
        return item;
    }

    /**
     * This is a normal thread factory, which adds a named prefix to all created
     * threads.
     *
     * Adapted from RRD4jService
     */
    private static class NamedThreadFactory implements ThreadFactory {

        protected final ThreadGroup group;
        protected final AtomicInteger threadNumber = new AtomicInteger(1);
        protected final String namePrefix;

        public NamedThreadFactory() {
            this.namePrefix = DYNAMODB_THREADPOOL_NAME;
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (!t.isDaemon()) {
                t.setDaemon(true);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }

            return t;
        }
    }

}
