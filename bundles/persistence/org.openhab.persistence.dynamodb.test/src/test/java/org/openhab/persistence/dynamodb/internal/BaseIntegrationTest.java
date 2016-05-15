package org.openhab.persistence.dynamodb.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.NotImplementedException;
import org.junit.BeforeClass;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.items.ItemRegistryChangeListener;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.LocationItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.library.tel.items.CallItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;

public class BaseIntegrationTest {
    protected static final Logger logger = LoggerFactory.getLogger(DynamoDBPersistenceService.class);
    protected static DynamoDBPersistenceService service;
    protected final static Map<String, Item> items = new HashMap<>();

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
    }

    @BeforeClass
    public static void initService() throws InterruptedException {
        items.put("dimmer", new DimmerItem("dimmer"));
        items.put("number", new NumberItem("number"));
        items.put("string", new StringItem("string"));
        items.put("switch", new SwitchItem("switch"));
        items.put("contact", new ContactItem("contact"));
        items.put("color", new ColorItem("color"));
        items.put("rollershutter", new RollershutterItem("rollershutter"));
        items.put("datetime", new DateTimeItem("datetime"));
        items.put("call", new CallItem("call"));
        items.put("location", new LocationItem("location"));

        service = new DynamoDBPersistenceService();
        service.setItemRegistry(new ItemRegistry() {

            @Override
            public void removeItemRegistryChangeListener(ItemRegistryChangeListener listener) {
                throw new NotImplementedException();

            }

            @Override
            public boolean isValidItemName(String itemName) {
                throw new NotImplementedException();
            }

            @Override
            public Collection<Item> getItems(String pattern) {
                throw new NotImplementedException();
            }

            @Override
            public Collection<Item> getItems() {
                throw new NotImplementedException();
            }

            @Override
            public Item getItemByPattern(String name) throws ItemNotFoundException, ItemNotUniqueException {
                throw new NotImplementedException();
            }

            @Override
            public Item getItem(String name) throws ItemNotFoundException {
                Item item = items.get(name);
                if (item == null) {
                    throw new ItemNotFoundException(name);
                }
                return item;
            }

            @Override
            public void addItemRegistryChangeListener(ItemRegistryChangeListener listener) {
                throw new NotImplementedException();
            }
        });

        HashMap<String, Object> config = new HashMap<>();
        config.put("region", System.getProperty("DYNAMODBTEST_REGION"));
        config.put("accessKey", System.getProperty("DYNAMODBTEST_ACCESS"));
        config.put("secretKey", System.getProperty("DYNAMODBTEST_SECRET"));
        config.put("tablePrefix", "dynamodb-integration-tests-");

        for (Entry<String, Object> entry : config.entrySet()) {
            if (entry.getValue() == null) {
                logger.warn(String.format(
                        "Expecting %s to have value for integration tests. Integration tests will be skipped",
                        entry.getKey()));
                service = null;
                return;
            }
        }

        service.activate(null, config);

        // Clear data
        for (String table : new String[] { "dynamodb-integration-tests-bigdecimal",
                "dynamodb-integration-tests-string" }) {
            try {
                service.getDb().getDynamoClient().deleteTable(table);
                service.getDb().getDynamoDB().getTable(table).waitForDelete();
            } catch (ResourceNotFoundException e) {
            }
        }

    }

}
